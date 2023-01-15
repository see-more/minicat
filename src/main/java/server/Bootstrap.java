package server;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Bootstrap {
    private int port=8080;
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void start() throws IOException {
        log.info("Minicat start on port "+ port );
        ServerSocket socket=new ServerSocket(port);
//        while (true){
//            Socket accept = socket.accept();
//            OutputStream outputStream = accept.getOutputStream();
//            String data="Hello MiniCat";
//            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
//            log.info("\n\r"+responseText);
//            outputStream.write(responseText.getBytes());
//            accept.close();
//        }

        while (true){
            Socket accept = socket.accept();
            InputStream inputStream = accept.getInputStream();
            Request request=new Request(inputStream);
            Response response=new Response(accept.getOutputStream());
            log.info(request.getUrl());
            response.outputHtml(request.getUrl());
            accept.close();
        }
    }



    /*
    * minicat的启动入口
    * */
    public static void main(String[] args) throws IOException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start();
    }
}
