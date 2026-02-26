package assessment5;

import java.util.Arrays;

public class SortInWaveForm {

    public static void sortInWaveForm(int[] arr){
        if(arr==null || arr.length==0) return;

        for(int i=0;i< arr.length-1;i+=2){
            swap(arr,i,i+1);
        }

    }

    private static void swap(int[] arr,int i,int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {3, 6, 5, 10, 7, 20,21};
        sortInWaveForm(arr);
        System.out.println(Arrays.toString(arr));
    }
}
