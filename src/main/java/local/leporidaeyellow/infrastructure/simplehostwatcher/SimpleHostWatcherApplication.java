package local.leporidaeyellow.infrastructure.simplehostwatcher;

import local.leporidaeyellow.infrastructure.simplehostwatcher.services.logging.LogToConsoleService;
import local.leporidaeyellow.infrastructure.simplehostwatcher.services.telegram.TelegramBotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SimpleHostWatcherApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SimpleHostWatcherApplication.class, args);

		context.getBean(LogToConsoleService.class).prestartLoggingToConsole();
		context.getBean(TelegramBotService.class).sendAboutStarting();
	}
}
