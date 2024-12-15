package org.example.repository.mapper;

import org.example.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PostResultSetMapperImpl implements ResultSetMapper<Post> {
    @Override
    public Post map(ResultSet resultSet) throws SQLException {
        if (resultSet == null){ return null; }
        Post post = new Post();
        post.setId(UUID.fromString(resultSet.getString("id")));
        post.setTitle(resultSet.getString("title"));
        post.setContent(resultSet.getString("content"));
        post.setUserId(UUID.fromString(resultSet.getString("user_id")));
        return post;
    }
}
