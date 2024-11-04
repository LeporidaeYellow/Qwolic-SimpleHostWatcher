package local.leporidaeyellow.infrastructure.simplehostwatcher.services.mongodb;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.event.ProceedEventService;
import local.leporidaeyellow.infrastructure.simplehostwatcher.model.mongodb.UniformResourceLocatorItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import local.leporidaeyellow.infrastructure.simplehostwatcher.repository.mongodb.MongoDbLocatorRepository;

import java.util.ArrayList;
import java.util.List;

import static local.leporidaeyellow.infrastructure.simplehostwatcher.configuration.Constants.*;

/**
 * service for getting lists from  MongoDB -  URL aggregator
 */

@Service
public class MongoDbLocatorService {

    @Autowired
    ProceedEventService proceedEventService;

    @Autowired
    MongoDbLocatorRepository mongoDbLocatorRepository;

    private List<String> newUrlsList;

    private List<UniformResourceLocatorItem> uniformResourceLocatorItems = new ArrayList<>();

    public List<UniformResourceLocatorItem> getUniformResourceLocatorItems() {
        return uniformResourceLocatorItems;
    }

    public void setUniformResourceLocatorItemList() {
        // getting from data repo (MongoDB) entities uniformResourceLocatorItems and save to "this"
        this.uniformResourceLocatorItems = mongoDbLocatorRepository.findAll();
    }

    public List<String> getStatusOkUniformResourceLocatorList() {
        // create list of URLs with type "Status200"
        // from uniformResourceLocatorItems and return list
        return uniformResourceLocatorItems
                .stream()
                .filter(item -> item.getType().matches(TYPE_STATUS_200))
                .filter(item -> item.getApproved().equals(Boolean.TRUE))
                .map(item -> item.getUrl())
                .toList();
    }

    public List<String> getCommonUniformResourceLocatorList() {
        // create list of domains (URLs) with type "common"
        // from uniformResourceLocatorItems and return list
        return uniformResourceLocatorItems
                .stream()
                .filter(item -> item.getType().matches(TYPE_COMMON))
                .filter(item -> item.getApproved().equals(Boolean.TRUE))
                .map(item -> item.getUrl())
                .toList();
    }

    public List<String> getAllUniformResourceLocatorList() {
        // create list of URLs with ALL types
        // from uniformResourceLocatorItems and return list
        return uniformResourceLocatorItems
                .stream()
                .map(item -> item.getUrl())
                .toList();
    }

    public void updateUniformResourceLocatorList() {
        // getting from data repo (MongoDB) entities uniformResourceLocatorItems and save to "this"
        setUniformResourceLocatorItemList();
        // create temporary list of URLs with type "Status200"
        newUrlsList = getStatusOkUniformResourceLocatorList();
        // if there are no registered event urls in updated list,
        // then these urls are deleted from event registry
        proceedEventService.compareUrlEventsWithUrlListAndClean(newUrlsList);
        newUrlsList = null;
    }

    public void checkStatusOkUniformResourceLocatorList() {
        // if list of URLs with type "Status200" is empty,
        // then sending alarm about empty list with type "Status200"
        if (getStatusOkUniformResourceLocatorList().isEmpty()) proceedEventService.processAlarmIfUrlListIsEmpty("Status200");
    }
}
