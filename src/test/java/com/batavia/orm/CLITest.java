package com.batavia.orm;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.batavia.orm.cli.Command;
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