package com.userauth.daos;

import com.userauth.models.User;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;

@Repository(value="com.userauth.daos.UserDao")
@Scope(BeanDefinition.SCOPE_SINGLETON)

public class UserDao {
    private static final String GET_ALL_USERS = "SELECT * FROM users";
    private static final String GET_USER = "SELECT username FROM users WHERE username = :username";
    private static final String INSERT_USER = "INSERT INTO users (username, password) SELECT :username, :password WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = :checkUsername )";
    private static final String UPDATE_USER = "UPDATE users SET username = :username, password = :password WHERE username = :username";
    private static final String DELETE_USER = "DELETE FROM users WHERE username = :username";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<User> getAllUsers() {
        return jdbcTemplate.query(GET_ALL_USERS, new MapSqlParameterSource(), (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    public List<User> getUser(String username) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username);
        return jdbcTemplate.query(GET_USER, params, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            return user;
        });
    }

    public void createUser(String username, String password) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username);
        params.addValue("password", password);
        params.addValue("checkUsername", username);
        jdbcTemplate.update(INSERT_USER, params);
    }

    public void updateUser(String username, String password) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username);
        params.addValue("password", password);
        jdbcTemplate.update(UPDATE_USER, params);
    }

    public void deleteUser(String username) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username);
        jdbcTemplate.update(DELETE_USER, params);
    }
}
