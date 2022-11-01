package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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


            student.addBook(new Book(faker.book().title(), LocalDateTime.now().minusDays(5)));
            student.addBook(new Book(faker.book().title(), LocalDateTime.now()));
            student.addBook(new Book(faker.book().title(), LocalDateTime.now().minusDays(200)));

            StudentIdCard studentIdCard = new StudentIdCard("123456789", student);

            student.setStudentIdCard(studentIdCard);
            studentRepository.save(student);


            studentRepository.findById(1L).ifPresent(s -> {
                System.out.println("fetch book lazy...");
                List<Book> books = s.getBooks();
                books.forEach(book -> {
                    System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
                });
            });

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
