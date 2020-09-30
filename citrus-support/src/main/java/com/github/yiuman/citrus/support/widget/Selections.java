package com.github.yiuman.citrus.support.widget;

import java.util.List;

/**
 * 下拉选择控件
 *
 * @author yiuman
 * @date 2020/5/6
 */
public class Selections extends BaseWidget<List<Selections.SelectItem>> {

    /**
     * 是否多选
     */
    private boolean multiple;

    private boolean clearable;

    public Selections() {
    }

    public Selections(Object object, String methodName) {
    }

    public Selections(String text, String key, List<SelectItem> model) {
        super(text, key, model);
    }

    public Selections(String text, String key, List<SelectItem> model, boolean multiple) {
        super(text, key, model);
        this.multiple = multiple;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isClearable() {
        return clearable;
    }

    public void setClearable(boolean clearable) {
        this.clearable = clearable;
    }

    public static class SelectItem {

        private String id;

        private String label;

        private Object value;

        public SelectItem(String id, String label, Object value) {
            this.id = id;
            this.label = label;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    @Override
    public String getWidgetName() {
        return "v-select";
    }
}
