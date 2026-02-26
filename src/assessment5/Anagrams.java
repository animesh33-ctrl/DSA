package assessment5;

import java.util.HashMap;

public class Anagrams {

    public static boolean checkAnagrams(String s1,String s2){
        if(s1==null || s2 == null || s1.length() != s2.length()) return false;
        HashMap<Character,Integer> map = new HashMap<>();
        for(char c : s1.toCharArray()){
            map.put(c,map.getOrDefault(c,0)+1);
        }
        for (char c : s2.toCharArray()) {
            if (!map.containsKey(c) || map.get(c) == 0)
                return false;
            map.put(c, map.get(c) - 1);
        }
        return true;
    }

    public static void main(String[] args) {
        String s1 = "aba";
        String s2 = "baa";
        System.out.println(checkAnagrams(s1,s2));
    }
}
