@param hr.ja.st.form.meta.FieldMeta f
@param org.springframework.validation.BindingResult br
@param Object value

<div class="mb-3">
  <label for="${f.name}" class="form-label">${f.label} @if (f.required) <span class="text-danger">*</span> @endif</label>

  @if (f.type == hr.ja.st.form.FieldType.TEXT || f.type == hr.ja.st.form.FieldType.EMAIL || f.type == hr.ja.st.form.FieldType.PASSWORD || f.type == hr.ja.st.form.FieldType.NUMBER)
    <input
      type="${f.type == hr.ja.st.form.FieldType.PASSWORD ? "password" : (f.type == hr.ja.st.form.FieldType.EMAIL ? "email" : (f.type == hr.ja.st.form.FieldType.NUMBER ? "number" : "text"))}"
      id="${f.name}" name="${f.name}" placeholder="${f.placeholder}"
      class="form-control"
      value="${value != null ? String.valueOf(value) : ""}"
      readonly="${f.readOnly}"
    />
    @if (br != null && br.hasFieldErrors(f.name))
        <div class="invalid-feedback d-block">${br.getFieldError(f.name).getDefaultMessage()}</div>
    @endif
  @elseif (f.type == hr.ja.st.form.FieldType.TEXTAREA)
    <textarea id="${f.name}" name="${f.name}" class="form-control" placeholder="${f.placeholder}">${value != null ? String.valueOf(value) : ""}</textarea>
    @if (br != null && br.hasFieldErrors(f.name))
        <div class="invalid-feedback d-block">${br.getFieldError(f.name).getDefaultMessage()}</div>
    @endif
  @elseif (f.type == hr.ja.st.form.FieldType.SELECT)
    <select id="${f.name}" name="${f.name}" class="form-select">
      @if (f.options != null)
        @for (hr.ja.st.form.meta.FieldMeta.OptionItem option : f.options)
          <option value="${option.value}" selected="${option.selected}">${option.text}</option>
        @endfor
      @endif
    </select>
    @if (br != null && br.hasFieldErrors(f.name))
        <div class="invalid-feedback d-block">${br.getFieldError(f.name).getDefaultMessage()}</div>
    @endif
  @endif
  @if (f.hint != null && !f.hint.isBlank())
    <div class="form-hint">${f.hint}</div>
  @endif
</div>
