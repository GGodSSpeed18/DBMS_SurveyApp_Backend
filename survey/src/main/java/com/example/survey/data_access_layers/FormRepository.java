package com.example.survey.data_access_layers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.data_transfer_objects.AbstractUserObj;
import com.example.survey.data_transfer_objects.FormQuestions;
import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.data_transfer_objects.UserResponseData;
// import com.example.survey.data_transfer_objects.UserResponseData;
import com.example.survey.entities.Form;
import com.example.survey.entities.Group;
import com.example.survey.entities.User;
import com.example.survey.mappers.AbstractUserRowMapper;
import com.example.survey.mappers.UserDTOWithRoleRowMapper;
import com.example.survey.mappers.UserWithRoleRowMapper;
import com.example.survey.utilities.TableNames;

@Repository
public class FormRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ResourceAvailabilityRepository resourceAvailabilityRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ConditionalRepository conditionalRepository;

    @Autowired
    private UserRepository userRepository;

    private Connection connection;

    public Boolean isAuthor(String email, long formid) {
        String sql = String.format(
                "SELECT COUNT(*) FROM %s u,%s f WHERE form_id=? AND author=user_id AND email='?'", TableNames.userT,
                TableNames.formT);
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, formid, email);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public List<Form> getFormsOfUser(long userid) {
        String sql = String.format("SELECT * FROM %s WHERE author=?", TableNames.formT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Form.class), new Object[] { userid });
    }

    public Long getMaxFormId() {
        String sql = "SELECT MAX(form_id) FROM forms";
        try {
            Long maxFormId = jdbcTemplate.queryForObject(sql, Long.class);
            return maxFormId != null ? maxFormId : 0; // Return 0 if no forms are present
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching max form_id", e);
        }
    }

    public Form createForm(Form newform) {
        String sql = String.format(
                "INSERT INTO %s (form_id, form_name,author,created_at,form_desc,start_time,end_time) VALUES (?, ?, ?, ?, ?, ?, ?)",
                TableNames.formT);
        newform.setForm_id(getMaxFormId() + 1);
        newform.setCreated_at(new Date());
        jdbcTemplate.update(sql, new Object[] {
                newform.getForm_id(), // Not inserting form_id as it's auto-incrementing
                newform.getForm_name(),
                newform.getAuthor(),
                newform.getCreated_at(),
                newform.getForm_desc(),
                newform.getStart_time(),
                newform.getEnd_time()
        });
        return newform;
    }

    public void dropForm(long formid) {
        String sql = String.format("DELETE FROM %s WHERE form_id=?", TableNames.formT);
        try {
            jdbcTemplate.update(sql, new Object[] { formid });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Invalid Form ID: " + formid, e);
        }
    }

    public List<Form> getAllForms() {
        String sql = String.format("SELECT * FROM %s", TableNames.formT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Form.class));
    }

    public List<Group> openToGroups(long formid) {
        String sql = String.format("SELECT g.* FROM %s cr,%s g WHERE cr.group_id=g.group_id AND cr.form_id=?",
                TableNames.canRespondT, TableNames.groupT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Group.class), formid);
    }

    public void addGrouptoForm(long formid, long grpid) {
        String sql = String.format("INSERT INTO %s VALUES (?, ?)", TableNames.opentoT);
        resourceAvailabilityRepository.checkFormExistence(formid);
        resourceAvailabilityRepository.checkGroupExistence(grpid);
        try {
            jdbcTemplate.update(sql, new Object[] { formid, grpid });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Integrity Constraints Violated !" + grpid, e);
        }
    }

    public void removeGroupFromForm(long formid, long grpid) {
        String sql = String.format("DELETE FROM %s WHERE form_id=? AND group_id=?", TableNames.canRespondT);
        resourceAvailabilityRepository.checkFormExistence(formid);
        resourceAvailabilityRepository.checkGroupExistence(grpid);
        try {
            jdbcTemplate.update(sql, new Object[] { formid, grpid });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Integrity Constraints Violated ! F: " + formid + " G: " + grpid, e);
        }
    }

    public Form getFormByID(long id) {
        String sql = String.format("SELECT * FROM %s WHERE form_id=?", TableNames.formT);
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Form.class), new Object[] { id });
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Form with id " + id + " does not exist!");
        }
    }

    public List<User> openToUsers(long formid, Boolean responded) {
        resourceAvailabilityRepository.checkFormExistence(formid);
        String sql;
        if (responded == null) {
            sql = String.format(
                    "SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id IN (SELECT user_id FROM %s,%s WHERE %s.group_id=%s.group_id AND form_id=%d)",
                    TableNames.userT, TableNames.roleT,
                    TableNames.opentoT, TableNames.memberT,
                    TableNames.opentoT, TableNames.memberT, formid);
        } else if (responded) {
            sql = String.format(
                    "SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id IN (SELECT user_id FROM %s WHERE form_id=%d)",
                    TableNames.userT, TableNames.roleT,
                    TableNames.responseT, formid);
        } else {
            sql = String.format(
                    "SELECT u.*, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id IN (SELECT user_id FROM %s,%s WHERE %s.group_id=%s.group_id AND form_id=%d) AND user_id NOT IN (SELECT user_id FROM %s WHERE form_id=%d)",
                    TableNames.userT, TableNames.roleT,
                    TableNames.opentoT, TableNames.memberT,
                    TableNames.opentoT, TableNames.memberT, formid, TableNames.responseT, formid);
        }

        return jdbcTemplate.query(sql, new UserWithRoleRowMapper());
    }

    public List<UserDTO> openToUsersDTO(long formid, Boolean responded) {
        resourceAvailabilityRepository.checkFormExistence(formid);
        String sql;
        if (responded == null) {
            sql = String.format(
                    "SELECT u.user_id,u.email,u.first_name,u.middle_name,u.last_name,u.dob,u.gender,u.user_role, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id IN (SELECT user_id FROM %s,%s WHERE %s.group_id=%s.group_id AND form_id=%d)",
                    TableNames.userT, TableNames.roleT,
                    TableNames.opentoT, TableNames.memberT,
                    TableNames.opentoT, TableNames.memberT, formid);
        } else if (responded) {
            sql = String.format(
                    "SELECT u.user_id,u.email,u.first_name,u.middle_name,u.last_name,u.dob,u.gender,u.user_role, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id IN (SELECT user_id FROM %s WHERE form_id=%d)",
                    TableNames.userT, TableNames.roleT,
                    TableNames.responseT, formid);
        } else {
            sql = String.format(
                    "SELECT u.user_id,u.email,u.first_name,u.middle_name,u.last_name,u.dob,u.gender,u.user_role, r.* FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id IN (SELECT user_id FROM %s,%s WHERE %s.group_id=%s.group_id AND form_id=%d) AND user_id NOT IN (SELECT user_id FROM %s WHERE form_id=%d)",
                    TableNames.userT, TableNames.roleT,
                    TableNames.opentoT, TableNames.memberT,
                    TableNames.opentoT, TableNames.memberT, formid, TableNames.responseT, formid);
        }

        return jdbcTemplate.query(sql, new UserDTOWithRoleRowMapper());
    }

    public List<AbstractUserObj> openToUsersAbstract(long formid, Boolean responded) {
        resourceAvailabilityRepository.checkFormExistence(formid);
        String sql;
        if (responded == null) {
            sql = String.format(
                    "SELECT u.user_id,u.email,u.user_role FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id IN (SELECT user_id FROM %s,%s WHERE %s.group_id=%s.group_id AND form_id=%d)",
                    TableNames.userT, TableNames.roleT,
                    TableNames.opentoT, TableNames.memberT,
                    TableNames.opentoT, TableNames.memberT, formid);
        } else if (responded) {
            sql = String.format(
                    "SELECT u.user_id,u.email,u.user_role FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id IN (SELECT user_id FROM %s WHERE form_id=%d)",
                    TableNames.userT, TableNames.roleT,
                    TableNames.responseT, formid);
        } else {
            sql = String.format(
                    "SELECT u.user_id,u.email,u.user_role FROM %s u JOIN %s r ON u.user_role=r.role_id WHERE user_id IN (SELECT user_id FROM %s,%s WHERE %s.group_id=%s.group_id AND form_id=%d) AND user_id NOT IN (SELECT user_id FROM %s WHERE form_id=%d)",
                    TableNames.userT, TableNames.roleT,
                    TableNames.opentoT, TableNames.memberT,
                    TableNames.opentoT, TableNames.memberT, formid, TableNames.responseT, formid);
        }

        return jdbcTemplate.query(sql, new AbstractUserRowMapper());
    }

    public Form saveFormWithQuestions(FormQuestions formQuestions) {
        if (!formQuestions.getQuestions().isEmpty()) {
            Form createdForm = createForm(formQuestions.getForm());
            questionRepository.addQuestionsWithOptionsToNewForm(formQuestions.getQuestions(), createdForm.getForm_id());

            if (formQuestions.getConditional_orderings() != null) {
                conditionalRepository.saveOrderings(formQuestions.getConditional_orderings(), createdForm.getForm_id());
            }

            // if(!formQuestions.getConditional_orderings().isEmpty()) {
            // formQuestions.getConditional_orderings().sort((c1,c2) -> {});
            // }

            // for (ConditionalOrdering conditionalOrdering :
            // formQuestions.getConditional_orderings()) {
            // conditionalOrdering.setForm_id(formQuestions.getForm().getForm_id());
            // try {
            // conditionalRepository.saveOrdering(conditionalOrdering);
            // } catch (Exception e) {
            // System.out.println("Skipped saving conditional for: " +
            // conditionalOrdering.getForm_id() + ","
            // + conditionalOrdering.getQuestion_id() + "," +
            // conditionalOrdering.getCondition_id());
            // }
            // }

            return createdForm;
            // System.out.println("Created form ID: " + createdForm.getForm_id()); // Debug
            // print
        } else {
            throw new IllegalArgumentException("Cannot save empty form!");
        }
    }

    public Map<Long, Map<String, Object>> getAllFormResponses(long formid, boolean export_enable) {
        String sql = String.format(
                "SELECT q.question_id, q.prompt, q.data_type, r.response_int, r.response_string, r.response_float, " +
                        "r.user_id, d.uses_options, d.mapped_to " +
                        "FROM %s q " +
                        "JOIN %s d ON q.data_type=d.type_id " +
                        "JOIN %s r ON q.form_id=r.form_id AND q.question_id=r.question_id " +
                        "WHERE q.form_id=?",
                TableNames.quesT, TableNames.typeT, TableNames.quesRespT);

        List<UserResponseData> allResponses = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(UserResponseData.class), formid);
        // "qid": {"prompt", "response":{"email": "answer"}};

        Map<Long, Map<String, Object>> formResponseData = new HashMap<>();

        for (UserResponseData response : allResponses) {
            // System.out.println(response);
            long questionId = response.getQuestion_id();

            Map<String, Object> questionData = formResponseData.computeIfAbsent(questionId, k -> new HashMap<>());

            if (!questionData.containsKey("prompt")) {
                questionData.put("prompt", response.getPrompt());
                questionData.put("responses", new ArrayList<Map<String, String>>());
            }

            // Check if need to use options
            String userResponse = null;
            if (response.isUses_options()) {
                String optionSql = String
                        .format("SELECT option_value_int, option_value_string, option_value_float FROM %s " +
                                "WHERE question_id = ? AND form_id = ? AND option_id = ?", TableNames.optionsT);
                Map<String, Object> option = jdbcTemplate.queryForMap(optionSql, response.getQuestion_id(), formid,
                        response.getResponse_int());

                if ("I".equals(response.getMapped_to())) {
                    userResponse = String.valueOf(option.get("option_value_int"));
                } else if ("F".equals(response.getMapped_to())) {
                    userResponse = String.valueOf(option.get("option_value_float"));
                } else if ("S".equals(response.getMapped_to())) {
                    userResponse = (String) option.get("option_value_string");
                }
            } else {
                if ("I".equals(response.getMapped_to())) {
                    userResponse = String.valueOf(response.getResponse_int());
                } else if ("F".equals(response.getMapped_to())) {
                    userResponse = String.valueOf(response.getResponse_float());
                } else if ("S".equals(response.getMapped_to())) {
                    userResponse = response.getResponse_string();
                }
            }

            // Add the user response
            Map<String, String> userResponseData = new HashMap<>();
            userResponseData.put("email", userRepository.findByIdDTO(response.getUser_id()).getEmail());
            userResponseData.put("response", userResponse);

            // Add to responses list
            List<Map<String, String>> responses = (List<Map<String, String>>) questionData.get("responses");
            // System.out.println(responses);
            responses.add(userResponseData);
        }
        if(export_enable){
            //?  WARN: Change the value of EXPORT DIRECTORY when using this 
            exportToCSV(formResponseData, formid);
        }
        return formResponseData;
    }

    @Value("${export.directory}")
    private String baseDir;

    private void exportToCSV(Map<Long, Map<String, Object>> formResponseData, long formid) {
        // CSV file path
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = baseDir + "/form_" + formid + "_responses_" + timestamp + ".csv";

        try {
            Files.createDirectories(Paths.get(filePath).getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        try (Writer writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                 .setHeader("Question ID", "Prompt", "Email", "Response")
                 .build())) {
    
            for (Map.Entry<Long, Map<String, Object>> entry : formResponseData.entrySet()) {
                long questionId = entry.getKey();
                Map<String, Object> questionData = entry.getValue();
                String prompt = (String) questionData.get("prompt");
                List<Map<String, String>> responses = (List<Map<String, String>>) questionData.get("responses");
    
                for (Map<String, String> response : responses) {
                    String email = response.get("email");
                    String userResponse = response.get("response");
    
                    // Write data to the CSV
                    csvPrinter.printRecord(questionId, prompt, email, userResponse);
                }
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

}
