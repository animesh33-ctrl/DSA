package heap;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class TopKMaxElements {

    public static List<Integer> topKMaxDistinct(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k <= 0)
            return new ArrayList<>();

        HashSet<Integer> seen = new HashSet<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int num : arr) {
            if (seen.add(num)) {   // O(1)
                pq.offer(num);
                if (pq.size() > k) {
                    pq.poll();
                }
            }
        }

        return new ArrayList<>(pq);
    }


    public static void main(String[] args) {
        int[] arr = {1,2,3,4,2,3,4,5,5};
        int k = 2;
        System.out.println(topKMaxDistinct(arr,k));
    }
}
