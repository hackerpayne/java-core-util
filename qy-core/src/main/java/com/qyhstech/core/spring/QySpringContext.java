package com.qyhstech.core.spring;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.QyStr;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.context.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 自定义Spring工具类
 * 普通类调用Spring bean对象：
 * 1、此类需要放到Application.java同包或者子包下才能被扫描，否则失效。
 * <p>
 * 2、使用时：
 *
 * @Bean public SpringUtilspringUtil2(){return new SpringContextUtils();}
 * 3、如果不在SpringBoot的扫描包下面：
 * @Import(value={SpringContextUtils.class}) 此时：SpringUtil是不需要添加@Component注解
 * <p>
 * 4、XML方式需要加入：<bean id="springContextUtil" class="com.kyle.SpringContextUtils" singleton="true" />
 * 然后使用时： ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml"); 注入
 */
@Component
//@Lazy(false)
@Slf4j
public class QySpringContext implements BeanFactoryPostProcessor, ApplicationContextAware, DisposableBean, Serializable {

    private static ApplicationContext applicationContext;

    /**
     * Spring应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;

    private static boolean addCallback = true;

    /**
     * 回调列表
     */
    private static final List<CallBack> CALL_BACKS = new ArrayList<>();

    /**
     * 获取ApplicationContext对象
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取BeanFactory
     *
     * @return
     */
    public static ListableBeanFactory getBeanFactory() {
        ListableBeanFactory factory = null == beanFactory ? applicationContext : beanFactory;
        if (null == factory) {
            throw new UtilException("QySpringContext No ConfigurableListableBeanFactory or ApplicationContext injected, maybe not in the Spring environment?");
        } else {
            return (ListableBeanFactory) factory;
        }
    }

    /**
     * @return
     * @throws UtilException
     */
    public static ConfigurableListableBeanFactory getConfigurableBeanFactory() throws UtilException {
        ConfigurableListableBeanFactory factory;
        if (null != beanFactory) {
            factory = beanFactory;
        } else {
            if (!(applicationContext instanceof ConfigurableApplicationContext)) {
                throw new UtilException("No ConfigurableListableBeanFactory from context!");
            }

            factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        }

        return factory;
    }


    /**
     * 设置容器
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (QySpringContext.applicationContext != null) {
            log.warn("QySpringContext中的ApplicationContext被覆盖, 原有ApplicationContext为:" + QySpringContext.applicationContext);
        }

        QySpringContext.applicationContext = applicationContext;
        if (addCallback) {
            for (CallBack callBack : QySpringContext.CALL_BACKS) {
                callBack.executor();
            }
            CALL_BACKS.clear();
        }
        QySpringContext.addCallback = false;
    }

    /**
     * 针对 某些初始化方法，在SpringContextHolder 未初始化时 提交回调方法。
     * 在SpringContextHolder 初始化后，进行回调使用
     *
     * @param callBack 回调函数
     */
    public synchronized static void addCallBacks(CallBack callBack) {
        if (addCallback) {
            QySpringContext.CALL_BACKS.add(callBack);
        } else {
            log.warn("CallBack：{} 已无法添加！立即执行", callBack.getCallBackName());
            callBack.executor();
        }
    }

    /**
     * 根据bean的名称获取bean
     *
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 根据bean的class来查找对象
     *
     * @param <T>
     * @param c
     * @return
     */
    public static <T> T getBean(Class<T> c) {
        return beanFactory.getBean(c);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return beanFactory.getBean(name, clazz);
    }

    /**
     * @param reference
     * @param <T>
     * @return
     */
    public static <T> T getBean(TypeReference<T> reference) {
        ParameterizedType parameterizedType = (ParameterizedType) reference.getType();
        Class<T> rawType = (Class) parameterizedType.getRawType();
        Class<?>[] genericTypes = (Class[]) Arrays.stream(parameterizedType.getActualTypeArguments()).map((type) -> {
            return (Class) type;
        }).toArray((x$0) -> {
            return new Class[x$0];
        });
        String[] beanNames = getBeanFactory().getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
        return getBean(beanNames[0], rawType);
    }

    /**
     * 根据bean的class来查找所有的对象（包括子类）
     *
     * @param <T>
     * @param c
     * @return
     */
    public static <T> Map<String, T> getBeans(Class<T> c) {
        return beanFactory.getBeansOfType(c);
    }

    /**
     * 是否包含Bean
     *
     * @param name
     * @return
     */
    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(String name) {
        return beanFactory.isSingleton(name);
    }

    /**
     * 获取Bean的类型
     *
     * @param name
     * @return
     */
    public static Class<?> getType(String name) {
        return beanFactory.getType(name);
    }


    /**
     * 获取Bean的别名,如果给定的bean名字在bean定义中有别名
     *
     * @param name
     * @return
     */
    public static String[] getAliases(String name) {
        return beanFactory.getAliases(name);
    }

    /**
     * 获取aop代理对象
     *
     * @param invoker
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }

    /**
     * 国际化使用
     *
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        return applicationContext.getMessage(key, null, Locale.getDefault());
    }

    /**
     * 国际化时使用
     *
     * @param code
     * @param args
     * @param defaultMessage
     * @param locale
     * @return
     */
    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return applicationContext.getMessage(code, args, defaultMessage, locale);
    }

    /**
     * 国际化时使用
     *
     * @param code
     * @param args
     * @param locale
     * @return
     */
    public static String getMessage(String code, Object[] args, Locale locale) {
        return applicationContext.getMessage(code, args, locale);
    }

    /**
     * 国际化时使用
     *
     * @param resolvable
     * @param locale
     * @return
     */
    public static String getMessage(MessageSourceResolvable resolvable, Locale locale) {
        return applicationContext.getMessage(resolvable, locale);
    }

    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前环境
     *
     * @return
     */
    public static String getActiveProfile() {
        String[] activeProfiles = getActiveProfiles();
        return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : StrUtil.EMPTY;
    }

    /**
     * 获取环境信息
     *
     * @return Environment
     */
    public static Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

    /**
     * 获取应用程序名称
     *
     * @return
     */
    public static String getApplicationName() {
        Environment env = applicationContext.getEnvironment();
        return env.getProperty("spring.application.name");
    }

    public static String getProperty(String key) {
        return null == applicationContext ? null : applicationContext.getEnvironment().getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return null == applicationContext ? null : applicationContext.getEnvironment().getProperty(key, defaultValue);
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return null == applicationContext ? null : applicationContext.getEnvironment().getProperty(key, targetType, defaultValue);
    }


    /**
     * 获取应用程序端口
     *
     * @return
     */
    public static String getServerPort() {
        Environment env = applicationContext.getEnvironment();
        return env.getProperty("server.port");
    }

    /**
     * 获取上下文的路径
     *
     * @return
     */
    public static String getContextPath() {
        Environment env = applicationContext.getEnvironment();
        return env.getProperty("server.servlet.context-path");
    }

    /**
     * 获取当前环境的Active Profile即活动的配置
     *
     * @return
     */
    public static String getEnvStr() {
        return getActiveProfile();
    }

    /**
     * 判断是否是指定的环境列表
     *
     * @param envList
     * @return
     */
    public static boolean isEnv(String... envList) {
        String profile = getActiveProfile();
        return List.of(envList).contains(profile);
    }

    /**
     * 发布事件
     *
     * @param event
     */
    public static void publishEvent(ApplicationEvent event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }
    }

    /**
     * 发布事件
     *
     * @param event
     */
    public static void publishEvent(Object event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }
    }

    /**
     * 以单例模式注入一个实体，比如：
     * registerSingleton("foo", new Queue("foo"))
     *
     * @param name
     * @param obj
     */
    public static void registerSingleton(String name, Object obj) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        configurableApplicationContext.getBeanFactory().registerSingleton(name, obj);
    }

    public static <T> void registerBean(String beanName, T bean) {
        ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        factory.autowireBean(bean);
        factory.registerSingleton(beanName, bean);
    }

    public static void unregisterBean(String beanName) {
        ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        if (factory instanceof DefaultSingletonBeanRegistry) {
            DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) factory;
            registry.destroySingleton(beanName);
        } else {
            throw new UtilException("Can not unregister bean, the factory is not a DefaultSingletonBeanRegistry!");
        }
    }


    /**
     * 获取HttpServletRequest
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    /**
     * 获取HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }

    /**
     * 获取控制器里面的所有链接
     *
     * @return
     */
    public static Map<RequestMappingInfo, HandlerMethod> getControllerUrlMap() {
        //        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class); //获取url与类和方法的对应信息，用Swagger时可能出错。重复获取
        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping"); //获取url与类和方法的对应信息
        return mapping.getHandlerMethods();
    }

//    /**
//     * 获取控制器里面的所有链接
//     *
//     * @return
//     */
//    public static List<QyRequestMethodItem> getControllerUrls() {
//        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping"); //获取url与类和方法的对应信息
//        return QyController.getAllUrls(mapping);
//    }


    @Override
    public void destroy() {
        QySpringContext.cleanContext();
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanContext() {
        log.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        applicationContext = null;
    }

    /**
     * 检查注入状态
     */
    private static void checkApplicationContext() {
        if (null == applicationContext) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }

    /**
     * 利用日志打印基本项目信息
     */
    public static void printInfo() {
        String host = "localhost";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("printInfo找不到本地主机名", e);
        }

        Environment env = getEnvironment();
        String port = env.getProperty("server.port");
        port = StrUtil.isNotBlank(port) ? port : "";

        String path = env.getProperty("server.servlet.context-path");
        String appName = env.getProperty("spring.application.name");

        String logInfo = "\n----------------------------------------------------------\n" +
                "Application: \t[ " + appName + " ] Works Fine!\n" +
                "Local: \thttp://localhost:" + StrUtil.emptyIfNull(port) + StrUtil.emptyToDefault(path, "/") + "\n" +
                "External: \thttp://" + host + ":" + StrUtil.emptyIfNull(port) + StrUtil.emptyToDefault(path, "/") + "\n" +
                "Doc: \thttp://" + host + ":" + StrUtil.emptyIfNull(port) + StrUtil.emptyToDefault(path, "/") + "doc.html\n" +
                "Profile: \t" + getEnvStr() + "\n" +
                "----------------------------------------------------------";

        log.info(QyStr.paddingBySemiColon(logInfo));
    }

    /**
     * 获取配置文件中的值
     *
     * @param key 配置文件的key
     * @return 当前的配置文件的值
     */
    public static String getRequiredProperty(String key) {
        return applicationContext.getEnvironment().getRequiredProperty(key);
    }

    /**
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        QySpringContext.beanFactory = beanFactory;
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param defaultValue 默认值
     * @param requiredType 返回类型
     * @return /
     */
    public static <T> T getProperties(String property, T defaultValue, Class<T> requiredType) {
        T result = defaultValue;
        try {
            result = getBean(Environment.class).getProperty(property, requiredType);
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property 属性key
     * @return /
     */
    public static String getProperties(String property) {
        return getProperties(property, null, String.class);
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param requiredType 返回类型
     * @return /
     */
    public static <T> T getProperties(String property, Class<T> requiredType) {
        return getProperties(property, null, requiredType);
    }

    /**
     * 获取 @Service 的所有 bean 名称
     *
     * @return
     */
    public static List<String> getAllServiceBeanName() {
        return new ArrayList<>(Arrays.asList(applicationContext.getBeanNamesForAnnotation(Service.class)));
    }

    /**
     * 判断是否支持虚拟线程
     *
     * @return
     */
    public static boolean isVirtual() {
        return Threading.VIRTUAL.isActive(getBean(Environment.class));
    }
}
