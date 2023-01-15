package server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * 把请求封装为Request对象
 */
@Slf4j
public class Request {
    private String method;
    private String url;
    private InputStream inputStream;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public Request(){

    }
    public Request(InputStream inputStream) throws IOException {
        this.inputStream=inputStream;
        int count = 0;
        while (count==0){
            count= inputStream.available();
        }
        byte[] bytes=new byte[count];
        int read = inputStream.read(bytes);
        String inputStr = new String(bytes);
        String firstLine = inputStr.split("\r\n")[0];
        log.info(firstLine);
        String[] strings= firstLine.split(" ");
        this.method=strings[0];
        this.url=strings[1];
        log.info("请求方式为"+method);
        log.info("请求的路径是"+url);
    }
}
