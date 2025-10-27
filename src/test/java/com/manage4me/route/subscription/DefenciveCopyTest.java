package com.manage4me.route.subscription;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DefenciveCopyTest {

    @Test
    void defenciveCopy() {
        // List.og retuyns an immutable list
        List<String> favoriteMovie = new ArrayList<>();
        favoriteMovie.add("Clerks");
        var person = new Person("Ted", favoriteMovie);
        System.out.println(person);
        favoriteMovie.add("Dogma");
        System.out.println(person);
    }

    class Person {
        private String name;

        private List<String> favoriteMovies;

        public Person(String name, List<String> favoriteMovies) {
            this.name = name;
            this.favoriteMovies = List.copyOf(favoriteMovies);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", favoriteMovies=" + favoriteMovies +
                    '}';
        }
    }
}
