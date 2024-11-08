package com.example.survey.data_access_layers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.data_transfer_objects.AbstractUserObj;
import com.example.survey.data_transfer_objects.CompleteResponse;
import com.example.survey.data_transfer_objects.FormResponse;
import com.example.survey.data_transfer_objects.QuestionResponse;
import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.Form;
import com.example.survey.entities.Group;
import com.example.survey.entities.User;
import com.example.survey.mappers.UserDTOWithRoleRowMapper;
import com.example.survey.mappers.UserWithRoleRowMapper;
import com.example.survey.utilities.DateConversions;
import com.example.survey.utilities.TableNames;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ResourceAvailabilityRepository resourceAvailabilityRepository;

    public boolean matchEmailToId(long userid, String email) {
        User user = findByEmail(email);
        return user.getEmail() == email;
    }

    public List<User> findAll() {

        String sql = String.format("SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id", TableNames.userT,
                TableNames.roleT);
        return jdbcTemplate.query(sql, new UserWithRoleRowMapper());

    }

    public List<UserDTO> findAllDTO() {

        String sql = String.format("SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id", TableNames.userT,
                TableNames.roleT);
        return jdbcTemplate.query(sql, new UserDTOWithRoleRowMapper());

    }

    public User findByEmail(String email) {
        // System.out.println("findByEmail for email: " + email);
        String sql = String.format("SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE email='%s'",
                TableNames.userT,
                TableNames.roleT, email);
        // System.out.println("Query: " + sql);
        try {
            return jdbcTemplate.queryForObject(sql, new UserWithRoleRowMapper());
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid user email: " + email, ex);
        }
    }

    public UserDTO findByEmailDTO(String email) {
        // System.out.println("findByEmail for email: " + email);
        String sql = String.format("SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE email='%s'",
                TableNames.userT,
                TableNames.roleT, email);
        // System.out.println("Query: " + sql);
        try {
            return jdbcTemplate.queryForObject(sql, new UserDTOWithRoleRowMapper());
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid user email: " + email, ex);
        }
    }

    public User findById(long userid) {
        String sql = String.format("SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id=%s",
                TableNames.userT,
                TableNames.roleT, userid);
        try {
            return jdbcTemplate.queryForObject(sql, new UserWithRoleRowMapper());
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid user ID: " + userid, ex);
        }
    }

    public UserDTO findByIdDTO(long userid) {
        String sql = String.format("SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id=%s",
                TableNames.userT,
                TableNames.roleT, userid);
        try {
            return jdbcTemplate.queryForObject(sql, new UserDTOWithRoleRowMapper());
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid user ID: " + userid, ex);
        }
    }

    public List<Group> findGroupsByUser(long userid) {
        resourceAvailabilityRepository.checkUserExistence(userid);
        String sql = String.format(
                "SELECT * FROM %s WHERE group_id IN (SELECT group_id FROM %s WHERE user_id=?)",
                TableNames.groupT, TableNames.memberT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Group.class), userid);
    }

    public List<Form> findFormsByUser(long userid, Boolean active) {
        resourceAvailabilityRepository.checkUserExistence(userid);
        String sql;
        if (active == null) {
            sql = String.format(
                    "SELECT * FROM %s WHERE form_id IN (SELECT form_id FROM %s,%s WHERE %s.group_id=%s.group_id AND user_id=?)",
                    TableNames.formT, TableNames.canRespondT, TableNames.memberT, TableNames.canRespondT,
                    TableNames.memberT);
        } else if (active) {
            sql = String.format(
                    "SELECT * FROM %s WHERE NOW()>=start_time AND (end_time IS NULL OR NOW()<end_time) AND form_id IN (SELECT form_id FROM %s,%s WHERE %s.group_id=%s.group_id AND user_id=?)",
                    TableNames.formT, TableNames.canRespondT, TableNames.memberT, TableNames.canRespondT,
                    TableNames.memberT);
        } else {
            sql = String.format(
                    "SELECT * FROM %s WHERE (NOW()<start_time OR (end_time IS NOT NULL AND NOW()>=end_time)) AND form_id IN (SELECT form_id FROM %s,%s WHERE %s.group_id=%s.group_id AND user_id=?)",
                    TableNames.formT, TableNames.canRespondT, TableNames.memberT, TableNames.canRespondT,
                    TableNames.memberT);
        }

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Form.class), userid);
    }

    public List<Form> findFormsByUserResponded(long userid, Boolean responded) {
        resourceAvailabilityRepository.checkUserExistence(userid);
        String sql;
        if (responded == null) {
            sql = String.format(
                    "SELECT * FROM %s WHERE form_id IN (SELECT form_id FROM %s,%s WHERE %s.group_id=%s.group_id AND user_id=?)",
                    TableNames.formT, TableNames.canRespondT, TableNames.memberT, TableNames.canRespondT,
                    TableNames.memberT, TableNames.quesRespT, userid);
        } else if (responded) {
            sql = String.format(
                    "SELECT * FROM %s WHERE form_id IN (SELECT form_id FROM %s,%s WHERE %s.group_id=%s.group_id AND user_id=?) AND form_id IN (SELECT form_id FROM %s nn WHERE nn.user_id=%d)",
                    TableNames.formT, TableNames.canRespondT, TableNames.memberT, TableNames.canRespondT,
                    TableNames.memberT, TableNames.quesRespT, userid);
        } else {
            sql = String.format(
                    "SELECT * FROM %s WHERE form_id IN (SELECT form_id FROM %s,%s WHERE %s.group_id=%s.group_id AND user_id=?) AND form_id NOT IN (SELECT form_id FROM %s nn WHERE nn.user_id=%d)",
                    TableNames.formT, TableNames.canRespondT, TableNames.memberT, TableNames.canRespondT,
                    TableNames.memberT, TableNames.quesRespT, userid);
        }

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Form.class), userid);
    }

    public List<Form> findResponses(long userid) {
        resourceAvailabilityRepository.checkUserExistence(userid);
        String sql = String.format("SELECT * FROM %s WHERE form_id IN (SELECT form_id FROM %s WHERE user_id=?)",
                TableNames.formT,
                TableNames.responseT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Form.class), userid);
    }

    public Boolean canRespondToForm(String email, long formid) {
        resourceAvailabilityRepository.checkFormExistence(formid);
        String sql = String.format(
                "SELECT COUNT(*) FROM %s u,%s m,%s r WHERE u.user_id=m.user_id AND email=? AND r.form_id=? AND r.group_id=m.group_id",
                TableNames.userT, TableNames.memberT, TableNames.canRespondT);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { email, formid });
        return count != null && count > 0;
    }

    public void formRespond(FormResponse response) {
        resourceAvailabilityRepository.checkFormExistence(response.getForm_id());
        String sql = String.format("INSERT INTO %s VALUES (?,?,?,?,?)", TableNames.responseT);
        String chksql = String.format(
                "SELECT COUNT(*) FROM %s WHERE form_id=%s AND user_id=%s",
                TableNames.responseT,
                response.getForm_id(),
                response.getUser_id());
        int count = jdbcTemplate.queryForObject(chksql, Integer.class);
        String chkallString = String.format(
                "SELECT COUNT(*) FROM %s WHERE required=1 AND question_id NOT IN (SELECT question_id FROM %s WHERE form_id=%d AND user_id=%d)",
                TableNames.quesT, TableNames.quesRespT, response.getForm_id(), response.getUser_id());
        int left = jdbcTemplate.queryForObject(chkallString, Integer.class);
        if (count == 0 && left == 0) {
            jdbcTemplate.update(
                    sql,
                    new Object[] {
                            response.getForm_id(),
                            response.getUser_id(),
                            DateConversions.dateToMySQLString(null),
                            response.getDevice(),
                            response.getLocation()
                    });
        }
    }

    public void completeFormRespond(CompleteResponse response) {
        FormResponse metadata = response.getMetadata();
        resourceAvailabilityRepository.checkFormExistence(metadata.getForm_id());
        String sql = String.format("SELECT question_id FROM %s WHERE form_id=%d", TableNames.quesT,
                metadata.getForm_id());
        String chksql = String.format("SELECT question_id FROM %s WHERE form_id=%d AND user_id=%d",
                TableNames.quesRespT,
                metadata.getForm_id(),
                metadata.getUser_id());
        List<Long> questions = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("question_id"));
        Set<Long> qset = new HashSet<>(questions);

        List<Long> respondedQuestions = jdbcTemplate.query(chksql, (rs, rowNum) -> rs.getLong("question_id"));
        Set<Long> respondedqset = new HashSet<>(respondedQuestions);

        String insqlString;
        for (QuestionResponse qr : response.getQuestions()) {
            if (qset.contains(qr.getQuestion_id())) {
                String respString = qr.getResponse_string() != null ? String.format("\"%s\"", qr.getResponse_string())
                        : "NULL";
                if (respondedqset.contains(qr.getQuestion_id())) {
                    insqlString = String.format(
                            "UPDATE %s SET response_int=%d,response_string=%s,response_float=%f WHERE user_id=%d AND form_id=%d AND question_id=%d",
                            TableNames.quesRespT, qr.getResponse_int(), respString, qr.getResponse_float(),
                            metadata.getUser_id(), metadata.getForm_id(), qr.getQuestion_id());
                    jdbcTemplate.update(insqlString);
                } else {
                    insqlString = String.format("INSERT INTO %s VALUES (%d,%d,%d,%d,%s,%f)",
                            TableNames.quesRespT, metadata.getUser_id(), qr.getQuestion_id(), metadata.getForm_id(),
                            qr.getResponse_int(), respString, qr.getResponse_float());
                    jdbcTemplate.update(insqlString);
                }
                qset.remove(qr.getQuestion_id());
            }
        }
        // TODO: Add condition for required questions before doing going past
        formRespond(metadata);
    }

    // Assuming DataIntegrityViolationException for FK constraint
    public void setUserRole(long userid, Integer roleid) {
        if (roleid == null) {
            throw new IllegalArgumentException("Can't setrole when role_id is null!");
        }
        resourceAvailabilityRepository.checkRoleExistence(roleid);
        String sql = String.format("UPDATE %s SET user_role=? WHERE user_id=?", TableNames.userT);
        try {
            jdbcTemplate.update(sql, new Object[] { roleid, userid });
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid user ID: " + userid);
        }
    }

    public void createUser(User newuser) {
        String sql = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", TableNames.userT);
        Integer user_role = newuser.getUser_role();
        long user_id = newuser.getUser_id();
        jdbcTemplate.update(sql, new Object[] {
                user_id,
                newuser.getFirst_name(),
                newuser.getMiddle_name(),
                newuser.getLast_name(),
                newuser.getEmail(),
                newuser.getPhone_code(),
                newuser.getPhone_number(),
                newuser.getDob(),
                newuser.getGender(),
                newuser.getPasskey(),
                user_role });
    }

    public User saveAndReturnUser(User newuser) {
        String sql = String.format("INSERT INTO %s (email,passkey) VALUES (?,?)", TableNames.userT);
        jdbcTemplate.update(sql, new Object[] { newuser.getEmail(), newuser.getPasskey() });
        User ru;
        try {
            ru = findByEmail(newuser.getEmail());
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid user email: " + newuser.getEmail(), ex);
        }
        return ru;
    }

    public User saveAndReturnUserFull(User newuser) {
        String sql = String.format(
                "INSERT INTO %s (email,passkey,first_name,middle_name,last_name,phone_code,phone_number,dob,gender) VALUES (?,?,?,?,?,?,?,?,?)",
                TableNames.userT);
        jdbcTemplate.update(sql,
                new Object[] { newuser.getEmail(), newuser.getPasskey(), newuser.getFirst_name(),
                        newuser.getMiddle_name(), newuser.getLast_name(), newuser.getPhone_code(),
                        newuser.getPhone_number(), newuser.getDob(), newuser.getGender() });
        User ru;
        try {
            ru = findByEmail(newuser.getEmail());
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid user email: " + newuser.getEmail(), ex);
        }
        return ru;
    }

    public void dropUser(long userid) {
        resourceAvailabilityRepository.checkUserExistence(userid);
        String sql = String.format("DELETE FROM %s WHERE user_id=?", TableNames.userT);
        try {
            jdbcTemplate.update(sql, new Object[] { userid });
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid user ID: " + userid, ex);
        }
    }

    public User updateUser(User user) {
        resourceAvailabilityRepository.checkUserExistence(user.getUser_id());
        User oldUser = findById(user.getUser_id());
        if (user.getFirst_name() != null) {
            oldUser.setFirst_name(user.getFirst_name());
        }
        if (user.getMiddle_name() != null) {
            oldUser.setMiddle_name(user.getMiddle_name());
        }
        if (user.getLast_name() != null) {
            oldUser.setLast_name(user.getLast_name());
        }
        if (user.getPhone_code() != null) {
            oldUser.setPhone_code(user.getPhone_code());
        }
        if (user.getPhone_number() != null) {
            oldUser.setPhone_number(user.getPhone_number());
        }
        if (user.getDob() != null) {
            oldUser.setDob(user.getDob());
        }
        if (user.getGender() != null) {
            oldUser.setGender(user.getGender());
        }
        String sql = String.format(
                "UPDATE %s SET first_name=?,middle_name=?,last_name=?,phone_code=?,phone_number=?,dob=?,gender=? WHERE user_id=%d",
                TableNames.userT, user.getUser_id());
        try {
            jdbcTemplate.update(sql,
                    new Object[] { oldUser.getFirst_name(), oldUser.getMiddle_name(), oldUser.getLast_name(),
                            oldUser.getPhone_code(), oldUser.getPhone_number(), oldUser.getDob(),
                            oldUser.getGender() });
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid arguments!");
        }
        return oldUser;
    }

    public UserDTO updateUserDTO(User user) {
        resourceAvailabilityRepository.checkUserExistence(user.getUser_id());
        User oldUser = findById(user.getUser_id());
        if (user.getFirst_name() != null) {
            oldUser.setFirst_name(user.getFirst_name());
        }
        if (user.getMiddle_name() != null) {
            oldUser.setMiddle_name(user.getMiddle_name());
        }
        if (user.getLast_name() != null) {
            oldUser.setLast_name(user.getLast_name());
        }
        if (user.getPhone_code() != null) {
            oldUser.setPhone_code(user.getPhone_code());
        }
        if (user.getPhone_number() != null) {
            oldUser.setPhone_number(user.getPhone_number());
        }
        if (user.getDob() != null) {
            oldUser.setDob(user.getDob());
        }
        if (user.getGender() != null) {
            oldUser.setGender(user.getGender());
        }
        String sql = String.format(
                "UPDATE %s SET first_name=?,middle_name=?,last_name=?,phone_code=?,phone_number=?,dob=?,gender=? WHERE user_id=%d",
                TableNames.userT, user.getUser_id());
        try {
            jdbcTemplate.update(sql,
                    new Object[] { oldUser.getFirst_name(), oldUser.getMiddle_name(), oldUser.getLast_name(),
                            oldUser.getPhone_code(), oldUser.getPhone_number(), oldUser.getDob(),
                            oldUser.getGender() });
        } catch (DataAccessException ex) {
            throw new IllegalArgumentException("Invalid arguments!");
        }
        return new UserDTO(oldUser);
    }

    public List<AbstractUserObj> findUserByEmailMatch(String email) {
        String sql = String.format("SELECT * FROM %s WHERE email LIKE '%%?%%'", TableNames.userT);
        List<User> found_matches = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class),
                new Object[] { email });
        List<AbstractUserObj> finalList = found_matches.stream().map(user -> new AbstractUserObj(user))
                .collect(Collectors.toList());
        return finalList;
    }

    public List<Form> findFormsForUser(long userid, Boolean responded) {
        String sql;
        Object[] params;
        if (responded == null) {
            sql = String.format(
                    "SELECT f.* FROM %s u JOIN %s m ON u.user_id=m.user_id JOIN %s c ON c.group_id=m.group_id JOIN %s f ON f.form_id=c.form_id WHERE u.user_id=? AND f.start_time >= CURRENT_TIMESTAMP",
                    TableNames.userT, TableNames.memberT, TableNames.canRespondT, TableNames.formT);
            params = new Object[] { userid };
        } else if (responded) {
            sql = String.format(
                    "SELECT f.* FROM %s u JOIN %s r ON u.user_id=r.user_id JOIN %s f ON r.form_id=f.form_id WHERE user_id=?",
                    TableNames.userT, TableNames.responseT, TableNames.formT);
            params = new Object[] { userid };
        } else {
            sql = String.format(
                    "SELECT f.* FROM %s u JOIN %s m ON u.user_id=m.user_id JOIN %s c ON c.group_id=m.group_id JOIN %s f ON f.form_id=c.form_id WHERE u.user_id=? AND f.form_id NOT IN (SELECT r.form_id FROM %s u2 JOIN %s r ON u2.user_id=r.user_id WHERE u2.user_id=?) AND f.start_time <= CURRENT_TIMESTAMP AND (f.end_time IS NULL OR f.end_time >= CURRENT_TIMESTAMP);",
                    TableNames.userT, TableNames.memberT, TableNames.canRespondT, TableNames.formT, TableNames.userT,
                    TableNames.responseT);
            params = new Object[] { userid, userid };
        }

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Form.class), params);
    }
}
