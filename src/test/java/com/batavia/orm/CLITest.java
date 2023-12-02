package com.batavia.orm;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import com.batavia.orm.cli.GenerateMigrationCommand;
import com.batavia.orm.cli.MigrateCommand;
import com.batavia.orm.cli.RevertCommand;
import com.batavia.orm.cli.ShowMigrationsCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

class CLITest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() throws IOException {
        System.setOut(new PrintStream(outputStreamCaptor));
        createMockEnvFile();
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    @Test
    void testStartCLI_ExitCommand() {
        System.setIn(new ByteArrayInputStream("exit\n".getBytes()));
        CLI.main(new String[]{});
        assertEquals("Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
    
    }

    // @Test
    // public void testGenerateCommand() throws IOException {
    //     // Create a mock BufferedReader
    //     BufferedReader reader = Mockito.mock(BufferedReader.class);

    //     // When readLine is called on the reader, return "generate" then "exit"
    //     Mockito.when(reader.readLine()).thenReturn("generate").thenReturn("exit");

    //     // Create an instance of CLI with the mock reader
    //     CLI cli = new CLI(reader);

    //     // Call the method to test
    //     cli.startCLI();

    //     // Verify that readLine was called on the reader
    //     Mockito.verify(reader, Mockito.times(2)).readLine();
    // }

    // @Test
    // void testParseCommand_GenerateMigrationCommand() throws Exception {
    //     // Create a mock instance of GenerateMigrationCommand
    //     GenerateMigrationCommand mockCommand = Mockito.mock(GenerateMigrationCommand.class);

    //     // Use Mockito to configure the mock
    //     doNothing().when(mockCommand).execute();

    //     // Create a mock BufferedReader that returns "generate test_migration" when readLine() is called
    //     BufferedReader mockReader = Mockito.mock(BufferedReader.class);
    //     Mockito.when(mockReader.readLine()).thenReturn("generate test_migration", "exit");

    //     // Capture System.out for verification
    //     ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    //     System.setOut(new PrintStream(outputStreamCaptor));

    //     // Create an instance of CLI with the mock command
    //     CLI cli = new CLI(mockReader);

    //     // Execute
    //     cli.startCLI();

    //     // Verify
    //     assertEquals("Please provide a command: Generating migration: 2023-12-01_025800_test_migration\n" + //
    //             "drop table employee in remote\n" + //
    //             "Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
    //     verify(mockCommand, times(1)).execute();
    // }

//     @Test
//     void testParseCommand_GenerateCommandNoFilename() throws Exception {
//         // Create a mock instance of GenerateMigrationCommand
//         GenerateMigrationCommand mockCommand = Mockito.mock(GenerateMigrationCommand.class);

//         // Use Mockito to configure the mock
//         doNothing().when(mockCommand).execute();

//         // Create a mock BufferedReader that returns "generate" when readLine() is called
//         BufferedReader mockReader = Mockito.mock(BufferedReader.class);
//         Mockito.when(mockReader.readLine()).thenReturn("generate", "exit");

//         // Capture System.out for verification
//         ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//         System.setOut(new PrintStream(outputStreamCaptor));

//         // Create an instance of CLI with the mock command
//         CLI cli = new CLI(mockReader, mockCommand, null, null, null);

//         // Execute
//         cli.startCLI();

//         // Verify
//         String expectedOutput = "Please provide a command: Please provide a command: Exiting...";
//         assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
//     }

//     @Test
//     void testStartCLI_MigrateCommand() throws Exception{
//         // Create a mock instance of GenerateMigrationCommand
//         MigrateCommand  mockCommand = Mockito.mock(MigrateCommand .class);

//         // Use Mockito to configure the mock
//         doNothing().when(mockCommand).execute();

//         // Create a mock BufferedReader that returns "migrate" when readLine() is called
//         BufferedReader mockReader = Mockito.mock(BufferedReader.class);
//         Mockito.when(mockReader.readLine()).thenReturn("migrate", "exit");

//         // Capture System.out for verification
//         ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//         System.setOut(new PrintStream(outputStreamCaptor));

//         // Create an instance of CLI with the mock command
//         CLI cli = new CLI(mockReader, null, mockCommand, null, null);

//         // Execute
//         cli.startCLI();

//         // Verify
//         assertEquals("Please provide a command: Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
//         verify(mockCommand, times(1)).execute();
//     }

//     @Test
//     void testStartCLI_ShowMigrationsCommand() throws Exception{
//         // Create a mock instance of GenerateMigrationCommand
//         ShowMigrationsCommand  mockCommand = Mockito.mock(ShowMigrationsCommand.class);

//         // Use Mockito to configure the mock
//         doNothing().when(mockCommand).execute();

//         // Create a mock BufferedReader that returns "show" when readLine() is called
//         BufferedReader mockReader = Mockito.mock(BufferedReader.class);
//         Mockito.when(mockReader.readLine()).thenReturn("show", "exit");

//         // Capture System.out for verification
//         ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//         System.setOut(new PrintStream(outputStreamCaptor));

//         // Create an instance of CLI with the mock command
//         CLI cli = new CLI(mockReader, null, null, null, mockCommand);

//         // Execute
//         cli.startCLI();

//         // Verify
//         assertEquals("Please provide a command: Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
//         verify(mockCommand, times(1)).execute();
//     }

//     @Test
//     void testStartCLI_RevertCommand() throws Exception{
//         // Create a mock instance of GenerateMigrationCommand
//         RevertCommand  mockCommand = Mockito.mock(RevertCommand.class);

//         // Use Mockito to configure the mock
//         doNothing().when(mockCommand).execute();

//         // Create a mock BufferedReader that returns "revert" when readLine() is called
//         BufferedReader mockReader = Mockito.mock(BufferedReader.class);
//         Mockito.when(mockReader.readLine()).thenReturn("revert", "exit");

//         // Capture System.out for verification
//         ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//         System.setOut(new PrintStream(outputStreamCaptor));

//         // Create an instance of CLI with the mock command
//         CLI cli = new CLI(mockReader, null, null, mockCommand, null);

//         // Execute
//         cli.startCLI();

//         // Verify
//         assertEquals("Please provide a command: Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
//         verify(mockCommand, times(1)).execute();
//     }

// //    @Test
// //    void testRevertCommand_execute() {
// //        // Create an instance of RevertCommand
// //        RevertCommand revertCommand = new RevertCommand();
// //
// //        // Capture System.out for verification
// //        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
// //        System.setOut(new PrintStream(outputStreamCaptor));
// //
// //        // Execute
// //        revertCommand.execute();
// //
// //        // Verify
// //        String expectedOutput = "Migrating...";
// //        assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
// //    }

    @Test
    void testParseCommand_HelpCommand() throws Exception {
        // Create a mock BufferedReader that returns "help" when readLine() is called
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("help", "exit");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with the mock reader
        CLI cli = new CLI(mockReader);
        cli.startCLI();
        String expectedOutput = "Please provide a command: Unknown command: help\n" + //
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

    @Test
    void testParseCommand_UnknownCommand() throws Exception{
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("unknown_command", "exit");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with the mock reader
        CLI cli = new CLI(mockReader);
        cli.startCLI();
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

    @Test
    void testStartCLI_IOException() throws Exception {
        // Create a mock BufferedReader that throws an IOException when readLine() is called
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenThrow(new IOException("Test exception"));

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with the mock reader
        CLI cli = new CLI(mockReader);
        cli.startCLI();
        assertEquals("Please provide a command: Error reading input: Test exception", outputStreamCaptor.toString().trim());
    }

    @Test
    void testParseCommand_NoCommandProvided() throws Exception {
        // Create a mock BufferedReader that returns an empty string when readLine() is called
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with the mock reader
        CLI cli = new CLI(mockReader);
        cli.startCLI();
        assertEquals("Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
    }

    @Test
    void testParseCommand_NoCommandProvided_MultipleSpaces() throws Exception {
        // Create a mock BufferedReader that returns "   " (three spaces) when readLine() is called
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.readLine()).thenReturn("   ", "exit");

        // Capture System.out for verification
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create an instance of CLI with the mock reader
        CLI cli = new CLI(mockReader);
        cli.startCLI();
        assertEquals("Please provide a command: Exiting...", outputStreamCaptor.toString().trim());
    }

    private File createMockEnvFile() throws IOException {
        String fileContent = "DATABASE_URL=test\nMIGRATIONS_DIR=test\nDATASOURCE_DIR=test";
        File envFile = new File(tempDir, ".env");
        envFile.createNewFile();

        FileWriter fileWriter = new FileWriter(envFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        envFile.deleteOnExit();

        return envFile;
    }

}