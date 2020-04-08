package nimble.trust.engine.service.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Ayeshmantha Perera on 2020-03-30.
 */
@Component
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "nimble")
public class NimbleConfigurationProperties {

	private LanguageID defaultLanguageID = LanguageID.ENGLISH;

	public LanguageID getDefaultLanguageID() {
		return defaultLanguageID;
	}

	public void setDefaultLanguageID(String defaultLanguageID) {
		this.defaultLanguageID = LanguageID.valueOf(defaultLanguageID);
	}

	public enum LanguageID {
		@JsonProperty("en")
		ENGLISH("en"),
		@JsonProperty("es")
		SPANISH("es"),
		@JsonProperty("de")
		GERMAN("de"),
		@JsonProperty("tr")
		TURKISH("tr"),
		@JsonProperty("it")
		ITALIAN("it");

		private final String id;

		LanguageID(final String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return id;
		}

		public static LanguageID fromString(String identifier) {
			return Arrays.stream(LanguageID.values()).filter(l -> l.id.equals(identifier)).findFirst().orElse(null);
		}
	}
}
