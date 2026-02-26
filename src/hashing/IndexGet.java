package hashing;

import java.util.HashSet;

public class IndexGet {
    public static void main(String[] args) {
        System.out.println(getIndex("dabba"));
    }

//    public static int getIndex(String str){
//        HashSet<Character> seen = new HashSet<>();
//        for(int i=0;i<str.length();i++){
//            char ch = str.charAt(i);
//            if(seen.contains(ch)){
//                System.out.print(ch +" : ");
//                return str.indexOf(String.valueOf(ch))+1;
//            }
//            seen.add(ch);
//        }
//        return -1;
//    }

    public static char getIndex(String str){
        HashSet<Character> seen = new HashSet<>();
        for(int i=0;i<str.length();i++){
            char ch = str.charAt(i);
            if(seen.contains(ch)){
                return ch;
            }
            seen.add(ch);
        }
        return '0';
    }
}
