# flyway-jpa-quickstart
Demo for [Flyway][7] migrations + JPA DDL Generation.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes

### Building

You can generate the initial database schema with:

```sh
mvn flyway:migrate
```

Then run the main application to execute a JPQL query
 
```sh
mvn compile exec:java
```

### Generating update scripts from JPA Model 

First let's modify the model. 

Open [`Person.java`][2] and uncomment lines [9][3], [21][4], [22][4] and [66][5]:

```java
package com.sevenrtc.stackoverflow.flyway.quickstart.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PERSON", uniqueConstraints = {
        // Uncomment to test JPA DDL generation
        @UniqueConstraint(name = "UK_PERSON_SSN", columnNames = "SSN")
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

//    Uncomment to test JPA DDL generation
    @Column(name = "SSN", nullable = false, length = 15)
    private String ssn;
    
/* ... more code ... */

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +                
                ", ssn='" + ssn + '\'' +
                '}';
    }
}
```

Run the following command to generate a SQL Script with your changes: 

```sh
mvn -PgenerateDDLFromJPAModel process-classes
```

The resulting script can be found at `target/jpa/sql/schema-update.sql`

```sql
    alter table PUBLIC.PERSON 
       add column SSN varchar(15) not null;

    alter table PUBLIC.PERSON 
       drop constraint if exists UK_PERSON_SSN;

    alter table PUBLIC.PERSON 
       add constraint UK_PERSON_SSN unique (SSN);
```

### Manually fixing scripts

`schema-update.sql` will not work by default. Can you spot the problem with it? 


While Hibernate is generating valid SQL code, you can't really add `not null` columns to `PERSON` due to the fact that
it has already been populated with some data. Even if you remove `not null` the unique constraint will fail.

It is very important to consider your data when writing migration scripts. The same script may be applied in multiple
environments with tables in different states (e.g., data may have been added manually). As a rule of thumb it is best
to avoid making assumptions about the data.

Fortunately the problem above is easily fixable. We just need to make sure to load data to the new `SSN` column before
making it `not null` and enabling the `unique` constraint.

We could certainly write a complex ETL procedure to load data from different tables or external data sources.
However, for the sake of simplicity I've modified the above script to generate fake data and saved it to 
[3__Add_SSN_to_person_table.sql][6]:

```sql
alter table PERSON
  add column SSN varchar(15);

update PERSON
set SSN = (select LPAD(ID, 15, '0'))
where SSN is null;

alter table PERSON
  alter column SSN varchar(15) not null;

alter table PERSON
  add constraint UK_PERSON_SSN unique (SSN);
```

In order to update the database we need to copy the revised script to the `db/migration` folder and instruct 
Flyway to migrate the schema

```sh
cp src/main/resources/db/new_migrations/V3__Add_SSN_to_person_table.sql src/main/resources/db/migration/
mvn flyway:migrate
```

Finally we can execute the main application one more time to verify the results:

```sh
mvn compile exec:java
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

[1]: https://pt.stackoverflow.com/q/297326/100
[2]: src/main/java/com/sevenrtc/stackoverflow/flyway/quickstart/entities/Person.java
[3]: src/main/java/com/sevenrtc/stackoverflow/flyway/quickstart/entities/Person.java#L9
[4]: src/main/java/com/sevenrtc/stackoverflow/flyway/quickstart/entities/Person.java#L21-L22
[5]: src/main/java/com/sevenrtc/stackoverflow/flyway/quickstart/entities/Person.java#L66
[6]: src/main/resources/db/new_migrations/V3__Add_SSN_to_person_table.sql
[7]: https://flywaydb.org/