package test;

import test.RequestParser.RequestInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class MyHTTPServer extends Thread implements HTTPServer{

    private final int port;
    private volatile boolean running;
    private ServerSocket serverSocket;

    private final Map<String, Map<String, Servlet>> servletReqs = new HashMap<>();


    //CTOR
    public MyHTTPServer(int port,int nThreads){
        this.port = port;
        this.running = false;
    }

    @Override
    public void addServlet(String httpCommanmd, String uri, Servlet s){
        //check if valid
        if (httpCommanmd == null || uri == null || s == null)
            return;

        Map<String, Servlet> byUri = servletReqs.get(httpCommanmd);
        if (byUri == null) {
            byUri = new HashMap<>();
            servletReqs.put(httpCommanmd, byUri);
        }

        byUri.put(uri, s);
    }
    @Override
    public void removeServlet(String httpCommanmd, String uri){
        Map<String, Servlet> byUri = servletReqs.get(httpCommanmd);
        if (byUri == null)
            return;

        byUri.remove(uri);

        if (byUri.isEmpty()) {
            servletReqs.remove(httpCommanmd);
        }
    }


    @Override
    public void run(){

        running = true;

        try {
            serverSocket = new ServerSocket(port);
            while (running) {
                Socket client = serverSocket.accept();
                try {
                    handleClient(client);
                } catch (Exception e) {
                }
                finally {
                    try {
                        client.close();
                    } catch (IOException ignored) {}
                }
            }

        } catch (IOException e) {
        }
        finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException ignored) {}
        }
    }
    @Override
    public void close(){
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException ignored) {}
        for (Map<String, Servlet> m : servletReqs.values()) {
            for (Servlet s : m.values()) {
                try {
                    s.close();
                } catch (IOException ignored) {}
            }
        }
        servletReqs.clear();
    }

    private void handleClient(Socket client) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        OutputStream toClient = client.getOutputStream();

        RequestInfo ri = RequestParser.parseRequest(reader);

        Servlet servlet = findBestServlet(ri);

        if (servlet == null) {
            send404(toClient);
            toClient.flush();
            return;
        }

        servlet.handle(ri, toClient);
        toClient.flush();
    }

    private Servlet findBestServlet(RequestInfo ri) {
        String cmd = ri.getHttpCommand();
        String uri = ri.getUri();

        String path = uri;

        int q = uri.indexOf('?');
        if (q != -1) {
            path = uri.substring(0, q);
        }

        Map<String, Servlet> byUri = servletReqs.get(cmd);
        if (byUri == null)
            return null;

        Servlet best = null;
        int bestLen = -1;

        for (Map.Entry<String, Servlet> entry : byUri.entrySet()) {
            String prefix = entry.getKey();
            if (path.startsWith(prefix)) {
                if (prefix.length() > bestLen) {
                    bestLen = prefix.length();
                    best = entry.getValue();
                }
            }
        }

        return best;
    }

    private void send404(OutputStream toClient) throws IOException {
        String msg = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Length: 0\r\n" +
                "\r\n";
        toClient.write(msg.getBytes());
    }

}
