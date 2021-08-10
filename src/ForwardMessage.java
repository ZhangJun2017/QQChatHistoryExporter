import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.HashMap;

public class ForwardMessage extends Message {
    private HashMap<String, Person> friendMap = new HashMap<>();
    public MessageStack messages;

    public ForwardMessage(Person sender, long time, long uniseq, ResultSet messageList, HashMap<String, Person> friendMap, String key) {
        super(sender, time, uniseq);
    }

    public static boolean isForwardMessage(byte[] msgData) {
        return new String(msgData, StandardCharsets.UTF_8).contains("viewMultiMsg");
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public String printToHtml() {
        return null;
    }
}
