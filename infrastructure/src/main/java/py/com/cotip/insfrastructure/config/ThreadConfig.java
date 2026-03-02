package py.com.cotip.insfrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadConfig {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService virtualThreadExecutor() {
        try {
            Method factoryMethod = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            return (ExecutorService) factoryMethod.invoke(null);
        } catch (Exception ignored) {
            return Executors.newCachedThreadPool();
        }
    }
}
