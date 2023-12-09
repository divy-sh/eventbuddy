package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class QueryManager {

  @Autowired
  JdbcTemplate jdbcTemplate;

  public <T> List<T> runQuery(String query, Class<T> classType, Object... params)
      throws BuddyError {
    try {
      SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query, (Object[]) params);
      if (classType == null) {
        return null;
      }
      return processRowSet(rowSet, classType);
    } catch (Exception e) {
      if (e.getCause() instanceof SQLException) {
        throw new BuddyError(e.getCause().getMessage());
      }
      throw new BuddyError(e.getMessage());
    }
  }

  public <T> List<T> runQuery(String query, Class<T> classType) throws BuddyError {
    try {
      SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);
      return processRowSet(rowSet, classType);
    } catch (Exception e) {
      if (e.getCause() instanceof SQLException) {
        throw new BuddyError(e.getCause().getMessage());
      }
      throw new BuddyError(e.getMessage());
    }
  }

  public void execute(String query) throws BuddyError {
    try {
      jdbcTemplate.execute(query);
    } catch (Exception e) {
      if (e.getCause() instanceof SQLException) {
        throw new BuddyError(e.getCause().getMessage());
      }
      throw new BuddyError(e.getMessage());
    }
  }

  public int update(String query) throws BuddyError {
    try {
      return jdbcTemplate.update(query);
    } catch (Exception e) {
      if (e.getCause() instanceof SQLException) {
        throw new BuddyError(e.getCause().getMessage());
      }
      throw new BuddyError(e.getMessage());
    }
  }

  public int update(String query, Object... params) throws BuddyError {
    try {
    return jdbcTemplate.update(query, params);
    } catch (Exception e) {
      if (e.getCause() instanceof SQLException) {
        throw new BuddyError(e.getCause().getMessage());
      }
      throw new BuddyError(e.getMessage());
    }
  }

  private <T> List<T> processRowSet(SqlRowSet rowSet, Class<T> classType) {
    List<T> list = new ArrayList<>();
    while (rowSet.next()) {
      Map<String, Object> data = new HashMap<>();
      for (String col : rowSet.getMetaData().getColumnNames()) {
        data.put(col, rowSet.getObject(col));
      }
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      list.add(objectMapper.convertValue(data, classType));
    }
    return list;
  }
}
