package hr.ja.st.form;

import hr.ja.st.form.annotations.UiField;
import hr.ja.st.form.annotations.UiForm;
import hr.ja.st.form.annotations.UiOptions;
import hr.ja.st.form.meta.FormMeta;
import hr.ja.st.form.options.OptionsProvider;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticMessageSource;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class FormMetaBuilderTests {

    @UiForm(action = "/x")
    static class Dto {
        @UiField(label = "Name", type = FieldType.TEXT, order = 1)
        @NotBlank
        String name;

        @UiField(label = "Owner", type = FieldType.SELECT, order = 2)
        @UiOptions(provider = DummyProvider.class, valueField = "id", labelField = "text")
        Long ownerId;
    }

    static class Dummy {
        public String id;
        public String text;
        Dummy(String id, String text){ this.id = id; this.text = text; }
    }

    static class DummyProvider implements OptionsProvider<Dummy> {
        @Override public List<Dummy> getOptions(Locale locale) {
            return List.of(new Dummy("1","One"), new Dummy("2","Two"));
        }
    }

    @Test
    void builds_meta_and_detects_required_and_options() {
        var ms = new StaticMessageSource();
        ms.addMessage("Dto.name.label", Locale.getDefault(), "Name");
        var builder = new FormMetaBuilder(ms, Locale.getDefault(), List.of(new DummyProvider()));
        var meta = builder.build(new Dto());
        assertThat(meta).isNotNull();
        assertThat(meta.fields).hasSize(2);
        var f0 = meta.fields.get(0);
        assertThat(f0.name).isEqualTo("name");
        assertThat(f0.required).isTrue();
        var f1 = meta.fields.get(1);
        assertThat(f1.options).isNotNull();
        assertThat(f1.options).hasSize(2);
    }
}

