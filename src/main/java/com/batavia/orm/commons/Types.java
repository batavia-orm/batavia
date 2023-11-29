package com.batavia.orm.commons;

import java.util.HashMap;

public class Types {
  private HashMap<String, String> javaToSqlTypeMap = new HashMap<String, String>() {{
    put("String", "VARCHAR");
    put("Integer", "INT");
    put("int", "INT");
    put("Long", "BIGINT");
    put("long", "BIGINT");
    put("Double", "DOUBLE");
    put("double", "DOUBLE");
    put("Float", "FLOAT");
    put("float", "FLOAT");
    put("Boolean", "BOOLEAN");
    put("boolean", "BOOLEAN");
    put("Date", "DATE");
    put("LocalDate", "DATE");
    put("Time", "TIME");
    put("LocalTime", "TIME");
    put("Timestamp", "TIMESTAMP");
    put("LocalDateTime", "TIMESTAMP");
  }};

  public static String getSqlType(String javaType) {
    Types types = new Types();
    return types.javaToSqlTypeMap.get(javaType);
  }
}
