package hr.ja.st.form.meta;

import hr.ja.st.form.FieldType;
import java.util.List;

public class FieldMeta {
    public String name;
    public String label;
    public FieldType type;
    public String placeholder;
    public boolean readOnly;
    public String hint;
    public List<String> css;
    public int order;
    public boolean required;
    public List<OptionItem> options;

    public static class OptionItem {
        public String value;
        public String text;
        public boolean selected;
    }
}

