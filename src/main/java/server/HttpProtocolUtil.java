package server;

/**
 * http工具类 主要是提供响应头 这里只提供200 和 404
 */
public class HttpProtocolUtil {
    /**
     * 为响应码200提供请求头
     * @return
     */
    public static String getHttpHeader200(long contentLength){
        return "HTTP/1.1 200 OK \n"+
                "Context-Type: text/html \n"+
                "Context-Length:"+contentLength+"\n"+
                "\r\n";

    }
    /*
        为404提供响应头
    */
    public static String getHttpHeader404(){
        String str404="<h1>404 NOT FOUND</h1>";
        return "HTTP/1.1 404 NOT Found \n"+
                "Context-Type: text/html \n"+
                "Context-Length:"+str404.getBytes().length+"\n"+
                "\r\n"+str404;
    }

}
