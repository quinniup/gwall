package org.hig.quinn;

import org.hig.quinn.server.ImServer;

/**
 * @author ZhihaoQuinn
 * @since 2020/9/14
 */
public class ServerApplication {

    public static void main(String[] args) {
        int port = 2222;
        new Thread(() -> {
            new ImServer().start(port);
        }).start();
    }
}
