
### 条件注解

参考：
https://gitee.com/whzhaochao/validation-plus

使用方法
使用说明
添加@EnableCondition开启validation-plus
添加@NotNullOn、@AssertTrueOn等注解
添加on = "#noticeType==0" el表达式
```java

@Data
@EnableCondition
public class UserNotice {
    @NotNull(message = "通知类型不能为空 0-短信 1-邮件")
    private Integer noticeType;
    
    @NotNullOn(on = "#noticeType==0", message = "手机号码不能为空")
    private String userMobile;
    
    @NotNullOn(on = "#noticeType==1", message = "邮箱不能为空")
    private String userEmail;
    
    @AssertTrueOn(on = "#noticeType==0", message = "必须是男生")
    private Boolean isBoy;
}
```

### TTL参考 多线程使用

https://github.com/alibaba/transmittable-thread-local
https://github.com/Dreamroute/ttl

ThreadPoolTaskExecutor支持TTL
https://github.com/alibaba/transmittable-thread-local/issues/173

相关学习文章：
https://github.com/alibaba/transmittable-thread-local/issues/123

### QyColl玩法
1、场景：查出来的list里面，我还需要遍历List，再查出对应的子结构，再装回List。
```
// 查出来的Product里面，是没有库存信息的，需要查出来库存信息。再回填数据。
List<ProductVo> dataList = baseMapper.selectVoList(lqw);

// 遍历List，执行findStockList，再把结果setStockList中，使用指定的线程池执行。
QyColl.listProcessor(dataList, this::findStockList, ProductVo::setStockList, mallTaskExecutor);
```

其中这个findStockList方法为：
```
public List<MallStockVO> findStockList(ProductVo entity) {
    List<MallStockEntity> stockList = stockManager.findByProductId(entity.getId());
    return MallStockConvert.INSTANCE.convertList(stockList);
}
```