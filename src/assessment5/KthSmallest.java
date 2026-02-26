package assessment5;

import java.util.PriorityQueue;

public class KthSmallest {
    public static void main(String[] args) {
        int[] arr = {7, 10, 4, 3, 20, 15};
        int k = 3;
        System.out.println(kthSmallest(arr, k)); // 7
    }

    private static int kthSmallest(int[] arr, int k) {
        if(arr==null || arr.length==0) return -1;

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int j : arr) {
            pq.add(j);
        }
        while(k-->1){
            pq.poll();
        }
        return pq.poll();
    }
}
