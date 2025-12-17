package hr.ja.st.project.web.options;

import hr.ja.st.form.options.OptionsProvider;
import hr.ja.st.user.domain.User;
import hr.ja.st.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserOptions implements OptionsProvider<User> {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> getOptions(Locale locale) {
        return userRepository.findAllUsers();
    }

}

