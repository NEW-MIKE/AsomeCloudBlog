package com.kaya.wifitransform.wifiTransfer;
import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Wifi传书 服务端
 *
 * @author yuyh.
 * @date 2016/10/10.
 */
public class SimpleFileServer extends NanoHTTPD {

    private static SimpleFileServer server;

    public static SimpleFileServer getInstance() {
        if (server == null) {
            server = new SimpleFileServer(Defaults.getPort());
        }
        return server;
    }

    public SimpleFileServer(int port) {
        super(port);
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms,
                          Map<String, String> files) {
        if (Method.GET.equals(method)) {
            try {
                uri = new String(uri.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }

            return new Response(Defaults.HTML_STRING);
        } else {
            return new Response("");
        }
    }
}
