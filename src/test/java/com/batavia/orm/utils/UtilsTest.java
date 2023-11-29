package com.batavia.orm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

public class UtilsTest {

   @TempDir
  private static Path tempMockMigrationDir;


  @BeforeEach
  public void clearFile() {
    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    try {
      Files.write(upMigrationFilePath, new byte[0]);
      Files.write(downMigrationFilePath, new byte[0]);
    } catch (IOException e) {
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

    Path upMigrationFilePath = tempMockMigrationDir.resolve(
      "up-script-test.sql"
    );

    String upPath = upMigrationFilePath.toString();

    Utils.writeToUpMigrationFile(upPath, mockedScript);

    String upFileContent = new String(
      Files.readAllBytes(Paths.get(upPath))
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
  public void testWriteToDownMigrationFile_3() throws Exception {
    String mockedScript = "DROP TABLE users;" + "\n";

     Path downMigrationFilePath = tempMockMigrationDir.resolve(
      "down-script-test.sql"
    );

    String downPath = downMigrationFilePath.toString();

    Utils.writeToDownMigrationFile(downPath, mockedScript);

    String downFileContent = new String(
      Files.readAllBytes(Paths.get(downPath))
    );

    assertEquals(mockedScript, downFileContent);
  }

  @Test
  public void testWriteToDownMigrationFile_4() throws Exception {
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
