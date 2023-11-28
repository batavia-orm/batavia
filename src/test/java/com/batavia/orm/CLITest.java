package com.batavia.orm;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import com.batavia.orm.cli.GenerateMigrationCommand;
import com.batavia.orm.cli.MigrateCommand;
import com.batavia.orm.cli.RevertCommand;
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
    void testParseCommand_GenerateMigrationCommand() throws Exception {
        // Create a mock instance of GenerateMigrationCommand
        GenerateMigrationCommand mockCommand = Mockito.mock(GenerateMigrationCommand.class);

        // Use Mockito to configure the mock
        doNothing().when(mockCommand).execute();

        // Create a mock BufferedReader that returns "generate test_migration" when readLine() is called
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("generate test_migration", "exit");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with the mock command
        CLI cli = new CLI(mockReader, mockCommand, null, null, null);

        // Execute
        cli.startCLI();

        // Verify
        assertEquals("Please provide a command: Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testStartCLI_MigrateCommand() throws Exception{
        // Create a mock instance of GenerateMigrationCommand
        MigrateCommand  mockCommand = Mockito.mock(MigrateCommand .class);

        // Use Mockito to configure the mock
        doNothing().when(mockCommand).execute();

        // Create a mock BufferedReader that returns "generate test_migration" when readLine() is called
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("migrate", "exit");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with the mock command
        CLI cli = new CLI(mockReader, null, mockCommand, null, null);

        // Execute
        cli.startCLI();

        // Verify
        assertEquals("Please provide a command: Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testStartCLI_ShowMigrationsCommand() throws Exception{
        // Create a mock instance of GenerateMigrationCommand
        ShowMigrationsCommand  mockCommand = Mockito.mock(ShowMigrationsCommand.class);

        // Use Mockito to configure the mock
        doNothing().when(mockCommand).execute();

        // Create a mock BufferedReader that returns "generate test_migration" when readLine() is called
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("show", "exit");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with the mock command
        CLI cli = new CLI(mockReader, null, null, null, mockCommand);

        // Execute
        cli.startCLI();

        // Verify
        assertEquals("Please provide a command: Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testStartCLI_RevertCommand() throws Exception{
        // Create a mock instance of GenerateMigrationCommand
        RevertCommand  mockCommand = Mockito.mock(RevertCommand.class);

        // Use Mockito to configure the mock
        doNothing().when(mockCommand).execute();

        // Create a mock BufferedReader that returns "revert" when readLine() is called
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("revert", "exit");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with the mock command
        CLI cli = new CLI(mockReader, null, null, mockCommand, null);

        // Execute
        cli.startCLI();

        // Verify
        assertEquals("Please provide a command: Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testParseCommand_UnknownCommand() throws Exception{
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("unknown_command", "exit");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI
        CLI cli = new CLI(mockReader, null, null, null, null);

        // Execute
        cli.startCLI();

        // Verify
        String expectedOutput = "Please provide a command: Unknown command: unknown_command\n" +
                        "Available commands: \n" +
                        "  generate <migration-filename>: Generate a migration\n" +
                        "  migrate: Migrate\n" +
                        "  revert: Revert\n" +
                        "  show: Show migrations\n" +
                        "  help: Display available commands\n" +
                        "  exit/quit: Exit the CLI\n" +
                        "Unknown command. Type 'help' for available commands.\n" +
                        "Please provide a command: Exiting...";
        expectedOutput = expectedOutput.trim().replace("\r\n", "\n");
        String actualOutput = outputStreamCaptor.toString().trim().replace("\r\n", "\n");
        assertEquals(expectedOutput, actualOutput);
    }

    /**TODO: test generate command when <migration-filename> is missing
     * statement coverage: command classes have additional methods
     * error handling for execute() and readLine() fails
     **/
}
