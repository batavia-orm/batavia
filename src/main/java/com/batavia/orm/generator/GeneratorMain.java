package com.batavia.orm.generator;

import java.util.HashMap;

import com.batavia.orm.commons.Column;
import com.batavia.orm.commons.Table;

// Strategy PATTERN

public class GeneratorMain {
    public static void main(String[] args) {

        // migration file generator

        // step 1 initialize
        HashMap<String, Table> tables = new HashMap<String, Table>();
        Table employer = new Table("employer");
        employer.addColumn(new Column("id", "uuid", true, true));
        employer.addColumn(new Column("employer_name", "VARCHAR(200)", false, false));

        Table company = new Table("company");
        company.addColumn(new Column("id", "uuid", true, true));
        company.addColumn(new Column("company_name", "VARCHAR(250)", false, false));

        tables.put(employer.getTableName(), employer);
        tables.put(company.getTableName(), company);

        // step 2 generate
        // 1- CREATE TABLE MIGRATION FILE
        CreateTableMigrationFileGenerator createTableMigrationFile = new CreateTableMigrationFileGenerator();
        String fileName1 = "create-table-employer";
        String fileName2 = "";
        createTableMigrationFile.generateMigrationFile(employer, fileName1, null);
        createTableMigrationFile.generateMigrationFile(company, fileName2, null);

        // 2 - ALTER TABLE MIGRATION FILE
        AlterTableMigrationFileGenerator alterTableAddColumnMigrationFile = new AlterTableMigrationFileGenerator("addColumn");
        AlterTableMigrationFileGenerator alterTableDropColumnMigrationFile = new AlterTableMigrationFileGenerator("dropColumn");
        String fileName3 = "alter-table-employer";
        Column[] columnsToAdd = new Column[2];
        columnsToAdd[0] = new Column("email", "VARCHAR(150)", false, false);
        columnsToAdd[1] = new Column("age", "INT", false, true);

        Column[] columnsToDrop = new Column[1];
        columnsToDrop[0] = company.getColumns().get("company_name");
        alterTableAddColumnMigrationFile.generateMigrationFile(employer, fileName3, columnsToAdd);
        alterTableDropColumnMigrationFile.generateMigrationFile(company, "", columnsToDrop);

        // 3 - DROP TABLE MIGRATION FILE
        DropTableMigrationFileGenerator dropTableMigrationFile = new DropTableMigrationFileGenerator();
        String fileName4 = "drop-table-employer";
        dropTableMigrationFile.generateMigrationFile(employer, fileName4, null);
    }
}
