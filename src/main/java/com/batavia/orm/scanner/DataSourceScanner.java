package com.batavia.orm.scanner;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import com.batavia.orm.annotations.Entity;
import com.batavia.orm.annotations.EntityColumn;
import com.batavia.orm.annotations.PrimaryColumn;
import com.batavia.orm.annotations.Unique;

import com.batavia.orm.commons.Table;
import com.batavia.orm.commons.Column;

import com.github.javaparser.StaticJavaParser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public class DataSourceScanner {
  private String dataSourcePath;

  public DataSourceScanner(String dataSourcePath) {
    this.dataSourcePath = dataSourcePath;
  }

  public HashMap<String, Table> findAllEntities() throws IOException {
    HashMap<String, Table> tables = new HashMap<String, Table>();
    ArrayList<ClassOrInterfaceDeclaration> entities = this.findAllEntityClass();

    for (ClassOrInterfaceDeclaration entity : entities) {
      Table table = this.convertEntityToTable(entity);
      tables.put(table.getTableName(), table);
    }
    return tables;
  }

  private Table convertEntityToTable(ClassOrInterfaceDeclaration entity) {
    String entityName = this.pascalCaseToSnakeCase(entity.getNameAsString());
    Table table = new Table(entityName);
    entity.getMembers().forEach(entityMember -> {
      if (entityMember.isAnnotationPresent(EntityColumn.class)) {
        FieldDeclaration entityField = entityMember.asFieldDeclaration();
        VariableDeclarator fieldVar = entityField.getVariable(0);

        Column tableColumn = new Column(this.camelCaseToSnakeCase(fieldVar.getNameAsString()),
            fieldVar.getTypeAsString());
        if (entityMember.isAnnotationPresent(PrimaryColumn.class)) {
          tableColumn.setIsPrimaryColumn(true);
          tableColumn.setIsUnique(true);
        }
        if (entityMember.isAnnotationPresent(Unique.class)) {
          tableColumn.setIsUnique(true);
        }
        table.addColumn(tableColumn);
      }
    });
    return table;
  }

  private ArrayList<ClassOrInterfaceDeclaration> findAllEntityClass() throws IOException {
    ArrayList<ClassOrInterfaceDeclaration> entities = new ArrayList<ClassOrInterfaceDeclaration>();
    Stream<Path> paths = Files.walk(Paths.get(this.dataSourcePath));
    paths
        .filter(p -> isJavaFile(p))
        .forEach(p -> {
          try {
            filterEntityClasses(p, entities);
          } catch (FileNotFoundException e) {
            System.out.println("ERR:FILE-NOT-FOUND");
          }
        });
    paths.close();
    return entities;
  }

  private boolean isJavaFile(Path path) {
    return Files.isRegularFile(path) && path.toString().endsWith(".java");
  }

  private void filterEntityClasses(Path path, ArrayList<ClassOrInterfaceDeclaration> filteredEntities)
      throws FileNotFoundException {
    CompilationUnit cUnit = StaticJavaParser.parse(path.toFile());
    cUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(clazz -> {
      if (clazz.isAnnotationPresent(Entity.class))
        filteredEntities.add(clazz);
    });
  }

  private String camelCaseToSnakeCase(String camelCaseString) {
    StringBuilder snakeCaseString = new StringBuilder();
    for (int i = 0; i < camelCaseString.length(); i++) {
      char c = camelCaseString.charAt(i);
      if (i != 0 && Character.isUpperCase(c)) {
        snakeCaseString.append("_");
        snakeCaseString.append(Character.toLowerCase(c));
      } else {
        snakeCaseString.append(Character.toLowerCase(c));
      }
    }
    return snakeCaseString.toString();
  }

  private String pascalCaseToSnakeCase(String pascalCaseString) {
    StringBuilder snakeCaseString = new StringBuilder();
    for (int i = 0; i < pascalCaseString.length(); i++) {
      char c = pascalCaseString.charAt(i);
      if (i != 0 && Character.isUpperCase(c)) {
        snakeCaseString.append("_");
        snakeCaseString.append(Character.toLowerCase(c));
      } else {
        snakeCaseString.append(Character.toLowerCase(c));
      }
    }
    return snakeCaseString.toString();
  }

}