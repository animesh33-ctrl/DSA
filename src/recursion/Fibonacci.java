package recursion;

public class Fibonacci {

    public static int fibonacci(int n){
        if(n==0 || n==1) return  n;
        return fibonacci(n-1)+fibonacci(n-2);
    }

    public static int fibonacci2(int n){
        int a=0;
        int b = 1;
        int sum=0;
        for(int i=2;i<=n;i++){
            sum = a+b;
            a=b;
            b=sum;
        }
        return sum;
    }


    public static void main(String[] args) {
        int n=5;
        System.out.println(fibonacci2(n));
    }
}
