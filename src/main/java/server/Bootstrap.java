package server;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class Bootstrap {
    private int port=8080;
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void start() throws Exception {

        loadServlet();

        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );

        log.info("Minicat start on port " + port);
        ServerSocket socket = new ServerSocket(port);
//        while (true){
//            Socket accept = socket.accept();
//            OutputStream outputStream = accept.getOutputStream();
//            String data="Hello MiniCat";
//            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
//            log.info("\n\r"+responseText);
//            outputStream.write(responseText.getBytes());
//            accept.close();
//        }
//
//        while (true){
//            Socket accept = socket.accept();
//            InputStream inputStream = accept.getInputStream();
//            Request request=new Request(inputStream);
//            Response response=new Response(accept.getOutputStream());
//            log.info("路径是===============》"+request.getUrl());
//            if (servletMap.get(request.getUrl())==null){
//                response.outputHtml(request.getUrl());
//            }else {
//                HttpServlet httpServlet = servletMap.get(request.getUrl());
//                httpServlet.service(request,response);
//            }
//            accept.close();
//        }
        //多线程改造(不使用线程池的情况下)
//        while (true) {
//            Socket accept = socket.accept();
//            RequestsProcessor requestsProcessor = new RequestsProcessor(accept, servletMap);
//            requestsProcessor.start();
//        }
        //使用线程池
        while (true) {
            Socket accept = socket.accept();
            RequestsProcessor requestsProcessor = new RequestsProcessor(accept, servletMap);
            threadPoolExecutor.execute(requestsProcessor);
//            requestsProcessor.start();
        }
    }
    private Map<String , HttpServlet> servletMap=new HashMap<>();
    /*
    加载接卸web.xml初始化
    */
    private void loadServlet(){
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document=saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.selectNodes("//servlet");
            for (int i = 0; i < list.size(); i++) {
                Element element=list.get(i);
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName=servletnameElement.getStringValue();
                Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass=servletClassElement.getStringValue();
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='"+servletName+"']");
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
                log.info("动态资源映射成功");
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
    /*
    * minicat的启动入口
    * */
    public static void main(String[] args) throws IOException {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
