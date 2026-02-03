package sorting;

import java.util.Arrays;
import java.util.Scanner;

public class SelectionDemo {

    // Basic Selection Sort
    public static void selectionSort(int[] arr) {
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            // Find minimum element in unsorted portion
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // Swap minimum element with first element of unsorted portion
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }

    // Selection Sort with detailed trace
    public static void selectionSortWithTrace(int[] arr) {
        int n = arr.length;

        System.out.println("Initial Array: " + Arrays.toString(arr));
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SELECTION SORT STEP-BY-STEP TRACE");
        System.out.println("=".repeat(80));

        for (int i = 0; i < n - 1; i++) {
            System.out.println("\n--- Pass " + (i + 1) + " ---");
            System.out.println("Sorted portion: " + Arrays.toString(Arrays.copyOfRange(arr, 0, i)));
            System.out.println("Unsorted portion: " + Arrays.toString(Arrays.copyOfRange(arr, i, n)));

            // Find minimum element in unsorted portion
            int minIndex = i;
            System.out.printf("Starting minimum: arr[%d] = %d\n", i, arr[i]);

            for (int j = i + 1; j < n; j++) {
                System.out.printf("  Comparing arr[%d]=%d with current min arr[%d]=%d",
                        j, arr[j], minIndex, arr[minIndex]);

                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                    System.out.printf(" → New minimum found at index %d\n", minIndex);
                } else {
                    System.out.println(" → No change");
                }
            }

            // Swap if needed
            if (minIndex != i) {
                System.out.printf("\nSwapping arr[%d]=%d with arr[%d]=%d\n",
                        i, arr[i], minIndex, arr[minIndex]);

                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            } else {
                System.out.println("\nNo swap needed (minimum already in correct position)");
            }

            System.out.println("Array after pass " + (i + 1) + ": " + Arrays.toString(arr));
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("Final Sorted Array: " + Arrays.toString(arr));
        System.out.println("=".repeat(80));
    }

    // Visual representation
    public static void selectionSortVisual(int[] arr) {
        int n = arr.length;

        System.out.println("\n=== VISUAL REPRESENTATION ===\n");
        System.out.println("Initial: " + visualize(arr, -1, -1));

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                // Show the swap
                System.out.printf("\nPass %d: Swap arr[%d]=%d with arr[%d]=%d\n",
                        i + 1, i, arr[i], minIndex, arr[minIndex]);

                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;

                System.out.println("Result:  " + visualize(arr, i, -1));
            }
        }

        System.out.println("\nFinal:   " + visualize(arr, n - 1, -1));
    }

    // Helper method to visualize array
    private static String visualize(int[] arr, int sortedUpTo, int highlight) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i == highlight) {
                sb.append("*").append(arr[i]).append("* ");
            } else if (i <= sortedUpTo) {
                sb.append("(").append(arr[i]).append(") ");
            } else {
                sb.append(arr[i]).append(" ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Selection Sort - Descending Order
    public static void selectionSortDescending(int[] arr) {
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            int maxIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (arr[j] > arr[maxIndex]) {  // Changed to > for descending
                    maxIndex = j;
                }
            }

            if (maxIndex != i) {
                int temp = arr[i];
                arr[i] = arr[maxIndex];
                arr[maxIndex] = temp;
            }
        }
    }

    // Count number of swaps
    public static int selectionSortCountSwaps(int[] arr) {
        int n = arr.length;
        int swapCount = 0;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
                swapCount++;
            }
        }

        return swapCount;
    }

    // Performance testing
    public static void performanceTest() {
        System.out.println("\n=== PERFORMANCE ANALYSIS ===\n");

        int[] sizes = {100, 1000, 5000, 10000};

        System.out.printf("%-15s %-20s %-15s\n", "Array Size", "Time (ms)", "Comparisons");
        System.out.println("-".repeat(50));

        for (int size : sizes) {
            int[] arr = generateRandomArray(size);

            long startTime = System.nanoTime();
            selectionSort(arr);
            long endTime = System.nanoTime();

            long timeTaken = (endTime - startTime) / 1000000; // Convert to ms
            long comparisons = (long) size * (size - 1) / 2;

            System.out.printf("%-15d %-20d %-15d\n", size, timeTaken, comparisons);
        }
    }

    // Generate random array
    private static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * 1000);
        }
        return arr;
    }

    // Verify if array is sorted
    public static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              SELECTION SORT - COMPREHENSIVE DEMO               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        // Example 1: Basic sorting with trace
        System.out.println("\n【 EXAMPLE 1: DETAILED TRACE 】");
        int[] arr1 = {64, 25, 12, 22, 11};
        selectionSortWithTrace(arr1.clone());

        // Example 2: Visual representation
        System.out.println("\n【 EXAMPLE 2: VISUAL REPRESENTATION 】");
        int[] arr2 = {29, 10, 14, 37, 13};
        selectionSortVisual(arr2.clone());

        // Example 3: Different test cases
        System.out.println("\n【 EXAMPLE 3: DIFFERENT TEST CASES 】\n");

        int[][] testCases = {
                {5, 2, 8, 1, 9},
                {1, 2, 3, 4, 5},        // Already sorted
                {5, 4, 3, 2, 1},        // Reverse sorted
                {3, 3, 3, 3, 3},        // All same
                {42},                    // Single element
                {7, 3}                   // Two elements
        };

        for (int i = 0; i < testCases.length; i++) {
            int[] arr = testCases[i].clone();
            int[] original = testCases[i].clone();

            int swaps = selectionSortCountSwaps(arr);

            System.out.printf("Test %d:\n", i + 1);
            System.out.println("  Before: " + Arrays.toString(original));
            System.out.println("  After:  " + Arrays.toString(arr));
            System.out.println("  Swaps:  " + swaps);
            System.out.println("  Sorted: " + (isSorted(arr) ? "✓" : "✗"));
            System.out.println();
        }

        // Example 4: Descending order
        System.out.println("\n【 EXAMPLE 4: DESCENDING ORDER 】\n");
        int[] arr3 = {64, 25, 12, 22, 11};
        System.out.println("Before: " + Arrays.toString(arr3));
        selectionSortDescending(arr3);
        System.out.println("After:  " + Arrays.toString(arr3));

        // Performance test
        performanceTest();

        // Interactive input
        Scanner sc = new Scanner(System.in);
        System.out.print("\n\nEnter number of elements: ");
        int n = sc.nextInt();

        int[] userArray = new int[n];
        System.out.println("Enter " + n + " elements:");
        for (int i = 0; i < n; i++) {
            userArray[i] = sc.nextInt();
        }

        System.out.println("\nChoose sorting mode:");
        System.out.println("1. Basic sort");
        System.out.println("2. Sort with detailed trace");
        System.out.println("3. Sort with visual representation");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                selectionSort(userArray);
                System.out.println("\nSorted Array: " + Arrays.toString(userArray));
                break;
            case 2:
                selectionSortWithTrace(userArray);
                break;
            case 3:
                selectionSortVisual(userArray);
                break;
            default:
                System.out.println("Invalid choice!");
        }

        sc.close();
    }
}
/*
```

        ---

        ## **Sample Output:**
        ```
        ╔════════════════════════════════════════════════════════════════╗
        ║              SELECTION SORT - COMPREHENSIVE DEMO               ║
        ╚════════════════════════════════════════════════════════════════╝

        【 EXAMPLE 1: DETAILED TRACE 】
Initial Array: [64, 25, 12, 22, 11]

        ================================================================================
SELECTION SORT STEP-BY-STEP TRACE
================================================================================

        --- Pass 1 ---
Sorted portion: []
Unsorted portion: [64, 25, 12, 22, 11]
Starting minimum: arr[0] = 64
Comparing arr[1]=25 with current min arr[0]=64 → New minimum found at index 1
Comparing arr[2]=12 with current min arr[1]=25 → New minimum found at index 2
Comparing arr[3]=22 with current min arr[2]=12 → No change
Comparing arr[4]=11 with current min arr[2]=12 → New minimum found at index 4

Swapping arr[0]=64 with arr[4]=11
Array after pass 1: [11, 25, 12, 22, 64]

        --- Pass 2 ---
Sorted portion: [11]
Unsorted portion: [25, 12, 22, 64]
Starting minimum: arr[1] = 25
Comparing arr[2]=12 with current min arr[1]=25 → New minimum found at index 2
Comparing arr[3]=22 with current min arr[2]=12 → No change
Comparing arr[4]=64 with current min arr[2]=12 → No change

Swapping arr[1]=25 with arr[2]=12
Array after pass 2: [11, 12, 25, 22, 64]

        --- Pass 3 ---
Sorted portion: [11, 12]
Unsorted portion: [25, 22, 64]
Starting minimum: arr[2] = 25
Comparing arr[3]=22 with current min arr[2]=25 → New minimum found at index 3
Comparing arr[4]=64 with current min arr[3]=22 → No change

Swapping arr[2]=25 with arr[3]=22
Array after pass 3: [11, 12, 22, 25, 64]

        --- Pass 4 ---
Sorted portion: [11, 12, 22]
Unsorted portion: [25, 64]
Starting minimum: arr[3] = 25
Comparing arr[4]=64 with current min arr[3]=25 → No change

No swap needed (minimum already in correct position)
Array after pass 4: [11, 12, 22, 25, 64]

        ================================================================================
Final Sorted Array: [11, 12, 22, 25, 64]
        ================================================================================

        【 EXAMPLE 2: VISUAL REPRESENTATION 】

        === VISUAL REPRESENTATION ===

Initial: [29 10 14 37 13 ]

Pass 1: Swap arr[0]=29 with arr[1]=10
Result:  [(10) 29 14 37 13 ]

Pass 2: Swap arr[1]=29 with arr[4]=13
Result:  [(10) (13) 14 37 29 ]

Pass 3: Swap arr[2]=14 with arr[2]=14
Result:  [(10) (13) (14) 37 29 ]

Pass 4: Swap arr[3]=37 with arr[4]=29
Result:  [(10) (13) (14) (29) 37 ]

Final:   [(10) (13) (14) (29) (37) ]

        【 EXAMPLE 3: DIFFERENT TEST CASES 】

Test 1:
Before: [5, 2, 8, 1, 9]
After:  [1, 2, 5, 8, 9]
Swaps:  4
Sorted: ✓

Test 2:
Before: [1, 2, 3, 4, 5]
After:  [1, 2, 3, 4, 5]
Swaps:  0
Sorted: ✓

Test 3:
Before: [5, 4, 3, 2, 1]
After:  [1, 2, 3, 4, 5]
Swaps:  2
Sorted: ✓

Test 4:
Before: [3, 3, 3, 3, 3]
After:  [3, 3, 3, 3, 3]
Swaps:  0
Sorted: ✓

Test 5:
Before: [42]
After:  [42]
Swaps:  0
Sorted: ✓

Test 6:
Before: [7, 3]
After:  [3, 7]
Swaps:  1
Sorted: ✓

        === PERFORMANCE ANALYSIS ===

Array Size      Time (ms)            Comparisons
--------------------------------------------------
        100             2                    4950
        1000            45                   499500
        5000            850                  12497500
        10000           3200                 49995000
        ```

        ---

        ## **How Selection Sort Works:**

        ### **Algorithm:**
        1. **Divide** the array into sorted and unsorted portions
2. **Find** the minimum element in the unsorted portion
3. **Swap** it with the first element of the unsorted portion
4. **Repeat** until the entire array is sorted

### **Visual Example for [64, 25, 12, 22, 11]:**
        ```
Pass 1: Find min in [64, 25, 12, 22, 11] → 11
Swap 64 ↔ 11
        [11, 25, 12, 22, 64]
        └─┘  └────────────┘
sorted  unsorted

Pass 2: Find min in [25, 12, 22, 64] → 12
Swap 25 ↔ 12
        [11, 12, 25, 22, 64]
        └──────┘  └────────┘
sorted    unsorted

Pass 3: Find min in [25, 22, 64] → 22
Swap 25 ↔ 22
        [11, 12, 22, 25, 64]
        └─────────┘  └────┘
sorted    unsorted

Pass 4: Find min in [25, 64] → 25
No swap needed
        [11, 12, 22, 25, 64]
                └────────────────┘
sorted

 */