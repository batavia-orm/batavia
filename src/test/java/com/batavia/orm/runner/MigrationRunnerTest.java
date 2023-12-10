package com.batavia.orm.runner;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;


public class MigrationRunnerTest {
    private MigrationRunner migrationRunner;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        connection = mock(Connection.class);
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        migrationRunner = new MigrationRunner(tempDir.toString(), connection);
        createMultipleMockMigrationFiles();
    }

    @Test
    void migrate_WhenMigrationsTableDoesNotExist_ShouldCreateTableAndExecuteMigrations() throws SQLException {
        when(resultSet.getBoolean(1)).thenReturn(false);

        migrationRunner.migrate();

        verify(statement).executeUpdate("CREATE TABLE batavia_migrations (id SERIAL PRIMARY KEY, migration_file VARCHAR(255))");
        verify(statement).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('a.sql')");
        verify(statement).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('b.sql')");
        verify(statement, never()).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('a.down.sql')");
        verify(statement, never()).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('b.down.sql')");
        verify(statement).execute("a-content");
        verify(statement).execute("b-content");
        verify(statement, never()).execute("a-down-content");
        verify(statement, never()).execute("b-down-content");
        verify(statement, times(1)).close();
    }

    @Test
    void migrate_WhenMigrationsTableExists_ShouldExecuteUnappliedMigrations() throws SQLException {
        when(resultSet.getBoolean(1)).thenReturn(true);

        when(resultSet.getInt(1)).thenReturn(0).thenReturn(0);

        migrationRunner.migrate();

        verify(statement, never()).executeUpdate("CREATE TABLE batavia_migrations (id SERIAL PRIMARY KEY, migration_file VARCHAR(255))");
        verify(statement).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('a.sql')");
        verify(statement, never()).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('a.down.sql')");
        verify(statement, never()).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('b.down.sql')");
        verify(statement).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('b.sql')");
        verify(statement).execute("a-content");
        verify(statement, never()).execute("a-down-content");
        verify(statement, never()).execute("b-down-content");
        verify(statement).execute("b-content");
        verify(statement, times(1)).close();
    }

    @Test
    void migrate_WhenMigrationsTableExists_ShouldNotExecuteAppliedMigrations() throws SQLException {
        when(resultSet.getBoolean(1)).thenReturn(true);

        when(resultSet.getInt(1)).thenReturn(1).thenReturn(1);

        migrationRunner.migrate();

        verify(statement, never()).executeUpdate("CREATE TABLE batavia_migrations (id SERIAL PRIMARY KEY, migration_file VARCHAR(255))");
        verify(statement, never()).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('a.sql')");
        verify(statement, never()).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('a.down.sql')");
        verify(statement, never()).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('b.down.sql')");
        verify(statement, never()).executeUpdate("INSERT INTO batavia_migrations (migration_file) VALUES ('b.sql')");
        verify(statement, never()).execute("a-content");
        verify(statement, never()).execute("a-down-content");
        verify(statement, never()).execute("b-down-content");
        verify(statement, never()).execute("b-content");
        verify(statement, times(1)).close();
    }

    private void createMultipleMockMigrationFiles() throws IOException {
        createMockMigrationFile("a.sql", "a-content");
        createMockMigrationFile("a.down.sql", "a-down-content");
        createMockMigrationFile("b.sql", "b-content");
        createMockMigrationFile("b.down.sql", "b-down-content");
    }

    private File createMockMigrationFile(String fileName, String content) throws IOException {
        String fileContent = content;
        File migrationFile = new File(tempDir, fileName);
        migrationFile.createNewFile();

        FileWriter fileWriter = new FileWriter(migrationFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        migrationFile.deleteOnExit();

        return migrationFile;
    }
}