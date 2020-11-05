package com.github.yiuman.citrus.support.utils;

import com.github.yiuman.citrus.support.model.Button;

import java.util.Arrays;
import java.util.List;

/**
 * @author yiuman
 * @date 2020/5/8
 */
public final class Buttons {

    private static final Button ADD = new Button("新增", "add", "primary", "plus");

    private static final Button EDIT = new Button("编辑", "edit", "#81b90c", "pencil");

    private static final Button DELETE = new Button("删除", "delete", "error", "delete-circle");

    private static final Button DELETE_BATCH = new Button("删除", "batchDelete", "error", "trash-can");

    private static final Button IMPORT = new Button("导入", "import", "", "file-upload");

    private static final Button EXPORT = new Button("导出", "export", "", "download");

    private static final Button MORE = new Button("更多操作", "primary", "chevron-down", IMPORT, EXPORT, DELETE_BATCH);

    private Buttons() {
    }

    public static Button add() {
        return ADD;
    }

    public static  Button edit(){
        return EDIT;
    }

    public static Button delete() {
        return DELETE;
    }

    public static Button deleteBatch() {
        return DELETE_BATCH;
    }

    public static Button importButton() {
        return IMPORT;
    }

    public static Button exportButton() {
        return EXPORT;
    }

    public static List<Button> defaultButtons() {
        return Arrays.asList(ADD, DELETE_BATCH, IMPORT, EXPORT);
    }

    public static List<Button> defaultButtonsWithMore() {
        return Arrays.asList(ADD, MORE);
    }

    public static List<Button> defaultActions() {
        return Arrays.asList(EDIT, DELETE);
    }

}
