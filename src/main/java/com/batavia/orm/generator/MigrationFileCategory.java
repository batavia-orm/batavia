package com.batavia.orm.generator;

import com.batavia.orm.commons.*;
import com.batavia.orm.generator.migrationFileGenerators.*;
import com.batavia.orm.generator.sqlScriptGenerators.AlterTableCategory;

public enum MigrationFileCategory {
    CREATE_TABLE,
    DROP_TABLE,
    ALTER_TABLE,
    OTHERS;

    public void runMigrationFileGenerator(
        Table table, 
        Column[] columns, 
        String fileName,
        AlterTableCategory alterType
    ) {
        if (this == CREATE_TABLE) {
            CreateTableMigrationFileGenerator createTableMigration = new CreateTableMigrationFileGenerator();
            createTableMigration.generateMigrationFile(table, fileName, null);
        } else if (this == DROP_TABLE) {
            DropTableMigrationFileGenerator dropTableMigration = new DropTableMigrationFileGenerator();
            dropTableMigration.generateMigrationFile(table, fileName, null);
        } else if (this == ALTER_TABLE) {
            AlterTableMigrationFileGenerator alterTableMigration = new AlterTableMigrationFileGenerator(alterType);
            alterTableMigration.generateMigrationFile(table, fileName, columns);
        } else if (this == OTHERS) {
            System.out.println("This command is not yet being supported");
        }
    }
}
