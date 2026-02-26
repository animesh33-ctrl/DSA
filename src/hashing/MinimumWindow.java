package hashing;

import java.util.HashMap;
import java.util.Map;

public class MinimumWindow {

    public static int minWindow(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() < s2.length())
            return -1;

        Map<Character, Integer> freq = new HashMap<>();

        // frequency of s2
        for (char c : s2.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        int left = 0;
        int minLen = Integer.MAX_VALUE;
        int required = freq.size();

        for (int right = 0; right < s1.length(); right++) {
            char ch = s1.charAt(right);

            if (freq.containsKey(ch)) {
                freq.put(ch, freq.get(ch) - 1);
                if (freq.get(ch) == 0) {
                    required--;
                }
            }

            // valid window
            while (required == 0) {
                minLen = Math.min(minLen, right - left + 1);

                char leftChar = s1.charAt(left);
                if (freq.containsKey(leftChar)) {
                    freq.put(leftChar, freq.get(leftChar) + 1);
                    if (freq.get(leftChar) > 0) {
                        required++;
                    }
                }
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? -1 : minLen;
    }

    public static void main(String[] args) {
        System.out.println(minWindow("ajsdshfbhsdbf", "abc"));
    }
}
