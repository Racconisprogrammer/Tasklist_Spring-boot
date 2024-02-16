package com.example.tasklist.repository.impl;


import com.example.tasklist.domain.exception.ResourceMappingException;
import com.example.tasklist.domain.user.Role;
import com.example.tasklist.domain.user.User;
import com.example.tasklist.repository.DataSourceConfig;
import com.example.tasklist.repository.UserRepository;
import com.example.tasklist.repository.mapper.TaskRowMapper;
import com.example.tasklist.repository.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Optional;


//@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            SELECT u.id as user_id,
                        u.name as user_name,
                        u.username as user_username,
                        u.password as user_password,
                        ur.role as user_role_role,
                        t.id as task_id,
                        t.title as task_title,
                        t.description as task_description,
                        t.expiration_date as task_expiration_date,
                        t.status as task_status
                 FROM users u
                          LEFT JOIN users_roles ur on u.id = ur.user_id
                          LEFT JOIN users_tasks ut on u.id = ut.user_id
                          LEFT JOIN tasks t on t.id = ut.task_id
                 WHERE u.id = ?
                        """;

    private final String FIND_BY_USERNAME = """
            SELECT u.id as user_id,
                        u.name as user_name,
                        u.username as user_username,
                        u.password as user_password,
                        ur.role as user_role_role,
                        t.id as task_id,
                        t.title as task_title,
                        t.description as task_description,
                        t.expiration_date as task_expiration_date,
                        t.status as task_status
                 FROM users u
                          LEFT JOIN users_roles ur on u.id = ur.user_id
                          LEFT JOIN users_tasks ut on u.id = ut.user_id
                          LEFT JOIN tasks t on t.id = ut.task_id
                 WHERE u.username = ?
                        """;

    private final String UPDATE = """
            UPDATE users
            SET name = ?,
            username = ?,
            password = ?
            where id = ?
            """;

    private final String CREATE = """
            INSERT INTO users (name, username, password) 
            values (?, ?, ?)
            """;

    private final String INSERT_USER_ROLE = """
            INSERT INTO users_roles (user_id, role) 
            values (?, ?)
            """;

    private final String IS_TASK_OWNER = """
            SELECT exists(
                    SELECT 1
                    FROM users_tasks
                    WHERE user_id = ?
                    AND task_id =?
            )
            """;

    private final String DELETE = """
            DELETE FROM users
            WHERE id = ?
            """;

    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }

        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error write find user by id.");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }

        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error write find user by username.");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            if (user.getName() == null) {
                statement.setNull(1, Types.VARCHAR);
            } else {
                statement.setString(1, user.getName());
            }
            if (user.getUsername() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, user.getUsername());
            }
            if (user.getPassword() == null) {
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(3, user.getPassword());
            }
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while updating user.");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            if (user.getName() == null) {
                statement.setNull(1, Types.VARCHAR);
            } else {
                statement.setString(1, user.getName());
            }
            if (user.getUsername() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, user.getUsername());
            }
            if (user.getPassword() == null) {
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(3, user.getPassword());
            }
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                user.setId(resultSet.getLong(1));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while creating user.");
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1, userId);
            statement.setString(2, role.name());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while inserting user role.");
        }
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_TASK_OWNER);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getBoolean(1);
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while checking if user is task owner.");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while deleting user.");
        }
    }
}
