package com.dsxx.base.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;

/**
 * @author slm
 * @date 2018/6/14
 */
public class JdbcTemplateUtils {

    /**
     * 插入返回主键
     * @param jdbcTemplate
     * @param sql
     * @param params
     * @return Long
     * @author slm
     * @date 2018/06/14
     */
    public static Long insertReturnId(JdbcTemplate jdbcTemplate, String sql, Object[] params){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,new String[]{ "id" });
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i+1, params[i]);
            }
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
