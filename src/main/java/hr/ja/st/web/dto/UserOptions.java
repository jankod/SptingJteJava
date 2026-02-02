package hr.ja.st.web.dto;

import hr.ja.st.domain.User;
import hr.ja.st.form.options.OptionsProvider;
import hr.ja.st.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserOptions implements OptionsProvider<User> {


    private final UserRepository userRepository;
    @Override
    public List<User> getOptions(Locale locale) {
        return userRepository  .findAll();
    }
}
