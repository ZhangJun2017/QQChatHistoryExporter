import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * QQChatHistoryExporter/
 * Description:
 *
 * @author:ZhangJun2017
 * @date:2021/7/24
 */

public class PokeMessage extends Message {
    private final String pokeJson;

    public PokeMessage(Person sender, long time, long uniseq, String pokeJson) {
        super(sender, time, uniseq);
        this.pokeJson = pokeJson;
    }

    @Override
    public String toString() {
        return "<PokeMessage>" + sender.nickName + "(" + sender.uin + "):" + printToHtml();
    }

    @Override
    public String printToHtml() {
        try {
            return GlobalValues.HtmlFormattingText.POKE_MESSAGE_HTML.replace("{POKE_MESSAGE_CONTENT}", new JsonParser().parse(pokeJson).getAsJsonObject().get("msg").getAsString());
        } catch (JsonSyntaxException e) {
            return GlobalValues.HtmlFormattingText.POKE_MESSAGE_HTML.replace("{POKE_MESSAGE_CONTENT}", "[戳一戳]");
        }
    }
}
