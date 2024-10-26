package local.leporidaeyellow.infrastructure.simplehostwatcher.services.telegram;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.config.GetVarsFromEnvService;
import local.leporidaeyellow.infrastructure.simplehostwatcher.services.logging.LogToConsoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TelegramBotService {

    private static final String CLUSTER = GetVarsFromEnvService.getHostCluster();

    private static final String SEND_MESSAGE = "https://api.telegram.org/bot%s/sendMessage";

    GetVarsFromEnvService getVarsFromEnvService;
    LogToConsoleService logToConsole;
    HttpClient httpClient;

    public void sendDirectMessage(String message) {
        sendMessage(message);
    }

    public void sendActiveAlarm(String url, long duration, String cause) {
        String message = "\uD83D\uDEA8 [%s] \nCouldn't get an response: %s\nTime of unavailability: %d seconds.\nCause: %s"
                .formatted(CLUSTER, url, duration, cause);
        sendMessage(message);
    }

    public void sendActiveAlarm(String url, long duration, int code) {
        HttpStatus status = HttpStatus.valueOf(code);

        String message = "\uD83D\uDEA8 [%s] \nGet a response %d %s: %s\nTime of unavailability: %d seconds."
                .formatted(CLUSTER, code, status.getReasonPhrase(), url, duration);
        sendMessage(message);
    }

    public void sendClosedAlarm(String url, long duration) {
        String message = "✅  [%s] \nRestoring the service: %s\nTime of unavailability: %d seconds."
                .formatted(CLUSTER, url, duration);
        sendMessage(message);
    }

    public void sendUrlListEmptyAlarm(String eventType) {
        String message = "🙈 [%s] \nList for checking resources %s: empty or missing."
                .formatted(CLUSTER, eventType);
        sendMessage(message);
    }

    public void sendCleanAlarm(String url, long duration) {
        String message = "🛠 [%s] \nService was removed from observing: %s\nTime of unavailability: %d seconds."
                .formatted(CLUSTER, url, duration);
        sendMessage(message);
    }

    public void sendAboutStarting() {
        String message = "\uD83D\uDE80 [" + System.getenv().get("ENV_HOST_CLUSTER") +
                "] \nStarting Simple-Host-Watcher 🛰";
        sendMessage(message);
    }

    public void sendDomainCertificateExpiration(Long remainDays, String domain) {
        String message = "🔐⌚️ [%s] \nSSL certificate %s expires after: %s days."
                .formatted(CLUSTER, domain, remainDays);
        sendMessage(message);
    }

    public void sendDomainCertificateUnreachable(Long remainDays, String domain) {
        String message = "🔐⌚️ [%s] \nSSL certificate %s unavailable. \nStatus: %s."
                .formatted(CLUSTER, domain, remainDays);
        sendMessage(message);
    }

    private void sendMessage(String messageText) {
        String data = "chat_id=" + getVarsFromEnvService.getTelegramChatId() + "&text=" + messageText;
        URI sendMessage = URI.create(SEND_MESSAGE.formatted(getVarsFromEnvService.getTelegramToken()));
        HttpRequest request = HttpRequest.newBuilder(sendMessage)
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            // logging to console
            logToConsole.logTelegramMessageToConsole(
                    HttpStatus.valueOf(response.statusCode()),
                    response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}