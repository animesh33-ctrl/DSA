package mock;
import java.util.*;

enum EventType {
    PAGE_VIEW, BUTTON_CLICK, FORM_SUBMIT, VIDEO_PLAY, SCROLL, DOWNLOAD
}

class ClickEvent {
    EventType type;
    String userId;
    String pageUrl;
    long timestamp;
    Map<String, String> metadata; // Additional data

    public ClickEvent(EventType type, String userId, String pageUrl) {
        this.type = type;
        this.userId = userId;
        this.pageUrl = pageUrl;
        this.timestamp = System.currentTimeMillis();
        this.metadata = new HashMap<>();
    }
}

interface IEventStore {
    void storeEvents(Queue<ClickEvent> events);
    Queue<ClickEvent> getAllEvents();
    List<ClickEvent> getEventsByUser(String userId);
}

interface IClickStreamAnalytics {
    void trackEvent(EventType type, String userId, String pageUrl);
    int getTotalEventsTracked();
    int getBufferedEvents();
    Map<EventType, Integer> getEventDistribution();
    List<String> getMostActiveUsers(int topN);
    Map<String, Integer> getPopularPages(int topN);
    double getAverageEventsPerUser();
}
public class _04_StreamAnalytics implements IClickStreamAnalytics{
    @Override
    public void trackEvent(EventType type, String userId, String pageUrl) {

    }

    @Override
    public int getTotalEventsTracked() {
        return 0;
    }

    @Override
    public int getBufferedEvents() {
        return 0;
    }

    @Override
    public Map<EventType, Integer> getEventDistribution() {
        return Map.of();
    }

    @Override
    public List<String> getMostActiveUsers(int topN) {
        return List.of();
    }

    @Override
    public Map<String, Integer> getPopularPages(int topN) {
        return Map.of();
    }

    @Override
    public double getAverageEventsPerUser() {
        return 0;
    }
}
