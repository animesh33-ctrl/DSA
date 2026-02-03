package recursion;

import java.util.Arrays;

public class Basic {
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7};
        modify(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void modify(int[] arr){
        arr[0] = Integer.MIN_VALUE;
    }
}
