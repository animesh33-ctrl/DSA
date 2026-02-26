package hashing;

import java.util.*;

public class Anagram {

    public static List<List<String>> groupAnagrams(String[] words){
        if(words.length == 0) return null;

        Map<String, List<String>> map = new HashMap<>();

        for (String word : words) {
            char[] arr = word.toCharArray();
            Arrays.sort(arr);
            String key = new String(arr);
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(word);
        }
        return new ArrayList<>(map.values());
    }

    public static void main(String[] args) {
        String[] words = {"eat","tea","tan","ate","tab","nat","bat"};

        List<List<String>> ans = groupAnagrams(words);
        System.out.println(ans);

    }
}
