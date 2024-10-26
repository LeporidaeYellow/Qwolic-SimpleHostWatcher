package local.leporidaeyellow.infrastructure.simplehostwatcher.controller.health;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/health")
@CrossOrigin(origins="*")
public class HealthController {

    @GetMapping("/isAlive")
    public String isAlive() {
        return "OK";
    }
}
