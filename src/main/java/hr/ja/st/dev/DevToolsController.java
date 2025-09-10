package hr.ja.st.dev;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
public class DevToolsController {

    @GetMapping("/__dev/open")
    public ResponseEntity<String> openInIde(@RequestParam("path") String path,
                                            @RequestParam(value = "line", required = false) Integer line) {
        if (!DevToolbarConfig.OPEN_ENABLED || !DevToolbarConfig.ENABLED) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dev open disabled");
        }
        try {
            File f = new File(path);
            if (!f.exists()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File not found");

            // Prefer JetBrains launcher if available
            String ideaCmd = findIdeaLauncher();
            Process proc;
            if (ideaCmd != null) {
                if (line != null) {
                    proc = new ProcessBuilder(ideaCmd, "--line", String.valueOf(line), f.getAbsolutePath()).start();
                } else {
                    proc = new ProcessBuilder(ideaCmd, f.getAbsolutePath()).start();
                }
            } else if (isMac()) {
                // Fallback macOS open
                if (line != null) {
                    proc = new ProcessBuilder("open", "-na", "IntelliJ IDEA", "--args", "--line", String.valueOf(line), f.getAbsolutePath()).start();
                } else {
                    proc = new ProcessBuilder("open", "-a", "IntelliJ IDEA", f.getAbsolutePath()).start();
                }
            } else {
                // Generic fallback
                proc = new ProcessBuilder(f.getAbsolutePath()).start();
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IOException e) {
            log.warn("Failed to open in IDE: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed: " + e.getMessage());
        }
    }

    private static boolean isMac() { return System.getProperty("os.name").toLowerCase().contains("mac"); }

    private static String findIdeaLauncher() {
        String[] candidates = new String[] {"idea", "idea64.exe"};
        for (String c : candidates) {
            try {
                Process p = new ProcessBuilder(c, "-h").start();
                return c;
            } catch (IOException ignored) {}
        }
        return null;
    }
}
