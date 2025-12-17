package hr.ja.st.form;

import hr.ja.st.form.annotations.UiField;
import hr.ja.st.form.annotations.UiForm;
import hr.ja.st.form.annotations.UiOptions;
import hr.ja.st.form.meta.FieldMeta;
import hr.ja.st.form.meta.FormMeta;
import hr.ja.st.form.options.OptionsProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.MessageSource;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class FormMetaBuilder {

    private final MessageSource messageSource;
    private final Locale locale;
    private final List<OptionsProvider<?>> providers;

    public FormMetaBuilder(MessageSource messageSource, Locale locale, List<OptionsProvider<?>> providers) {
        this.messageSource = messageSource;
        this.locale = locale == null ? Locale.getDefault() : locale;
        this.providers = providers == null ? java.util.List.of() : providers;
    }

    public FormMeta build(Object dto) {
        Class<?> clazz = dto.getClass();
        UiForm uf = clazz.getAnnotation(UiForm.class);
        if (uf == null) throw new IllegalArgumentException("@UiForm missing on " + clazz.getName());

        List<FieldMeta> fields = Arrays.stream(clazz.getDeclaredFields())
                .map(f -> toFieldMeta(dto, f))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(m -> m.order))
                .collect(Collectors.toList());

        FormMeta fm = new FormMeta();
        fm.action = uf.action();
        fm.method = uf.method();
        fm.cssClass = uf.cssClass();
        fm.fields = fields;
        return fm;
    }

    private FieldMeta toFieldMeta(Object dto, Field f) {
        UiField ui = f.getAnnotation(UiField.class);
        if (ui == null) return null;
        FieldMeta m = new FieldMeta();
        m.name = f.getName();
        m.type = ui.type();
        m.placeholder = ui.placeholder();
        m.readOnly = ui.readOnly();
        m.hint = ui.hint();
        m.css = List.of(ui.css());
        m.order = ui.order();
        m.label = resolveLabel(dto, f, ui.label());
        m.required = hasRequiredConstraint(f);

        UiOptions uo = f.getAnnotation(UiOptions.class);
        if (uo != null) {
            @SuppressWarnings("unchecked")
            OptionsProvider<Object> provider = (OptionsProvider<Object>) providers.stream()
                    .filter(p -> uo.provider().isInstance(p))
                    .findFirst().orElse(null);
            if (provider != null) {
                List<Object> opts = provider.getOptions(locale);
                String current = readDtoFieldString(dto, f);
                m.options = opts.stream().map(o -> {
                    FieldMeta.OptionItem it = new FieldMeta.OptionItem();
                    it.value = readField(o, uo.valueField());
                    it.text = readField(o, uo.labelField());
                    it.selected = current != null && !current.isBlank() && current.equals(it.value);
                    return it;
                }).collect(Collectors.toList());
            }
        }
        return m;
    }

    private String resolveLabel(Object dto, Field f, String explicit) {
        if (explicit != null && !explicit.isBlank()) return explicit;
        String code = dto.getClass().getSimpleName() + "." + f.getName() + ".label";
        return messageSource == null
                ? capitalize(f.getName())
                : messageSource.getMessage(code, null, capitalize(f.getName()), locale);
    }

    private boolean hasRequiredConstraint(Field f) {
        return f.isAnnotationPresent(NotNull.class)
                || f.isAnnotationPresent(NotBlank.class)
                || f.isAnnotationPresent(NotEmpty.class);
    }

    private String readField(Object obj, String fieldName) {
        try {
            var fld = obj.getClass().getDeclaredField(fieldName);
            fld.setAccessible(true);
            Object v = fld.get(obj);
            return v == null ? "" : String.valueOf(v);
        } catch (Exception e) {
            return "";
        }
    }

    private String readDtoFieldString(Object dto, Field f) {
        try {
            f.setAccessible(true);
            Object v = f.get(dto);
            return v == null ? null : String.valueOf(v);
        } catch (Exception e) {
            return null;
        }
    }

    private static String capitalize(String s) {
        return s == null || s.isBlank() ? "" : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
