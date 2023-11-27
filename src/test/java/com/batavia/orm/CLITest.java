package com.batavia.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import com.batavia.orm.cli.Command;
import com.batavia.orm.cli.GenerateMigrationCommand;
import com.batavia.orm.cli.MigrateCommand;
import com.batavia.orm.cli.ShowMigrationsCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Collections;

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
        assertEquals("Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
    
    }

    @Test
    void testStartCLI(){

    }

    @Test
    void testParseCommand_GenerateMigrationCommand() throws Exception {
        // Set up
        String userInput = "generate test_migration";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Create a mock instance of GenerateMigrationCommand
        GenerateMigrationCommand mockCommand = Mockito.mock(GenerateMigrationCommand.class);

        // Use Mockito to configure the mock
        doNothing().when(mockCommand).execute();

        // Create a mock BufferedReader that returns "exit" when readLine() is called
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("generate test_migration", "exit");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with dependencies
        CLI cli = new CLI(mockReader, mockCommand);

        // Execute
        cli.startCLI();

        // Verify
        assertEquals("Please provide a command: Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testStartCLI_MigrateCommand() {
        // // Set up
        // String userInput = "--migrate";
        // System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        // MigrateCommand mockCommand = mock(MigrateCommand.class);

        // // Use doNothing() since it's a void method
        // doNothing().when(mockCommand).execute();

        // // Execute
        // CLI.main(new String[]{});

        // // Verify
        // assertEquals("Welcome to Batavia ORM\nPlease provide a command: Migrating...", outputStreamCaptor.toString().trim());
        // verify(mockCommand, times(1)).execute();
    }

    @Test
    void testStartCLI_ShowMigrationsCommand() {
        // // Set up
        // String userInput = "--show-migrations";
        // System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        // ShowMigrationsCommand mockCommand = mock(ShowMigrationsCommand.class);

        // // Use doNothing() since it's a void method
        // doNothing().when(mockCommand).execute();

        // // Execute
        // CLI.main(new String[]{});

        // // Verify
        // assertEquals("Welcome to Batavia ORM\nPlease provide a command: Showing migrations...", outputStreamCaptor.toString().trim());
        // verify(mockCommand, times(1)).execute();
    }

    @Test
    void testParseCommand_UnknownCommand() {
    }
}
