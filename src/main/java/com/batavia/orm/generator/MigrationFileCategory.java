package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.migrationFileGenerators.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableCategory;

public enum MigrationFileCategory {
    CREATE_TABLE,
    DROP_TABLE,
    ALTER_TABLE,
    OTHERS;

    public void runMigrationFileGenerator(Table table, Column[] columns, String fileName,
            AlterTableCategory alterType) {

        if (this == CREATE_TABLE) {
            CreateTableMigrationFileGenerator createTableMigrationFile = new CreateTableMigrationFileGenerator();
            createTableMigrationFile.generateMigrationFile(table, fileName, null);
        } else if (this == DROP_TABLE) {
            DropTableMigrationFileGenerator dropTableMigrationFile = new DropTableMigrationFileGenerator();
            dropTableMigrationFile.generateMigrationFile(table, fileName, null);
        } else if (this == ALTER_TABLE) {
            AlterTableMigrationFileGenerator alterTableMigrationFile = new AlterTableMigrationFileGenerator(alterType);
            alterTableMigrationFile.generateMigrationFile(table, fileName, columns);
        } else if (this == OTHERS) {
            System.out.println("This command is not yet being supported");
        }
    }
}
