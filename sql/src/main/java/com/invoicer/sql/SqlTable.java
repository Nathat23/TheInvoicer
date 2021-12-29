package com.invoicer.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SqlTable<T extends StoreableObject> {

    private final Class<T> objectClass;
    private String tableName;
    private final SqlHandler sqlHandler;

    public SqlTable (SqlHandler sqlHandler, Class<T> objectClass) {
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
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        Set<Attribute> set = new HashSet<>();
        for (int i = 2; i <= rsMetaData.getColumnCount(); i++) {
            set.add(getAttribute(resultSet, i));
        }
        return objectClass.getConstructor(int.class, Collection.class).newInstance(resultSet.getInt(1), set);
    }

    private Attribute getAttribute(ResultSet resultSet, int columnId) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        switch (resultSetMetaData.getColumnType(columnId)) {
            case Types.VARCHAR:
                return new StringAttribute(resultSetMetaData.getColumnName(columnId), resultSet.getString(columnId));
            case Types.INTEGER:
                return new IntAttribute(resultSetMetaData.getColumnName(columnId), resultSet.getInt(columnId));
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
            parameters.setCharAt(parameters.length() - 1, ' ');
            values.setCharAt(values.length() - 1, ' ');
            updateValues.setCharAt(updateValues.length() - 1, ' ');
            parameters.append(")");
            values.append(")");;
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
}
