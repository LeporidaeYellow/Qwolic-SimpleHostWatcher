package local.leporidaeyellow.infrastructure.simplehostwatcher.services.event;

import local.leporidaeyellow.infrastructure.simplehostwatcher.model.Event;
import local.leporidaeyellow.infrastructure.simplehostwatcher.services.http.HttpClientService;
import local.leporidaeyellow.infrastructure.simplehostwatcher.services.http.HttpSecureCertificateCheckingService;
import local.leporidaeyellow.infrastructure.simplehostwatcher.services.telegram.TelegramBotService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Event service (processing) - operations with the event registry
 */

@Service
public class ProceedEventService {

    private static final int FEW_DAYS_LEFT = 10;
    private static final int DAYS_MORE_THAN = 0;
    private static final int COUNT_DAYS_UNKNOWN = -1;
    private static final int HTTP_STATUS_CODE_OK = 200;
    private static final int HTTP_STATUS_CODE_UNREACHABLE = 523;
    private static final String HTTP_STATUS_REASON_PHRASE_UNREACHABLE = "Origin Is Unreachable";

    @Autowired
    EventsService eventsService;

    @Autowired
    TelegramBotService telegramBotService;

    @Autowired
    HttpClientService httpClientService;

    @Autowired
    HttpSecureCertificateCheckingService httpSecureCertificateCheckingService;

    HttpResponse<Void> response;

    public void processDropEvent(String url) {
        // delete event from the registry
        Event event = eventsService.dropEvent(url);

        // check for existing event
        if (event != null) {
            // send OK message that alarm event has been removed from event registry
            telegramBotService.sendClosedAlarm(url, event.getDuration());
        }
    }

    public void processSendEvent(String url, Integer code, String cause) {
        // send event to event registry
        Event event = eventsService.handleEvent(url);

        // send event message
        if (event.sendAlarm()) {
            val duration = event.getDuration();
            telegramBotService.sendActiveAlarm(url, duration, code + " " + cause);
        }

    }

    public void processCleanEvent(String url) {
        // delete event from event registry
        Event event = eventsService.dropEvent(url);

        // check for existing event
        if (event != null) {
            // send OK message that alarm event has been removed from event registry
            telegramBotService.sendCleanAlarm(url, event.getDuration());
        }
    }

    public void checkStatusCode(String url) throws IOException, InterruptedException {
        // receiving response from http client  -  need http response code
        response = httpClientService.getHttpResponse(url);
        // if http code is not null and not 200
        if (response != null && response.statusCode() != HTTP_STATUS_CODE_OK) {
            // process sending event to event registry (code and string representation code)
            processSendEvent(url, response.statusCode(), HttpStatus.valueOf(response.statusCode()).getReasonPhrase());
        // no response received
        } else if (response == null) {
            // sending event to event registry (code will be 523, which will mean "Origin Is Unreachable")
            // 523 Origin Is Unreachable, occurs when the web server is unreachable; non-standard CloudFlare code.
            processSendEvent(url, HTTP_STATUS_CODE_UNREACHABLE, HTTP_STATUS_REASON_PHRASE_UNREACHABLE);
        // response was received - 200 OK
        } else {
            // delete event from event registry
            processDropEvent(url);
        }
        response = null;
    }

    public void checkDomainCertificateExpiration(String domain) throws Exception {
        val remainDays = httpSecureCertificateCheckingService.getRemainDays(domain);
        //
        if (remainDays < FEW_DAYS_LEFT && remainDays >= DAYS_MORE_THAN) {
            telegramBotService.sendDomainCertificateExpiration(remainDays, domain);
        }
        //
        if (remainDays == COUNT_DAYS_UNKNOWN) {
            telegramBotService.sendDomainCertificateUnreachable(remainDays, domain);
        }
    }

    public void compareUrlEventsWithUrlListAndClean(List<String> newUrlsList) {
        // if list is not empty or not null
        if (!newUrlsList.isEmpty() && newUrlsList != null) {
            // if there are no registered event URLs in the updated list,
            // then these URLs are deleted from event registry
            eventsService.getListNameEvent().stream()
                    .forEach(event -> {
                        if (!newUrlsList.contains(event)) processCleanEvent(event);
                    });
        }
    }

    public void processAlarmIfUrlListIsEmpty(String eventType) {
        // sending event message of "eventType" type
        // that the verification list of resources of this type is empty
        telegramBotService.sendUrlListEmptyAlarm(eventType);
    }
}
