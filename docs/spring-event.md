# Spring Event 事件订阅发布工具类

## 概述

`QyEvent` 工具类为 Spring Boot 应用程序提供了一个基于泛型的事件发布订阅机制。该工具基于 Spring Framework 的事件发布机制，提供了更强的类型安全性和更好的泛型支持。

### 核心特性

- **泛型支持**: 通过泛型事件类实现类型安全的事件传递
- **自动类型解析**: 集成 Spring 的 `ResolvableTypeProvider`，实现自动类型解析
- **简单易用**: 简洁的 API 设计，易于理解和使用
- **Spring 原生集成**: 基于 Spring Framework 的事件机制，与 Spring 生态系统完美集成

---

## 核心组件

### 1. QyEvent\<T\> - 泛型事件类

**位置**: `qy-spring/src/main/java/com/qyhstech/spring/event/QyEvent.java`

**类定义**:

```java
public class QyEvent<T> extends ApplicationEvent implements ResolvableTypeProvider
```

**核心功能**:

- 继承自 Spring 的 `ApplicationEvent`，提供基础事件能力
- 实现 `ResolvableTypeProvider` 接口，支持泛型类型解析
- 携带泛型数据 `data`，实现类型安全的数据传递
- 支持两种构造函数方式

**构造函数**:

```java
// 方式1: 使用数据作为事件源
public QyEvent(T data)

// 方式2: 显式指定事件源和数据
public QyEvent(Object source, T data)
```

**关键方法**:

- `getResolvableType()`: 自动解析泛型类型，支持精确的事件监听

### 2. QyEventService - 事件服务接口

**位置**: `qy-spring/src/main/java/com/qyhstech/spring/event/QyEventService.java`

**接口定义**:

```java
public interface QyEventService {
    <T> void publishEvent(QyEvent<T> qyEvent);
}
```

**核心方法**:

- `publishEvent(QyEvent<T> qyEvent)`: 发布泛型事件到 Spring 应用上下文

### 3. QySpringEventServiceImpl - 事件服务实现类

**位置**: `qy-spring/src/main/java/com/qyhstech/spring/event/impl/QySpringEventServiceImpl.java`

**实现特性**:

- 依赖注入 `ApplicationContext`，使用 Spring 容器发布事件
- 实现 `QyEventService` 接口，提供标准化的事件发布服务
- 直接委托给 Spring 的 `applicationContext.publishEvent()` 方法

**使用方式** (建议启用):

```java
// 在配置类中添加 @Component 注解或通过 @Bean 注入
@Component
public class QySpringEventServiceImpl implements QyEventService {
    // ... 实现代码
}
```

---

## 使用方法

### 步骤 1: 定义事件类

根据业务需求，创建具体的事件类继承 `QyEvent<T>`:

```java
import com.qyhstech.spring.event.QyEvent;

/**
 * 用户注册事件
 */
public class UserRegisterEvent extends QyEvent<UserDTO> {
    public UserRegisterEvent(UserDTO userData) {
        super(userData);
    }
}

/**
 * 订单创建事件
 */
public class OrderCreateEvent extends QyEvent<OrderDTO> {
    public OrderCreateEvent(OrderDTO orderData) {
        super(orderData);
    }

    // 也可以使用指定事件源的构造方式
    public OrderCreateEvent(Object source, OrderDTO orderData) {
        super(source, orderData);
    }
}
```

### 步骤 2: 创建事件监听器

使用 Spring 的 `@EventListener` 注解创建事件监听器:

```java
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    @EventListener
    public void handleUserRegisterEvent(UserRegisterEvent event) {
        UserDTO userData = event.getData();
        // 处理用户注册后续逻辑，如：
        // - 发送欢迎邮件
        // - 记录日志
        // - 更新统计数据
        System.out.println("处理用户注册事件: " + userData.getUsername());
    }

    @EventListener
    public void handleOrderCreateEvent(OrderCreateEvent event) {
        OrderDTO orderData = event.getData();
        // 处理订单创建后续逻辑
        System.out.println("处理订单创建事件: " + orderData.getOrderId());
    }
}
```

**基于条件的监听**:

```java
@EventListener
public void handleHighValueOrder(OrderCreateEvent event) {
    OrderDTO order = event.getData();
    if (order.getAmount() > 10000) {
        // 只处理高价值订单
        System.out.println("高价值订单预警: " + order.getOrderId());
    }
}
```

### 步骤 3: 发布事件

通过 `QyEventService` 发布事件:

```java
import com.qyhstech.spring.event.QyEvent;
import com.qyhstech.spring.event.QyEventService;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final QyEventService eventService;

    public UserService(QyEventService eventService) {
        this.eventService = eventService;
    }

    public void registerUser(UserDTO user) {
        // 1.执行业务逻辑
        // saveUserToDatabase(user);

        // 2.发布事件
        UserRegisterEvent event = new UserRegisterEvent(user);
        eventService.publishEvent(event);
    }

    public void createOrder(OrderDTO order) {
        // 1.执行业务逻辑
        // saveOrderToDatabase(order);

        // 2.发布事件
        OrderCreateEvent event = new OrderCreateEvent(order);
        eventService.publishEvent(event);
    }
}
```

### 步骤 4: 配置类设置 (可选)

如果需要手动配置 `QySpringEventServiceImpl`:

```java
import com.qyhstech.spring.event.QyEventService;
import com.qyhstech.spring.event.impl.QySpringEventServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    public QyEventService qyEventService(ApplicationContext applicationContext) {
        return new QySpringEventServiceImpl(applicationContext);
    }
}
```

---

## 完整示例

### 示例 1: 用户注册流程

**1. 定义事件类**:

```java
// UserRegisterEvent.java
public class UserRegisterEvent extends QyEvent<UserDTO> {
    public UserRegisterEvent(UserDTO userData) {
        super(userData);
    }
}
```

**2. 创建多个监听器**:

```java
// WelcomeEmailListener.java
@Component
public class WelcomeEmailListener {

    @EventListener
    public void sendWelcomeEmail(UserRegisterEvent event) {
        UserDTO user = event.getData();
        emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());
    }
}

// AuditLogListener.java
@Component
public class AuditLogListener {

    @EventListener
    public void logUserRegistration(UserRegisterEvent event) {
        UserDTO user = event.getData();
        auditService.log("USER_REGISTERED", user.getId(), user.getUsername());
    }
}

// StatisticsListener.java
@Component
public class StatisticsListener {

    @EventListener
    public void updateUserCount(UserRegisterEvent event) {
        statisticsService.incrementUserCount();
    }
}
```

**3. 发布事件**:

```java
// UserService.java
@Service
public class UserService {

    @Autowired
    private QyEventService eventService;

    public void register(UserDTO user) {
        // 保存用户到数据库
        userRepository.save(user);

        // 发布事件，触发后续处理
        eventService.publishEvent(new UserRegisterEvent(user));
    }
}
```

### 示例 2: 异步事件处理

如果事件处理是耗时的，可以使用异步处理:

```java
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncEventHandler {

    @Async
    @EventListener
    public void handleUserRegisterAsync(UserRegisterEvent event) {
        // 异步处理，不会阻塞主流程
        UserDTO user = event.getData();
        // 发送邮件、分析用户行为等耗时操作
        processUserBehaviorAnalysis(user);
    }
}
```

**配置异步支持**:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Event-");
        executor.initialize();
        return executor;
    }
}
```

---

## 最佳实践

### 1. 事件设计原则

- **单一职责**: 每个事件类只负责一种业务场景
- **不可变性**: 事件对象应该是不可变的，避免在传递过程中被修改
- **命名规范**: 事件类名使用 `业务动作 + Event` 的格式

```java
// ✅ 好的设计
public class UserRegisterEvent extends QyEvent<UserDTO>
public class OrderPaidEvent extends QyEvent<OrderDTO>

// ❌ 不好的设计
public class UserEvent extends QyEvent<Object>  // 过于泛化
public class SomethingHappenedEvent extends QyEvent<Data>  // 语义不明确
```

### 2. 事件监听器建议

- **保持监听器轻量**: 避免在监听器中执行耗时操作
- **异常处理**: 妥善处理监听器中的异常，避免影响其他监听器
- **使用异步**: 对于非关键路径的逻辑，使用异步处理

```java
@Component
public class RobustEventListener {

    @EventListener
    public void handleEvent(UserRegisterEvent event) {
        try {
            // 处理逻辑
            process(event.getData());
        } catch (Exception e) {
            // 记录异常，但不要抛出，避免影响其他监听器
            logger.error("处理事件失败", e);
            // 可以选择发送到死信队列或错误报告系统
        }
    }
}
```

### 3. 事务处理

**事件发布时机**:

- **事务提交后发布**: 如果事件处理依赖于事务成功，使用 `@TransactionalEventListener`

```java
@TransactionalEventListener
public void handleUserRegisterCommitted(UserRegisterEvent event) {
    // 只有在事务成功提交后才会执行
    sendWelcomeEmail(event.getData());
}
```

### 4. 性能考虑

- **避免过多监听器**: 一个事件关联过多监听器会影响性能
- **使用异步处理**: 对于不影响主流程的操作，使用异步
- **合理设计事件粒度**: 避免过细粒度的事件导致频繁发布

---

## 注意事项

1. **同步执行**: 默认情况下，事件监听器与事件发布在同一个线程中同步执行
2. **事务边界**: 事件发布通常在事务边界内，需要注意事务提交/回滚对事件处理的影响
3. **内存泄漏**: 避免监听器持有大量内存，使用完毕后及时清理
4. **顺序保证**: Spring 不保证多个监听器的执行顺序，如有顺序要求需自行处理

---

## 相关资源

- [Spring Framework Event Handling](https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events)
- [ApplicationEvent](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationEvent.html)
- [@EventListener](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/event/EventListener.html)

---

## 版本信息

- **框架版本**: Spring Boot 3.2.0+
- **Java 版本**: Java 21+
- **最后更新**: 2025-12-02