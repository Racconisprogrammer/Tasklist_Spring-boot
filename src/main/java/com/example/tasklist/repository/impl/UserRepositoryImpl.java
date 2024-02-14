package com.example.tasklist.repository.impl;


import com.example.tasklist.domain.user.Role;
import com.example.tasklist.domain.user.User;
import com.example.tasklist.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Repository
public class UserRepositoryImpl implements UserRepository {

    private final String FIND_BY_ID = """
            SELECT t.id as task_id,
                    t.title as task_title,
                    t.description as task_description,
                    t.expiration_date as task_expiration_date,
                    t.status as task_status         
            FROM tasks t 
            WHERE id = ?
            """;

    private final String FIND_ALL_BY_USER_ID = """
            SELECT t.id as task_id,
                    t.title as task_title,
                    t.description as task_description,
                    t.expiration_date as task_expiration_date,
                    t.status as task_status         
            FROM tasks t 
                JOIN users_tasks ut on t.id = ut.task_id
            WHERE ut.user_id = ?
            """;

    private final String ASSIGN = """
            INSERT INTO users_tasks(task_id, user_id)
            values (?, ?)
            """;

    private final String UPDATE = """
            UPDATE tasks
            SET title = ?,
            description = ?,
            expiration_date = ?,
            status = ?
            where id = ?
            """;

    private final String CREATE = """
            INSERT INTO tasks (title, description, expiration_date, status) 
            values (?, ?, ?. ?)
            """;

    private final String DELETE = """
            DELETE FROM tasks
            WHERE id = ?
            """;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void create(User user) {

    }

    @Override
    public void insertUserRole(Long userId, Role role) {

    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        return false;
    }

    @Override
    public void delete(Long id) {

    }
}
