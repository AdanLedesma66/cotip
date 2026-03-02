package py.com.cotip.insfrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static py.com.cotip.insfrastructure.config.CotipConstants.COTIP_API_VERSION_HEADER;

@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.useRequestHeader(COTIP_API_VERSION_HEADER);
    }

}
