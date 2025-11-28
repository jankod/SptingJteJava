package hr.ja.st.form.options;

import java.util.List;
import java.util.Locale;

public interface OptionsProvider<T> {
    List<T> getOptions(Locale locale);
}

