package com.batavia.orm.generator;

interface MigrationGenerator {
    String generateMigration(String tableName);
}
