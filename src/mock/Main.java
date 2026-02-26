package mock;

import java.util.*;

enum ActionEnum {
    operation1,
    operation2,
    operation3,
    click,
    view
}

interface IAnalytics{
    void registerAction(ActionEnum action);
    int getTotalNumberOfLoggedActions();
    int gettotalNotRegisterdActionInIAnalyticsStore();
    List<ActionEnum> getMostFrequentlyUsedActions();
}

interface IAnalyticsStore{
    void storeActions(Queue<ActionEnum> actions);
    Queue<ActionEnum> getTotalRegisteredActions();
}

class AnalyticStore implements IAnalyticsStore{
    private Queue<ActionEnum> queue = new LinkedList<>();

    public void storeActions(Queue<ActionEnum> actions){
        while(!actions.isEmpty()){
            queue.add(actions.poll());
        }
    }

    public Queue<ActionEnum> getTotalRegisteredActions(){
        return new LinkedList<>(queue);
    }
}

class Analytics implements IAnalytics{

    private IAnalyticsStore analyticsStore;
    private int K;
    private Queue<ActionEnum> buffer = new LinkedList<>();

    public Analytics(IAnalyticsStore analyticsStore, int k) {
        this.analyticsStore = analyticsStore;
        this.K = k;
    }

    @Override
    public void registerAction(ActionEnum action) {
        buffer.add(action);

        if(buffer.size() == K){
            analyticsStore.storeActions(buffer);
        }
    }

    @Override
    public int getTotalNumberOfLoggedActions() {
        return analyticsStore.getTotalRegisteredActions().size();
    }

    @Override
    public int gettotalNotRegisterdActionInIAnalyticsStore() {
        return buffer.size();
    }

    @Override
    public List<ActionEnum> getMostFrequentlyUsedActions() {

        Queue<ActionEnum> allActions = analyticsStore.getTotalRegisteredActions();
        Map<ActionEnum, Integer> freqMap = new HashMap<>();

        // Count frequencies
        for (ActionEnum action : allActions) {
            freqMap.put(action, freqMap.getOrDefault(action, 0) + 1);
        }

        // Put all actions into list
        List<ActionEnum> result = new ArrayList<>(freqMap.keySet());

        // Sort by frequency DESC, then name ASC
        result.sort((a, b) -> {
            int freqCompare = Integer.compare(freqMap.get(b), freqMap.get(a));
            if (freqCompare != 0)
                return freqCompare;
            return a.name().compareTo(b.name());
        });

        return result;
    }

}

public class Main{
    public static void main(String[] args) {

        IAnalyticsStore store = new AnalyticStore();
        IAnalytics analytics = new Analytics(store, 3);

        Random random = new Random();

        // simulate random calls using switch
        for(int i = 1; i <= 10; i++){
            int choice = random.nextInt(5);

            switch(choice){
                case 0:
                    analytics.registerAction(ActionEnum.operation1);
                    break;
                case 1:
                    analytics.registerAction(ActionEnum.operation2);
                    break;
                case 2:
                    analytics.registerAction(ActionEnum.operation3);
                    break;
                case 3:
                    analytics.registerAction(ActionEnum.click);
                    break;
                case 4:
                    analytics.registerAction(ActionEnum.view);
                    break;
            }
        }

        // Results
        System.out.println("Total logged actions: " +
                analytics.getTotalNumberOfLoggedActions());

        System.out.println("Not yet sent to store: " +
                analytics.gettotalNotRegisterdActionInIAnalyticsStore());

        System.out.println("Most frequently used actions: " +
                analytics.getMostFrequentlyUsedActions());
    }
}
