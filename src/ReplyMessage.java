import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class ReplyMessage extends Message {
    public final String content;
    public final String extStr;
    public ReplyLink replyLink;

    public ReplyMessage(Person sender, long time, long uniseq, long msgUid, String content, String extStr, FriendManager friendManager) {
        super(sender, time, uniseq, msgUid);
        this.content = content;
        this.extStr = extStr;
        this.replyLink = new ReplyLink(RawMessage.hexStringToByteArray(sourceMsgInfoExtract(extStr)), friendManager);
    }

    public static String sourceMsgInfoParse(String json) {
        //ideal situation
        try {
            JsonElement sens_msg_source_msg_info;
            sens_msg_source_msg_info = new JsonParser().parse(json).getAsJsonObject().get("sens_msg_source_msg_info");
            if (sens_msg_source_msg_info == null) {
                return "";
            } else {
                return sens_msg_source_msg_info.toString();
            }
        } catch (JsonSyntaxException | IllegalStateException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sourceMsgInfoExtract(String json) {
        if (json == null) {
            return "";
        }
        String[] jsonSplit = json.split(",");
        if (jsonSplit.length >= 1) {
            for (int i = 0; i < jsonSplit.length; i++) {
                if (jsonSplit[i].contains("sens_msg_source_msg_info")) {
                    String[] jsonFieldSplit = jsonSplit[i].split("\"");
                    if (jsonFieldSplit.length >= 4 && jsonFieldSplit[3].contains("ACED")) {
                        return jsonFieldSplit[3];
                    }
                }
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "<ReplyMessage>" + sender.nickName + "(" + sender.uin + "):" + replyLink + " <- " + content;
    }

    @Override
    public String printToHtml() {
        return GlobalValues.HtmlFormattingText.REPLY_MESSAGE_HTML.replace("{REPLY_LINK}", replyLink.toString()).replace("{REPLY_MESSAGE_CONTENT}", TextMessage.escapeHtml(content));
    }
}
