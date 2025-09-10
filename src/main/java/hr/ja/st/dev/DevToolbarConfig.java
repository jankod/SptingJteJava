package hr.ja.st.dev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DevToolbarConfig {
    public static volatile boolean ENABLED;
    public static volatile boolean OPEN_ENABLED;
    public static volatile String PROJECT_DIR;
    public static volatile String IDE_PATTERN;

    public DevToolbarConfig(
            @Value("${gg.jte.development-mode:false}") boolean jteDev,
            @Value("${app.dev.toolbar.enabled:true}") boolean toolbarEnabled,
            @Value("${app.dev.toolbar.open-enabled:true}") boolean openEnabled,
            @Value("${app.dev.ide.pattern:idea://open?file=%%f&line=%%l}") String idePattern
    ) {
        ENABLED = jteDev && toolbarEnabled;
        OPEN_ENABLED = openEnabled;
        PROJECT_DIR = System.getProperty("user.dir");
        IDE_PATTERN = idePattern;
    }

    public static String buildIdeUrl(String path, Integer line) {
        String pattern = IDE_PATTERN != null ? IDE_PATTERN : "idea://open?file=%%f&line=%%l";
        try {
            String encodedPath = java.net.URLEncoder.encode(path, java.nio.charset.StandardCharsets.UTF_8);
            String out = pattern.replace("%%f", encodedPath);
            if (out.contains("%%l")) {
                if (line != null && line > 0) {
                    out = out.replace("%%l", String.valueOf(line));
                } else {
                    // try remove "line" param if present
                    out = out.replace("&line=%%l", "").replace("?line=%%l", "");
                    out = out.replace("%%l", "1");
                }
            }
            return out;
        } catch (Exception e) {
            return pattern.replace("%%f", path).replace("%%l", line != null ? String.valueOf(line) : "1");
        }
    }
}
