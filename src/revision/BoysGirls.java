package revision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BoysGirls {

    static boolean check(int[] b, int[] g, boolean startWithBoy) {
        int n = b.length;
        int i = 0, j = 0;
        int prev = -1;

        for (int k = 0; k < 2 * n; k++) {
            int curr;

            if ((k % 2 == 0 && startWithBoy) || (k % 2 == 1 && !startWithBoy)) {
                if (i >= n) return false;
                curr = b[i++];
            } else {
                if (j >= n) return false;
                curr = g[j++];
            }

            if (prev > curr) return false;
            prev = curr;
        }
        return true;
    }

    public static String match(int[] b, int[] g) {
        Arrays.sort(b);
        Arrays.sort(g);
        System.out.println("Boys sorted array : "+Arrays.toString(b));
        System.out.println("Girls sorted array : "+Arrays.toString(g));
        if (check(b, g, true) || check(b, g, false)) {
            return "yes";
        }
        return "no";
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] b = new int[n];
        int[] g = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = sc.nextInt();
        }
        for (int i = 0; i < n; i++) {
            g[i] = sc.nextInt();
        }
        System.out.println(match(b, g));
    }
}
