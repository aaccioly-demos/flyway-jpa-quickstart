package com.sevenrtc.stackoverflow.flyway.quickstart.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PERSON", uniqueConstraints = {
        // Uncomment to test JPA DDL generation
//        @UniqueConstraint(name = "UK_PERSON_SSN", columnNames = "SSN")
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    // Uncomment to test JPA DDL generation
//    @Column(name = "SSN", nullable = false, length = 15)
//    private String ssn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

/*   public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", ssn='" + ssn + '\'' +
                '}';
    }
}
