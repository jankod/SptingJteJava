package hr.ja.st.dev.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import hr.ja.st.dev.web.interceptor.DevDebugInterceptor;

@Configuration
@Profile("dev")
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
