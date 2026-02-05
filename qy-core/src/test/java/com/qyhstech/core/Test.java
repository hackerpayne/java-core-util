package com.qyhstech.core;

import lombok.AllArgsConstructor;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class Test implements Runnable {
    private CompletableFuture<Integer> re = null;

    public void run() {
        int myRe = 0;
        try {
            myRe = re.get() * re.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(myRe);
    }

    public static void main(String[] args) throws InterruptedException {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        new Thread(new Test(future)).start();
        //模拟长时间的计算过程
        Thread.sleep(1000);
        //告知完成结果
        future.complete(60);
    }
}

