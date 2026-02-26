package streamapi;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public class Basic {
    public static List<Employee> filterOdd(Employee[] arr) {
        List<Employee> ans =  Arrays.stream(arr).filter(emp->emp.id%2==0).collect(Collectors.toList());
        ans.sort((e1,e2)->e1.id-e2.id);
        return ans;
    }

    public static void main(String[] args) {
        Employee[] arr = {new Employee(1),new Employee(2),new Employee(3),new Employee(4),new Employee(5),new Employee(6),new Employee(7),
                new Employee(8),new Employee(9),new Employee(10),new Employee(11),new Employee(12),new Employee(13),new Employee(14),new Employee(15)};
        System.out.println(filterOdd(arr));
    }
}

class Employee{
    int id;

    public Employee(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                '}';
    }
}
