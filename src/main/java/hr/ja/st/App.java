package hr.ja.st;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication(scanBasePackages = "hr.ja.st")
public class App {

    public static void main(String[] args) {
        //SpringApplication.run(SptingJteJavaApplication.class, args);


        SpringApplication app = new SpringApplication(App.class);
        app.setApplicationStartup(new BufferingApplicationStartup(2048));
        app.run(args);

    }

}
