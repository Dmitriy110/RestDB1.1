package org.example.repository.mapper;

import org.example.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserResultSetMapperImpl implements ResultSetMapper<User> {
    @Override
    public User map(ResultSet resultSet) throws SQLException {
        if (resultSet == null){ return null; }
        User user = new User();
        user.setId(UUID.fromString(resultSet.getString("id")));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }
}
