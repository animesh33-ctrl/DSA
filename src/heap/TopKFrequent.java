package heap;

import java.util.*;

public class TopKFrequent {

//    public static List<Integer> topKFreq(int[] nums, int k) {
//        if (nums == null || nums.length == 0) return new ArrayList<>();
//
//        // Step 1: Build frequency map
//        Map<Integer, Integer> freqMap = new HashMap<>();
//        for (int n : nums) {
//            freqMap.put(n, freqMap.getOrDefault(n, 0) + 1);
//        }
//        System.out.println("=== STEP 1: Frequency Map ===");
//        System.out.println("Frequency Map: " + freqMap);
//        System.out.println();
//
//        // Step 2: Create min-heap
//        PriorityQueue<Integer> pq =
//                new PriorityQueue<>((a, b) -> freqMap.get(a) - freqMap.get(b));
//
//        System.out.println("=== STEP 2: Building Min-Heap (size=" + k + ") ===");
//
//        // Step 3: Maintain heap of size k
//        for (int num : freqMap.keySet()) {
//            pq.offer(num);
//            System.out.println("Added " + num + " (freq=" + freqMap.get(num) + ") → Heap: " + pq);
//
//            if (pq.size() > k) {
//                int removed = pq.poll();
//                System.out.println("  ❌ Heap size > " + k + ", removed " + removed +
//                        " (freq=" + freqMap.get(removed) + ") → Heap: " + pq);
//            }
//        }
//        System.out.println("\nFinal Min-Heap: " + pq);
//        System.out.println();
//
//        // Step 4: Extract from heap
//        System.out.println("=== STEP 3: Extracting from Heap ===");
//        List<Integer> result = new ArrayList<>();
//
//        while (!pq.isEmpty()) {
//            int num = pq.poll();
//            result.add(num);
//            System.out.println("Extracted: " + num + " (freq=" + freqMap.get(num) +
//                    ") → Result: " + result);
//        }
//        System.out.println();
//
//        // Step 5: Sort in descending frequency order
//        System.out.println("=== STEP 4: Sorting by Frequency (Descending) ===");
//        System.out.println("Before sort: " + result);
//        result.sort((a, b) -> freqMap.get(b) - freqMap.get(a));
//        System.out.println("After sort:  " + result);
//        System.out.println();
//
//        return result;
//    }

    public static List<Integer> topKFreq(int[] nums,int k){
        if (nums == null || nums.length == 0) return null;

        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int n : nums) {
            freqMap.put(n, freqMap.getOrDefault(n, 0) + 1);
        }

        PriorityQueue<Integer> pq =
                new PriorityQueue<>((a, b) -> freqMap.get(a) - freqMap.get(b));

        for (int num : freqMap.keySet()) {
            pq.offer(num); //element is added based on the less freq...so when
            if (pq.size() > k) {
                pq.poll();
            }
        }

        System.out.println(pq);
        List<Integer> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        result.sort((a, b) -> freqMap.get(b) - freqMap.get(a));
        return result;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 1, 1, 2, 2, 4, 1, 4, 6};
        int k = 2;

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  Finding Top " + k + " Frequent Elements    ║");
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("Input Array: " + Arrays.toString(arr));
        System.out.println();

        List<Integer> result = topKFreq(arr, k);

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           FINAL RESULT                 ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Top " + k + " frequent elements: " + result);
    }
}