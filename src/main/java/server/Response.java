package server;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 把对象封装为response对象
 */
@Slf4j
public class Response {
    private OutputStream outputStream;
    public Response(){}
    public Response(OutputStream outputStream){
        this.outputStream=outputStream;
    }

    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }


    /**
     *
     * @param path url根据url来获取静态资源文件
     */
    public void outputHtml(String path) throws IOException {
        String absoluteResourcePath="";
        String absolutePath = StaticResourceUtil.getAbsolutePath(path);
        String substring = absolutePath.substring(1);
        File file=new File(substring);
        if (file.exists() && file.isFile()){
            StaticResourceUtil.outputStaticResource(new FileInputStream(file),outputStream);
            //使用工具类读取文件
        }else {
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }
}
