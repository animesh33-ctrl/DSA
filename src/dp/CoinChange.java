package dp;

import java.util.Arrays;
import java.util.List;

public class CoinChange {

    public static int coinNums(int[] nums, int amount){
        if(amount == 0){
            return 0;
        }

        if(amount < 0) return -1;

        int mincoins = Integer.MAX_VALUE;
        for(int coin : nums){
            int res = coinNums(nums,amount-coin);

            if(res != -1){
                mincoins = Math.min(res+1,mincoins);
            }
        }
        return (mincoins==Integer.MAX_VALUE) ? -1:mincoins;
    }

    //Memoization
    public static int coinNums(int[] nums, int amount, int[] memo){
        if (amount == 0) return 0;
        if (amount < 0) return -1;

        // Check if already computed
        if (memo[amount] != -2) return memo[amount];

        int minCoins = Integer.MAX_VALUE;

        // Try each coin
        for (int coin : nums) {
            int result = coinNums(nums, amount - coin, memo);
            if (result >= 0) { // Valid solution found
                minCoins = Math.min(minCoins, result + 1);
            }
        }

        // Store result
        memo[amount] = (minCoins == Integer.MAX_VALUE) ? -1 : minCoins;
        return memo[amount];
    }

    // Tabulation
    public static int coinNumsTab(int[] nums, int amount){
        int dp[] = new int[amount+1];
        Arrays.fill(dp,-1);
        dp[0] = 0;
        for(int i=1;i<=amount;i++){
            for(int coin:nums){
                if(coin <= i){
                    dp[i] = Math.min(dp[i],dp[i-coin]+1);
                }
            }
        }
        return dp[amount];
    }



    public static void main(String[] args) {
        int[] nums = {1, 3, 4};
        int amount = 6;

        // Pure recursion
        System.out.println("Recursion: " + coinNums(nums, amount));
//
//        // Memoization - MUST initialize with -2
//        int[] memo = new int[amount + 1];
//        Arrays.fill(memo, -2);  // ✓ CRITICAL: Fill with -2
//        System.out.println("Memoization: " + coinNums(nums, amount, memo));
        System.out.println("Tabulation: " + coinNumsTab(nums, amount));
    }
}
