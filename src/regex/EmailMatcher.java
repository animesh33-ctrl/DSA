package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailMatcher {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_ $#.]+@[a-zA_Z]+.[a-z]{2,}$");
        Matcher matcher = pattern.matcher("paluianimesh31@gmail.com");
        System.out.println(matcher.matches());
    }
}
