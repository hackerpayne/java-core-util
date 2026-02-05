package com.qyhstech.spring.thirdparty;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeishuBotNotifierTest {

    @Test
    void sendAlert() {
        String token = "https://open.feishu.cn/open-apis/bot/v2/hook/";
        FeishuBotNotifier feishuBotNotifier = new FeishuBotNotifier(token);
        feishuBotNotifier.sendAlert("Test", "发生异常了，请检查");
    }

    @Test
    void sendMessage() {
        String token = "https://open.feishu.cn/open-apis/bot/v2/hook/";
        FeishuBotNotifier feishuBotNotifier = new FeishuBotNotifier(token);
        feishuBotNotifier.sendMessage("发生异常了，请检查一下");
    }
}