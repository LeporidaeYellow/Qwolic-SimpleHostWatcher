package local.leporidaeyellow.infrastructure.simplehostwatcher.services.event;

import local.leporidaeyellow.infrastructure.simplehostwatcher.model.Event;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

/**
 * Event Storage Service - Event registry
 */

@Service
public class EventsService {
    private final Map<String, Event> map = new ConcurrentHashMap<>();

    public Map<String, Event> getMap() {
        return map;
    }

    public Event handleEvent(String name) {
        return map.computeIfAbsent(name, Event::new);
    }

    public Event dropEvent(String name) {
        return map.remove(name);
    }

    public List<Event> getListEvent() {
        return new ArrayList<>(map.values());
    }

    public List<String> getListNameEvent() {
        return getListEvent()
                .stream()
                .map(event -> event.getName())
                .toList();
    }

    public boolean existEvent(String name) {
        return map.containsKey(name);
    }

    public String listEvents() {
        return map.toString();
    }
}

