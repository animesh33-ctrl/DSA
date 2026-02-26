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

    public static int fibonacci3(int n){
        int[] dp = new int[n+1];
        dp[0] = 0;
        dp[1] = 1;
        int prev2=0;
        int prev1 = 1;
        int curr=0;
        for(int i=2;i<=n;i++){
            curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }


    public static void main(String[] args) {
        int n=60;
        System.out.println(fibonacci3(n));
    }
}
