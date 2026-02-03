package sorting;

public class BubbleSort {

    public static void bubbleSort(int[] arr){
        int n=arr.length;
        if(n==0) return;

        for(int i=0;i<n-1;i++){
            for(int j=0;j<n-1-i;j++){
                if(arr[j]>arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
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

        bubbleSort(arr);

        display(arr,"sorted array : ");
    }
}
