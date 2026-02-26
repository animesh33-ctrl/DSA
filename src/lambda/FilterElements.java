package lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class FilterElements {

//    public static List<Integer> filterOdd(int[] arr){
//        if(arr.length==0){
//            return null;
//        }
//
//        List<Integer> ans = new ArrayList<>();
//        for(int i:arr){
//            if(i%2==0){
//                ans.add(i);
//            }
//        }
//        return ans;
//    }
    public static List<Integer> filterOdd(int[] arr) {
//        IntPredicate in = new IntPredicate() {
//            @Override
//            public boolean test(int value) {
//                return value%2==0;
//            }
//        };

        IntPredicate intPredicate = (value) ->  value % 2 == 0;
        return Arrays.stream(arr).filter(intPredicate).boxed().toList();
    }

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        System.out.println(filterOdd(arr));
    }
}
