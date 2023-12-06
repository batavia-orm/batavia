package com.batavia.orm.reverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MigrationReverterTest {
    private MigrationReverter migrationReverter;
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
        migrationReverter = new MigrationReverter(tempDir.toString(), connection);
        createMultipleMockMigrationFiles();
    }

    @Test
    void revert_withArgument_shouldRevertMigrationsInReverseOrderUntilDesiredLastAppliedMigration() throws SQLException {
        // migrationFileExists(desiredLastAppliedMigration) flow
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getBoolean(1)).thenReturn(true);

        // getMigrationsToRevert flow
        when(resultSet.next()).thenReturn(true, true, true, true, false);
        when(resultSet.getString("migration_file")).thenReturn("migration3.sql", "migration2.sql", "migration1.sql");
       
        // revertMigration
        migrationReverter.revert("migration1.sql");

        // Assert
        verify(statement).executeQuery("SELECT EXISTS (SELECT * FROM batavia_migrations WHERE migration_file = 'migration1.sql')");
        verify(statement).executeQuery("SELECT migration_file FROM batavia_migrations ORDER BY id DESC");
        verify(statement).execute("migration3-down-content");
        verify(statement).executeUpdate("DELETE FROM batavia_migrations WHERE migration_file = 'migration3.sql'");
        verify(statement).execute("migration2-down-content");
        verify(statement).executeUpdate("DELETE FROM batavia_migrations WHERE migration_file = 'migration2.sql'");
    }

    @Test
    void revert_withArgument_shouldNotRevertAnyMigrationsIfDesiredLastAppliedMigrationDoesNotExist() throws SQLException {
        // migrationFileExists(desiredLastAppliedMigration) flow
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getBoolean(1)).thenReturn(false);

        // revertMigration
        migrationReverter.revert("migration1.sql");

        // Assert
        verify(statement).executeQuery("SELECT EXISTS (SELECT * FROM batavia_migrations WHERE migration_file = 'migration1.sql')");
        verify(statement, never()).executeQuery("SELECT migration_file FROM batavia_migrations ORDER BY id DESC");
        verify(statement, never()).executeUpdate(anyString());
        verify(statement, never()).execute(anyString());
    }

    @Test
    void revert_withoutArgument_shouldRevertLastAppliedMigrationIfItExists() throws SQLException {
        // getLastAppliedMigration flow
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("migration_file")).thenReturn("migration3.sql");

        // revertMigration
        migrationReverter.revert();

        // Assert
        verify(statement).executeQuery("SELECT migration_file FROM batavia_migrations ORDER BY id DESC LIMIT 1");
        verify(statement).execute("migration3-down-content");
        verify(statement).executeUpdate("DELETE FROM batavia_migrations WHERE migration_file = 'migration3.sql'");
    }

    @Test
    void revert_withoutArgument_shouldNotRevertAnyMigrationsIfNoMigrationIsApplied() throws SQLException {
        // getLastAppliedMigration flow
        when(resultSet.next()).thenReturn( false);

        // revertMigration
        migrationReverter.revert();

        // Assert
        verify(statement).executeQuery("SELECT migration_file FROM batavia_migrations ORDER BY id DESC LIMIT 1");
        verify(statement, never()).execute(anyString());
        verify(statement, never()).executeUpdate(anyString());
    }

    @Test
    void revert_withArgument_whenException_shouldPrintStackTraces() throws SQLException {
        // migrationFileExists(desiredLastAppliedMigration) flow
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getBoolean(1)).thenReturn(true);

        // getMigrationsToRevert flow
        when(resultSet.next()).thenReturn(true, true, true, true, false);
        when(resultSet.getString("migration_file")).thenReturn("migration3.sql", "migration2.sql", "migration1.sql");

        // revertMigration
        doThrow(new SQLException("Test Exception")).when(statement).execute(anyString());

        // Redirect System.out to capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setErr(printStream);

        // Call revert() and expect exception to be caught and stack traces printed
        migrationReverter.revert("migration1.sql");

        // Reset System.out
        System.setErr(System.out);

        // Assert
        assertTrue(outputStream.toString().contains("java.sql.SQLException: Test Exception"));
    }

    @Test
    void revert_withoutArgument_whenException_shouldPrintStackTraces() throws SQLException {
        // getLastAppliedMigration flow
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("migration_file")).thenReturn("migration3.sql");

        // revertMigration
        doThrow(new SQLException("Test Exception")).when(statement).execute(anyString());

        // Redirect System.out to capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setErr(printStream);

        // Call revert() and expect exception to be caught and stack traces printed
        migrationReverter.revert();

        // Reset System.out
        System.setErr(System.out);

        // Assert
        assertTrue(outputStream.toString().contains("java.sql.SQLException: Test Exception"));
    }

    private void createMultipleMockMigrationFiles() throws IOException {
        createMockMigrationFile("migration1.sql", "migration1-content");
        createMockMigrationFile("migration1.down.sql", "migration1-down-content");
        createMockMigrationFile("migration2.sql", "migration2-content");
        createMockMigrationFile("migration2.down.sql", "migration2-down-content");
        createMockMigrationFile( "migration3.sql", "migration3-content");
        createMockMigrationFile("migration3.down.sql", "migration3-down-content");
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