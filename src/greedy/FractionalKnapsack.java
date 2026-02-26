package greedy;

import java.util.Arrays;
import java.util.Comparator;

class Item {
    int value;
    int weight;
    double ratio;

    Item(int value, int weight) {
        this.value = value;
        this.weight = weight;
        this.ratio = (double) value / weight;
    }
}

public class FractionalKnapsack {

    public static double getMax(int[] value, int[] weight, int capacity) {

        int n = value.length;
        Item[] items = new Item[n];

        for (int i = 0; i < n; i++) {
            items[i] = new Item(value[i], weight[i]);
        }

        // Sort by ratio descending
        Arrays.sort(items, (a, b) -> Double.compare(b.ratio, a.ratio));

        double totalProfit = 0.0;

        for (Item item : items) {
            if (capacity == 0)
                break;

            if (capacity >= item.weight) {
                // Take full item
                capacity -= item.weight;
                totalProfit += item.value;
            } else {
                // Take fraction
                totalProfit += item.value * ((double) capacity / item.weight);
                capacity = 0;
            }
        }

        return totalProfit;
    }

    public static void main(String[] args) {

        int value[] = {25, 24, 15};
        int weight[] = {18,15,10};
        int capacity = 30;

        System.out.println(getMax(value, weight, capacity));
    }
}
