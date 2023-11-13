package com.batavia.orm.generator;

import java.util.Optional;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableCategory;
import com.batavia.orm.utils.Utils;

// Strategy PATTERN

public class GeneratorMain {
    private Table tableToBeApplied;
    private Column[] columnsToBeApplied;
    private String migrationFilePath;

    public GeneratorMain(Table table, String migrationFilePath) {
        this.tableToBeApplied = table;
        this.migrationFilePath = migrationFilePath;
    }

    public GeneratorMain(Table table, Column[] columns, String migrationFilePath) {
        this.tableToBeApplied = table;
        this.migrationFilePath = migrationFilePath;
        this.columnsToBeApplied = columns;
    }

    public void runSqlScriptGeneratorToFile(
            SqlCommandCategory sqlCommand,
            AlterTableCategory optionalAlterTableCategory) {

        Optional<AlterTableCategory> optionalParameter = Optional.ofNullable(optionalAlterTableCategory);
        AlterTableCategory alterTableCategory = optionalParameter.isPresent() ? optionalParameter.get() : null;

        String resultScript = sqlCommand.runSqlScriptGenerator(this.tableToBeApplied, this.columnsToBeApplied,
                alterTableCategory);

        // write resultSCript to migration file
        Utils.writeToMigrationFile(this.migrationFilePath, resultScript);
    }
}
