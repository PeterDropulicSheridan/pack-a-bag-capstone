package ca.sheridancollege.packabag.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.packabag.model.User;

@Repository
@Transactional
public class UserRegistrationDatabaseAccess {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    protected NamedParameterJdbcTemplate jdbc;

    public void addUser(String username, String password) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "insert into sec_users (username, password) values (:username, :password)";
        parameters.addValue("username", username);
        parameters.addValue("password", passwordEncoder.encode(password));
        jdbc.update(query, parameters);
    }

    public void addRole(int userId, int roleId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "insert into sec_userroles (userId, roleId) values (:userId, :roleId)";
        parameters.addValue("userId", userId);
        parameters.addValue("roleId", roleId);
        jdbc.update(query, parameters);
    }

    public User findUserAccount(String username) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "SELECT userId, username, password FROM sec_users WHERE username = :username";
        parameters.addValue("username", username);
        ArrayList<User> users = (ArrayList<User>) jdbc.query(query, parameters,
                new BeanPropertyRowMapper<User>(User.class));
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

public List<String> getRolesById(int userId) {
    ArrayList<String> roles = new ArrayList<>();
    MapSqlParameterSource parameters = new MapSqlParameterSource();
    String query = "SELECT r.roleName "
            + "FROM sec_users u, sec_roles r, sec_userroles ur "
            + "WHERE ur.userId = :userId AND ur.roleId = r.roleId AND u.userId = :userId";
    parameters.addValue("userId", userId);
    List<java.util.Map<String, Object>> rows = jdbc.queryForList(query, parameters);
    for (java.util.Map<String, Object> row : rows) {
        roles.add((String) row.get("roleName"));
    }
    return roles;
}

    public List<User> getUserId() {
        String query = "SELECT userId, username, password FROM sec_users";
        ArrayList<User> users = (ArrayList<User>) jdbc.query(query,
                new BeanPropertyRowMapper<User>(User.class));
        return users;
    }
}
