package test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
		//create a RequestInfo variable using the BufferedReader we got
        String request = reader.readLine(); //"GET /api/resource?id=123&name=test HTTP/1.1"
        String[] reqInfoArray = request.split(" "); // ["GET", "/api/resource?id=123&name=test", "HTTP/1.1"]

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


        //find content length
        int contentLength = 0;
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }




        //skip empty lines
        while ((line = reader.readLine()) != null && line.isEmpty()) { }

        //if the line has = add to parameters
        if (line != null && line.contains("=")) {
            int eqIndex = line.indexOf("=");
            params.put(line.substring(0, eqIndex), line.substring(eqIndex + 1));

            line = reader.readLine();
        }

        //content
        String line1 = reader.readLine();
        byte[] bodyBytes;
        if (line1 != null) {
            bodyBytes = (line1 + "\n").getBytes("UTF-8");
        } else {
            bodyBytes = new byte[0];
        }


        //RequestInfo
        return new RequestInfo(reqInfoArray[0], uri, uriSegments, params, bodyBytes);
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
