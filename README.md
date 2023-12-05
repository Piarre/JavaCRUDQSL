## Java CRUD SQL

### Description
A CLI application that allows the user to create, read, update, and delete records from a database (MySQL).

### Technologies
- Java
- MySQL
- JDBC
- Maven


### User table
```sql
create table users
(
    id        int auto_increment primary key,
    name      varchar(255)                        null,
    surname   varchar(255)                        null,
    email     varchar(255)                        not null,
    password  varchar(255)                        not null,
    actif     varchar(255)                        null,
    age       int                                 null,
    createdAt timestamp default CURRENT_TIMESTAMP not null,
    updatedAt timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    createdBy varchar(255)                        not null,
    constraint email_unique
        unique (email)
);
```