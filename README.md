# Java DDL Patch Library

Java DDL Patch Library (or jDDL) is a small tool that allows to path SQL tables definitions. The easiest way to think about it is to see at as a diff tool.

You have two DDLs:

* The one you have in database
* The one you define in your code

jDDL will generate a list of SQL statements ('ALTER TABLE ...') that brings your database schema to scheme defined in your code. For schema definition jDDL uses YML format instead of SQL.

Example:

#### Table in your database

```sql
CREATE TABLE person (
  ID int NOT NULL PRIMARY KEY,
  LastName varchar(255) NOT NULL,
  FirstName` varchar(255),
);
```

#### Schema defined in your app

```yaml
tables:
  - table: person
    columns:
      - name: 'ID'
        type: 'int'
        non-null: 'true'
        options: 'PRIMARY KEY'
      - name: 'FirstName'
        type: 'varchar(255)'
      - name: 'City'
        type: 'varchar(255)'
        default: 'Dublin'


```

jDDL will generate a following set of statements:

```sql
ALTER TABLE `SQL_TABLE` DROP COLUMN `FIRSTNAME`;
ALTER TABLE `SQL_TABLE` ADD `City` varchar(255) DEFAULT 'Dublin'
```

If table doesn't exist, it will be created.

## Usage

Apply changes to database

```java
try (Reader reader = new FileReader("/path/to/schema.yml"); 
     Connection conn = getConnection()) {
     new JDDL(reader, conn).applyChanges();    
}

```

Generate list of statements

```java
try (Reader reader = new FileReader("/path/to/schema.yml"); 
     Connection conn = getConnection()) {
     List<String> statements = new JDDL(reader, conn).generatePatch();    
     //apply `statements` manually
}
```



## Limitations

* jDDL doesn't support sync of indeces
* jDDL doesn't support the change of types. If column kept the name but type has changed. 

## Advanced Features

### Placeholders

Sometimes different types can be user in schema depending on environment. A good example would be JSON: an application may use json type in production environment (such as pSQL or MySQL) but use TEXT instead in integration tests with H2 (where JSON type is not support). In that case placeholders can be used in YAML:

``` yaml
tables:
  - table: person
    columns:
      - name: 'ID'
        type: 'int'
        non-null: 'true'
        options: 'PRIMARY KEY'
      - name: '${DATA_COLUMN_NAME}'
        type: '${JSON_TYPE:TEXT}'
```

Following Java code will appy placeholders:

```java
try (Reader reader = new FileReader("/path/to/schema.yml");
     Connection conn = getConnection()) {
			new JDDL(reader, conn, Map.of("DATA_COLUMN_NAME", "data")).applyChanges();
}

```

Placeholders can have default values which will be user if value is not specified. Syntax: ${name:defaultValue}. YAML above will be preprecessed to

```yaml
tables:
  - table: person
    columns:
      - name: 'ID'
        type: 'int'
        non-null: 'true'
        options: 'PRIMARY KEY'
      - name: 'data'
        type: 'text'
```



## Next features

* Support of column comparison: if column with same name appeared both in DB and expected schema — jDDL will compare types and other settings and will try to make a change. Otherwise it will throw an exception
* Support of indeces, primary keys and contstraints
* Support of SQL DDL in addition to in-house YML

## Dependencies

The library is design to be as lightweight as possible. For DB communication it uses JDBC which is a part of core Java library. However it has a few external dependencies:

* [Jackson](https://github.com/FasterXML/jackson) —core, databind, annotations and databind-yml. For parsing YML definition
* [Guava](https://github.com/google/guava)