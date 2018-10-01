package nimble.trust.application;

import nimble.trust.config.KafkaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

import nimble.trust.config.AuditorAwarer;

import javax.annotation.PostConstruct;

@Configuration
@EnableCircuitBreaker
@EnableAutoConfiguration
@EnableEurekaClient
@EnableFeignClients(basePackages="nimble.trust.engine.restclient")
@RestController
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
@EnableJpaRepositories(basePackages = "nimble.trust.engine.repository")
@EntityScan(basePackages = "nimble.trust.engine.domain")
@SpringBootApplication
@ComponentScan(basePackages = "nimble.trust")
public class TrustApplication implements CommandLineRunner {

    @Value("${nimble.kafka.topics.trustScoreUpdates}")
    private String trustScoreUpdatesTopic;

    @Autowired
    private KafkaTemplate<String, KafkaConfig.AuthorizedMessage> kafkaTemplate;
	
	// initialize (auditorAwareRef="auditorProvider")
	@Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwarer();
    }

    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    public static void main(String[] args) throws Exception {
        new SpringApplication(TrustApplication.class).run(args);
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }

    @PostConstruct
    public void init(){
        KafkaConfig.AuthorizedMessage message = new KafkaConfig.AuthorizedMessage("VALLLUUUEE", "accesstoekn");
        kafkaTemplate.send(trustScoreUpdatesTopic, message);
    }
}
