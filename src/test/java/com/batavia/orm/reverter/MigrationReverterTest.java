package com.batavia.orm.reverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MigrationReverterTest {
    private MigrationReverter migrationReverter;
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
        migrationReverter = new MigrationReverter(MIGRATIONS_DIR, connection);
    }

    @Test
    void revert_withArgument_shouldRevertMigrationsInReverseOrderUntilDesiredLastAppliedMigration() throws SQLException, IOException {
        createMultipleMockMigrationFiles();

        // migrationFileExists(desiredLastAppliedMigration) flow
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getBoolean(1)).thenReturn(true);

        // getMigrationsToRevert flow
        when(resultSet.next()).thenReturn(true, true, true, false);
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
    void revert_withArgument_shouldNotRevertAnyMigrationsIfDesiredLastAppliedMigrationDoesNotExist() throws SQLException, IOException {
        createMultipleMockMigrationFiles();

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
    void revert_withoutArgument_shouldRevertLastAppliedMigrationIfItExists() throws SQLException, IOException {
        createMultipleMockMigrationFiles();

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
    void revert_withoutArgument_shouldNotRevertAnyMigrationsIfNoMigrationIsApplied() throws SQLException, IOException {
        createMultipleMockMigrationFiles();

        // getLastAppliedMigration flow
        when(resultSet.next()).thenReturn( false);

        // revertMigration
        migrationReverter.revert();

        // Assert
        verify(statement).executeQuery("SELECT migration_file FROM batavia_migrations ORDER BY id DESC LIMIT 1");
        verify(statement, never()).execute(anyString());
        verify(statement, never()).executeUpdate(anyString());
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