package hr.ja.st.dev;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DevWebConfig implements WebMvcConfigurer {
    private final DevDebugInterceptor interceptor;

    public DevWebConfig(DevDebugInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (DevToolbarConfig.ENABLED) {
            registry.addInterceptor(interceptor).order(Integer.MAX_VALUE - 100);
        }
    }
}

