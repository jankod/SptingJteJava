package hr.ja.st.form.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UiForm {
    String action();
    String method() default "POST";
    String cssClass() default "card card-body";
}

