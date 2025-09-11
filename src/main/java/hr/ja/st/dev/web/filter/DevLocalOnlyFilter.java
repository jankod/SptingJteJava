package hr.ja.st.dev.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;

@Component
@Profile("dev")
@Order(0)
public class DevLocalOnlyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri != null && uri.startsWith("/__dev/")) {
            String remote = request.getRemoteAddr();
            if (!isLocal(remote)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "__dev only allowed from localhost");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isLocal(String addr) {
        if (addr == null) return false;
        if ("127.0.0.1".equals(addr) || "::1".equals(addr)) return true;
        try {
            InetAddress ip = InetAddress.getByName(addr);
            return ip.isLoopbackAddress();
        } catch (Exception e) {
            return false;
        }
    }
}
