package local.leporidaeyellow.infrastructure.simplehostwatcher.controller.certs;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.http.HttpSecureCertificateCheckingService;
import local.leporidaeyellow.infrastructure.simplehostwatcher.services.mongodb.MongoDbLocatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/certs")
@CrossOrigin(origins="*")
public class SslCertificateTest {

    @Autowired
    HttpSecureCertificateCheckingService httpSecureCertificateCheckingService;

    @Autowired
    MongoDbLocatorService mongoDbLocatorService;

    @GetMapping("/check/{domain}")
    public Long check(@PathVariable String domain) throws Exception {
        return httpSecureCertificateCheckingService.getRemainDays(domain);
    }

    @GetMapping("/domain-list")
    public List<String> commonItems() {
        // getting a list of Common type domains (type: common)
        return mongoDbLocatorService.getCommonUniformResourceLocatorList();
    }
}