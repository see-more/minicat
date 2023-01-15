package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestsProcessor extends Thread {
    private Socket socket;
    private Map<String, HttpServlet> servletMap;

    public RequestsProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            if (servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            } else {
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request, response);
            }
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
