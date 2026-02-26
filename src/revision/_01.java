package revision;

import java.util.Arrays;
import java.util.Scanner;

public class _01 {
    public static void main(String[] args) {
        Scanner  sc = new Scanner(System.in);
        int n = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        int[] arr = new int[n];
        for(int i=0;i<n;i++){
            arr[i] = sc.nextInt();
        }
        System.out.println(Arrays.toString(arr));
        Arrays.sort(arr);
        System.out.println("Sorted Array : "+Arrays.toString(arr));
        int p = arr[y] - arr[y-1]-1;
        System.out.println("There are "+p+" numbers of arr that satisfy the condition");
        sc.close();
    }
}
