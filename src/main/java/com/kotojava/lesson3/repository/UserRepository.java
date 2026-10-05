package com.kotojava.lesson3.repository;

import com.kotojava.lesson3.model.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final DataSource dataSource;

    public UserRepository(DataSource pgUserDataSource) {
        this.dataSource = pgUserDataSource;
    }

    public void save(User newUser) {
        String sql = "INSERT INTO users (name, surname, age, country, profession, email) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, newUser.getName());
            statement.setString(2, newUser.getSurname());
            statement.setInt(3, newUser.getAge());
            statement.setString(4, newUser.getCountry());
            statement.setString(5, newUser.getProfession());
            statement.setString(6, newUser.getEmail());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newUser.setId(generatedKeys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    public User getUserById(long id) {
        String sql = "SELECT id, name, surname, age, country, profession, email FROM users WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    public List<User> getUsers() {
        String sql = "SELECT id, name, surname, age, country, profession, email FROM users ORDER BY id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(mapRow(resultSet));
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    private User mapRow(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .surname(resultSet.getString("surname"))
                .age(resultSet.getInt("age"))
                .country(resultSet.getString("country"))
                .profession(resultSet.getString("profession"))
                .email(resultSet.getString("email"))
                .build();
    }
}