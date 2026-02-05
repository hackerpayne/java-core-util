
# 通用 增删改查(CRUD) 模块开发规范与模板

本文件用于为 AI 明确 **开发策略、目录结构、类职责、编码规范、注意要点**，从而在生成代码时保持一致的风格与质量。

---

# 📚 总览说明

一个完整的业务模块通常包含以下结构：

| 层级                     | 目录           | 作用                                             |
| :----------------------- | :------------- | :----------------------------------------------- |
| **Entity**（业务实体类） | `domain`       | 映射数据库字段、包含基础属性                     |
| **VO**（视图对象）       | `domain/vo`    | 用于前端展示的数据模型                           |
| **BO**（业务对象）       | `domain/bo`    | 新增、修改、查询、导入、导出时使用的业务参数对象 |
| **Service 接口**         | `service`      | 定义业务方法                                     |
| **Service 实现**         | `service/impl` | 实现业务逻辑                                     |
| **Controller**           | `controller`   | HTTP 接口层                                      |
| **ImportListener**       | `listener`     | Excel 数据导入解析与校验                         |

**AI 必须遵守本文档的规则生成模块代码。**

**主要技术栈**：MybatisPlus、SpringBoot3、Hutool

---

# 统一编码规范

- 除了 Controller，所有代码都需要生成文档注释（Javadoc Comment）
-

---

# 1️⃣ Entity：业务实体类规范（domain）

## 📌 核心职责

- 对应数据库表
- 作为 MyBatisPlus 的持久化对象（PO）
- 存放在：`domain/xxxEntity.java`

## 📌 必须遵守的规则

| 要点                       | 说明                                         |
| :------------------------- | :------------------------------------------- |
| `@TableName`               | 指定数据库表名                               |
| 继承 `BaseEntity`          | 简化通用字段，如创建时间、更新时间等         |
| `@AutoMapper`              | 默认支持转换为 VO                            |
| `@TableId`                 | 指定主键策略                                 |
| 字段名必须与数据库一致     | 便于 MyBatisPlus 自动映射                    |
| 字段名必须填写字段注释信息 | 使用数据库字段说明添加注释，如果没有帮我生成 |

## ✅ 示例模板：Entity

```java
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("your_table_name")
@AutoMapper(target = XxxVo.class)
public class XxxEntity extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    // 示例字段
    /**
     * 分类ID
     */
    private Long typeId;
}
```

---

# 2️⃣ VO：视图对象规范（domain/vo）

## 📌 作用

- 返回给前端展示的数据模型
- 用于 Excel 导出

## 📌 必须遵守规则

| 要点                      | 说明                                          |
| :------------------------ | :-------------------------------------------- |
| `@ExcelIgnoreUnannotated` | 未标注 `ExcelProperty` 的字段不会导出         |
| `@AutoMapper`             | 绑定 Entity                                   |
| `@ExcelProperty`          | 设置 Excel 列名，列名文字根据实体类字段名填写 |

## ✅ 示例模板：VO

```java
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = XxxEntity.class)
public class XxxVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty("主键")
    private Long id;

    // 示例字段
    private Long typeId;
}
```

---

# 3️⃣ BO：业务对象规范（domain/bo）

## 📌 作用

接收前端输入的数据，如：

- 新增参数
- 修改参数
- 查询参数
- Excel 导入参数

## 📌 关键规则

| 要点                                    | 说明                                |
| :-------------------------------------- | :---------------------------------- |
| 继承 `BaseEntity`                       | 自动集成通用字段                    |
| `@AutoMapper(target = XxxEntity.class)` | 可转换为 Entity                     |
| 使用校验注解                            | `@NotNull`、`AddGroup`、`EditGroup` |
| BO 字段不应包含数据库不允许修改的字段   | 如 `createTime`                     |

## ✅ 示例模板：BO

```java
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = XxxEntity.class, reverseConvertGenerate = false)
public class XxxBo extends BaseEntity {

    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    // 示例字段
    /**
     * 关键词名称
     */
    @NotNull(message = "名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

}
```

---

# 4️⃣ Service 接口规范（service）

## 📌 作用

- 提供业务功能接口
- 查询、增删改、分页、导入导出等

## 📌 关键规则

| 要点                   | 说明             |
| :--------------------- | :--------------- |
| 必须添加完整的注释说明 | 自动添加接口注释 |

## 📌 必须包含的常规方法

```java
public interface IXxxService {

    XxxVo queryById(Long id);

    Page<XxxVo> queryPageList(XxxBo bo, PageQuery pageQuery);

    List<XxxVo> queryList(XxxBo bo);

    Boolean insertByBo(XxxBo bo);

    Boolean updateByBo(XxxBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<QyModelNameValue> queryOptions(XxxBo bo);
}
```

---

# 5️⃣ ServiceImpl 服务实现规范（service/impl）

## 📌 作用

- 处理实际业务逻辑
- 封装查询条件
- 所有逻辑从 Controller 解耦

## 📌 必须遵守规则

| 要点                                   | 说明                              |
| :------------------------------------- | :-------------------------------- |
| 使用 `LambdaQueryWrapper` 封装查询条件 | 封装在 `buildQueryWrapper()` 方法 |
| 新增/修改前执行校验逻辑                | `validEntityBeforeSave()`         |
| 对象转换                               | 使用 `MapstructUtils`             |
| 分页查询                               | 使用 `baseMapper.queryVoPage()`   |
| 必须添加完整的注释说明                 | 自动添加注释                      |

## ✅ 示例模板：ServiceImpl

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class XxxServiceImpl implements IXxxService {

    private final XxxMapper baseMapper;

    private LambdaQueryWrapper<XxxEntity> buildQueryWrapper(XxxBo bo) {
        LambdaQueryWrapper<XxxEntity> lqw = Wrappers.lambdaQuery()
            // 如下为示例字段
            .like(StringUtils.isNotBlank(bo.getName()), XxxEntity::getName, bo.getName())
            .eq(Objects.nonNull(bo.getTypeId()), XxxEntity::getTypeId, bo.getTypeId());

        lqw.orderByDesc(XxxEntity::getId);
        return lqw;
    }

    @Override
    public XxxVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public Page<XxxVo> queryPageList(XxxBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<XxxEntity> lqw = buildQueryWrapper(bo);
        return baseMapper.queryVoPage(pageQuery.build(), lqw);
    }

    @Override
    public Boolean insertByBo(XxxBo bo) {
        XxxEntity entity = MapstructUtils.convert(bo, XxxEntity.class);
        validEntityBeforeSave(entity);
        boolean success = baseMapper.insert(entity) > 0;
        if (success) bo.setId(entity.getId());
        return success;
    }

    @Override
    public Boolean updateByBo(XxxBo bo) {
        XxxEntity entity = MapstructUtils.convert(bo, XxxEntity.class);
        validEntityBeforeSave(entity);
        return baseMapper.updateById(entity) > 0;
    }

    private void validEntityBeforeSave(XxxEntity entity) {
        // TODO 唯一性等业务校验
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // TODO 删除前业务校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public List<QyModelNameValue> queryOptions(XxxBo bo) {
        LambdaQueryWrapper<XxxEntity> lqw = buildQueryWrapper(bo);
        return baseMapper.queryOptions(lqw, XxxEntity::getName, XxxEntity::getId);
    }
}
```

---

# 6️⃣ ImportListener 数据导入监听器规范

## 📌 作用

- 从 Excel 中逐行读取数据
- 校验（Bo → Entity）
- 调用 Service 写入数据库
- 记录成功与失败条目

## 📌 必须遵守规则

| 要点                             | 说明           |
| :------------------------------- | :------------- |
| 继承 `AnalysisEventListener<Bo>` | 处理每行数据   |
| 使用 Hutool `BeanUtil`           | 数据转换       |
| 使用 `ValidatorUtils`            | 数据校验       |
| 保存逻辑必须委托给 Service       |                |
| 记录成功/失败信息                | 仅记录前 10 条 |

## ✅ 示例模板：ImportListener

```java
@Slf4j
public class XxxImportListener extends AnalysisEventListener<XxxImportBo>
        implements ExcelListener<XxxImportBo> {

    private final IXxxService xxxService;
    private final Boolean isUpdateSupport;

    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    public XxxImportListener(Boolean isUpdateSupport) {
        this.xxxService = SpringUtils.getBean(IXxxService.class);
        this.isUpdateSupport = isUpdateSupport;
    }

    @Override
    public void invoke(XxxImportBo bo, AnalysisContext context) {
        try {
            // 校验、转换、入库逻辑，如下是一个示例
            XxxVo dbEntity = this.xxxService.queryByName(bo.getName());
            // QyAssert.throwIfNull(dbEntity, "数据不存在，请检查"); // 根据业务需求决定是否需要校验存在性
            if (Objects.isNull(dbEntity)) {
                // 没有就新增
                XxxBo add = BeanUtil.toBean(bo, XxxBo.class);
                ValidatorUtils.validate(add); // 校验数据

                // 保存入库，成功加1
                xxxService.insertByBo(add);
                successNum++;
                if (successNum < 10) {
                    successMsg.append("<br/>").append(successNum).append("、数据xxx [").append(add.getName()).append("] 导入成功");
                }
            } else if (isUpdateSupport) {
                XxxBo update = BeanUtil.toBean(bo, XxxBo.class);
                update.setId(dbEntity.getId()); // 更新需要设置ID
                ValidatorUtils.validate(update);

                xxxService.updateByBo(update);

                successNum++;
                if (successNum < 10) {
                    successMsg.append("<br/>").append(successNum).append("、数据xxx [").append(update.getName()).append("] 更新成功");
                }
            } else {
                failureNum++;
                if (failureNum < 10) {
                    failureMsg.append("<br/>").append(failureNum).append("、数据xxx [").append(dbEntity.getName()).append("] 已存在");
                }
            }
        } catch (Exception e) {
            failureNum++;
            String message = e.getMessage();
            if (failureNum <= 10) {
                failureMsg.append("<br/>").append(failureNum).append("、")
                        .append("导入失败：").append(message);
            }
            log.error("导入失败：{}", message);
        }
    }

    @Override
    public ExcelResult<XxxImportBo> getExcelResult() {
        return new ExcelResult<>() {
            @Override
            public String getAnalysis() {
                if (failureNum > 0) {
                    failureMsg.insert(0, "导入失败！共 " + failureNum + " 条错误：");
                    throw new ServiceException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "导入成功，共 " + successNum + " 条：");
                }
                return successMsg.toString();
            }

            @Override
            public List<XxxImportBo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }
}
```

---

# 7️⃣Excel 导入 BO 规范

## ✅ 示例模板：ImportBo

```java
@Data
@ExcelIgnoreUnannotated
public class XxxImportBo {

    // 示例字段
    @ExcelIgnore
    private Long typeId;

    // 示例字段
    @ExcelProperty("名称")
    private String name;
}
```

---

# 8️⃣ Controller 控制器规范（controller）

## 📌 作用

- 提供 REST API
- 接收参数、调用 Service、返回结果

## 📌 关键规则

| 要点                               | 说明               |
| :--------------------------------- | :----------------- |
| 必须使用 `@SaCheckPermission`      | 权限控制           |
| 必须使用 `@Log`                    | 添加业务说明注释   |
| 增删改必须使用 `@RepeatSubmit`     | 防重复提交         |
| `@Validated` + 分组校验            | 限定新增或修改字段 |
| 不允许直接写业务逻辑               | 必须调用 Service   |
| 导出使用 `ExcelUtil.exportExcel()` |                    |

## ✅ 示例模板：Controller

```java
@RestController
@RequestMapping("/manage/xxx")
@RequiredArgsConstructor
@Slf4j
public class XxxController extends BaseController {

    private final IXxxService xxxService;

    @SaCheckPermission("xxx:list")
    @Log(title = "获取关键词分页列表")
    @PostMapping("/list")
    public TableDataInfo<XxxVo> list(@Validated @RequestBody XxxBo bo, @RequestBody PageQuery pageQuery) {
        return TableDataInfo.build(xxxService.queryPageList(bo, pageQuery));
    }

    @SaCheckPermission("xxxx:query")
    @GetMapping("/{id}")
    public R<XxxVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(xxxService.queryById(id));
    }

    @SaCheckPermission("xxx:add")
    @Log(title = "添加数据", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody XxxBo bo) {
        return toAjax(xxxService.insertByBo(bo));
    }

    @SaCheckPermission("xxx:edit")
    @Log(title = "修改数据", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody XxxBo bo) {
        return toAjax(xxxService.updateByBo(bo));
    }

    @SaCheckPermission("xxx:remove")
    @Log(title = "删除数据", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R<Void> remove(@Validated @RequestBody QyParamLong param) {
        return toAjax(xxxService.deleteWithValidByIds(param.getIdList(), true));
    }

    @Log(title = "获取下拉选项" )
    @PostMapping("getOptions")
    public TableDataInfo<QyModelNameValue> getOptions(@Validated @RequestBody XxxBo bo) {
        return TableDataInfo.build(xxxService.queryOptions(bo));
    }

    @Log(title = "下载导入模板" )
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "导入数据模板", XxxImportBo.class, response);
    }

    @Log(title = "导入数据", businessType = BusinessType.IMPORT)
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file, boolean updateSupport) throws Exception {
        ExcelResult<XxxImportBo> result = ExcelUtil.importExcel(file.getInputStream(), XxxImportBo.class, new XxxImportListener(updateSupport));
        return R.ok(result.getAnalysis());
    }

}
```
