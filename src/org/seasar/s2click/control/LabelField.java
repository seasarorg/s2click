package org.seasar.s2click.control;

import org.apache.click.control.Field;
import org.apache.click.util.HtmlStringBuffer;

public class LabelField extends Field {

    private static final long serialVersionUID = 1L;

    public LabelField(String name) {
        super(name);
    }

    public LabelField(String name, String label) {
        super(name, label);
    }

    public LabelField() {
        super();
    }

    public boolean onProcess() {
        return true;
    }

    public void render(HtmlStringBuffer buffer) {
        buffer.append(getValue());
    }

    public String toString() {
        return getValue();
    }
}
