package local.leporidaeyellow.infrastructure.simplehostwatcher.services.http;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * service that issues response after request at given URL
 */

@Service
public class HttpClientService {

    // number of repetitions in the absence of server response
    private static final int MAX_RESEND = 2;
    private static final int MAX_TIMEOUT = 2;

    HttpResponse<Void> response;

    private HttpResponse<Void> getHttpResponseFromClient(String stringUrl) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(stringUrl))
                .timeout(Duration.ofSeconds(2))
                .GET()
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (IOException e) {
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<Void> getHttpResponse(String stringUrl) throws InterruptedException {
        int count = 0;
        response = getHttpResponseFromClient(stringUrl);
        while (response == null && count <= MAX_RESEND) {
            TimeUnit.SECONDS.sleep(MAX_TIMEOUT);
            response = getHttpResponseFromClient(stringUrl);
            count ++;
        }
        return response;
    }
}
