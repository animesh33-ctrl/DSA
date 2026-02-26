package orange;

import java.util.*;

public class OrangePartitionInPlace {

    public static int partition(int[] arr, int n) {
        int pivot = arr[n - 1];
        int i = -1;

        for (int j = 0; j < n - 1; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[n - 1];
        arr[n - 1] = temp;

        return i + 1;
    }


    public static void quicksort(int[] array,int low,int high){
        int pivot = partition(array,array.length);

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int[] oranges = new int[n];

        for (int i = 0; i < n; i++) {
            oranges[i] = sc.nextInt();
        }

        int pivotIndex = partition(oranges, n);

        System.out.println("Partitioned Array: " + Arrays.toString(oranges));
        System.out.println("Pivot Index: " + pivotIndex+1);
    }
}
