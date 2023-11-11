package com.batavia.orm.generator;

import java.util.Optional;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableCategory;

// Strategy PATTERN

public class GeneratorMain {
    private Table tableToBeApplied;
    private Column[] columnsToBeApplied;
    private String migrationFileName;

    public GeneratorMain(Table table, Column[] columns, String migrationFileName) {
        this.tableToBeApplied = table;
        this.migrationFileName = migrationFileName;
        this.columnsToBeApplied = columns;
    }

    public void runMigrationFileGenerator(
        MigrationFileCategory sqlCommand,
        AlterTableCategory optionalAlterTableCategory
    ) {
        Optional<AlterTableCategory> optionalParameter = Optional.ofNullable(optionalAlterTableCategory);
        AlterTableCategory alterTableCategory = optionalParameter.isPresent() ? optionalParameter.get() : null;
        sqlCommand.runMigrationFileGenerator(this.tableToBeApplied, this.columnsToBeApplied, this.migrationFileName, alterTableCategory);
    }

    // TO BE REMOVED
    // public void generatorDemo() {
    //     // step 1 obtain input
    //     HashMap<String, Table> tables = new HashMap<String, Table>();
    //     Table employer = new Table("employer");
    //     employer.addColumn(new Column("id", "uuid", true, true));
    //     employer.addColumn(new Column("employer_name", "VARCHAR(200)", false, false));

    //     Table company = new Table("company");
    //     company.addColumn(new Column("id", "uuid", true, true));
    //     company.addColumn(new Column("company_name", "VARCHAR(250)", false, false));

    //     tables.put(employer.getTableName(), employer);
    //     tables.put(company.getTableName(), company);

    //     // step 2 generate
    //     // 1- CREATE TABLE MIGRATION FILE
    //     CreateTableMigrationFileGenerator createTableMigrationFile = new CreateTableMigrationFileGenerator();

    //     String fileName1 = "create-table-employer";
    //     String fileName2 = "";
    //     createTableMigrationFile.generateMigrationFile(employer, fileName1, null);
    //     createTableMigrationFile.generateMigrationFile(company, fileName2, null);

    //     // 2 - ALTER TABLE MIGRATION FILE
    //     AlterTableCategory addColumn = AlterTableCategory.ADD_COLUMN;
    //     AlterTableCategory dropColumn = AlterTableCategory.DROP_COLUMN;
    //     AlterTableMigrationFileGenerator alterTableAddColumnMigrationFile = new AlterTableMigrationFileGenerator(
    //             addColumn);
    //     AlterTableMigrationFileGenerator alterTableDropColumnMigrationFile = new AlterTableMigrationFileGenerator(
    //             dropColumn);
    //     String fileName3 = "alter-table-employer";
    //     Column[] columnsToAdd = new Column[2];
    //     columnsToAdd[0] = new Column("email", "VARCHAR(150)", false, false);
    //     columnsToAdd[1] = new Column("age", "INT", false, true);

    //     Column[] columnsToDrop = new Column[1];
    //     columnsToDrop[0] = company.getColumns().get("company_name");
    //     alterTableAddColumnMigrationFile.generateMigrationFile(employer, fileName3, columnsToAdd);
    //     alterTableDropColumnMigrationFile.generateMigrationFile(company, "", columnsToDrop);

    //     // 3 - DROP TABLE MIGRATION FILE
    //     DropTableMigrationFileGenerator dropTableMigrationFile = new DropTableMigrationFileGenerator();
    //     String fileName4 = "drop-table-employer";
    //     dropTableMigrationFile.generateMigrationFile(employer, fileName4, null);
    // }
}
