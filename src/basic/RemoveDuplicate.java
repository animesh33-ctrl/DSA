package basic;

public class RemoveDuplicate   {
    public static void main(String[] args) {
        int[] arr = {1,1,2,1,2,3,4,3};
        int ele = 1;
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] == ele){
                arr[i] = Integer.MIN_VALUE;
            }
        }

        for(int e:arr){
            if(e!=Integer.MIN_VALUE){
                System.out.println(e+" ");
            }
        }
    }
}
