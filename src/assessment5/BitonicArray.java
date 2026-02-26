package assessment5;

public class BitonicArray {

    public static int bitonicElement(int[] arr){
        if(arr==null || arr.length==0) return -1;
        if(arr.length == 1) return arr[0];
        int low = 0,high = arr.length-1;
        while(low<high){
            int mid = low + (high-low)/2;
            if(arr[mid]<arr[mid+1]){
                low=mid+1;
            }
            else{
                high=mid;
            }
        }
        return arr[high];
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 8, 12, 4, 2};
        System.out.println(bitonicElement(arr));
    }
}
