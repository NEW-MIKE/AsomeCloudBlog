package com.kaya.wifitransform.wifiTransfer;

import java.io.IOException;

/**
 * Wifi传书 服务端
 *
 * @author yuyh.
 * @date 2016/10/10.
 */
public class ServerRunner {

    private static SimpleFileServer server;
    public static boolean serverIsRunning = false;

    public static SimpleFileServer  startServer() {
        server = SimpleFileServer.getInstance();
        try {
            if (!serverIsRunning) {
                server.start();
                serverIsRunning = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return server;
    }

    public static void stopServer() {
        if (server != null) {
            server.stop();
            serverIsRunning = false;
        }
    }
}