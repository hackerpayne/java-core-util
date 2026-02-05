package com.qyhstech.spring.thirdparty;

import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.QyAssert;
import com.qyhstech.spring.restclient.interceptor.RestClientLoggingRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * 打开飞书群聊 → 设置 → 群机器人 → 添加机器人 → 选择 自定义机器人。
 */
@Slf4j
public class FeishuBotNotifier {

    private final String WEBHOOK_URL;

    private final RestClient restClient;

    /**
     * 指定Token
     *
     * @param token
     */
    public FeishuBotNotifier(String token) {
        this(token, null);
    }

    /**
     * 注入Token和请求
     *
     * @param token
     * @param restClientBuilder
     */
    public FeishuBotNotifier(String token, RestClient.Builder restClientBuilder) {

        QyAssert.notEmpty(token, "FeiShu Token must not be empty");

        // 构造默认的处理类
        if (restClientBuilder == null) {
            restClientBuilder = RestClient.builder()
                    .requestFactory(new HttpComponentsClientHttpRequestFactory())
                    .requestInterceptor(new RestClientLoggingRequestInterceptor())
            ;
        }
        this.restClient = restClientBuilder.build();

        if (token.startsWith("http")) {
            this.WEBHOOK_URL = token;
        } else {
            this.WEBHOOK_URL = StrUtil.format("https://open.feishu.cn/open-apis/bot/v2/hook/{}", token);
        }
    }

    /**
     * 提定标题和内容，发送异常告警
     *
     * @param title
     * @param content
     */
    public void sendAlert(String title, String content) {
        sendMessage(String.format("【异常告警】\n标题: %s\n详情: %s", title, content));
    }

    /**
     * 发送消息提醒
     *
     * @param content 消息体的内容
     */
    public void sendMessage(String content) {

        Map<String, String> text = new HashMap<>();
        text.put("text", content);

        Map<String, Object> body = new HashMap<>();
        body.put("msg_type", "text");
        body.put("content", text);

        try {
            String response = restClient.post()
                    .uri(WEBHOOK_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            // 可选：记录响应结果
            log.info("飞书通知发送成功: {}", response);
        } catch (Exception e) {
            // 异常处理
            throw new RuntimeException("发送飞书通知失败", e);
        }
    }


}