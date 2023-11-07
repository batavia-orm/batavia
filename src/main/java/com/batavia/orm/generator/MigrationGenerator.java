package com.batavia.orm.generator;

interface MigrationGenerator {
    void generateMigration(String tableName);
}
