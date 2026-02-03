package array;

public class RemoveDuplicates {

    public static int removeDuplicates(int arr[]){
        int n = arr.length;
        if(n==0 || arr == null) return 0;
        int uniqueIndex = 0;
        for(int i=1;i<n;i++){
            if(arr[i] != arr[uniqueIndex]){
                arr[++uniqueIndex] = arr[i];
            }
        }
        return uniqueIndex;

    }

    public static void printArray(int arr[],int length){
        for(int i=0;i<length;i++){
            System.out.print(arr[i]+" ");
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 2, 2, 3, 4, 4, 5, 5, 5, 6};

        System.out.println("Original Array:");
        printArray(arr, arr.length);

        // Remove duplicates
        int newLength = removeDuplicates(arr);

        System.out.println("\nArray after removing duplicates:");
        printArray(arr, newLength);

        System.out.println("\nNumber of unique elements: " + newLength);
    }
}
