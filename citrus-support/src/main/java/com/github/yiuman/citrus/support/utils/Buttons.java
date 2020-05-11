package com.github.yiuman.citrus.support.utils;

import com.github.yiuman.citrus.support.model.Button;

import java.util.Arrays;
import java.util.List;

/**
 * @author yiuman
 * @date 2020/5/8
 */
public final class Buttons {

    private static Button ADD = new Button("新增", "add", "primary","plus-circle-outline");

    private static Button EDIT = new Button("","edit","success","pencil");

    private static Button DELETE = new Button("", "delete", "error","delete-circle");

    private static Button DELETE_BATCH = new Button("批量删除", "batchDelete", "error","delete-circle-outline");

    private static Button IMPORT = new Button("导入", "import","","file-upload");

    private static Button EXPORT = new Button("导出", "export","","download");

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

    public static List<Button> defaultActions(){
        return Arrays.asList(EDIT, DELETE);
    }

}
