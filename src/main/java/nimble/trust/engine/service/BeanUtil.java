package nimble.trust.engine.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 
 * Created by Marko on 2018-10-04
 *
 */
@Service
public class BeanUtil implements ApplicationContextAware {
    
	private static ApplicationContext context;
    
	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}