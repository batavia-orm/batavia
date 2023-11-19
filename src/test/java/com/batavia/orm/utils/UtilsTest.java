package com.batavia.orm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.*;

public class UtilsTest {

  private static final Dotenv dotenv = Dotenv
    .configure()
    .directory("C:\\Users\\alvin\\OneDrive\\Desktop\\batavia\\batavia\\.env")
    .load();

  private static final String upMigrationFilePath = dotenv.get(
    "UP_MIGRATION_PATH"
  );

  private static final String downMigrationFilePath = dotenv.get(
    "DOWN_MIGRATION_PATH"
  );

  @BeforeEach
  public void clearFile() {
    try {
      Files.write(Paths.get(upMigrationFilePath), new byte[0]);
      Files.write(Paths.get(downMigrationFilePath), new byte[0]);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Test // use reflection
  public void testUtilsConstructor() throws Exception {
    Constructor<Utils> constructor = Utils.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);
    assertThrows(
      InvocationTargetException.class,
      () -> {
        constructor.newInstance();
      }
    );
  }

  @Test
  public void testWriteToUpMigrationFile_1() throws Exception {
    String mockedScript = "DROP TABLE users;" + "\n";

    Utils.writeToUpMigrationFile(upMigrationFilePath, mockedScript);

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    assertEquals(mockedScript, upFileContent);
  }

  @Test
  public void testWriteToUpMigrationFile_2() throws Exception {
    String nonExistentPath = "non_existent_directory/up_migration.sql";

    String mockedScript = "DROP TABLE users;" + "\n";

    assertThrows(
      IOException.class,
      () -> {
        Utils.writeToUpMigrationFile(nonExistentPath, mockedScript);
      }
    );
  }

  @Test
  public void testWriteToDownMigrationFile_1() throws Exception {
    String mockedScript = "DROP TABLE users;" + "\n";

    Utils.writeToDownMigrationFile(upMigrationFilePath, mockedScript);

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upMigrationFilePath))
    );

    assertEquals(mockedScript, upFileContent);
  }

  @Test
  public void testWriteToDownMigrationFile_2() throws Exception {
    String nonExistentPath = "non_existent_directory/down_migration.sql";

    String mockedScript = "DROP TABLE users;" + "\n";

    assertThrows(
      IOException.class,
      () -> {
        Utils.writeToDownMigrationFile(nonExistentPath, mockedScript);
      }
    );
  }
}
