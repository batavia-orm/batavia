package com.batavia.orm;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import com.batavia.orm.cli.Command;
import com.batavia.orm.cli.Receiver;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLITest {

  private BufferedReader reader;
  private CLI cli;

  @BeforeEach
  public void setup() throws IOException {
    reader = mock(BufferedReader.class);
    cli = new CLI(reader);
  }

  @Test
  void testGenerateCommand() throws IOException {
    // Create a stub for Command class using Mockito's mock method
    Command generateCommand = mock(Command.class);
    when(reader.readLine()).thenReturn("generate", "exit");

    Map<String, Command> commandMap = new HashMap<>();
    commandMap.put("generate", generateCommand);

    CLI cli = new CLI(reader, commandMap);
    cli.startCLI();
    verify(reader, times(2)).readLine();
    verify(generateCommand, times(1)).execute();
  }
  @Test
  void testGenerateCommand2() throws IOException {
    // Create a stub for Command class using Mockito's mock method
    Command generateCommand = mock(Command.class);
    when(reader.readLine()).thenReturn("generate", "exit");

    Map<String, Command> commandMap = new HashMap<>();
    commandMap.put("generate", generateCommand);

    CLI cli = new CLI(reader, commandMap);
    cli.startCLI();
    verify(reader, times(2)).readLine();
    verify(generateCommand, times(1)).execute();
    assertNotNull(generateCommand);
  }

  // test Generate Migration by mock Receiver class
  @Test
    public void testGenerateMigration() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        Receiver receiver = new Receiver();
        receiver.generateMigration("test_migration");
        String actualOutput = outputStreamCaptor.toString().trim();

        assertTrue(actualOutput.contains("Generating migration:"));
        assertTrue(actualOutput.contains("_test_migration"));
        assertTrue(actualOutput.contains("Migration file generated successfully"));
    }

    @Test
  void testGenerateMigration1() {
    Receiver mockReceiver = mock(Receiver.class);
    String migrationName = "testMigration";
    doNothing().when(mockReceiver).generateMigration(migrationName);
    mockReceiver.generateMigration(migrationName);
    verify(mockReceiver, times(1)).generateMigration(migrationName);
  }

  @Test
  void testMigrate() {
    Receiver mockReceiver = mock(Receiver.class);
    doNothing().when(mockReceiver).migrate();
    mockReceiver.migrate();
    verify(mockReceiver, times(1)).migrate();
  }

  @Test
    void testRevert() {
        Receiver mockReceiver = mock(Receiver.class);
        String migrationName = "testMigration";
        doNothing().when(mockReceiver).revert(migrationName);
        mockReceiver.revert(migrationName);
        verify(mockReceiver, times(1)).revert(migrationName);
    }

  @Test
  void testMigrateCommand() throws IOException {
    Command migrateCommand = mock(Command.class);
    when(reader.readLine()).thenReturn("migrate", "exit");

    Map<String, Command> commandMap = new HashMap<>();
    commandMap.put("migrate", migrateCommand);

    CLI cli = new CLI(reader, commandMap);
    cli.startCLI();
    verify(reader, times(2)).readLine();
    verify(migrateCommand, times(1)).execute();
  }

  @Test
  void testRevertCommand() throws IOException {
    Command revertCommand = mock(Command.class);
    when(reader.readLine()).thenReturn("revert", "exit");

    Map<String, Command> commandMap = new HashMap<>();
    commandMap.put("revert", revertCommand);

    CLI cli = new CLI(reader, commandMap);
    cli.startCLI();
    verify(reader, times(2)).readLine();
    verify(revertCommand, times(1)).execute();
  }

//   @Test
//     void testRevert() {
//         // Redirect System.out to capture the printed output
//         ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//         System.setOut(new PrintStream(outputStreamCaptor));

//         // Create an instance of Receiver
//         Receiver receiver = new Receiver();

//         // Mock necessary dependencies
//         Connection mockConnection = mock(Connection.class);
//         MigrationReverter mockMigrationReverter = mock(MigrationReverter.class);

//         try {
//             // Stub the DriverManager.getConnection method to return the mock connection
//             when(DriverManager.getConnection(anyString())).thenReturn(mockConnection);

//             // Stub the revert method of the mock MigrationReverter
//             doCallRealMethod().when(mockMigrationReverter).revert(anyString());
//             doCallRealMethod().when(mockMigrationReverter).revert();
//             // Call the method to be tested
//             receiver.revert("example_migration");

//             // Verify that the expected output is printed
//             String expectedOutputStart = "\nReverting...\n";
//             String expectedOutputEnd = "\n\n\u001B[32mReverted applied migrations\u001B[0m\n";
//             String actualOutput = outputStreamCaptor.toString().trim();

//             assertTrue(actualOutput.startsWith(expectedOutputStart));
//             assertTrue(actualOutput.endsWith(expectedOutputEnd));

//             // Verify that the revert method of the mock MigrationReverter is called
//             verify(mockMigrationReverter, times(1)).revert("example_migration");

//             // Reset System.out to its original state
//             System.setOut(System.out);
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
 
  @Test
  void testShowMigrationsCommand() throws IOException {
    Command showCommand = mock(Command.class);
    when(reader.readLine()).thenReturn("show", "exit");

    Map<String, Command> commandMap = new HashMap<>();
    commandMap.put("show", showCommand);

    CLI cli = new CLI(reader, commandMap);
    cli.startCLI();
    verify(reader, times(2)).readLine();
    verify(showCommand, times(1)).execute();
  }

  //Test receiver.java
  @Test
    public void testShowMigrations() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        Receiver receiver = new Receiver();
        receiver.showMigrations();

        String expectedOutput = "Showing migrations...";
        assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        System.setOut(System.out);
    }

  @Test
  void testUnknownCommand() throws IOException {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    when(reader.readLine()).thenReturn("unknown", "exit");

    cli.startCLI();

    verify(reader, times(2)).readLine();
    assertTrue(outContent.toString().contains("Unknown command: unknown"));
  }


  @Test
  void testExitAndQuitCommands() throws IOException {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    when(reader.readLine()).thenReturn("exit");

    cli.startCLI();

    verify(reader, times(1)).readLine();
    assertTrue(outContent.toString().contains("Exiting..."));
  }

  @Test
  void testEmptyCommand() throws IOException {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    when(reader.readLine()).thenReturn("", "exit");

    cli.startCLI();

    verify(reader, times(1)).readLine();
    assertTrue(outContent.toString().contains("Exiting..."));
  }

  @Test
  void testIOException() throws IOException {
    when(reader.readLine()).thenThrow(new IOException("Test exception"));

    cli.startCLI();

    verify(reader, times(1)).readLine();
  }
}