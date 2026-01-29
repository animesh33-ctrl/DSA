package basic;

public class RemoveDuplicate2D {
    public static void main(String[] args) {
        int[][] arr = {
                {1,2,3,4},
                {2,3,4,5}
        };

        int ele = 5;
        for (int i = 0; i < arr.length; i++) {
            for(int j=0;j<arr[i].length;j++){

                if(arr[i][j] == ele){
                    arr[i][j] = Integer.MIN_VALUE;
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for(int j=0;j<arr[i].length;j++) {
                if (arr[i][j] != Integer.MIN_VALUE) {
                    System.out.print(arr[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
