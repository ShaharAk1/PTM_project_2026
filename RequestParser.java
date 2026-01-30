package test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
		//create a RequestInfo variable using the BufferedReader we got
        String firstLine = reader.readLine();
        if (firstLine == null || firstLine.isEmpty()) {
            return new RequestInfo("", "", new String[0], new HashMap<>(), new byte[0]);
        }
        String[] reqInfoArray = firstLine.split(" "); // ["GET", "/api/resource?id=123&name=test", "HTTP/1.1"]

        String uri = reqInfoArray[1];

        int queryIndex = uri.indexOf("?");
        String pathOnly = "";
        String paramsOnly = "";

        if (queryIndex != -1) {
            pathOnly = uri.substring(0, queryIndex); //get uri segments
            paramsOnly = uri.substring(queryIndex+1); //get uri parameters
        } else {
            pathOnly = uri;
        }

        //separate to segments
        String[] uriSegments = Arrays.stream(pathOnly.split("/"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        Map<String, String> params = new HashMap<>();
        if (!paramsOnly.isEmpty()) {
            String[] paramsArray = paramsOnly.split("&");
            //split and put parameters in a map
            for(String par : paramsArray){
                int eqIndex = par.indexOf("=");
                if(eqIndex != -1){
                    params.put(par.substring(0, eqIndex), par.substring(eqIndex + 1));
                }
            }
        }

        String line = reader.readLine();
        while (line != null && !line.isEmpty()) {
            line = reader.readLine();
        }
        if (!reader.ready()) {
            return new RequestInfo(reqInfoArray[0], uri, uriSegments, params, new byte[0]);
        }

        line = reader.readLine();
        while (line != null && !line.isEmpty()) {
            int eq = line.indexOf("=");
            if (eq != -1) {
                params.put(line.substring(0, eq).trim(), line.substring(eq + 1).trim());
            }
            if (!reader.ready()) {
                return new RequestInfo(reqInfoArray[0], uri, uriSegments, params, new byte[0]);
            }
            line = reader.readLine();
        }

        if (!reader.ready()) {
            return new RequestInfo(reqInfoArray[0], uri, uriSegments, params, new byte[0]);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        line = reader.readLine();
        while (line != null && !line.isEmpty()) {
            out.write(line.getBytes(StandardCharsets.UTF_8));
            out.write('\n');
            if (!reader.ready()) break;
            line = reader.readLine();
        }

        byte[] content = out.toByteArray();
        return new RequestInfo(reqInfoArray[0], uri, uriSegments, params, content);
    }
	
	// RequestInfo given internal class
    public static class RequestInfo {
        private final String httpCommand;
        private final String uri;
        private final String[] uriSegments;
        private final Map<String, String> parameters;
        private final byte[] content;

        public RequestInfo(String httpCommand, String uri, String[] uriSegments, Map<String, String> parameters, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameters = parameters;
            this.content = content;
        }

        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
