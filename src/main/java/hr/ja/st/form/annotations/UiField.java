package hr.ja.st.form.annotations;

import hr.ja.st.form.FieldType;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UiField {
    String label() default "";
    FieldType type() default FieldType.TEXT;
    String placeholder() default "";
    boolean readOnly() default false;
    String hint() default "";
    String[] css() default {};
    int order() default Integer.MAX_VALUE;
}

