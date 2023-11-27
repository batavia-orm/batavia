package com.batavia.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.batavia.orm.cli.GenerateMigrationCommand;
import com.batavia.orm.cli.MigrateCommand;
import com.batavia.orm.cli.ShowMigrationsCommand;

import static org.mockito.Mockito.*;

class CLITest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    @Test
    void testStartCLI_ExitCommand() {
        // Set up
        System.setIn(new ByteArrayInputStream("exit\n".getBytes()));

        // Execute
        CLI.main(new String[]{});

        // Verify
        assertEquals("Welcome to Batavia ORM\nPlease provide a command: Exiting...", outputStreamCaptor.toString().trim());
    
    }

    @Test
    void testParseCommand_GenerateMigrationCommand() {
        // Set up
        String userInput = "--generate-migration test_migration";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        GenerateMigrationCommand mockCommand = mock(GenerateMigrationCommand.class);

        // Use doNothing() since it's a void method
        doNothing().when(mockCommand).execute();

        // Execute
        CLI.main(new String[]{});

        // Verify
        assertEquals("Welcome to Batavia ORM\nPlease provide a command: Generating migration: test_migration", outputStreamCaptor.toString().trim());
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testStartCLI_MigrateCommand() {
        // Set up
        String userInput = "--migrate";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        MigrateCommand mockCommand = mock(MigrateCommand.class);

        // Use doNothing() since it's a void method
        doNothing().when(mockCommand).execute();

        // Execute
        CLI.main(new String[]{});

        // Verify
        assertEquals("Welcome to Batavia ORM\nPlease provide a command: Migrating...", outputStreamCaptor.toString().trim());
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testStartCLI_ShowMigrationsCommand() {
        // Set up
        String userInput = "--show-migrations";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        ShowMigrationsCommand mockCommand = mock(ShowMigrationsCommand.class);

        // Use doNothing() since it's a void method
        doNothing().when(mockCommand).execute();

        // Execute
        CLI.main(new String[]{});

        // Verify
        assertEquals("Welcome to Batavia ORM\nPlease provide a command: Showing migrations...", outputStreamCaptor.toString().trim());
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testParseCommand_UnknownCommand() {
        // any command that are not recognizable
        String userInput = "--invalid-command";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Execute
        CLI.main(new String[]{});

        // Verify
        assertEquals("Welcome to Batavia ORM\nPlease provide a command: Unknown command: --invalid-command\nUsage: \n  --generate-migration <migration-filename>: Generate a migration\n  --migrate: Migrate\n  --show-migrations: Show migrations", outputStreamCaptor.toString().trim());
    }
}
