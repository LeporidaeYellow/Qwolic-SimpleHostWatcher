package local.leporidaeyellow.infrastructure.simplehostwatcher.job;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.event.ProceedEventService;
import local.leporidaeyellow.infrastructure.simplehostwatcher.services.mongodb.MongoDbLocatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * This service is responsible for verifying SSL certificates on HTTP servers.
 *
 * Using MongoDbLocatorService::updateUniformResourceLocatorList
 * the list of domains from the MongoDB database is updated.
 */

@Service
public class CertsX509Observer {
    @Autowired
    ProceedEventService proceedEventService;

    @Autowired
    MongoDbLocatorService mongoDbLocatorService;

    @Scheduled(cron = "0 0 4 * * *")
    public void checkCertsX509() throws IOException {

        // Getting list with domains by common type
        // and accept for each domain ProceedEventService::checkDomainCertificateExpiration
        mongoDbLocatorService.getCommonUniformResourceLocatorList().parallelStream()
                .forEach(domain -> {
                    try {
                        // Checking the status code of the resource.
                        // If negative result then sending alert with URL to the event service
                        proceedEventService.checkDomainCertificateExpiration(domain);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}