package com.batavia.orm.scanner;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import com.batavia.orm.annotations.*;
import com.batavia.orm.commons.Table;
import com.batavia.orm.commons.Column;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class DataSourceParser {
  public static void main(String[] args) throws IOException {
    DataSourceParser parser = new DataSourceParser("/Users/enryleinhard/Projects/JavaSourceCode");
    HashMap<String, Table> tables = parser.findAllEntities();
    for (String tableName : tables.keySet()) {
      Table table = tables.get(tableName);
      System.out.println(table.getTableName());
      for (Column column : table.getColumn().values()) {
        System.out.println(column.getColumnName() + " " + column.getColumnType() + " " + column.isPrimary() + " " + column.isUnique());
      }
    }
  }

  private String dataSourcePath;

  public DataSourceParser(String dataSourcePath) {
    this.dataSourcePath = dataSourcePath;
  }

  public HashMap<String, Table> findAllEntities() throws IOException {
    ArrayList<ClassOrInterfaceDeclaration> entities = findAllEntityClass(this.dataSourcePath);
    HashMap<String, Table> tables = new HashMap<String, Table>();
    for (ClassOrInterfaceDeclaration entity : entities) {
      Table table = convertEntityToTable(entity);
      tables.put(table.getTableName(), table);
    }
    return tables;
  }

  public Table convertEntityToTable(ClassOrInterfaceDeclaration entity) {
    String entityName = entity.getNameAsString();
    Table table = new Table(entityName);
    entity.getMembers().forEach(entityMember -> {
      VariableDeclarator variable = entityMember.asFieldDeclaration().getVariables().get(0);
      Boolean isPrimaryKey = entityMember.isAnnotationPresent(PrimaryColumn.class);
      Boolean isUnique = entityMember.isAnnotationPresent(PrimaryColumn.class);
      Column column = new Column(variable.getNameAsString(), variable.getTypeAsString(), isPrimaryKey, isUnique);
      table.addColumn(column);
    });
    return table;
  }

  public ArrayList<ClassOrInterfaceDeclaration> findAllEntityClass(String dataSourcePath) throws IOException {
    ArrayList<ClassOrInterfaceDeclaration> entities = new ArrayList<ClassOrInterfaceDeclaration>();
    Stream<Path> paths = Files.walk(Paths.get(dataSourcePath));
    paths
    .filter(Files::isRegularFile)
    .filter(p -> p.toString().endsWith(".java"))
    .forEach(p -> {
      try {
        CompilationUnit unit = StaticJavaParser.parse(p.toFile());
        List<ClassOrInterfaceDeclaration> classes = unit.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration clazz : classes) {
          if (clazz.isAnnotationPresent(Entity.class)) entities.add(clazz);
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    });
    paths.close();
    return entities;
  }
}