package com.example.survey.data_access_layers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.analytics.AnalysisReturnObject;
import com.example.survey.analytics.DynamicInvoker;
import com.example.survey.data_transfer_objects.AnalyticFunctionRecord;
import com.example.survey.data_transfer_objects.QuestionDTO;
import com.example.survey.entities.DataType;
import com.example.survey.utilities.TableNames;

@Repository
public class AnalyticFunctionRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private QuestionRepository questionRepository;

    public List<AnalyticFunctionRecord> getAllFunctions() {
        String sql = String.format("SELECT * FROM %s", TableNames.funcT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AnalyticFunctionRecord.class));
    }

    public List<AnalyticFunctionRecord> getFunctionsForDataType(int data_id) {
        String sql = String.format("SELECT f.* FROM %s f,%s a WHERE a.func_id=f.func_id AND a.type_id=?",
                TableNames.funcT, TableNames.analysedByT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AnalyticFunctionRecord.class), data_id);
    }

    public List<AnalyticFunctionRecord> getFunctionsForDataType(DataType dataType) {
        return getFunctionsForDataType(dataType.getType_id());
    }

    public List<AnalysisReturnObject> getAnalysisForList(int data_id, List<?> list) {
        List<AnalyticFunctionRecord> funcs = getFunctionsForDataType(data_id);
        List<AnalysisReturnObject> results = new ArrayList<>();
        DynamicInvoker dynamicInvoker;
        for (AnalyticFunctionRecord func : funcs) {
            try {
                // System.out.println("Trying function: " + func.getFunc_name());
                // System.out.println(list);
                dynamicInvoker = new DynamicInvoker(func.getFunc_path());
                AnalysisReturnObject analysisReturnObject = dynamicInvoker.invokeFunc(list);
                analysisReturnObject.setName(func.getFunc_name());
                results.add(analysisReturnObject);
            } catch (Exception e) {
                // System.out.println("Failed function: " + func.getFunc_name());
                e.printStackTrace();
            }
        }
        return results;
    }

    public List<AnalysisReturnObject> getAnalysisForQuestion(long form_id, long question_id) {
        QuestionDTO question = questionRepository.getQuestion(form_id, question_id);
        DataType dataType = question.getDataType();
        List<?> list;
        String put;
        Class<?> cl;

        if (dataType.getMappedTo() == 1) {
            cl = Integer.class;
            put = "int";
        } else if (dataType.getMappedTo() == 2) {
            cl = Float.class;
            put = "float";
        } else {
            cl = String.class;
            put = "string";
        }
        put = (dataType.isUsesOptions() ? "o.option_value_" : "qr.response_") + put;
        String optionssql = String.format(
                "SELECT %s FROM %s qr JOIN %s o ON qr.question_id=o.question_id AND qr.form_id=o.form_id WHERE qr.form_id=? AND qr.question_id=? AND qr.response_int=o.option_id",
                put,
                TableNames.quesRespT, TableNames.optionsT),
                normalsql = String.format("SELECT %s FROM %s qr WHERE qr.form_id=? AND qr.question_id=?", put,
                        TableNames.quesRespT);

        list = jdbcTemplate.queryForList(dataType.isUsesOptions() ? optionssql : normalsql, cl,
                new Object[] { form_id,
                        question_id });
        // System.out.println(list);
        return getAnalysisForList(dataType.getType_id(), list);
    }

    public List<AnalysisReturnObject> getAnalysisForQuestion(QuestionDTO question) {
        // QuestionDTO question = questionRepository.getQuestion(form_id, question_id);
        DataType dataType = question.getDataType();
        List<?> list;
        String put;
        Class<?> cl;

        if (dataType.getMappedTo() == 1) {
            cl = Integer.class;
            put = "int";
        } else if (dataType.getMappedTo() == 2) {
            cl = Float.class;
            put = "float";
        } else {
            cl = String.class;
            put = "string";
        }
        put = (dataType.isUsesOptions() ? "o.option_value_" : "qr.response_") + put;
        String optionssql = String.format(
                "SELECT %s FROM %s qr JOIN %s o ON qr.question_id=o.question_id AND qr.form_id=o.form_id WHERE qr.form_id=? AND qr.question_id=? AND qr.response_int=o.option_id",
                put,
                TableNames.quesRespT, TableNames.optionsT),
                normalsql = String.format("SELECT %s FROM %s qr WHERE qr.form_id=? AND qr.question_id=?", put,
                        TableNames.quesRespT);

        list = jdbcTemplate.queryForList(dataType.isUsesOptions() ? optionssql : normalsql, cl,
                new Object[] { question.getForm_id(),
                        question.getQuestion_id() });
        // System.out.println(list);
        return getAnalysisForList(dataType.getType_id(), list);
    }
}
