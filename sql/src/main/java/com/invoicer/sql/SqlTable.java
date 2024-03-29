package com.invoicer.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class SqlTable {

    private String tableName;
    private final SqlHandler sqlHandler;
    private final Config config;

    public SqlTable (SqlHandler sqlHandler, Config config) {
        this.config = config;
        this.sqlHandler = sqlHandler;
    }

    public String getTableName() {
        if (tableName == null) {
            tableName = config.getStoredObjectConfig().getTableName();
        }
        return tableName;
    }

    public void init() {
        try (Connection connection = sqlHandler.getHikariDataSource().getConnection(); Statement statement = connection.createStatement()) {
            StringBuilder structure = new StringBuilder("(id int NOT NULL,");
            StringBuilder foreignKey = new StringBuilder();
            for (AttributeConfig attributeConfig : config.getStoredObjectConfig().getList()) {
                structure.append(attributeConfig.getName()).append(" ").append(attributeConfig.getType().getStrType()).append(" NOT NULL,");
                if (attributeConfig.getForeignKeyTable() != null) {
                    foreignKey.append("FOREIGN KEY (").append(attributeConfig.getName()).append(") ");
                    foreignKey.append(" REFERENCES ").append(attributeConfig.getForeignKeyTable()).append("(").append(attributeConfig.getForeignKeyColumn()).append(") ");
                    foreignKey.append("ON DELETE CASCADE,");
                }
            }
            structure.append("PRIMARY KEY (id),");
            structure.append(foreignKey);
            structure.setCharAt(structure.length() - 1, ')');
            statement.execute("CREATE TABLE IF NOT EXISTS " + getTableName() + " " + structure + "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AttributeGroup getObject(int id) {
        try (Connection connection = sqlHandler.getHikariDataSource().getConnection(); Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT * FROM " + getTableName() + " WHERE id=" + id);
            rs.next();
            if (!rs.isLast()) {
                throw new UnsupportedOperationException("Multiple rows with same id!!!");
            }
            return processResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<AttributeGroup> getObjects() {
        try (Connection connection= sqlHandler.getHikariDataSource().getConnection(); Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM " + getTableName());
            HashSet<AttributeGroup> objects = new HashSet<>();
            while (rs.next()) {
                objects.add(processResultSet(rs));
            }
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AttributeGroup processResultSet(ResultSet resultSet) throws SQLException {
        List<Attribute> set = new ArrayList<>();
        int i = 2;
        for (AttributeConfig attributeConfig : config.getStoredObjectConfig().getList()) {
            set.add(getAttribute(attributeConfig, resultSet, i));
            i++;
        }
        return new AttributeGroup(resultSet.getInt(1), config, set);
    }

    private Attribute getAttribute(AttributeConfig config, ResultSet resultSet, int columnId) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        switch (resultSetMetaData.getColumnType(columnId)) {
            case Types.VARCHAR:
                return new StringAttribute(config, resultSetMetaData.getColumnName(columnId), resultSet.getString(columnId));
            case Types.INTEGER:
                return new IntAttribute(config, resultSetMetaData.getColumnName(columnId), resultSet.getInt(columnId));
            case Types.TIMESTAMP:
                return new DateTimeAttribute(config, resultSetMetaData.getColumnName(columnId), resultSet.getTimestamp(columnId).toLocalDateTime());
            case Types.BIT:
                return new BooleanAttribute(config, resultSetMetaData.getColumnName(columnId), resultSet.getBoolean(columnId));
            case Types.DOUBLE:
                return new DoubleAttribute(config, resultSetMetaData.getColumnName(columnId), resultSet.getDouble(columnId));
        }
        throw new UnsupportedOperationException("SQL data type not currently supported! (" + resultSetMetaData.getColumnTypeName(columnId) + ")");
    }

    public void updateObject(AttributeGroup object) {
        try (Connection connection = sqlHandler.getHikariDataSource().getConnection()) {
            StringBuilder parameters = new StringBuilder("(id,");
            StringBuilder values = new StringBuilder("(?,");
            StringBuilder updateValues = new StringBuilder("");
            for (Attribute attribute : object.getAttributes()) {
                parameters.append(attribute.getName()).append(",");
                values.append("?").append(",");
                if (attribute.isModified()) {
                    updateValues.append(attribute.getName()).append("=VALUES(").append(attribute.getName()).append("),");
                }
            }
            parameters.setCharAt(parameters.length() - 1, ')');
            values.setCharAt(values.length() - 1, ')');
            updateValues.setCharAt(updateValues.length() - 1, ' ');
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + getTableName() + " " + parameters + " VALUES " + values + " ON DUPLICATE KEY UPDATE " + updateValues);
            statement.setInt(1, object.getId());
            int i = 2;
            for (Attribute attribute : object.getAttributes()) {
                statement.setObject(i, attribute.getValue());
                i += 1;
            }
            int result = statement.executeUpdate();
            if (result != 0) {
                object.getAttributes().forEach(attribute -> attribute.setModified(false));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteObject(AttributeGroup attributeGroup) {
        try (Connection connection = sqlHandler.getHikariDataSource().getConnection(); Statement statement = connection.createStatement()){
            statement.execute("DELETE FROM " + getTableName() + " WHERE id=" + attributeGroup.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
