package com.qyhstech.spring.controller;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.domain.response.QyResp;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 一些常用的功能封装进来，使用时，继承可用
 * public class TestController extends BaseController
 * 也可以使用QyWebUtil.getRequest()获取，也是线程安全的类
 */
public class QyBaseController {

    /**
     * 定义一个Map,作用是在tController和FeignClientInterceptor(拦截器)中共享HttpServletRequest
     * 使用时：concurrentHashMap.put(Thread.currentThread(),request);
     */
    public static final ConcurrentHashMap<Thread, HttpServletRequest> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    /**
     * 实现线程安全的Request对象取值
     */
    @Resource
    protected HttpServletRequest request;

    /**
     *
     * @param view
     * @return
     */
    public ModelAndView view(String view) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName(view);
        return mv;
    }

    /**
     *
     * @param view
     * @param model
     * @return
     */
    public ModelAndView view(String view, Model model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName(view);
        mv.addAllObjects(model.asMap());
        return mv;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected QyResp toAjax(int rows) {
        return rows > 0 ? QyResp.success() : QyResp.fail();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected QyResp toAjax(boolean result) {
        return result ? QyResp.success() : QyResp.fail();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StrUtil.format("redirect:{}", url);
    }

}
