package com.github.yiuman.citrus.support.utils;


import com.github.yiuman.citrus.support.widget.Button;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yiuman
 * @date 2020/5/8
 */
public final class Buttons {

    private static final Button ADD = Button.builder().key("ADD").model("add()").text("新增").build();

    private static final Button EDIT = Button.builder().key("EDIT").model("edit()").text("编辑").build();

    private static final Button DELETE = Button.builder().key("DELETE").model("delete()").text("删除").build();

    private static final Button DELETE_BATCH = Button.builder().key("BATCH_DELETE").model("batchDelete()").text("批量删除").build();

    private static final Button IMPORT = Button.builder().key("IMPORT").model("imp()").text("导入").build();

    private static final Button EXPORT = Button.builder().key("EXPORT").model("exp()").text("导出").build();

    private static final Button MORE = Button.builder().key("SHOW_MORE").text("更多操作")
            .actions(Stream.of(IMPORT, EXPORT, DELETE_BATCH).collect(Collectors.toList())).build();

    private Buttons() {
    }

    public static Button add() {
        return ADD;
    }

    public static Button edit() {
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
