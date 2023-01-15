package server;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
        while (true){
            ServerSocket socket=new ServerSocket(port);
            Socket accept = socket.accept();
            OutputStream outputStream = accept.getOutputStream();
            outputStream.write("Hello MiniCat".getBytes());
            socket.close();
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
