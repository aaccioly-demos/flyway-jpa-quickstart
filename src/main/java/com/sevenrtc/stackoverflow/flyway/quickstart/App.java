package com.sevenrtc.stackoverflow.flyway.quickstart;

import com.sevenrtc.stackoverflow.flyway.quickstart.entities.Person;

import javax.persistence.*;
import java.util.List;

public class App {

    private static final String PERSISTENCE_UNIT_NAME = "PeoplePersistenceUnit";

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        // read the existing entries and write to console
        TypedQuery<Person> q = em.createQuery("select p from Person p", Person.class);
        List<Person> people = q.getResultList();
        for (Person person : people) {
            System.out.println(person);
        }
        System.out.println("Size: " + people.size());
        em.close();
        factory.close();
    }
}
