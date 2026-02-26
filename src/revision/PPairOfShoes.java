package revision;

import java.util.Arrays;
import java.util.Scanner;

public class PPairOfShoes {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int p = sc.nextInt();

        int[] arr = new int[n];
        for(int i=0;i<n;i++){
            arr[i] = sc.nextInt();
        }
        System.out.println("Original : "+Arrays.toString(arr));
        Arrays.sort(arr);
        System.out.println("Sorted Array : "+Arrays.toString(arr));
        int paisa = 0;
        for(int i=0;i<p;i++){
            paisa += arr[i];
        }
        if(paisa >0)paisa=0;
        System.out.println("Maximum paisa that can be earned : "+Math.abs(paisa));
        sc.close();
    }
}
