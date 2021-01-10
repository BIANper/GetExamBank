import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class YuJson {

    public static void main(String[] args) {

        File outFile = new File("exam_result.txt");
        StringBuilder sb = new StringBuilder();

        ArrayList<String> urlList = readUrl();
        for (String url : urlList) {
            String examJson = YuClient.getExamJson(url);
            JSONObject examObj = JSON.parseObject(examJson);
            JSONArray questionArray = examObj.getJSONArray("c");

            for (int queNum = 2; queNum < questionArray.size(); queNum++) {
                JSONObject question = (JSONObject) questionArray.get(queNum);
                sb.append(queNum-1).append(".");
                String description = (String)question.get("a");
                description = description.replace("&nbsp; ","");
                sb.append(description).append("\r\n");
                JSONArray optionArray = question.getJSONArray("b");
                char optNum = 65;
                for (Object oObj : optionArray) {
                    JSONObject option = (JSONObject) oObj;
                    sb.append(optNum).append(".").append((String) option.get("o")).append("\r\n");
                    optNum++;
                }
                String answer = (String)question.get("c");
                sb.append(answerToEn(answer)).append("\r\n\r\n");
                try {
                    Writer out = new FileWriter(outFile, true);
                    out.write(sb.toString());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.setLength(0);
            }
        }
    }

    private static ArrayList<String> readUrl() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader("exam_url.txt");
            BufferedReader bf = new BufferedReader(fr);
            String str;
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
        /*String path = Objects.requireNonNull(YuJson.class.getClassLoader().getResource(fileName)).getPath();
        try {
            File jsonFile = new File(path);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }*/
    }

    private static String answerToEn(String answer) {
        StringBuilder sb = new StringBuilder();
        int iAnswer = Integer.parseInt(answer);
        String binary = Integer.toBinaryString(iAnswer);
        for (int i = 1 ; i <= binary.length(); i++) {
            if (binary.charAt(binary.length()-i) == '1') sb.append((char) (64+i));
        }
        return sb.toString();
    }

}
