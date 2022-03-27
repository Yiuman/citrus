use
citrus;

-- 用户表
INSERT INTO sys_user (user_id, login_id, password, username, email, mobile, uuid, status, created_time, created_by,
                      last_modified_time, last_modified_by, version, admin, avatar)
VALUES (1, 'admin', '$2a$10$gK0BhYud6iSM7um4RLCYvuYEvtWSLYjsKb3VlTEcxgbtPQC3pAK9C', '平台管理员', '415481084@qq.com',
        '13119593102', 'YvuYEvtWSLYjsKb3VlTEcxgbtPQC3pAK9C', 1, '2020-09-23 03:27:51', null, '2020-09-23 03:27:51', 1,
        1, 1, null);

-- 组织机构表
INSERT INTO sys_organ (organ_id, organ_name, parent_id, left_value, right_value, created_time, created_by,
                       last_modified_time, last_modified_by, remark, organ_code, deep)
VALUES (1, '平台管理', null, 1, 18, null, null, null, null, null, '3', 1);

-- 菜单及操作资源
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1264742816068694018, '系统管理', null, 0, null, null, null, null, null, null, null, 'mdi-cogs', null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1264847011765583874, '用户管理', 1264742816068694018, 0, '/rest/users', null, null, null, null, null,
        'components/CrudTable', 'mdi-account', null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1264851760833429505, '角色管理', 1264742816068694018, 0, '/rest/roles', null, null, null, null, null,
        'components/CrudTable', 'mdi-account-cowboy-hat', null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1288720100249116674, '组织机构管理', 1264742816068694018, 0, '/rest/organs', null, null, null, null, null,
        'components/CrudTree', 'mdi-file-tree-outline', null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1288721127266394114, '菜单管理', 1264742816068694018, 0, '/rest/menus', null, null, null, null, null,
        'components/CrudTree', 'mdi-microsoft-xbox-controller-menu', null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1288721239736655873, '资源管理', 1264742816068694018, 0, '/rest/resources', null, null, null, null, null,
        'components/CrudTable', 'mdi-semantic-web', null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1288721499544428545, '数据范围管理', 1264742816068694018, 0, '/rest/scopes', null, null, null, null, null,
        'view/system/DataScopes', 'mdi-account-arrow-left', null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1288721592632811521, '权限管理', 1264742816068694018, 0, '/rest/auth', null, null, null, null, null,
        'view/system/Authority', 'mdi-shield-account-outline', null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1290121928620699649, '字典管理', 1264742816068694018, 0, '/rest/dicts', null, null, null, null, null,
        'components/CrudTable', 'mdi-library', null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1347441018663956481, '访问日志', 1264742816068694018, 0, '/rest/access/log', null, null, null, null, null,
        'components/CrudTable', null, null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324166688647704578, '列表', 1264847011765583874, 2, '/rest/users', 'GET', null, null, null, null, null, null,
        'list', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324166688664481793, '查看', 1264847011765583874, 2, '/rest/users/{key}', 'GET', null, null, null, null, null,
        null, 'get', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324166688677064706, '导出', 1264847011765583874, 2, '/rest/users/export', 'GET', null, null, null, null, null,
        null, 'export', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324166688693841922, '新增', 1264847011765583874, 2, '/rest/users', 'POST', null, null, null, null, null, null,
        'add', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324166688702230529, '编辑', 1264847011765583874, 2, '/rest/users', 'POST', null, null, null, null, null, null,
        'edit', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324166688714813441, '删除', 1264847011765583874, 2, '/rest/users/{key}', 'DELETE', null, null, null, null, null,
        null, 'delete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324166688727396353, '批量删除', 1264847011765583874, 2, '/rest/users/batch_delete', 'POST', null, null, null, null,
        null, null, 'batchDelete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324166688739979266, '导入', 1264847011765583874, 2, '/rest/users/import', 'POST', null, null, null, null, null,
        null, 'import', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172170896654338, '列表', 1264851760833429505, 2, '/rest/roles', 'GET', null, null, null, null, null, null,
        'list', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172170926014466, '查看', 1264851760833429505, 2, '/rest/roles/{key}', 'GET', null, null, null, null, null,
        null, 'get', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172170942791682, '导出', 1264851760833429505, 2, '/rest/roles/export', 'GET', null, null, null, null, null,
        null, 'export', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172170959568898, '新增', 1264851760833429505, 2, '/rest/roles', 'POST', null, null, null, null, null, null,
        'add', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172170976346113, '编辑', 1264851760833429505, 2, '/rest/roles', 'POST', null, null, null, null, null, null,
        'edit', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172171001511937, '删除', 1264851760833429505, 2, '/rest/roles/{key}', 'DELETE', null, null, null, null, null,
        null, 'delete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172171009900546, '批量删除', 1264851760833429505, 2, '/rest/roles/batch_delete', 'POST', null, null, null, null,
        null, null, 'batchDelete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172171030872065, '导入', 1264851760833429505, 2, '/rest/roles/import', 'POST', null, null, null, null, null,
        null, 'import', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172244007567361, '列表', 1288720100249116674, 2, '/rest/organs', 'GET', null, null, null, null, null, null,
        'list', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172244032733185, '查看', 1288720100249116674, 2, '/rest/organs/{key}', 'GET', null, null, null, null, null,
        null, 'get', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172244049510401, '导出', 1288720100249116674, 2, '/rest/organs/export', 'GET', null, null, null, null, null,
        null, 'export', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172244066287618, '新增', 1288720100249116674, 2, '/rest/organs', 'POST', null, null, null, null, null, null,
        'add', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172244078870530, '编辑', 1288720100249116674, 2, '/rest/organs', 'POST', null, null, null, null, null, null,
        'edit', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172244095647746, '删除', 1288720100249116674, 2, '/rest/organs/{key}', 'DELETE', null, null, null, null, null,
        null, 'delete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172244108230657, '批量删除', 1288720100249116674, 2, '/rest/organs/batch_delete', 'POST', null, null, null,
        null, null, null, 'batchDelete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172244129202177, '导入', 1288720100249116674, 2, '/rest/organs/import', 'POST', null, null, null, null, null,
        null, 'import', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172263456555010, '加载树', 1288720100249116674, 2, '/rest/organs/tree', 'GET', null, null, null, null, null,
        null, null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172263477526529, '加载子节点', 1288720100249116674, 2, '/rest/organs/tree/{parentKey}', 'GET', null, null, null,
        null, null, null, null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172263498498050, '移动', 1288720100249116674, 2, '/rest/organs/tree/move', 'POST', null, null, null, null,
        null, null, 'move', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172263515275266, '初始化', 1288720100249116674, 2, '/rest/organs/tree/init', 'POST', null, null, null, null,
        null, null, null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172287963873281, '列表', 1288721127266394114, 2, '/rest/menus', 'GET', null, null, null, null, null, null,
        'list', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172287976456194, '查看', 1288721127266394114, 2, '/rest/menus/{key}', 'GET', null, null, null, null, null,
        null, 'get', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172287989039106, '导出', 1288721127266394114, 2, '/rest/menus/export', 'GET', null, null, null, null, null,
        null, 'export', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172288005816322, '新增', 1288721127266394114, 2, '/rest/menus', 'POST', null, null, null, null, null, null,
        'add', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172288014204929, '编辑', 1288721127266394114, 2, '/rest/menus', 'POST', null, null, null, null, null, null,
        'edit', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172288026787842, '删除', 1288721127266394114, 2, '/rest/menus/{key}', 'DELETE', null, null, null, null, null,
        null, 'delete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172288043565058, '批量删除', 1288721127266394114, 2, '/rest/menus/batch_delete', 'POST', null, null, null, null,
        null, null, 'batchDelete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172288051953666, '导入', 1288721127266394114, 2, '/rest/menus/import', 'POST', null, null, null, null, null,
        null, 'import', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172310541811714, '加载树', 1288721127266394114, 2, '/rest/menus/tree', 'GET', null, null, null, null, null,
        null, null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172310562783233, '加载子节点', 1288721127266394114, 2, '/rest/menus/tree/{parentKey}', 'GET', null, null, null,
        null, null, null, null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172310587949057, '移动', 1288721127266394114, 2, '/rest/menus/tree/move', 'POST', null, null, null, null,
        null, null, 'move', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172310608920578, '初始化', 1288721127266394114, 2, '/rest/menus/tree/init', 'POST', null, null, null, null,
        null, null, null, 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172751115698177, '列表', 1288721239736655873, 2, '/rest/resources', 'GET', null, null, null, null, null, null,
        'list', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172751132475394, '查看', 1288721239736655873, 2, '/rest/resources/{key}', 'GET', null, null, null, null, null,
        null, 'get', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172751153446914, '导出', 1288721239736655873, 2, '/rest/resources/export', 'GET', null, null, null, null,
        null, null, 'export', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172751174418434, '新增', 1288721239736655873, 2, '/rest/resources', 'POST', null, null, null, null, null,
        null, 'add', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172751191195650, '编辑', 1288721239736655873, 2, '/rest/resources', 'POST', null, null, null, null, null,
        null, 'edit', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172751203778561, '删除', 1288721239736655873, 2, '/rest/resources/{key}', 'DELETE', null, null, null, null,
        null, null, 'delete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172751233138690, '批量删除', 1288721239736655873, 2, '/rest/resources/batch_delete', 'POST', null, null, null,
        null, null, null, 'batchDelete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172751254110209, '导入', 1288721239736655873, 2, '/rest/resources/import', 'POST', null, null, null, null,
        null, null, 'import', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172761668567042, '列表', 1288721499544428545, 2, '/rest/scopes', 'GET', null, null, null, null, null, null,
        'list', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172761689538561, '查看', 1288721499544428545, 2, '/rest/scopes/{key}', 'GET', null, null, null, null, null,
        null, 'get', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172761706315778, '导出', 1288721499544428545, 2, '/rest/scopes/export', 'GET', null, null, null, null, null,
        null, 'export', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172761718898689, '新增', 1288721499544428545, 2, '/rest/scopes', 'POST', null, null, null, null, null, null,
        'add', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172761731481601, '编辑', 1288721499544428545, 2, '/rest/scopes', 'POST', null, null, null, null, null, null,
        'edit', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172761744064513, '删除', 1288721499544428545, 2, '/rest/scopes/{key}', 'DELETE', null, null, null, null, null,
        null, 'delete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172761752453122, '批量删除', 1288721499544428545, 2, '/rest/scopes/batch_delete', 'POST', null, null, null,
        null, null, null, 'batchDelete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172761765036034, '导入', 1288721499544428545, 2, '/rest/scopes/import', 'POST', null, null, null, null, null,
        null, 'import', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172768622723073, '列表', 1288721592632811521, 2, '/rest/auth', 'GET', null, null, null, null, null, null,
        'list', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172768635305985, '查看', 1288721592632811521, 2, '/rest/auth/{key}', 'GET', null, null, null, null, null,
        null, 'get', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172768647888898, '导出', 1288721592632811521, 2, '/rest/auth/export', 'GET', null, null, null, null, null,
        null, 'export', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172768660471810, '新增', 1288721592632811521, 2, '/rest/auth', 'POST', null, null, null, null, null, null,
        'add', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172768677249026, '编辑', 1288721592632811521, 2, '/rest/auth', 'POST', null, null, null, null, null, null,
        'edit', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172768685637634, '删除', 1288721592632811521, 2, '/rest/auth/{key}', 'DELETE', null, null, null, null, null,
        null, 'delete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172768698220546, '批量删除', 1288721592632811521, 2, '/rest/auth/batch_delete', 'POST', null, null, null, null,
        null, null, 'batchDelete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1324172768710803458, '导入', 1288721592632811521, 2, '/rest/auth/import', 'POST', null, null, null, null, null,
        null, 'import', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1347441067863142402, '列表', 1347441018663956481, 2, '/rest/access/log', 'GET', null, null, null, null, null,
        null, 'list', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1347441067871531010, '查看', 1347441018663956481, 2, '/rest/access/log/{key}', 'GET', null, null, null, null,
        null, null, 'get', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1347441067888308225, '导出', 1347441018663956481, 2, '/rest/access/log/export', 'GET', null, null, null, null,
        null, null, 'export', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1404462498928685057, '列表', 1290121928620699649, 2, '/rest/dicts', 'GET', null, null, null, null, null, null,
        'list', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1404462498928685058, '查看', 1290121928620699649, 2, '/rest/dicts/{key}', 'GET', null, null, null, null, null,
        null, 'get', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1404462498932879362, '导出', 1290121928620699649, 2, '/rest/dicts/export', 'GET', null, null, null, null, null,
        null, 'export', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1404462498970628098, '新增', 1290121928620699649, 2, '/rest/dicts', 'POST', null, null, null, null, null, null,
        'add', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1404462498970628099, '编辑', 1290121928620699649, 2, '/rest/dicts', 'POST', null, null, null, null, null, null,
        'edit', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1404462498974822401, '删除', 1290121928620699649, 2, '/rest/dicts/{key}', 'DELETE', null, null, null, null, null,
        null, 'delete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1404462498974822402, '批量删除', 1290121928620699649, 2, '/rest/dicts/batch_delete', 'POST', null, null, null, null,
        null, null, 'batchDelete', 0);
INSERT INTO citrus.sys_resource (resource_id, resource_name, parent_id, type, path, operation, created_time, created_by,
                                 last_modified_time, last_modified_by, component, icon, resource_code, hidden)
VALUES (1404462498974822403, '导入', 1290121928620699649, 2, '/rest/dicts/import', 'POST', null, null, null, null, null,
        null, 'import', 0);

-- 数据范围
INSERT INTO sys_scope (scope_id, scope_name, organ_id)
VALUES (1, '当前部门', -1);
INSERT INTO sys_scope (scope_id, scope_name, organ_id)
VALUES (2, '当前部门及下属部门', -1);
INSERT INTO sys_scope (scope_id, scope_name, organ_id)
VALUES (3, '当前部门的下属部门', -1);
INSERT INTO sys_scope (scope_id, scope_name, organ_id)
VALUES (4, '当前部门及上级部门', -1);
INSERT INTO sys_scope (scope_id, scope_name, organ_id)
VALUES (5, '二级部门', -1);
INSERT INTO sys_scope (scope_id, scope_name, organ_id)
VALUES (6, '三级部门', -1);

-- 数据范围的定义
INSERT INTO sys_scope_define (id, scope_id, organ_id, scope_rule, scope_types)
VALUES (1, 1, 0, 0, 1);
INSERT INTO sys_scope_define (id, scope_id, organ_id, scope_rule, scope_types)
VALUES (2, 2, 0, 0, 3);
INSERT INTO sys_scope_define (id, scope_id, organ_id, scope_rule, scope_types)
VALUES (3, 3, 0, 0, 2);
INSERT INTO sys_scope_define (id, scope_id, organ_id, scope_rule, scope_types)
VALUES (4, 4, 0, 0, 5);