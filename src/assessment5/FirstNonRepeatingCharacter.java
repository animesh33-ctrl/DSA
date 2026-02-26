package assessment5;

import java.util.LinkedHashMap;
import java.util.Map;

public class FirstNonRepeatingCharacter {

    public static int firstNonRepeating(String str){
        if(str.length()==0) return -1;
        LinkedHashMap<Character,Integer> map = new LinkedHashMap<>();
        for (int i=0;i<str.length();i++){
            char ch = str.charAt(i);
            map.put(ch,map.getOrDefault(ch,0)+1);
        }
        for(Map.Entry<Character,Integer> entry : map.entrySet()){
            if(entry.getValue() == 1){
                return str.indexOf(entry.getKey());
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String str = "aabcdd";
        System.out.println(firstNonRepeating(str));
    }
}
