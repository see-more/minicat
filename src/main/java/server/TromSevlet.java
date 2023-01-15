package server;

import java.io.IOException;

public class TromSevlet extends HttpServlet{
    @Override
    public void doGet(Request request, Response response) {
        String content="<h1>trom get</h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        this.doPost(request,response);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }
}
