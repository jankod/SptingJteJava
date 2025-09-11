package hr.ja.st.dev.web.interceptor;

import hr.ja.st.dev.config.DevToolbarConfig;
import hr.ja.st.dev.support.DebugContext;
import hr.ja.st.dev.support.DebugInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DevDebugInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!DevToolbarConfig.ENABLED) return true;
        if (handler instanceof HandlerMethod hm) {
            DebugInfo info = new DebugInfo();
            info.requestUri = request.getRequestURI();
            info.controllerClass = hm.getBeanType().getName();
            info.controllerMethod = hm.getMethod().getName();
            // best-effort source path guess for dev sources
            String srcRel = "src/main/java/" + info.controllerClass.replace('.', '/') + ".java";
            info.controllerSourcePath = Path.of(DevToolbarConfig.PROJECT_DIR, srcRel).toString();
            // best-effort find method line
            try {
                java.nio.file.Path p = java.nio.file.Path.of(info.controllerSourcePath);
                if (java.nio.file.Files.exists(p)) {
                    java.util.List<String> lines = java.nio.file.Files.readAllLines(p);
                    String needle = info.controllerMethod + "(";
                    for (int i = 0; i < lines.size(); i++) {
                        if (lines.get(i).contains(needle)) {
                            info.controllerSourceLine = i + 1;
                            break;
                        }
                    }
                }
            } catch (Exception ignored) {
            }
            // Extract required roles from annotations (@Secured on method/class)
            try {
                java.util.Set<String> req = new java.util.LinkedHashSet<>();
                Secured mSecured = hm.getMethod().getAnnotation(Secured.class);
                if (mSecured != null) req.addAll(java.util.Arrays.asList(mSecured.value()));
                Secured cSecured = hm.getBeanType().getAnnotation(Secured.class);
                if (cSecured != null) req.addAll(java.util.Arrays.asList(cSecured.value()));
                // JSR-250 optional support
                try {
                    jakarta.annotation.security.RolesAllowed mRa = hm.getMethod().getAnnotation(jakarta.annotation.security.RolesAllowed.class);
                    if (mRa != null) req.addAll(java.util.Arrays.asList(mRa.value()));
                    jakarta.annotation.security.RolesAllowed cRa = hm.getBeanType().getAnnotation(jakarta.annotation.security.RolesAllowed.class);
                    if (cRa != null) req.addAll(java.util.Arrays.asList(cRa.value()));
                } catch (Throwable ignored) {
                }
                info.requiredRoles = new java.util.ArrayList<>(req);
            } catch (Throwable ignored) {
            }
            DebugContext.set(info);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) {
        if (!DevToolbarConfig.ENABLED) return;
        DebugInfo info = DebugContext.current();
        if (info == null) return;
        if (modelAndView != null) {
            info.viewName = modelAndView.getViewName();
            List<String> files = new ArrayList<>();
            if (info.viewName != null && info.viewName.endsWith(".jte")) {
                String rel = "src/main/jte/" + info.viewName;
                files.add(Path.of(DevToolbarConfig.PROJECT_DIR, rel).toString());
            }
            // base layout
            files.add(Path.of(DevToolbarConfig.PROJECT_DIR, "src/main/jte/layout/base.jte").toString());
            info.templateFiles = files;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        DebugContext.clear();
    }
}
