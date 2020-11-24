### 通用CRUD指南

#### 如何使用？

##### 最简单的方式

1. 定义表实体，如Student

   ```java
   @Data
   @TableName("demo_student")
   public class Student {
   
       @TableId(type = IdType.ASSIGN_ID)
       private Long studentId;
   
       private String studentName;
   
       private Long studentNo;
   
   }
   ```

2. 继承基础REST控制器BaseCrudController

   ```java
   @RestController
   @RequestMapping("/rest/students")
   public class StudentController extends BaseCrudController<Student, Long> {
   
       public StudentController() {
         //根据学生编码进行倒序排序
         addSortBy("studentNo",true)
       }
}
   ```
   

> 至此，通用的CRUD功能已经实现，已经包含基础REST风格的CRUD功能（分页、保存、获取、更新、删除）



##### 树形结构

1. 定义表实体，继承BaseTree（最简单的树形）或BasePreOrderTree（预遍历树，有左右值）

   ```java
   @Data
   @NoArgsConstructor
   @TableName("demo_menu")
   @EqualsAndHashCode(of = {"menuId"}, callSuper = false)
   public class Menu extends BaseTree<Menu, Long> {
   
       @JsonSerialize(using = ToStringSerializer.class)
       @TableId(type = IdType.ASSIGN_ID)
       private Long menuId;
   
       /**
        * 菜单名
        */
       private String menuName;
   
       /**
        * 父菜单ID
        */
       @JsonSerialize(using = ToStringSerializer.class)
       private Long parentId;
   
       /**
        * 资源路径
        */
       private String path;
   
       @JsonSerialize(using = ToStringSerializer.class)
       @Override
       public Long getId() {
           return this.menuId;
       }
   
       @Override
       public void setParentId(Long parentId) {
           this.parentId = parentId;
       }
   
   }
   
   ```

2. 继承基础的树形控制器BaseTreeController

   ```java
   @RestController
   @RequestMapping("/rest/menus")
   public class MenuController extends BaseTreeController<Menu, Long> {
     
    		public MenuController() {
         //是否懒加载
       	setLazy(true)
       }
   }
   
   ```


> 至此，通用的树形CRUD功能已经实现，已经包含除基础REST风格的CRUD功能（分页、保存、获取、更新、删除）外的树形相关操作接口，如加载树形接口/tree ，根据父节加载/tree/{parentKey}，导出树形JSON文件等。



##### 复杂业务逻辑的CRUD

1. **情况一**：接参与库表基本一致时，可不使用DTO进行传输，简单的重写相关方法进行逻辑处理，如Student的学号属性需要初始化。下面提供两种方式进行处理

   - 方式一，重写BaseCrudController中的save方法

     ```java
     @RestController
     @RequestMapping("/rest/students")
     public class StudentController extends BaseCrudController<Student, Long> {
     
         public StudentController() {
           //根据学生编码进行倒序排序
           addSortBy("studentNo",true)
         }
       
       	@Override
         public Long save(Student entity) throws Exception {
             if(Objects.isNull(entity.getStudentId())){
               entity.setStudentNo(UUID.randomUUID().toString());
             }
             return super.save(entity);
         }
     }
     ```

   - 方式二：重写protected CrudService<T, K> getService() 方法，引用自己的逻辑层进行实现

     ```java
     @RestController
     @RequestMapping("/rest/students")
     public class StudentController extends BaseCrudController<Student, Long> {
     
       	//注入逻辑层
     		private final StudentService studentService
     
         public StudentController(StudentService studentService) {
           //根据学生编码进行倒序排序
           addSortBy("studentNo",true)
           this.studentService = studentService;
         }
       
      		@Override
         protected CrudService<Student, Long> getService() {
             return studentService;
         }
       
     }
     ```

     继承BaseService,实现自己的逻辑

     ```java
     @Service
     public class StudentService extends BaseService<Student, Long> {
     
       	//保存前设置学生编码
       	@Override
         public boolean beforeSave(Student entity) {
             entity.setStudentNo(UUID.randomUUID().toString())
             return true;
         }
     }
     ```

   

   2. **情况二**，表单与库表差距巨大，业务逻辑复杂，需要定义传输类DTO进行表单接收及逻辑处理。

      - 定义逻辑表单传输类DTO

        ```java
        @Data
        public class UserDto {
        
            @JsonSerialize(using = ToStringSerializer.class)
            private Long userId;
        
            private String loginId;
        
            @NotBlank
            private String username;
        
            @NotBlank
            private String mobile;
        
            @JsonIgnore
            @ExcelIgnore
            private String password;
        
            private String email;
        
            @ExcelIgnore
            private String avatar;
        
            @JsonSerialize(contentUsing = ToStringSerializer.class)
            private List<Long> roleIds;
        
            /**
             * 所属组织ID
             */
            @JsonSerialize(contentUsing = ToStringSerializer.class)
            private List<Long> organIds;
        
            private String uuid;
        
            private Integer version;
        }
        ```

        

      - 继承BaseDtoService<E, K extends Serializable, D>实现业务逻辑

        ```java
        @Service
        public class UserService extends BaseDtoService<User, Long, UserDto> {
        
            /**
             * 匿名登录的认证对象principal
             */
            private final static String ANONYMOUS = "anonymousUser";
        
            private final PasswordEncoder passwordEncoder;
        
            private final UserMapper userMapper;
        
            /**
             * 用户角色mapper
             */
            private final UserRoleMapper userRoleMapper;
        
            /**
             * 用户组织机构mapper
             */
            private final UserOrganMapper userOrganMapper;
        
            public UserService(PasswordEncoder passwordEncoder, UserMapper userMapper, UserRoleMapper userRoleMapper, UserOrganMapper userOrganMapper, UserOnlineCache userOnlineCache) {
                this.passwordEncoder = passwordEncoder;
                this.userMapper = userMapper;
                this.userRoleMapper = userRoleMapper;
                this.userOrganMapper = userOrganMapper;
                this.userOnlineCache = userOnlineCache;
            }
        
          //保存前的操作
            @Override
            public boolean beforeSave(UserDto entity) throws Exception {
                if (Objects.isNull(entity.getUserId())) {
                    //若是新增则添加默认密码及默认版本号
                    entity.setPassword(passwordEncoder.encode("123456"));
                    entity.setVersion(1);
                    entity.setUuid(UUID.randomUUID().toString().replace("-",""));
                }
        
                return true;
            }
        
          	//实体保存后的操作
            @Override
            public void afterSave(UserDto entity) {
                //保存组织机构信息
                List<Long> organIds = entity.getOrganIds();
                if (CollectionUtils.isEmpty(organIds)) {
                    organIds = Collections.singletonList(-1L);
                }
                //先删除用户旧的角色部门数据
                userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, entity.getUserId()));
                userOrganMapper.delete(Wrappers.<UserOrgan>lambdaQuery().eq(UserOrgan::getUserId, entity.getUserId()));
        
                organIds.forEach(LambdaUtils.consumerWrapper(organId -> {
                    userOrganMapper.saveEntity(new UserOrgan(entity.getUserId(), organId));
                    //保存组织机构角色数据
                    List<UserRole> userRoles = entity.getRoleIds().stream().map(roleId -> {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(entity.getUserId());
                        userRole.setRoleId(roleId);
                        userRole.setOrganId(organId);
                        return userRole;
                    }).collect(Collectors.toList());
                    userRoleMapper.saveBatch(userRoles);
                }));
            }
        
        }
        ```

        

      - 重写contorller中的protected CrudService<T, K> getService()方法

        ```java
        @RestController
        @RequestMapping("/rest/users")
        @Authorize(HasLoginHook.class)
        @Slf4j
        public class UserController extends BaseCrudController<UserDto, Long> {
        
            private final UserService userService;
        
            public UserController(UserService userService) {
                this.userService = userService;
                setParamClass(UserQuery.class);
            }
        
            @Override
            protected CrudService<UserDto, Long> getService() {
                return userService;
            }
        }
        
        ```

   
   > 至此，非常复杂的业务逻辑，使用DTO进行前后端交互的CRUD例子已完成
   
   

##### 查询、排序

> 一遍的列表或者树形都有或许简单、或许复杂的查询，底层用的是Mybatis-plus，顶层已经进行了注解式封装及简化。

1. 定义查询参数类

   ```java
   @Data
   public class StudentQuery {
   
       @QueryParam(type = "like")
       private String studentName;
   
     	@QueryParam(type = "eq",mapping="student_no")
       private String studentNo
         
       @QueryParam(handler=ClassIdsQueryHandler.class)  
       private List<Long> classIds;
   }
   ```

   上面的`@QueryParam`用于标记此字段是查询参数字段，若类中没有进行标记的则不作查询参数处理，请看下面是QueryParam的详细解释

   ```java
   @Target({ElementType.FIELD, ElementType.TYPE})
   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   public @interface QueryParam {
   
       /**
        * 参数处理类型
        *
        * @return 对应mybatis-plus中的wrapper类型
        */
       String type() default "eq";
   
       /**
        * 用于映射查询名称
        * 比如此字段是userId   映射到表的是user_id 此值为user_id
        *
        * @return 映射的查询名称
        */
       String mapping() default "";
   
       /**
        * 为空时是否拼接条件
        *
        * @return true/false
        */
       boolean condition() default true;
   
       /**
        * 查询参数处理器
        *
        * @return 查询参数处理器的类型，实现QueryParamHandler,处理查询条件
        */
       Class<? extends QueryParamHandler> handler() default DefaultQueryParamHandler.class;
   }
   ```

   查询参数处理器目前默认使用的是`DefaultQueryParamHandler`，会对参数自定自行封装及处理拼接，也可自定义参数参数的拼接，如下上面的查询参数类的classIds所属班级的复杂查询拼接

   ```java
   @Component
   public static class ClassIdsQueryHandler implements QueryParamHandler {
   
     private final StudentService studentSerive;
   
     public ClassIdsQueryHandler(StudentService studentSerive) {
       this.studentSerive = studentSerive;
     }
   
     @SuppressWarnings("unchecked")
     @Override
     public void handle(QueryParam queryParam, Object object, Field field, QueryWrapper<?> queryWrapper) 				throws Exception {
       QueryWrapper<StudentClasses> query = Wrappers.query();
       ReflectionUtils.makeAccessible(field);
       List<Long> classIds = (List<Long>) ReflectionUtils.getField(field, object);
       if (classIds == null) {
         return;
       }
       query.in("class_id", roleIds);
       List<Long> studentClasses = studentSerive.selectList(query)
         .stream()
         .map(StudentClasses::getClassesId)
         .collect(Collectors.toList());
   
       if (CollectionUtils.isEmpty(studentClasses)) {
         studentClasses = Collections.singletonList(0L);
   
       }
   
       queryWrapper.in(true, "class_id", studentClasses);
     }
   }
   ```

   

2. 在Controller构造进行设置查询类型与字段排序方式

   ```java
   @RestController
   @RequestMapping("/rest/users")
   @Slf4j
   public class UserController extends BaseCrudController<UserDto, Long> {
     
    		public UserController(UserService userService) {
         	//设置查询类
           setParamClass(UserQuery.class);
         
         	//添加查询排序
         	addSortBy("createdTime",true);
         	addSortBy("userId");
       }
   }
   ```

   其中addSortBy有两个重载方法，分别是两个参数addSortBy以及单参数的addSortBy

   ```java
   /**
    * 添加排序项
    * 在构造中使用
    *
    * @param 实体字段名，拼接查询时自动进行驼峰处理
    * @param isDesc 是否倒序
    */
   protected void addSortBy(String column, boolean isDesc) {}
   
   /**
    * 添加排序项
    * 在构造中使用
    *
    * @param 实体字段名，拼接查询时自动进行驼峰处理
    */
   protected void addSortBy(String column) {}
   ```

3. 自动入参填充/重写手动拼写查询参数

   如步骤2中已经设置了查询参数类以及添加了排序，则会基于Mybatis-plus的QueryWrapper自动拼接查询，拼出来的实现如下：

   ```java
   QueryWrapper<User> wraaper = Wrappers.query()
     .like("student_name",studentQuery.studentName)
     .eq("student_no",studentQuery.studentNo)
     .orderBy(true,true,"created_time").
     .orderBy(true,false,"user_id");
   ```

   查询条件的拼接方式默认的是按照`DefaultQueryParamHandler`进行处理的，若需要完全的自定义查询拼接方式与查询处理,可重写Controller中的拼接查询条件方法，有三个

   ```java
   //入参是当前请求，出参是Mybatis-plus中的查询Wrapper 
   QueryWrapper<T> getQueryWrapper(HttpServletRequest request) 
     
   //入参是查询参数的实体，已基于当前请求将参数整合到查询参数类中  出参是Mybatis-plus中的查询Wrapper
   QueryWrapper<T> getQueryWrapper(Object params)
   
   //处理排序的方法  入参是已经拼接过当前查询参数的Wrapper以及当前请求
   void handleSortWrapper(QueryWrapper<T> wrapper, HttpServletRequest request)
   ```



#### 相关基类说明


   基础的相关的CRUD操作已抽出基础的三层，即Controller-Service-Dao，控制层-逻辑层-持久层。

   	**控制层**：

- 列表的控制器（BaseCrudController<T, K extends Serializable>）；

- 树形结构的控制器（BaseTreeController<T extends Tree<K>, K extends Serializable>）。

   **逻辑层**：

- 最基础的实现（BaseService<E, K extends Serializable>用于基础的增删改查逻辑）；

- 使用传输类转化处理的（BaseDtoService<E, K extends Serializable, D>）；

- 简单的树形数据结构（BaseSimpleTreeService<E extends BaseTree<E, K>）；

- 预遍历树逻辑层（BasePreOrderTreeService<E extends BasePreOrderTree<E, K>, K extends Serializable>）

   **持久层**：

- 基础接口CrudMapper<T>  基于Mybaits-plus的BaseMapper<T>

- 树形Mapper接口TreeMapper<T extends Tree<?>>  主要用于预遍历树的查询



#### 背后的魔法

##### 1.为什么只要定义实体继承基础的控制器便可实现基础的增删改查？

> 根据定义的实体类，使用字节码技术动态地构造一层或者两层的CRUD操作实现，如上面的StudentController,会根据Student实体，构造出StudentService，StudentMapper并放进Spring进行管理。相当于生成器自动生成了3层代码。

##### 2.从接口入参到数据库查询的实现流程

> 查询接口通过HttpServletRequest进行接口参数，通过反射与DataBinder对request中的参数进行解释及将绑定到对应的查询类上。此时已知查询条件。再通过解析查询类中标记的@QueryParms注解，找到Mybatis-Plus中的Wrapper相关方法进行拼接进行查询














