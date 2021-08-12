import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MessageWrapper {
    public MessageStack messages;
    Person host;
    Person opposite;

    public MessageWrapper(MessageStack messages, Person host, Person opposite) {
        this.messages = messages;
        this.host = host;
        this.opposite = opposite;
    }

    public String printToHtml() {
        StringBuilder htmlBuilder = new StringBuilder();
        String htmlTitle = GlobalValues.HtmlFormattingText.HTML_TITLE.replace("{HOST_NICKNAME}", host.nickName).replace("{HOST_UIN}", host.uin).replace("{OPPOSITE_NICKNAME}", opposite.nickName).replace("{OPPOSITE_UIN}", opposite.uin);
        htmlBuilder.append(GlobalValues.HtmlFormattingText.HTML_FILE_HEADER.replace("{HTML_TITLE}", htmlTitle));
        ArrayList emotionConfig = new Gson().fromJson(new JsonParser().parse(new JsonReader(new InputStreamReader(Main.class.getResourceAsStream("face_config.json")))).getAsJsonObject().get("sysface").getAsJsonArray(), new TypeToken<List<QEmotion>>() {
        }.getType());
        htmlBuilder.append(messages.replaceEmotion(messages.printToHtml(host), emotionConfig));
        htmlBuilder.append(GlobalValues.HtmlFormattingText.HTML_FILE_FOOTER);
        return htmlBuilder.toString();
    }
}
