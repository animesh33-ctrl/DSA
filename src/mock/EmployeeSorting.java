package mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Arrays;

public class EmployeeSorting {

//    private static List<Employee> getSortedEmployee(Employee[] employees){
////        Arrays.sort(employees,(a,b)->a.getDateOfBirth().compareTo(b.getDateOfBirth()));
////        Arrays.sort(employees,(a,b)->a.getTimeOfBirth().compareTo(b.getTimeOfBirth()));
//        Arrays.sort(employees,(a,b)-> LocalDateTime.of(a.getDateOfBirth(), a.getTimeOfBirth())
//                .compareTo( LocalDateTime.of(b.getDateOfBirth(), b.getTimeOfBirth())));
//        return Arrays.asList(employees);
//    }

    public static List<Employee> getSortedEmployee(Employee[] employees){

//        List<Employee> employeeList = new ArrayList<>(Arrays.asList(employees));
//
//        Comparator<Employee> dateOfBirth=(e1, e2) -> e1.getDateOfBirth().compareTo(e2.getDateOfBirth());
//        Comparator<Employee> timeOfBirth=(e1, e2) -> e1.getTimeOfBirth().compareTo(e2.getTimeOfBirth());
//
//        employeeList.sort(dateOfBirth.thenComparing(timeOfBirth)
//                .thenComparing((o1, o2) -> o1.getExp() - o2.getExp())
//                .thenComparing((o1, o2) -> o1.getId() - o2.getId()));

//        return employeeList;

//        return Arrays.stream(employees)
//                .sorted(Comparator.comparing((Employee obj)->obj.getDateOfBirth()).thenComparing(obj-> obj.getTimeOfBirth()))
//                .collect(Collectors.toList());
        return Arrays.stream(employees)
        .sorted(Comparator.comparing(Employee::getDateOfBirth).thenComparing(Employee::getTimeOfBirth))
        .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Employee[] employees = {
                new Employee(1,"Name1","name1@gmail.com",2,
                        LocalDate.of(1921,Month.DECEMBER,12),
                        LocalTime.of(12,30),1234567890),

                new Employee(2, "Name2", "name2@gmail.com", 1,
                        LocalDate.of(1995, Month.JANUARY, 5),
                        LocalTime.of(9, 15), 9876543210L),

                new Employee(3, "Name3", "name3@gmail.com", 3,
                        LocalDate.of(1988, Month.MARCH, 22),
                        LocalTime.of(10, 45), 9123456789L),

                new Employee(4, "Name4", "name4@gmail.com", 2,
                        LocalDate.of(2000, Month.JULY, 18),
                        LocalTime.of(14, 0), 9988776655L),

                new Employee(5, "Name5", "name5@gmail.com", 4,
                        LocalDate.of(1992, Month.NOVEMBER, 30),
                        LocalTime.of(16, 20), 9090909090L),

                new Employee(6, "Name6", "name6@gmail.com", 1,
                        LocalDate.of(1984, Month.OCTOBER, 9),
                        LocalTime.of(11, 10), 9345678123L),

                new Employee(7, "Name7", "name7@gmail.com", 5,
                        LocalDate.of(1999, Month.SEPTEMBER, 25),
                        LocalTime.of(18, 45), 9765432109L),

                new Employee(8, "Name8", "name8@gmail.com", 2,
                        LocalDate.of(1993, Month.FEBRUARY, 14),
                        LocalTime.of(8, 30), 9812345678L),

                new Employee(9, "Name9", "name9@gmail.com", 3,
                        LocalDate.of(1987, Month.APRIL, 6),
                        LocalTime.of(9, 50), 9823456789L),

                new Employee(10, "Name10", "name10@gmail.com", 1,
                        LocalDate.of(1998, Month.JUNE, 21),
                        LocalTime.of(11, 0), 9834567890L),

                new Employee(11, "Name11", "name11@gmail.com", 4,
                        LocalDate.of(1991, Month.AUGUST, 3),
                        LocalTime.of(13, 40), 9845678901L),

                new Employee(12, "Name12", "name12@gmail.com", 2,
                        LocalDate.of(1984, Month.OCTOBER, 9),
                        LocalTime.of(11, 10), 9856789012L),

                new Employee(13, "Name13", "name13@gmail.com", 5,
                        LocalDate.of(2001, Month.DECEMBER, 1),
                        LocalTime.of(17, 10), 9867890123L),

                new Employee(14, "Name14", "name14@gmail.com", 3,
                        LocalDate.of(1996, Month.JANUARY, 19),
                        LocalTime.of(10, 5), 9878901234L),

                new Employee(15, "Name15", "name15@gmail.com", 1,
                        LocalDate.of(1989, Month.MARCH, 11),
                        LocalTime.of(12, 55), 9889012345L),

                new Employee(16, "Name16", "name16@gmail.com", 4,
                        LocalDate.of(1994, Month.MAY, 29),
                        LocalTime.of(14, 35), 9890123456L),

                new Employee(17, "Name17", "name17@gmail.com", 2,
                        LocalDate.of(2000, Month.JULY, 7),
                        LocalTime.of(16, 45), 9901234567L)

        };

//        List<Employee> list = Arrays.asList(employees); // it is a immutable collection bcz of using asList
//        list.add(new Employee());// so we can't add

        //but there is a method to do the work
        List<Employee> list = new ArrayList<>(Arrays.asList(employees));
        list.add(new Employee());

        List<Employee> ans = getSortedEmployee(employees);

        for( Employee e : ans){
            System.out.println(e);
        }
//        System.out.println(Arrays.toString(employees));
    }
}
