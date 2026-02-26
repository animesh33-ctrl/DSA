package hashing;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

public class SimpleHashing {
    public static void main(String[] args) {
        HashSet<Employee> employees = new HashSet<>();
        employees.add(new Employee(1,"Mark1","Male",
                LocalDate.of(1990, Month.DECEMBER,21),3,"mark1@gmail.com",1234567980));

        employees.add(new Employee(2,"Mark2","Female",
                LocalDate.of(1990, Month.NOVEMBER,21),3,"mark2@gmail.com",1234560798));

        employees.add(new Employee(3,"Mark3","Male",
                LocalDate.of(1990, Month.OCTOBER,21),1,"mark3@gmail.com",1234567980));

        employees.add(new Employee(4,"Mark4","Female",
                LocalDate.of(1990, Month.SEPTEMBER,21),2,"mark4@gmail.com",1234567980));

        employees.add(new Employee(5,"Mark5","Male",
                LocalDate.of(1990, Month.AUGUST,21),7,"mark5@gmail.com",1234567980));

        employees.add(new Employee(6,"Mark6","Female",
                LocalDate.of(1990, Month.JULY,21),5,"mark6@gmail.com",1234567980));

        employees.add(new Employee(7,"Mark7","Male",
                LocalDate.of(1990, Month.JUNE,21),4,"mark7@gmail.com",1234567980));

        employees.add(new Employee(7,"Mark7","Male",
                LocalDate.of(1990, Month.JUNE,21),4,"mark7@gmail.com",1234567980));

        System.out.println("=".repeat(45));

        for(Employee employee : employees)
            System.out.println(employee);
    }
}
