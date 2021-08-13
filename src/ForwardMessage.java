import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ForwardMessage extends Message {
    public MessageStack messages = new MessageStack();
    public static final Person unknown = new Person("未知", "未知");
    public Person host = unknown;
    public Person opposite = unknown;
    public static final TextMessage invalidForwardMessageNotice = new TextMessage(new Person("", ""), 0, 0, "无效的转发消息");

    public ForwardMessage(Person sender, long time, long uniseq, String key, HashMap<String, HashMap<String, Person>> multiMsgFriendMap, HashMap<String, Person> friendMap, HashMap<String, ArrayList<RawMessage>> multiMsgList) {
        super(sender, time, uniseq);
        if (!multiMsgList.containsKey(String.valueOf(uniseq))) {
            messages.add(invalidForwardMessageNotice);
            return;
        }
        for (RawMessage message : multiMsgList.get(String.valueOf(uniseq))) {
            Message toAdd = message.parse(key, multiMsgFriendMap, friendMap, multiMsgList);
            messages.add(toAdd);
            if (host == unknown || opposite == unknown) {
                if (message.selfuin.equals(message.senderuin)) {
                    host = toAdd.sender;
                } else {
                    opposite = toAdd.sender;
                }
            }
        }
    }

    public static boolean isForwardMessage(byte[] msgData) {
        return new String(msgData, StandardCharsets.UTF_8).contains("viewMultiMsg");
    }

    @Override
    public String toString() {
        return "<ForwardMessage>" + sender.nickName + "(" + sender.uin + "):" + printToHtml();
    }

    @Override
    public String printToHtml() {
        return GlobalValues.HtmlFormattingText.FORWARD_MESSAGE_HTML.replace("{FORWARD_MESSAGE_HTML_URL}", GlobalValues.AssetsPath.HTML_FILE_PATH + uniseq + ".html").replace("{FORWARD_MESSAGE_COUNT}", String.valueOf(messages.messages.size()));
    }

    @Override
    public String getExternalOperationCmdline() {
        try {
            Main.writeFile(new MessageWrapper(messages, host, opposite).printToHtml(), System.getProperty("java.io.tmpdir") + "\\" + uniseq + ".html");
        } catch (IOException e) {
            System.err.println("写出转发的聊天记录失败");
            e.printStackTrace();
        }
        return messages.getExternalOperationCmdline() + "move /Y \"" + System.getProperty("java.io.tmpdir") + "\\" + uniseq + ".html" + "\" \"<destDir>\\html\\" + uniseq + ".html" + "\" >nul\r\n";
    }
}
