package com.invoicer.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SqlTable<T extends StoreableObject> {

    private final Class<T> objectClass;
    private String tableName;
    private final SqlHandler sqlHandler;
    private final Config config;

    public SqlTable (SqlHandler sqlHandler, Config config, Class<T> objectClass) {
        this.config = config;
        this.sqlHandler = sqlHandler;
        this.objectClass = objectClass;
    }

    public String getTableName() {
        if (tableName == null) {
            StoreableObjectData storeableObjectData = objectClass.getAnnotation(StoreableObjectData.class);
            tableName = storeableObjectData.tableName();
        }
        return tableName;
    }

    public void init() {
        try {
            StringBuilder structure = new StringBuilder("(id int NOT NULL,");
            StringBuilder foreignKey = new StringBuilder();
            for (AttributeConfig attributeConfig : config.getStoredObjectConfig().getList()) {
                structure.append(attributeConfig.getName()).append(" ").append(attributeConfig.getType().getStrType()).append(" NOT NULL,");
                if (attributeConfig.getForeignKeyTable() != null) {
                    foreignKey.append("FOREIGN KEY (").append(attributeConfig.getName()).append(") ");
                    foreignKey.append(" REFERENCES ").append(attributeConfig.getForeignKeyTable()).append("(").append(attributeConfig.getForeignKeyColumn()).append("),");
                }
            }
            structure.append("PRIMARY KEY (id),");
            structure.append(foreignKey);
            structure.setCharAt(structure.length() - 1, ')');
            Statement statement = sqlHandler.getHikariDataSource().getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + getTableName() + " " + structure + "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public T getObject(int id) {
        try {
            Statement statement = sqlHandler.getHikariDataSource().getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + getTableName() + " WHERE id=" + id);
            rs.next();
            if (!rs.isLast()) {
                throw new UnsupportedOperationException("Multiple rows with same id!!!");
            }
            return processResultSet(rs);
        } catch (SQLException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<T> getObjects() {
        try {
            Statement statement = sqlHandler.getHikariDataSource().getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + getTableName());
            HashSet<T> objects = new HashSet<>();
            while (rs.next()) {
                objects.add(processResultSet(rs));
            }
            return objects;
        } catch (SQLException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private T processResultSet(ResultSet resultSet) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        List<Attribute> set = new ArrayList<>();
        int i = 2;
        for (AttributeConfig attributeConfig : config.getStoredObjectConfig().getList()) {
            set.add(getAttribute(attributeConfig, resultSet, i));
            i++;
        }
        return objectClass.getConstructor(int.class, Config.class, List.class).newInstance(resultSet.getInt(1), config, set);
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

    public void updateObject(StoreableObject object) {
        try {
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
            PreparedStatement statement = sqlHandler.getHikariDataSource().getConnection().prepareStatement(
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

    public void deleteObject(StoreableObject storeableObject) {
        try {
            Statement statement = sqlHandler.getHikariDataSource().getConnection().createStatement();
            statement.execute("DELETE FROM " + getTableName() + " WHERE id=" + storeableObject.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
