package nimble.trust.config;

import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

@Configuration
public class EventBusConfig {
	
    @Bean
    public EventBus eventBus() {
        return new EventBus(); // guava event bus
    }
    
    @Bean
    public AsyncEventBus asyncEventBus() {
        return new AsyncEventBus(Executors.newCachedThreadPool()); // guava event bus
    }

}