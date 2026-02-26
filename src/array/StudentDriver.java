package array;

import javax.naming.Name;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

public class StudentDriver {
    public static void main(String[] args) {

        Student[] students = {
                new Student(1,"Animesh","animesh@gmail.com","Male", LocalDate.of(2003, Month.DECEMBER,23)),
                new Student(2,"Aditya","adity@gmail.com","Female", LocalDate.of(2004, Month.DECEMBER,23)),
                new Student(3,"Sahil","sahil@gmail.com","Male", LocalDate.of(2005, Month.DECEMBER,23)),
                new Student(4,"Sombit","sombit@gmail.com","Female", LocalDate.of(2006, Month.DECEMBER,23)),
                new Student(5,"Arnab","arnab@gmail.com","Male", LocalDate.of(2007, Month.DECEMBER,23))
        };

        for(Student student : students){
            System.out.println(student);
        }
        System.out.println("-".repeat(80));
        Arrays.sort(students); // sort default
//        Arrays.sort(students,(s1,s2) -> s2.getId() - s1.getId()); // sorting based on id

        NameComparator nameComparator = new NameComparator();
//        Arrays.sort(students,nameComparator); // sorting based on name

        Arrays.sort(students,(s1,s2)->s1.getName().compareTo(s2.getName()));


        for(Student student : students){
            System.out.println(student);
        }

    }
}
