package hr.ja.st.form.annotations;

import hr.ja.st.form.options.OptionsProvider;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UiOptions {
    Class<? extends OptionsProvider<?>> provider();
    String valueField() default "id";
    String labelField() default "name";
}

