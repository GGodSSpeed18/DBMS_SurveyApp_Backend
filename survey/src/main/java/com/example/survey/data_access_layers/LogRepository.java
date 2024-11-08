package com.example.survey.data_access_layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.data_transfer_objects.ActionLog;
import com.example.survey.utilities.TableNames;

@Repository
public class LogRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveLog(ActionLog actionLog) {
        String sql = String.format("INSERT INTO %s (log_time,user_id,login,device,location) VALUES (?,?,?,?,?)",
                TableNames.logT);
        jdbcTemplate.update(sql, actionLog.getDate(), actionLog.getUser_id(), actionLog.getLogin(),
                actionLog.getDevice(), actionLog.getLocation());
    }
}
