package local.leporidaeyellow.infrastructure.simplehostwatcher.controller.events;

import local.leporidaeyellow.infrastructure.simplehostwatcher.model.Event;
import local.leporidaeyellow.infrastructure.simplehostwatcher.services.event.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/events", produces="application/json")
@CrossOrigin(origins="*")
public class EventController {

    @Autowired
    EventsService eventsService;

    @GetMapping("/all")
    public List<Event> allEvents() {
        return eventsService.getListEvent();
    }
}
