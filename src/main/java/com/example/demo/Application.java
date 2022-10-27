package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, StudentIdCardRepository studentIdCardRepository, BookRepository bookRepository){
        return args -> {
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));
            StudentIdCard studentIdCard = new StudentIdCard("123456789", student);
            studentIdCardRepository.save(studentIdCard);

//            String bookName = faker.book().title();
//            Timestamp createdAt = new Timestamp(faker.number().randomNumber());
//
//            String bookName2 = faker.book().title();
//            Timestamp createdAt2= new Timestamp(faker.number().randomNumber());
//
//            Book book = new Book(student, bookName2, createdAt2);
//            Book book2 = new Book(student, bookName2, createdAt2);
//
//            bookRepository.saveAll(List.of(book, book2));
        };
    }

    private static void paging(StudentRepository studentRepository) {
        PageRequest pageRequest = PageRequest.of(
                0,
                5,
                Sort.by( "firstName").ascending());
        Page<Student> page = studentRepository.findAll(pageRequest);
        System.out.println(page);
    }

    private static void sorting(StudentRepository studentRepository) {
        Sort sort = Sort.by( "firstName").ascending().
                and(Sort.by("age").descending());
        studentRepository.findAll(sort).
                forEach(student -> System.out.println(student.getFirstName() + "    " + student.getAge()));
    }

    private static void generateRandomStudents(StudentRepository studentRepository) {
        for (int i = 0; i < 20; i++) {
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));
            studentRepository.save(student);
        }
    }
}
