package array;

import java.util.Scanner;

public class RotateElementsLeftByD {

    public static void reverseArr(int arr[],int st,int end){
        while (st<=end){
            int temp = arr[st];
            arr[st] = arr[end];
            arr[end] = temp;
            st++;
            end--;
        }
    }

    public static void rotateArrayLeftByDPositions(int[] arr, int d){
        int n = arr.length;
        if(d==0 || d>=n ) return;

        reverseArr(arr,0,d-1);
        reverseArr(arr,d,n-1);
        reverseArr(arr,0,n-1);

    }

    public static void display(int arr[]){
        for(int i:arr){
            System.out.print(i+" ");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] arr = new int[N];
        int D = sc.nextInt();
        for(int i=0;i<N;i++){
            arr[i] = sc.nextInt();
        }
        rotateArrayLeftByDPositions(arr,D);
        display(arr);
    }
}
