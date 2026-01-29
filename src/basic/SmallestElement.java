package basic;

public class SmallestElement {
    public static void main(String[] args) {
        int [] arr = {1,2,3,4,5,6,7,-1};

        int min = Integer.MAX_VALUE;
        for (int j : arr) {
            if (j < min) min = j;
        }
        System.out.println("MIN : "+min);
    }
}

