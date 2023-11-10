package com.batavia.orm.generator;

import com.batavia.orm.commons.*;

interface MigrationFileGenerator {
    public void generateMigrationFile(Table table, String fileName, Column[] columns);
}
