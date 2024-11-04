package local.leporidaeyellow.infrastructure.simplehostwatcher.services.logging;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.config.GetVarsFromEnvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogToConsoleService {

    @Autowired
    GetVarsFromEnvService getVarsFromEnvService;

    public void logTelegramMessageToConsole(HttpStatus status, String contentResponseToString) {
        log.info("Sending a message to TELEGRAM => ");
        log.info("Message status: " + status.getReasonPhrase());
        log.info("Content of the message from TELEGRAM response: " + contentResponseToString);
        log.info("=================================================================");
    }

    public void prestartLoggingToConsole() {
        log.info("Telegram group id : \n" + getVarsFromEnvService.getTelegramChatId());
        log.info("SimpleHostWatcher was started ........... ");
        log.info("====  ====  ====  ====  ====  ====  ====  ====  ====");
    }
}