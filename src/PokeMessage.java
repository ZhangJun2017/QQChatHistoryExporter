import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class PokeMessage extends Message {
    private final String pokeJson;

    public PokeMessage(Person sender, long time, long uniseq, long msgUid, String pokeJson) {
        super(sender, time, uniseq, msgUid);
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
