package regex;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcceptNumber {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String regex = "[0-9]{10}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sc.nextLine());
        System.out.println(matcher.matches());
        sc.close();
    }
}
