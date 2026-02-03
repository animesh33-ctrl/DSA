package sorting;

public class SelectionSort {

    public static void selectionSort(int[] arr){
        int n=arr.length;
        if(n==0) return;

        int minidx = -1;

        for(int i=0;i<n-1;i++){
            minidx = i;
            for(int j=i+1;j<n;j++){
                if(arr[minidx] > arr[j] ){
                    minidx = j;
                }
            }
            if(minidx != i){
                int temp = arr[minidx];
                arr[minidx] = arr[i];
                arr[i] = temp;
            }
        }
    }

    public static void display(int[] arr,String label){
        System.out.print(label+" ");
        for (int j : arr) {
            System.out.print(j + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = {5,4,3,2,1};
        display(arr,"original array : ");

        selectionSort(arr);

        display(arr,"sorted array : ");
    }
}
