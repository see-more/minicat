package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticResourceUtil {
    /**
     * 获取绝对路径
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path){
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        absolutePath=absolutePath.substring(0, absolutePath.length()-1);
        return absolutePath.replaceAll("\\\\","/")+path;
    }

    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count=0;
        while (count==0){
            count=inputStream.available();
        }
        int resourceSize=count;
        outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes());
        long written = 0 ;//已经读取的内容长度
        int byteSize=1024;
        byte[] bytes=new byte[byteSize];
        while (written< resourceSize){
            if (written+byteSize>resourceSize){
                byteSize= (int) (resourceSize-written);
                bytes=new byte[byteSize];
            }
            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            written+=byteSize;
        }

    }
}
