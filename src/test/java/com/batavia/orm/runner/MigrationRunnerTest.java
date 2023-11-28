package com.batavia.orm.runner;

import org.junit.jupiter.api.*;

// import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;


public class MigrationRunnerTest {
    private MigrationRunner migrationRunner;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private static final Dotenv dotenv = Dotenv.load();
    private static final String MIGRATIONS_DIR = dotenv.get("MIGRATIONS_DIR");


    @BeforeEach
    void setUp() throws SQLException {
        connection = mock(Connection.class);
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        migrationRunner = new MigrationRunner(MIGRATIONS_DIR, connection);
    }

    @Test
    void migrate_WhenMigrationsTableDoesNotExist_ShouldCreateTableAndExecuteMigrations() throws SQLException, IOException {
        createMockMigrationFile("migration1.sql", "migration1-content");
        createMockMigrationFile("migration2.sql", "migration2-content");
        when(resultSet.getBoolean(1)).thenReturn(false);

        migrationRunner.migrate();

        verify(statement).executeUpdate("CREATE TABLE batavia_migrations (id SERIAL PRIMARY KEY, migration_file VARCHAR(255))");
        verify(statement).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('migration1.sql')");
        verify(statement).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('migration2.sql')");
        verify(statement).execute("migration1-content");
        verify(statement).execute("migration2-content");
        verify(statement, times(1)).close();
    }

    @Test
    void migrate_WhenMigrationsTableExists_ShouldOnlyExecuteUnappliedMigrations() throws SQLException, IOException {
        createMockMigrationFile("applied_migration.sql", "applied_migration-content");
        createMockMigrationFile("unapplied_migration.sql", "unapplied_migration-content");
        when(resultSet.getBoolean(1)).thenReturn(true);

        when(resultSet.getInt(1)).thenReturn(1).thenReturn(0);

        migrationRunner.migrate();

        verify(statement, never()).executeUpdate("CREATE TABLE batavia_migrations (id SERIAL PRIMARY KEY, migration_file VARCHAR(255))");
        verify(statement, never()).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('applied_migration.sql')");
        verify(statement).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('unapplied_migration.sql')");
        verify(statement, never()).execute("applied_migration-content");
        verify(statement).execute("unapplied_migration-content");
        verify(statement, times(1)).close();
    }

    private File createMockMigrationFile(String fileName, String content) throws IOException {
        String fileContent = content;
        String filePath = MIGRATIONS_DIR + '/' + fileName;

        File migrationFile = new File(filePath);
        migrationFile.createNewFile();

        FileWriter fileWriter = new FileWriter(migrationFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        migrationFile.deleteOnExit();

        return migrationFile;
    }
}