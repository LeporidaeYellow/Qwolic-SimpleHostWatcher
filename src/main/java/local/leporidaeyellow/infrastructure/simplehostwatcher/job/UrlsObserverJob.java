package local.leporidaeyellow.infrastructure.simplehostwatcher.job;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.event.ProceedEventService;
import local.leporidaeyellow.infrastructure.simplehostwatcher.services.mongodb.MongoDbLocatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service is responsible for polling lists (type "status200") of URLs.
 */

@Service
public class UrlsObserverJob {
    @Autowired
    ProceedEventService proceedEventService;

    @Autowired
    MongoDbLocatorService mongoDbLocatorService;

    @Scheduled(cron = "*/30 * * * * *")
    public void checkUrls() throws IOException {
        // getting list URLs with type "Status200"
        // and then proceed for each url ProceedEventService::checkStatusCode
        mongoDbLocatorService.getStatusOkUniformResourceLocatorList().parallelStream()
                .forEach(url -> {
                    try {
                        // checking status code (waiting status 200) of resource
                        // - passing URL to event service
                        proceedEventService.checkStatusCode(url);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}


