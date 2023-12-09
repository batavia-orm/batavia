# Batavia ORM
Batavia ORM is an innovative lightweight Java to SQL ORM, designed to revolutionize the way developers interact with databases. Batavia ORM embraces a minimalist design philosophy, allowing developers to seamlessly map Java objects to SQL tables with minimal setup and configuration. It includes 3 essential functionalities; migration generator, migration runner, migration reverter.

- [Installation](#installation)
- [Setup](#setup)
- [Usage](#usage)


## Installation
Install the intended Batavia ORM version from the [release page](https://github.com/batavia-orm/batavia/releases). The installation will include 2 JAR files with the name:

`batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar`

`batavia-{version_number}-SNAPSHOT.jar` 
    

## Setup
Add both files to the root file of your Java project
```
├───src
│   ├───main
│   └───test
└───.env
└───batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar
└───batavia-{version_number}-SNAPSHOT.jar
└───README.md
```
### Annotation
In the eclipse package explorer, right click on your java project and click properties

<img width="229" alt="image" src="https://github.com/batavia-orm/batavia/assets/101443060/589d440f-4cd1-47f6-acea-c416355416f4">

Then, click on the `Java Build Path` tab and choose `Libraries` in the internal tab. Click on `modulepath` then click `Add JARs` and choose the `batavia-{version_number}-SNAPSHOT.jar`. Last, click the `apply and close`.

<img width="701" alt="image" src="https://github.com/batavia-orm/batavia/assets/101443060/c7167e27-3e3c-44f3-88f3-ae18c9c0d42b">

### Environment Variables
In the .env file, add 3 variables

- `DATABASE_URL : the JDBC Database URL`
- `UP_MIGRATION_PATH : the path to the up migration path folder`
- `DOWN_MIGRATION_PATH : the path to the down migration path folder`

## Usage

### Annotation
There are 4 annotations that can be used to denote the properties of the object:

- `@Entity: to denote SQL table`
- `@EntityColumn: to denote SQL table column`
- `@PrimaryColumn: to denote the primary column of a table`
- `@Unique: to denote a unique content in column`

### CLI
There are 2 ways to run the CLI commands:
- `java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar`: which will run the program in loop mode, accepting and executing the commands
- `java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar -{COMMAND}`: which will execute the inputted command
#### Generate
There are 2 ways to run the generate command:

`java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar -generate`: which will create a migration file with an auto generated file name

`java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar -generate {file_name}`: which will create a migration file with the given file name
Both command will have a timestamp added to the file name to maintain uniqueness
#### Migrate
`java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar -migrate`: which will execute the migration to the database
#### Revert
`java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar -revert`: which will revert the last migration done to the database
`java -jar batavia-{version_number}-SNAPSHOT-jar-with-dependencies.jar -revert {file_name}`: which will revert up to the specified migration file
