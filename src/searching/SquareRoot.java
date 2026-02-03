package searching;

import java.util.Scanner;

public class SquareRoot {

    public static int returnSquareRoot(int n){
        if(n == 0 || n == 1) {
            return n;
        }

        int left = 1, right = n;
        int res = 0;

        while(left <= right){
            int mid = left + (right - left) / 2;

            if(mid <= n / mid){
                res = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println("Square Root of N is : "+returnSquareRoot(n));
        sc.close();
    }
}
