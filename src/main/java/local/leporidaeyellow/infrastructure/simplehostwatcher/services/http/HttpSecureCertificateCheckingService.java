package local.leporidaeyellow.infrastructure.simplehostwatcher.services.http;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.security.cert.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class HttpSecureCertificateCheckingService {
    public long getRemainDays(String domain) throws Exception {

        String name;
        URL url = new URL("https://" + domain);

        // defining domain mask and write it to variable
        if ((domain.length() - domain.replace(".", "").length()) > 1) {
            name = domain.replaceAll("^[a-z]+.", "");
        } else {
            name = domain;
        }

        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });

            conn.getResponseCode();
            Certificate[] certs = conn.getServerCertificates();
            Certificate currentCertificate = Arrays.stream(certs)
                    .filter(cert -> cert.toString().contains(name))
                    .findAny().orElseThrow();
            conn.disconnect();

            return remainDays(getVal(currentCertificate));
            
        } catch (IOException e) {
            // if could not determine, then return -1
            return (long) -1;
        }
    }

    private static long remainDays(Date targetDay) {
        return TimeUnit.DAYS.convert(targetDay.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
    }

    private static Date getVal(Certificate cert) {
        try {
            X509Certificate x509Certificate = (X509Certificate) CertificateFactory
                    .getInstance("X509")
                    .generateCertificate(
                            new ByteArrayInputStream(cert.getEncoded())
                    );
            return x509Certificate.getNotAfter();
        } catch (CertificateEncodingException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }
}
