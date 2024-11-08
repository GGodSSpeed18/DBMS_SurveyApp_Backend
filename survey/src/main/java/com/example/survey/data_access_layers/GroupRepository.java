package com.example.survey.data_access_layers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Group;
import com.example.survey.entities.User;
import com.example.survey.utilities.TableNames;

@Repository
public class GroupRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceAvailabilityRepository resourceAvailabilityRepository;

    public Boolean isMember(long group_id, String email) {
        String sql = String.format(
                "SELECT COUNT(*) FROM %s u,%s m WHERE u.user_id=m.user_id AND group_id=? AND email='?'",
                TableNames.userT,
                TableNames.memberT);
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, group_id, email);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            return false; // Handle exceptions, e.g., SQL errors
        }
    }

    public Boolean isAuthor(long group_id, String email) {
        String sql = String.format(
                "SELECT COUNT(*) FROM %s u,%s g WHERE group_id=? AND group_owner=user_id AND email='?'",
                TableNames.userT,
                TableNames.groupT);
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, group_id, email);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            return false; // Handle exceptions, e.g., SQL errors
        }
    }

    public void addUserToGroup(long userid, long groupid) {
        resourceAvailabilityRepository.checkGroupExistence(groupid);
        resourceAvailabilityRepository.checkUserExistence(userid);
        String sql = String.format("INSERT INTO %s VALUES (?, ?)", TableNames.memberT);
        try {
            jdbcTemplate.update(sql, new Object[] { groupid, userid });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Integrity Constrains Violated", e);
        }
    }

    public void dropGroup(long id) {
        String sql = String.format("DELETE FROM %s WHERE group_id=?", TableNames.groupT);
        try {
            jdbcTemplate.update(sql, new Object[] { id });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Invalid group ID: " + id, e);
        }
    }

    public void createGroup(Group newgrp) {
        String sql = String.format("INSERT INTO %s (group_name, created_at, group_owner) VALUES (?, ?, ?)",
                TableNames.groupT);
        Date createdAt = new Date();
        jdbcTemplate.update(sql, new Object[] {
                newgrp.getGroup_name(),
                createdAt,
                newgrp.getGroup_owner()
        });
    }

    public List<Group> getAllGroups() {
        String sql = String.format("SELECT * FROM %s", TableNames.groupT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Group.class));
    }

    public List<Group> getAllGroupsFromUser(long userid) {
        resourceAvailabilityRepository.checkUserExistence(userid);
        String sql = String.format("SELECT * FROM %s WHERE group_owner=?", TableNames.groupT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Group.class), new Object[] { userid });
    }

    public List<UserDTO> getUsersofGroup(long groupid) {
        resourceAvailabilityRepository.checkGroupExistence(groupid);
        String sql = String.format("SELECT * from %s WHERE group_id=?", TableNames.memberT);
        return jdbcTemplate.query(sql, (rs, rowNum) -> new UserDTO(userRepository.findById(rs.getLong("user_id"))),
                new Object[] { groupid });
    }

    public Group getGroupbyId(long groupid) {
        resourceAvailabilityRepository.checkGroupExistence(groupid);
        String sql = String.format("SELECT * FROM %s WHERE group_id=?", TableNames.groupT);
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Group.class), new Object[] { groupid });
    }

    public void modifyGroup(Group updgrp, long groupid) {
        resourceAvailabilityRepository.checkGroupExistence(groupid);
        String sql = String.format("SELECT * FROM %s WHERE group_id=?", TableNames.groupT);
        Group current_grp = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Group.class),
                new Object[] { groupid });
        if (current_grp == null) {
            throw new IllegalArgumentException("Invalid Group ID: " + groupid);
        }
        String grp_name = current_grp.getGroup_name();
        Long grp_owner = current_grp.getGroup_owner();
        if (updgrp.getGroup_name() != null) {
            grp_name = updgrp.getGroup_name();
        }
        if (updgrp.getGroup_owner() != null) {
            grp_owner = updgrp.getGroup_owner();
        }
        String sql2 = String.format("UPDATE %s SET group_name=?, group_owner=? WHERE group_id=?", TableNames.groupT);
        jdbcTemplate.update(sql2, new Object[] { grp_name, grp_owner, groupid });
    }

    public void dropUserFromGroup(long userid, long id) {
        resourceAvailabilityRepository.checkGroupExistence(id);
        resourceAvailabilityRepository.checkUserExistence(userid);
        String sql = String.format("DELETE FROM %s WHERE user_id=? AND group_id=?", TableNames.memberT);
        try {
            jdbcTemplate.update(sql, new Object[] { userid, id });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("User is not present in the Group !", e);
        }
    }

    public void addUserToGroupByEmail(String email, long grpid) {
        User user = userRepository.findByEmail(email);
        resourceAvailabilityRepository.checkGroupExistence(grpid);   
        String sql = String.format("INSERT INTO %s VALUES (?, ?)", TableNames.memberT);
        try {
            jdbcTemplate.update(sql, new Object[] { grpid, user.getUser_id() });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Integrity Constrains Violated", e);
        }   
    }

}
