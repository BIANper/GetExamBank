import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class YuClient {

    public static String getExamJson(String infoUrl) {

        //获得 leId 和 tokenLeId
        String s = sendGet(infoUrl);
        int i = s.indexOf("var leid = ");
        String leId = s.substring(i + 12, i + 21);
        int j = s.indexOf("var vp4tokenleid = ");
        String tokenLeId = s.substring(j + 20, j + 52);
        //生成json
        String tempUrl = "http://www.examcoo.com/editor/rpc/getreexamcontent/leid/"+ leId + "/tokenleid/" + tokenLeId;
        String reExam = sendGet(tempUrl);
        JSONObject reExamObj = JSON.parseObject(reExam);
        JSONArray questionArray = reExamObj.getJSONArray("b");
        int size = questionArray.size()-2;
        StringBuilder stringBuilder = new StringBuilder("{\"leid\":\""+leId+"\",\"tokenleid\":\""+tokenLeId+"\",\"data\":[{\"id\":\"a\",\"a\":\"\",\"b\":\"\",\"c\":\"user\"},{\"id\":\"b\",\"c\":[");
        for (int count = 1; count <= size; count++) {
            stringBuilder.append("{\"a\":\"0\"}");
            if (count!=size) stringBuilder.append(",");
        }
        stringBuilder.append("]}]}");
        String param = stringBuilder.toString();
        //提交json
        String saveUrl = "https://www.examcoo.com/editor/rpc/saveexam";
        sendPost(saveUrl,param);
        //获取
        String jsonUrl = "http://www.examcoo.com/editor/rpc/getexamcontent/leid/"+ leId + "/tokenleid/" + tokenLeId;
        return sendGet(jsonUrl);
    }
    public static String sendGet(String url) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            URL connURL = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            httpConn.connect();
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            httpConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static void sendPost(String url, String param) {
        byte[] paramBytes = param.getBytes(StandardCharsets.UTF_8);
        BufferedReader in = null;
        try {
            URL connURL = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConn.setRequestProperty("Content-Length", String.valueOf(paramBytes.length));
            httpConn.connect();
            OutputStream out = httpConn.getOutputStream();
            out.write(paramBytes);
            out.flush();
            out.close();
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8));
            while (in.readLine() != null);
            httpConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
