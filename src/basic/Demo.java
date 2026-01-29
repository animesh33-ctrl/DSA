package basic;

public class Demo {
    Demo(){
        interface AB{
            
        }
    }

    public static int[] arrayReverse(int arr[]){
        int i=0,j=arr.length-1;
        while (i<j){
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            i++;j--;
        }
        return  arr;
    }

    public static void display(int arr[]){
        for(int a:arr){
            System.out.print(a+" ");
        }
    }public static void display(int arr[][]){
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr[i].length;j++){
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int arr[] = {1,2,3,4,5,66,7,8,9};
        arr = arrayReverse(arr);
//        display(arr);

        int arr2[][] =  new int[4][];
        for(int i=0;i<arr2.length;i++){
            arr2[i] = new int[3];
        }
        for(int i=0;i<arr2.length;i++){
            for(int j=0;j<arr2[i].length;j++){
                arr2[i][j] = (i+1)*(j+1);
            }
        }
        display(arr2);

    }
}