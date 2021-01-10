import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class BlanksTest {

    @Test
    public void testBlanks() {

        StringBuilder sb = new StringBuilder();
        String jsonStr = readJson();
        String replace = jsonStr.replace("\\\\", "\\");
        JSONObject blanksObj = JSON.parseObject(replace);
        JSONArray blanksArray = blanksObj.getJSONArray("c");
        for (int queNum = 2; queNum < blanksArray.size(); queNum++) {
            JSONObject question = (JSONObject) blanksArray.get(queNum);
            sb.append(queNum-1).append(".");
            String description = (String)question.get("a");
            description = description.replace(" ","")
                    .replaceAll("<input.*?>"," ____ ")
                    .replace("&nbsp;","");
            sb.append(description).append("\r\n");
            JSONArray answerArray = question.getJSONArray("c");
            for (Object aObj : answerArray) {
                JSONObject option = (JSONObject) aObj;
                String answer = (String) option.get("a");
                sb.append(answer).append("    ");
            }
            sb.append("\r\n\r\n");
        }
        System.out.println(sb.toString());
    }

    private String readJson() {
        StringBuilder sb = new StringBuilder();
        FileReader fr;
        try {
            fr = new FileReader("exam_test.txt");
            BufferedReader bf = new BufferedReader(fr);
            String str;
            while ((str = bf.readLine()) != null) {
                sb.append(str);
            }
            bf.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

}
