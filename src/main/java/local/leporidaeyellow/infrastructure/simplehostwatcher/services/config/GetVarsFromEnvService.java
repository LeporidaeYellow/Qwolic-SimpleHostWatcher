package local.leporidaeyellow.infrastructure.simplehostwatcher.services.config;

import org.springframework.stereotype.Service;

@Service
public class GetVarsFromEnvService {
    public static String getTelegramToken() {
        // giving telegram token from environmental variables
        return System.getenv().get("ENV_TG_TOKEN");
    }

    public static String getTelegramChatId() {
        // giving telegram id chat from environmental variables
        return System.getenv().get("ENV_CHAT_ID");
    }

    public static String getHostCluster() {
        // giving deployment-environment-name (or cluster-name, or host-name) from environmental variables
        return System.getenv().get("ENV_HOST_CLUSTER");
    }
}
