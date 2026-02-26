//package _pr;
import java.util.*;
public class LongestSubstring {
    public static int lengthOfLongestSubstring(String str){
        if (str == null || str.length() <=0) return -1;
        Set<Character> set = new HashSet<>();
        int left = 0;
        int max = 0;
        for(int i=0;i<str.length();i++){
            while(set.contains(str.charAt(i))){
                set.remove(str.charAt(left++));
            }
            set.add(str.charAt(i));
            max = Math.max(max,i-left+1);
        }
        return max;
    }

	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the string : ");
		String str = sc.next();
		System.out.println(lengthOfLongestSubstring(str));
	}
}
