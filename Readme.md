# Database Class

This repository contains a Java implementation of a `Database` class. The `Database` class simulates basic database operations such as insert, delete, find, and update using a 2D array to store data and Treaps for indexing.

## Features

- **Insert**: Add new rows to the database.
- **Remove**: Delete rows based on a condition.
- **Find**: Search for rows based on a condition.
- **Update**: Modify rows based on a condition.
- **Indexing**: Support for creating and managing indexes on columns using Treaps.

## Usage

### Initialization

To create a new database instance, initialize it with the column names and the maximum size of the database.

```
String[] columns = {"ID", "Name", "Age"};
int maxSize = 100;
Database db = new Database(columns, maxSize);

```
## Insert

To insert a new row into the database:
```
String[] newRow = {"1", "John Doe", "30"};
try {
    db.insert(newRow);
} catch (DatabaseException e) {
    e.printStackTrace();
}
```

## Remove
```
try {
    String[] removedRow = db.removeFirstWhere("Name", "John Doe");
} catch (DatabaseException e) {
    e.printStackTrace();
}
```

To remove all rows where a column matches a specified value:

```
try {
    String[][] removedRows = db.removeAllWhere("Name", "John Doe");
} catch (DatabaseException e) {
    e.printStackTrace();
}
```

## Find

To find the first row where a column matches a specified value:

```
try {
    String[] foundRow = db.findFirstWhere("Name", "John Doe");
} catch (DatabaseException e) {
    e.printStackTrace();
}
```
To find all rows where a column matches a specified value:

```
try {
    String[][] foundRows = db.findAllWhere("Name", "John Doe");
} catch (DatabaseException e) {
    e.printStackTrace();
}
```
## Update

To update the first row where a column matches a specified condition:

```
try {
    String[] updatedRow = db.updateFirstWhere("Name", "John Doe", "Jonathan Doe");
} catch (DatabaseException e) {
    e.printStackTrace();
}
```
To update all rows where a column matches a specified condition:
```
try {
    String[][] updatedRows = db.updateAllWhere("Name", "John Doe", "Jonathan Doe");
} catch (DatabaseException e) {
    e.printStackTrace();
}
```
## Indexing

To create an index on a specific column:
```
try {
    db.generateIndexOn("Name");
} catch (DatabaseException e) {
    e.printStackTrace();
}
```
To create indexes on all columns:
```
try {
    db.generateIndexAll();
} catch (DatabaseException e) {
    e.printStackTrace();
}
```
## Exception Handling

The Database class uses a custom DatabaseException class to handle various error conditions such as duplicate inserts, invalid column names, and full database.

## Classes and Methods

### `Database`

- `Database(String[] cols, int maxSize)`: Constructor to initialize the database.
- `void insert(String[] newRowDetails)`: Inserts a new row into the database.
- `String[] removeFirstWhere(String col, String data)`: Removes the first row matching the condition.
- `String[][] removeAllWhere(String col, String data)`: Removes all rows matching the condition.
- `String[] findFirstWhere(String col, String data)`: Finds the first row matching the condition.
- `String[][] findAllWhere(String col, String data)`: Finds all rows matching the condition.
- `String[] updateFirstWhere(String col, String updateCondition, String data)`: Updates the first row matching the condition.
- `String[][] updateAllWhere(String col, String updateCondition, String data)`: Updates all rows matching the condition.
- `Treap<Cell> generateIndexOn(String col)`: Generates an index on the specified column.
- `Treap<Cell>[] generateIndexAll()`: Generates indexes on all columns.
- `int countOccurences(String col, String data)`: Counts the occurrences of a value in a specified column.

##Dependencies
The Database class depends on the Treap and Cell classes, as well as the DatabaseException class.

