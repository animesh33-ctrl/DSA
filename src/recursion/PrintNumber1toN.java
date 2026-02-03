package recursion;

import java.util.Scanner;

public class PrintNumber1toN {

    public static void printNumber(int N){
        if(N==0) return;
        printNumber(N-1);
        System.out.print(N+" ");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        printNumber(N);
    }
}
