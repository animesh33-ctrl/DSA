package mock;

import java.util.HashMap;

interface IRateLimiter {
    boolean allowRequest(String userId);
    int getRemainingRequests(String userId);
    void resetUser(String userId);
}

public class RateLimiter implements IRateLimiter{
    private final int maxReq;

    public RateLimiter(int maxReq) {
        this.maxReq = maxReq;
    }

    HashMap<String,Integer> map = new HashMap<>();

    @Override
    public boolean allowRequest(String userId) {
        if(map.containsKey(userId)){
            int val = map.get(userId);
            if (val < maxReq){
                map.put(userId,map.get(userId)+1);
                return true;
            }
            return false;
        }
        map.put(userId,1);
        return true;

    }

    @Override
    public int getRemainingRequests(String userId) {
        return maxReq-map.getOrDefault(userId, 0);
    }

    @Override
    public void resetUser(String userId) {
        map.put(userId,0);
    }
}

class Main2 {
    public static void main(String[] args) throws InterruptedException {
        // Allow 5 requests per 5 seconds
        IRateLimiter limiter = new RateLimiter(5);

        System.out.println("=== Testing User: Alice ===");

        // Should allow 5 requests
        for (int i = 1; i <= 5; i++) {
            boolean allowed = limiter.allowRequest("Alice");
            int remaining = limiter.getRemainingRequests("Alice");
            System.out.println("Request " + i + ": " + allowed +
                    " | Remaining: " + remaining);
        }

        // 6th request should fail
        boolean blocked = limiter.allowRequest("Alice");
        System.out.println("Request 6: " + blocked +
                " | Remaining: " + limiter.getRemainingRequests("Alice"));

        System.out.println("\n⏳ Waiting 5 seconds for window reset...\n");
        Thread.sleep(5000);

        // Should work again after window expires
        boolean afterReset = limiter.allowRequest("Alice");
        System.out.println("After 5s: " + afterReset +
                " | Remaining: " + limiter.getRemainingRequests("Alice"));

        // Test manual reset
        limiter.resetUser("Alice");
        System.out.println("After manual reset: Remaining = " +
                limiter.getRemainingRequests("Alice"));
    }
}
/*

**Expected Output:**
        ```
        === Testing User: Alice ===
Request 1: true | Remaining: 4
Request 2: true | Remaining: 3
Request 3: true | Remaining: 2
Request 4: true | Remaining: 1
Request 5: true | Remaining: 0
Request 6: false | Remaining: 0

⏳ Waiting 5 seconds for window reset...

After 5s: true | Remaining: 4
After manual reset: Remaining = 5

*/