package searching;

import java.util.Scanner;

public class LinearSearch {

    public static int linearSearch(int arr[],int target){
        for(int i=0;i<arr.length;i++){
            if(arr[i] == target){
                return i+1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] arr = new int[N];
        int target = sc.nextInt();
        for(int i=0;i<N;i++){
            arr[i] = sc.nextInt();
        }
        int res = linearSearch(arr,target);
        if(res != -1){
            System.out.println("Found at Position : "+res);
        }
        else {
            System.out.println("Target not Found");
        }

        sc.close();
    }
}
