package assessment5;

import java.util.Arrays;

public class _021Sort {
    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 0, 2, 1, 0};
        sortArr(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sortArr(int[] arr) {
        if(arr.length==0) return;
        int low=0,mid=0,high=arr.length-1;
        while(mid<=high){
            if(arr[mid]==0){
                swap(arr,low,mid);
                low ++;
                mid++;
            } else if (arr[mid] == 1) {
                swap(arr,mid,high);
                high--;
            }
            else {
                mid++;
            }
        }
    }

    public static void swap(int[] arr,int i,int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
