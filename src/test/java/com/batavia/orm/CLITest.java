package com.batavia.orm;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.batavia.orm.adapters.Config;
import com.batavia.orm.cli.Command;
import com.batavia.orm.cli.Receiver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class CLITest {

  private BufferedReader reader;
  private CLI cli;
  
  @TempDir
  File tempDir;

  @BeforeAll
  public static void setConfig() throws SQLException {
    Config mock_Config = mock(Config.class);
    MockedStatic<Config> mock_Config_static = Mockito.mockStatic(Config.class);
    mock_Config_static.when(Config::getConfig).thenReturn(mock_Config);
    when(mock_Config.getDatabaseURL()).thenReturn("mock");
    when(mock_Config.getMigrationsDir()).thenReturn("mock");
    when(mock_Config.getDatasourceDir()).thenReturn("mock");
    
  }

  @BeforeEach
  public void setup() throws IOException, SQLException {
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

  // @Test
  //   public void testGenerateMigration() {
  //       ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  //       System.setOut(new PrintStream(outputStreamCaptor));

  //       Receiver receiver = new Receiver();
  //       receiver.generateMigration("test_migration");
  //       String actualOutput = outputStreamCaptor.toString().trim();

  //       assertTrue(actualOutput.contains("Generating migration:"));
  //       assertTrue(actualOutput.contains("_test_migration"));
  //       assertTrue(actualOutput.contains("Migration file generated successfully"));
  //   }


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