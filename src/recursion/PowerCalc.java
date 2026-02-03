package recursion;

public class PowerCalc {

    public static int multiply(int a,int n){
        if(n==1) return a;
        return a*multiply(a,n-1);
    }

    public static void main(String[] args) {
        int a=3;
        int n=3;
        System.out.println(multiply(a,n));
    }
}
