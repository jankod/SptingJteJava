package hr.ja.st.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"default", "dev"})
public class DevDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String username = "marko";
        if (userRepository.findByUsername(username).isEmpty()) {
            User u = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode("tajna"))
                    .enabled(true)
                    .roles("ROLE_USER")
                    .build();
            userRepository.save(u);
            log.info("Kreiran demo korisnik: {} / {}", username, "tajna");
        }
    }
}

