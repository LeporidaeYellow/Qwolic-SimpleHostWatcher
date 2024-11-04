package local.leporidaeyellow.infrastructure.simplehostwatcher.controller.urlslist;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.mongodb.MongoDbLocatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * We can get lists that are used to check resources
 */

@RestController
@RequestMapping(path="/urls-list", produces="application/json")
@CrossOrigin(origins="*")
public class UniformResourceLocatorController {

    @Autowired
    MongoDbLocatorService mongoDbLocatorService;

    // Getting a list of urls with type "Status200"
    @GetMapping("/status200")
    public List<String> statusOkItems() {
        return mongoDbLocatorService.getStatusOkUniformResourceLocatorList();
    }

    // Getting a list of urls with type "common"
    @GetMapping("/common")
    public List<String> commonItems() {
        return mongoDbLocatorService.getCommonUniformResourceLocatorList();
    }

    // Updating all lists and get a list with type "Status200"
    @GetMapping("/pull")
    public List<String> updateItems() {
        mongoDbLocatorService.setUniformResourceLocatorItemList();
        return mongoDbLocatorService.getStatusOkUniformResourceLocatorList();
    }

    @GetMapping("/ok")
    public String ok() {
        return "OK";
    }
}