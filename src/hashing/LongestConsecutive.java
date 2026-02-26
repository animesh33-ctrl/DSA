package hashing;

import java.util.*;

public class LongestConsecutive {

    public static int longestConsecutive(int []arr){
        if(arr.length == 0) return 0;
        HashSet<Integer> set = new HashSet<>();
        int max = 0;
        for (int j : arr) {
            set.add(j);
        }
        for(int number : set){
            if(!set.contains(number-1)){
                int curr = number;
                int count = 1;
                while(set.contains(curr+1)){
                    curr++;
                    count++;
                }
                max = Math.max(max,count);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[] arr = {400,4,200,1,3,2,5};
        System.out.println(longestConsecutive(arr));
    }

}
