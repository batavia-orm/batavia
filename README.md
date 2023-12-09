# BataviaORM
![Screenshot 2023-12-10 at 1 52 19 AM](https://github.com/batavia-orm/batavia/assets/42536665/4cd2454a-9d92-4b65-ae4c-22cf9b095522)

BataviaORM is an innovative lightweight Java-to-SQL ORM (Object-Relational Mapper) framework, designed to revolutionize the way developers interact with databases. BataviaORM embraces a minimalist design philosophy, allowing developers to seamlessly map Java classes to SQL tables with minimal setup and configuration, and apply database schema changes without writing a single SQL code yourself. It includes 3 core functionalities; migration generator, migration runner, migration reverter.

- [Installation](#installation)
- [Setup](#setup)
- [Usage](#usage)


## Installation
Install the intended Batavia ORM version from the [release page](https://github.com/batavia-orm/batavia/releases). The installation will include 2 JAR files with the name:

`batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar`

`batavia-{version_number}-SNAPSHOT.jar` 
    

## Setup
### Copy / move JAR files to your project directory
Add both JAR files to the root directory of your Java project
```
├───src
│   ├───main
│   └───test
└───.env
└───batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar
└───batavia-{version_number}-SNAPSHOT.jar
└───README.md
```

### Set environment variables
In the .env file, set 3 necessary variables

- `DATABASE_URL`: the full JDBC Database URL (connection string). Format: `jdbc:postgresql://host:port/database?properties`
- `MIGRATIONS_DIR`: the path to the migration directory
- `DATASOURCE_DIR`: the path to your Java schema/classes directory
  
For example, for the following directory structure:
```
├── bin
│   └── pokerProject
│       └── Employee.class
├── migrations
│   ├── 2023-12-01_171223_automatic.down.sql
│   └── 2023-12-01_171223_automatic.sql
└── src
    └── pokerProject
        └── Employee.java
```
The complete .env file would look like the following:
```
DATABASE_URL=jdbc:postgresql://host:port/database?properties
MIGRATIONS_DIR=migrations
DATASOURCE_DIR=src
```
  
### Set up batavia build path in your Java project
In the eclipse package explorer, right click on your java project and click properties

<img width="229" alt="image" src="https://github.com/batavia-orm/batavia/assets/101443060/589d440f-4cd1-47f6-acea-c416355416f4">

Then, click on the `Java Build Path` tab and choose `Libraries` in the internal tab. Click on `modulepath` then click `Add JARs` and choose the `batavia-{version_number}-SNAPSHOT.jar`. Last, click the `apply and close`.

<img width="701" alt="image" src="https://github.com/batavia-orm/batavia/assets/101443060/c7167e27-3e3c-44f3-88f3-ae18c9c0d42b">

You can now import the annotation classes from batavia as such to annotate your Java schema (explanation in the next section)
```
import com.batavia.orm.annotations.Entity;
import com.batavia.orm.annotations.EntityColumn;
import com.batavia.orm.annotations.PrimaryColumn;
```


## Usage

### Schema Annotations
There are 4 annotations that can be used to denote the properties of the object:

- `@Entity: to denote SQL table`
- `@EntityColumn: to denote SQL table column`
- `@PrimaryColumn: to denote the primary column of a table`
- `@Unique: to denote a unique content in column`

Complete Java schema file example:
```
package employeeProject;

import com.batavia.orm.annotations.Entity;
import com.batavia.orm.annotations.EntityColumn;
import com.batavia.orm.annotations.PrimaryColumn;

@Entity
class Employee {

  @EntityColumn
  @PrimaryColumn
  private String name;

  @EntityColumn
  private Integer age;
  
  @EntityColumn
  private Boolean married;
  
  @EntityColumn
  private Boolean religious;
}
```

### CLI
There are 2 ways to run the CLI commands:
```
java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar
```
which will run the program in loop mode, accepting and executing the commands, or
```
java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar {COMMAND}
```
which will directly execute the inputted command. The commands include the following:

#### Generate
Once you have made the changes to your Java schema/classes and are ready to capture the changes and generate the migration, there are 2 ways to run the generate command:
```
java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar generate
```
which will create a migration file with a time-stamped auto-generated file name (e.g. `2023-12-01_171223_automatic.sql`), or
```
java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar generate {file_name}
```
which will create a time-stamped migration file with the given file name (e.g. `2023-12-01_171223_create_employee_table.sql`)

Both commands will generate time-stamped filenames to maintain file uniqueness and sortedness in the migration folder. The generated files will include the down migration file that will be needed in case of reverts.

#### Migrate
Once the migration files are generated and you are ready to run the migration against the database, you can run the following:
```
java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar migrate
```
which will execute the migration and apply the schema changes to the remote database

#### Revert
If you intend to rollback / undo any changes applied by your previous migration(s) to the remote database, you can revert the migrations through the following commands:
```
java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar revert
```
which will revert the last migration applied to the database, you can also do:
```
java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar revert {previous_migration_filename}
```
which will return the database schema state back to the specified migration by reverting all migrations up to the specified `previous_migration_filename`.
Say, for instance, the migrations folder in your app is something like below with migration `0012_latest_migration.sql` being applied the most recently.
```
0010_previous_migration.sql
0011_next_migration.sql
0012_latest_migration.sql
```
If you want to go back to `0010_previous_migration.sql`, you can run the following:
```
java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar revert 0010_previous_migration.sql
```
The reverter will then revert `0012_latest_migration.sql` by executing `0012_latest_migration.down.sql` against the database, and subsequently reverting `0011_next_migration.sql` the same way. You can then delete the local files of the migrations that got reverted if you'd like to further generate a different migration to ensure migration consistency.

