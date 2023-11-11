package com.batavia.orm.generator.migrationFileGenerators;

import com.batavia.orm.commons.*;

interface IMigrationFileGenerator {
    public void generateMigrationFile(Table table, String fileName, Column[] columns);
}
