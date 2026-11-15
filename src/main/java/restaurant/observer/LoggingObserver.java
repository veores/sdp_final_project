package restaurant.observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class LoggingObserver implements OrderObserver {
    private final List<String> messages = new ArrayList<>();

    protected void publish(String message) {
        messages.add(message);
        System.out.println(message);
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
