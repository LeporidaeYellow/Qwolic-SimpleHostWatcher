package local.leporidaeyellow.infrastructure.simplehostwatcher.controller.forward;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.telegram.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/*
 * Receive a request with text.
 * Send the text to telegram
 */

@CrossOrigin
@RestController
@RequestMapping("/forward")
public class ForwardAlertWebhookController {

    @Autowired
    TelegramBotService telegramBotService;

    @RequestMapping(value = "/text", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public void greetingJson(HttpEntity<String> httpEntity) {
        telegramBotService.sendDirectMessage(httpEntity.getBody());
    }
}
