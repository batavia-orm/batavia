package com.batavia.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        // Execute
        CLI.main(new String[]{});

        // Verify
        assertEquals("Welcome to Batavia ORM\nPlease provide a command: Generating migration: test_migration", outputStreamCaptor.toString().trim());
    }

    @Test
    void testStartCLI_MigrateCommand() {
        // Set up
        String userInput = "--migrate";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Execute
        CLI.main(new String[]{});

        // Verify
        assertEquals("Migrating...", outputStreamCaptor.toString().trim());
    }

    @Test
    void testStartCLI_ShowMigrationsCommand() {
        // Set up
        String userInput = "--show-migrations";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Execute
        CLI.main(new String[]{});

        // Verify
        assertEquals("Showing migrations...", outputStreamCaptor.toString().trim());
    }

    @Test
    void testParseCommand_UnknownCommand() {
        // Set up
        String userInput = "--unknown-command";
        String[] args = userInput.split("\\s+");

        // Execute

        // Verify
    }
}
