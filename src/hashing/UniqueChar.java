package hashing;

import java.util.Arrays;
import java.util.HashSet;

public class UniqueChar {

    public static char[] getUniqueChar(String str){
        if(str.isEmpty()) return null;

        HashSet<Character> set = new HashSet<>();
        for(int i=0;i<str.length();i++){
            char ch = str.charAt(i);
            if(set.contains(ch)){
                set.remove(ch);
            }else {
                set.add(ch);
            }
        }
        char[] arr = new char[set.size()];
        int i = 0;

        for (char ch : set) {
            arr[i++] = ch;
        }
        return arr;
    }

    public static void main(String[] args) {
        String str = "abba";

        char ans[] = getUniqueChar(str);
        if(ans!=null){
            System.out.println(Arrays.toString(ans));
        }
        else{
            System.out.println("Null");
        }
    }

}
