package array;

public class Array1D {

    public static void display(int arr[]){
        for(int i: arr){
            System.out.print(i+" ");
        }
    }

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,9};
        display(arr);
    }
}
