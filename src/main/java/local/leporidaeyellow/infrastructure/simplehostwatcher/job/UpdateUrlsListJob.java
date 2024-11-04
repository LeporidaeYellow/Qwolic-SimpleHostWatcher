package local.leporidaeyellow.infrastructure.simplehostwatcher.job;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.mongodb.MongoDbLocatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * This service returns updates to the lists of URLs and domains.
 *
 * Using the MongoDbLocatorService::updateUniformResourceLocatorList,
 * the list of URLs and domains from the MongoDB will be updated.
 *
 * Resource polling is performed via local.leporidaeyellow.infrastructure.simplehostwatcher.job.UrlsObserverJob
 * The domain survey is performed via local.leporidaeyellow.infrastructure.simplehostwatcher.job.CertsX509Observer
 */

@Service
@EnableScheduling
public class UpdateUrlsListJob {

    @Autowired
    MongoDbLocatorService mongoDbLocatorService;

    @Scheduled(fixedRate = 60)
    public void listUniformResourceLocatorUpdate() {
        // updating all lists from MongoDB
        mongoDbLocatorService.updateUniformResourceLocatorList();
        // checking that updated list (type is "status200") is not empty, otherwise send alert
        mongoDbLocatorService.checkStatusOkUniformResourceLocatorList();
    }
}
