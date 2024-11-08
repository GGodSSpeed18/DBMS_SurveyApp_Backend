package com.example.survey.data_access_layers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.data_transfer_objects.ConditionalOrdering;
import com.example.survey.utilities.TableNames;

@Repository
public class ConditionalRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ResourceAvailabilityRepository resourceAvailabilityRepository;

    public List<ConditionalOrdering> getFormOrderings(long form_id) {
        resourceAvailabilityRepository.checkFormExistence(form_id);
        String sql = String.format("SELECT * FROM %s WHERE form_id=?", TableNames.conditionT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ConditionalOrdering.class),
                new Object[] { form_id });
    }

    public ConditionalOrdering getOrdering(long form_id, long question_id, long condition_id) {
        String sql = String.format("SELECT * FROM %s WHERE form_id=? AND question_id=? AND condition_id=?",
                TableNames.conditionT);
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ConditionalOrdering.class),
                new Object[] { form_id, question_id, condition_id });
    }

    public int isConditional(long form_id, long question_id) {
        String sql1 = String.format("SELECT COUNT(*) FROM %s WHERE form_id=? AND question_id=?", TableNames.conditionT);
        String sql2 = String.format(
                "SELECT COUNT(*) FROM %s WHERE form_id=? AND (question_alt=? || question_default=?)",
                TableNames.conditionT);
        Integer count1 = jdbcTemplate.queryForObject(sql1, Integer.class, new Object[] { form_id, question_id });
        Integer count2 = jdbcTemplate.queryForObject(sql2, Integer.class,
                new Object[] { form_id, question_id, question_id });
        int ans = 0;
        if (count1 != null && count1 > 0)
            ans |= 1;
        if (count2 != null && count2 > 0)
            ans |= 2;
        return ans;
    }

    public int getMaxConditionId(long form_id, long question_id) {
        String sql = String.format(
                "SELECT COALESCE(MAX(condition_id), 0) + 1 FROM %s WHERE form_id = ? AND question_id=?",
                TableNames.conditionT);
        return jdbcTemplate.queryForObject(sql, int.class, new Object[] { form_id, question_id });
    }

    public ConditionalOrdering saveOrdering(ConditionalOrdering conditionalOrdering) {
        String sql = String.format("INSERT INTO %s VALUES (?,?,?,?,?,?,?,?)", TableNames.conditionT);
        conditionalOrdering.setCondition_id(
                getMaxConditionId(conditionalOrdering.getForm_id(), conditionalOrdering.getQuestion_id()));
        jdbcTemplate.update(sql, new Object[] {
                conditionalOrdering.getForm_id(),
                conditionalOrdering.getQuestion_id(),
                conditionalOrdering.getCondition_id(),
                conditionalOrdering.getCompare_value_int(),
                conditionalOrdering.getCompare_value_float(),
                conditionalOrdering.getCompare_value_string(),
                conditionalOrdering.getQuestion_default(),
                conditionalOrdering.getQuestion_alt()
        });
        return conditionalOrdering;
    }

    public static List<ConditionalOrdering> sortAndRemoveDuplicates(List<ConditionalOrdering> conditionalOrderings) {
        conditionalOrderings.sort(Comparator
                .comparing(ConditionalOrdering::getQuestion_id)
                .thenComparing(ConditionalOrdering::getQuestion_default)
                .thenComparing(ConditionalOrdering::getQuestion_alt));

        Map<Long, Set<Long>> seenMap = new HashMap<>();

        return conditionalOrderings.stream()
                .filter(obj -> {
                    long questionId = obj.getQuestion_id();
                    long defaultQuestion = obj.getQuestion_default();
                    long altQuestion = obj.getQuestion_alt();

                    Set<Long> seenSet = seenMap.computeIfAbsent(questionId, k -> new HashSet<>());

                    if (seenSet.contains(defaultQuestion) || seenSet.contains(altQuestion)) {
                        return false;
                    }

                    seenSet.add(defaultQuestion);
                    seenSet.add(altQuestion);
                    return true;
                })
                .collect(Collectors.toList());
    }

    public void saveOrderings(List<ConditionalOrdering> conditionalOrderings, long form_id) {
        if (conditionalOrderings.isEmpty()) {
            return;
        }
        StringBuilder sql = new StringBuilder(
                "INSERT INTO " + TableNames.conditionT
                        + " (form_id, question_id, condition_id, compare_value_int, compare_value_float, compare_value_string, question_default, question_alt) VALUES ");
        List<Object> params = new ArrayList<>();
        List<ConditionalOrdering> filteredList = sortAndRemoveDuplicates(conditionalOrderings);
        long cn = 1;
        ConditionalOrdering last = filteredList.get(0);
        for (int i = 0; i < filteredList.size(); i++) {
            ConditionalOrdering obj = filteredList.get(i);
            if (i > 0) {
                cn = last.getQuestion_id() == obj.getQuestion_id() ? cn + 1 : 1;
            } else {
                cn = 1;
            }
            last = obj;
            sql.append("(?,?,?,?,?,?,?,?),");
            params.add(form_id);
            params.add(obj.getQuestion_id());
            params.add(cn);
            params.add(obj.getCompare_value_int());
            params.add(obj.getCompare_value_float());
            params.add(obj.getCompare_value_string());
            params.add(obj.getQuestion_default());
            params.add(obj.getQuestion_alt());
        }
        sql.setLength(sql.length() - 1);
        jdbcTemplate.update(sql.toString(), params.toArray());
        return;
    }

    // public ConditionalOrdering saveOrdering(ConditionalOrdering
    // conditionalOrdering) {

    // }
}
