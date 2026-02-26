package _pr;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Demo1 {
    public static void main(String[] args) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-M-d");
        System.out.println(LocalDate.parse("1983-9-1",formatter));
    }
}
