package com.batavia.orm.generator;

import java.util.Optional;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableCategory;
import com.batavia.orm.utils.Utils;

// Strategy PATTERN

public class GeneratorMain {
    private Table tableToBeApplied;
    private Column[] columnsToBeApplied;
    private String migrationFileName;

    public GeneratorMain(Table table, String migrationFileName) {
        this.tableToBeApplied = table;
        this.migrationFileName = migrationFileName;
    }

    public GeneratorMain(Table table, Column[] columns, String migrationFileName) {
        this.tableToBeApplied = table;
        this.migrationFileName = migrationFileName;
        this.columnsToBeApplied = columns;
    }

    public void runSqlScriptGenerator (
        SqlCommandCategory sqlCommand,
        String fileName,
        AlterTableCategory optionalAlterTableCategory
    ) {
        String resultScript = "";

        Optional<AlterTableCategory> optionalParameter = Optional.ofNullable(optionalAlterTableCategory);
        AlterTableCategory alterTableCategory = optionalParameter.isPresent() ? optionalParameter.get() : null;

        // loop through and accumulate the resultScript
        sqlCommand.runSqlScriptGenerator(this.tableToBeApplied, this.columnsToBeApplied, this.migrationFileName,
                alterTableCategory);

        // write resultSCript to migration file
        Utils.writeToMigrationFile(fileName, resultScript);
    }
}
