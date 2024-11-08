package com.example.survey.data_access_layers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.data_transfer_objects.Option;
import com.example.survey.data_transfer_objects.QuestionDTO;
import com.example.survey.entities.DataType;
import com.example.survey.entities.Question;
import com.example.survey.mappers.OptionsRowMapper;
import com.example.survey.mappers.QuestionDataTypeRowMapper;
import com.example.survey.utilities.TableNames;

@Repository
public class QuestionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataTypeRepository dataTypeRepository;
    @Autowired
    private ResourceAvailabilityRepository resourceAvailabilityRepository;

    // ! Depricated use getAllQuestionsWithOptions
    public List<Question> getAllQuestions(long id) {
        resourceAvailabilityRepository.checkFormExistence(id);
        String sql = String.format("SELECT * FROM %s WHERE form_id=?", TableNames.quesT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Question.class), new Object[] { id });
    }

    public List<QuestionDTO> getAllQuestionsWithOptions(long formid) {
        resourceAvailabilityRepository.checkFormExistence(formid);
        String sql = String.format(
                "SELECT q.form_id,q.question_id,q.data_type,q.required,q.prompt,q.min_val AS q_min_val,q.max_val AS q_max_val,q.conditional,d.type_id,d.type_name,d.type_desc,d.max_val AS d_max_val,d.min_val AS d_min_val,d.mapped_to,d.uses_options FROM %s q,%s d WHERE form_id=? AND q.data_type=d.type_id",
                TableNames.quesT, TableNames.typeT);
        List<QuestionDTO> questions = jdbcTemplate.query(sql, new QuestionDataTypeRowMapper(), new Object[] { formid });
        for (QuestionDTO question : questions) {
            if (question.getDataType().isUsesOptions()) {
                sql = String.format("SELECT * FROM %s WHERE form_id=? AND question_id=?", TableNames.optionsT);
                question.setOptions(jdbcTemplate.query(sql, new OptionsRowMapper(),
                        new Object[] { formid, question.getQuestion_id() }));
            }
        }
        return questions;
    }

    public QuestionDTO getQuestion(long form_id, long question_id) {
        resourceAvailabilityRepository.checkQuestionExistence(form_id, question_id);
        String sql = String.format(
                "SELECT q.form_id,q.question_id,q.data_type,q.required,q.prompt,q.min_val AS q_min_val,q.max_val AS q_max_val,q.conditional,d.type_id,d.type_name,d.type_desc,d.max_val AS d_max_val,d.min_val AS d_min_val,d.mapped_to,d.uses_options FROM %s q,%s d WHERE form_id=? AND question_id=? AND q.data_type=d.type_id",
                TableNames.quesT, TableNames.typeT);
        QuestionDTO question = jdbcTemplate.queryForObject(sql, new QuestionDataTypeRowMapper(),
                new Object[] { form_id, question_id });
        return question;
    }

    // ! Depricated, use addQuesWithOptionsToForm instead
    public void addQuesToForm(Question newQ, long id) {
        resourceAvailabilityRepository.checkFormExistence(id);
        String sql = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?, ?)", TableNames.quesT);
        String sql2 = String.format("SELECT COALESCE(MAX(question_id), 0) + 1 FROM %s WHERE form_id = %d",
                TableNames.quesT, id);
        int qid = jdbcTemplate.queryForObject(sql2, Integer.class);
        try {
            jdbcTemplate.update(sql,
                    new Object[] {
                            qid,
                            id,
                            newQ.getData_type(),
                            newQ.isRequired(),
                            newQ.getPrompt(),
                            newQ.getMin_val(),
                            newQ.getMax_val(),
                            newQ.isConditional()
                    });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Integrity Constraints Violated !", e);
        }
    }

    public void addQuesWithOptionsToForm(QuestionDTO newQ, long formid) {
        resourceAvailabilityRepository.checkFormExistence(formid);
        DataType dataType = dataTypeRepository.findById(newQ.getData_type());

        String sql = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?, ?)", TableNames.quesT);
        String sql2 = String.format("SELECT COALESCE(MAX(question_id), 0) + 1 FROM %s WHERE form_id = %d",
                TableNames.quesT, formid);
        int qid = jdbcTemplate.queryForObject(sql2, Integer.class);
        StringBuilder option_sql = new StringBuilder(
                "INSERT INTO " + TableNames.optionsT
                        + " (form_id, question_id, option_id, option_value_int, option_value_string, option_value_float) VALUES ");
        List<Object> params = new ArrayList<>();

        if (dataType.isUsesOptions()) {
            if (newQ.getOptions().isEmpty()) {
                throw new IllegalStateException("Expected non-empty options list!");
            }
            int mt = dataType.getMappedTo();
            long opnum = 1;
            for (Option option : newQ.getOptions()) {
                if (mt == 1 && option.getOption_int() == null) {
                    throw new IllegalStateException("Expected non-null option int value for multiple choice!");
                } else if (mt == 2 && option.getOption_float() == null) {
                    throw new IllegalStateException("Expected non-null option float value for multiple choice!");
                } else if (mt == 3 && option.getOption_string() == null) {
                    throw new IllegalStateException("Expected non-null option string value for multiple choice!");
                }
                // option_sql.append("(?, ?, ?, ?, ?, ?, ?),");
                option_sql.append("(?, ?, ?, ?, ?, ?),");
                params.add(formid);
                params.add(qid);
                params.add(opnum++);
                params.add(option.getOption_int());
                params.add(option.getOption_string());
                params.add(option.getOption_float());
                // params.add(option.getSpecial_option() != null && option.getSpecial_option());
            }
            option_sql.setLength(option_sql.length() - 1);
        }
        try {
            jdbcTemplate.update(sql,
                    new Object[] {
                            qid,
                            formid,
                            newQ.getData_type(),
                            newQ.isRequired(),
                            newQ.getPrompt(),
                            newQ.getMin_val(),
                            newQ.getMax_val(),
                            newQ.isConditional()
                    });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Integrity Constraints Violated !", e);
        }

        if (dataType.isUsesOptions()) {
            jdbcTemplate.update(option_sql.toString(), params.toArray());
        }
    }

    public void addQuestionsToForm(List<Question> questions, long formId) {
        // Base INSERT statement
        StringBuilder sql = new StringBuilder(
                "INSERT INTO " + TableNames.quesT
                        + " (form_id, question_id, data_type, required, prompt, min_val, max_val, conditional) VALUES ");
        List<Object> params = new ArrayList<>();
        // params.add(TableNames.quesT);

        // Populate VALUES placeholders and parameters
        long qn = 1;
        for (Question question : questions) {
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?),");
            params.add(formId); // form_id
            params.add(qn++); // qn (question number)
            params.add(question.getData_type()); // data_type
            params.add(question.isRequired()); // required
            params.add(question.getPrompt()); // prompt
            params.add(question.getMin_val()); // min_val
            params.add(question.getMax_val()); // max_val
            params.add(question.isConditional()); // conditional
        }

        // Remove trailing comma and finalize the query
        sql.setLength(sql.length() - 1);

        // Execute the batch insert query
        jdbcTemplate.update(sql.toString(), params.toArray());
    }

    public void addQuestionsWithOptionsToNewForm(List<QuestionDTO> questions, long formid) {
        StringBuilder sql = new StringBuilder(
                "INSERT INTO " + TableNames.quesT
                        + " (form_id, question_id, data_type, required, prompt, min_val, max_val, conditional) VALUES ");
        List<Object> params = new ArrayList<>();
        StringBuilder option_sql = new StringBuilder(
                "INSERT INTO " + TableNames.optionsT
                        + " (form_id, question_id, option_id, option_value_int, option_value_string, option_value_float) VALUES ");
        List<Object> option_params = new ArrayList<>();
        DataType datatype;
        boolean options_used = false;
        long qid = 1;
        for (QuestionDTO question : questions) {
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?),");
            params.add(formid);
            params.add(qid);
            params.add(question.getData_type());
            params.add(question.isRequired());
            params.add(question.getPrompt());
            params.add(question.getMin_val());
            params.add(question.getMax_val());
            params.add(question.isConditional());
            datatype = dataTypeRepository.findById(question.getData_type());
            if (datatype.isUsesOptions()) {
                options_used = true;
                if (question.getOptions().isEmpty()) {
                    throw new IllegalStateException("Expected non-empty options list!");
                }
                int mt = datatype.getMappedTo();
                long opnum = 1;
                for (Option option : question.getOptions()) {
                    if (mt == 1 && option.getOption_int() == null) {
                        throw new IllegalStateException("Expected non-null option int value for multiple choice!");
                    } else if (mt == 2 && option.getOption_float() == null) {
                        throw new IllegalStateException("Expected non-null option float value for multiple choice!");
                    } else if (mt == 3 && option.getOption_string() == null) {
                        throw new IllegalStateException("Expected non-null option string value for multiple choice!");
                    }
                    option_sql.append("(?, ?, ?, ?, ?, ?),");
                    // option_sql.append("(?, ?, ?, ?, ?, ?, ?),");
                    option_params.add(formid);
                    option_params.add(qid);
                    option_params.add(opnum++);
                    option_params.add(option.getOption_int());
                    option_params.add(option.getOption_string());
                    option_params.add(option.getOption_float());
                    // option_params.add(option.getSpecial_option() != null &&
                    // option.getSpecial_option());
                }
            }
            qid++;
        }
        // Remove trailing comma and finalize the query
        sql.setLength(sql.length() - 1);
        option_sql.setLength(option_sql.length() - 1);
        // Execute the batch insert query
        jdbcTemplate.update(sql.toString(), params.toArray());
        if (options_used) {
            jdbcTemplate.update(option_sql.toString(), option_params.toArray());
        }
    }

    public void dropQues(long id, long qid) {
        String sql = String.format("DELETE FROM %s WHERE form_id=? and question_id=?", TableNames.quesT);
        try {
            jdbcTemplate.update(sql, new Object[] { id, qid });
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Integrity Constraint Violated", e);
        }
    }

}
