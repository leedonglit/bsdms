package com.isec;

import twitter4j.*;

public class TwitterCrawler {

    public static void main(String[] args) {
        // 配置Twitter
        Twitter twitter = twitter = Twitter.newBuilder()
                .oAuthConsumer("8OpaHI4j7rHBgmbzCAmkrobC3", "WS8eKEMTrwJ5c8xZppyVt0AYhASIUJwXMxhF8NSfzFVIfYTwiE")
                .oAuthAccessToken("1839531914929086464-jYlJlSxn6ZSgox8UO0vTBh0QeeJhxZ", "9cDGE9s9pVjtQGLXzNEpyVOP4VxBFde1jJ1HwyHTTtCoh")
                .build();
        try {
            // 搜索马斯克的用户ID
            long userId = twitter.v1().users().showUser("elonmusk").getId();
            System.out.println(userId);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
