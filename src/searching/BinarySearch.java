package searching;

import java.util.Scanner;

public class BinarySearch {

    public static int binarySearch(int arr[],int target){
        if(arr.length == 0) return -1;

        int left = 0,right = arr.length-1;

        while(left<right){
            int mid = (left+right)/2;
            if(arr[mid]==target) return mid;
            else if(arr[mid]>target){
                right=mid-1;
            }
            else{
                left = mid+1;
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
        int res = binarySearch(arr,target);
        if(res != -1){
            System.out.println("Found at Position : "+res+1);
        }
        else {
            System.out.println("Target not Found");
        }

        sc.close();
    }
}
