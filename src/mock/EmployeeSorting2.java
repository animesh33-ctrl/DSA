package mock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class EmployeeSorting2 {

    public static List<List<String>> getSortedList(List<List<String>> employees){
        if(employees.size()<=1) return  null;

////        employees.sort((o1,o2)->{
////            return (o1.get(4)+o1.get(5)).compareTo((o2.get(4)+o2.get(5)));
////        });
//
//        //another option
//        Comparator<List<String>> date = (a,b) -> LocalDate.parse(a.get(4)).compareTo(LocalDate.parse(b.get(4)));
//        Comparator<List<String>> time = (a,b) -> LocalTime.parse(a.get(5)).compareTo(LocalTime.parse(b.get(5)));
//
//        employees.sort(date.thenComparing(time));

//        return employees;

        return employees.stream().sorted(Comparator.comparing((List<String> obj)-> LocalDate.parse(obj.get(4)))
                .thenComparing(obj->LocalTime.parse(obj.get(5)))).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<List<String>> data = new ArrayList<>();

        data.add(List.of("201", "Zayan", "zayan@gmail.com", "4", "1997-11-23", "09:40", "9876543210"));

        data.add(List.of("202", "Meera", "meera@gmail.com", "2", "2001-06-14", "07:20", "9123456789"));

        data.add(List.of("203", "Arjun", "arjun@gmail.com", "6", "1995-03-02", "10:05", "9988776655"));

        data.add(List.of("204", "Kavya", "kavya@gmail.com", "3", "1997-11-23", "06:50", "8899001122"));

        data.add(List.of("205", "Ishaan", "ishaan@gmail.com", "5", "1999-09-18", "08:30", "9001122334"));

        data.add(List.of("206", "Ritika", "ritika@gmail.com", "1", "2001-06-14", "09:10", "9011223344"));

//        List<List<String>> ans = getSortedList(data);
//        assert ans != null;
//        for(List<String> list:ans){
//            System.out.println(list);
//        }

        getSortedList(data).forEach(list -> System.out.println(list));

    }
}
