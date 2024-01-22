package ac.dnd.bookkeeping.server.global.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "ac.dnd.bookkeeping.server")
public class ConfigurationProcessorConfig {
}
