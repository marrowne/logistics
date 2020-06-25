package it.logistics.hr.infrastructure.persistence;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStore {

    private DataSource dataSource;

    public UserStore(DataSource dataSource) {this.dataSource = dataSource;};

    public Long addUser(String role) throws SQLException {
        final String password = "$2a$04$5zAGm9JlMLXkWRi0wnLipeFEj3ekSGY879ji1.tzoz.5mhxhSeHEm";
        String query2 = "INSERT INTO `users` (`id`, `passwordHash`, `authority`, `active`) " +
                        "VALUES (?, ?, ?, 1);";
        PreparedStatement insertStatement = null;
        Long userId = nextId();

        try {
            insertStatement = dataSource.getConnection().prepareStatement(query2);
            insertStatement.setLong(1, userId);
            insertStatement.setString(2, password);
            insertStatement.setString(3, role);
            insertStatement.execute();
        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (insertStatement != null) {
                insertStatement.close();
            }
        }
        return userId;
    }

    public void removeUser(Long userId) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "UPDATE USERS SET ACTIVE = 0, DELETED = 1 WHERE ID=?;";
        try {
            preparedStatement = dataSource.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, userId);
            preparedStatement.execute();
        } catch (SQLException e ) {
            System.out.println(e);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    private Long nextId() throws SQLException {
        String jdbcDriverName = DriverManager.getDriver(
                    dataSource.getConnection().getMetaData().getURL()
                ).getClass().getName();
        String sequenceQuery = null;
        if(jdbcDriverName.equals("org.h2.Driver")) {
            sequenceQuery = "SELECT USERS_SEQUENCE.nextval";
        } else {
            sequenceQuery = "SELECT AUTO_INCREMENT FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = \"identity\" AND TABLE_NAME = \"users\";";
        }
        PreparedStatement nextIdStatement = null;
        Long userId = null;
        try {
            nextIdStatement = dataSource.getConnection().prepareStatement(sequenceQuery);
            ResultSet rs = nextIdStatement.executeQuery();
            if (rs.next()) {
                userId = rs.getLong(1);
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (nextIdStatement != null) {
                nextIdStatement.close();
            }
        }
        return userId;
    }

}
