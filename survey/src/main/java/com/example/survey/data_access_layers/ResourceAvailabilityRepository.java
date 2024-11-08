package com.example.survey.data_access_layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.utilities.TableNames;

@Repository
public class ResourceAvailabilityRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean formExists(long formid) {
        String checkFormSql = String.format("SELECT COUNT(*) FROM %s WHERE form_id = ?", TableNames.formT);
        Integer formCount = jdbcTemplate.queryForObject(checkFormSql, Integer.class, formid);
        return (formCount != null && formCount > 0);
    }

    public void checkFormExistence(long formid) {
        if (!formExists(formid)) {
            throw new IllegalArgumentException("Form ID does not exist: " + formid);
        }
    }

    public boolean groupExists(long groupid) {
        String checkGroupSql = String.format("SELECT COUNT(*) FROM %s WHERE group_id = ?", TableNames.groupT);
        Integer groupCount = jdbcTemplate.queryForObject(checkGroupSql, Integer.class, groupid);
        return (groupCount != null && groupCount > 0);
    }

    public void checkGroupExistence(long groupid) {
        if (!groupExists(groupid)) {
            throw new IllegalArgumentException("Group ID does not exist: " + groupid);
        }
    }

    public boolean userExists(long userid) {
        String checkUserSql = String.format("SELECT COUNT(*) FROM %s WHERE user_id = ?", TableNames.userT);
        Integer userCount = jdbcTemplate.queryForObject(checkUserSql, Integer.class, userid);
        return (userCount != null && userCount > 0);
    }

    public void checkUserExistence(long userid) {
        if (!userExists(userid)) {
            throw new IllegalArgumentException("User ID does not exist: " + userid);
        }
    }

    public boolean roleExists(int roleid) {
        String checkRoleSql = String.format("SELECT COUNT(*) FROM %s WHERE role_id = ?", TableNames.roleT);
        Integer roleCount = jdbcTemplate.queryForObject(checkRoleSql, Integer.class, roleid);
        return (roleCount != null && roleCount > 0);
    }

    public void checkRoleExistence(int roleid) {
        if (!roleExists(roleid)) {
            throw new IllegalArgumentException("Role ID does not exist: " + roleid);
        }
    }

    public boolean questionExists(long formid, long questionid) {
        String checkQuestionSql = String.format("SELECT COUNT(*) FROM %s WHERE form_id = ? AND question_id=?",
                TableNames.quesT);
        Integer questionCount = jdbcTemplate.queryForObject(checkQuestionSql, Integer.class,
                new Object[] { formid, questionid });
        return (questionCount != null && questionCount > 0);
    }

    public void checkQuestionExistence(long formid, long questionid) {
        if (!questionExists(formid, questionid)) {
            throw new IllegalArgumentException("Question ID does not exist: (" + formid + "," + questionid + ")");
        }
    }
}
