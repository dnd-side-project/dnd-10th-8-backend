package ac.dnd.mur.server.global.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "ac.dnd.mur.server")
public class ConfigurationProcessorConfig {
}
