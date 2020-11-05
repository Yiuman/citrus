package com.github.yiuman.citrus.support.crud.rest;

/**
 * Restful操作
 *
 * @author yiuman
 * @date 2020/8/25
 */
public interface Operations {

    /**
     * 默认，表示LIST、Page、SAVE等不带参的操作
     */
    String DEFAULT = "";

    String SAVE = Operations.DEFAULT;

    String PAGE = Operations.DEFAULT;

    String LIST = Operations.DEFAULT;

    /**
     * 单个更新删除获取
     */
    String KEY = "/{key}";

    String GET = Operations.KEY;

    String DELETE = Operations.KEY;


    /**
     * 批量删除
     */
    String BATCH_DELETE = "/batch_delete";


    /**
     * 导出
     */
    String EXPORT = "/export";

    /**
     * 导入
     */
    String IMPORT = "/import";

    interface Tree {

        String DEFAULT_PREFIX = "/tree";

        String TREE = DEFAULT_PREFIX;

        String MOVE = DEFAULT_PREFIX+"/move";

        String GET_BY_PARENT = DEFAULT_PREFIX + "/{parentKey}";

        String INIT = DEFAULT_PREFIX+"/init";

    }

    interface Code{

        String LIST = "list";

        String ADD = "add";

        String EDIT = "edit";

        String GET = "get";

        String DELETE = "delete";

        String BATCH_DELETE = "batchDelete";

        String IMPORT = "import";

        String EXPORT = "export";

        String MOVE = "move";
    }

}
