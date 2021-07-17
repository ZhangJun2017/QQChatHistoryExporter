/**
 * QQChatHistoryExporter/
 * Description:
 *
 * @author:ZhangJun2017
 * @date:2021/7/8
 */

public class TextMessage extends Message {
    public final String content;

    public TextMessage(Person sender, long time, long uniseq, String content) {
        super(sender, time, uniseq);
        this.content = content;
    }

    @Override
    public String toString() {
        return "<TextMessage>" + sender.nickName + "(" + sender.uin + "):" + content;
    }

    public static String escapeHtml(String toEscape) {
        toEscape = toEscape.replace("&", "&amp;");
        toEscape = toEscape.replace("\"", "&quot;");
        toEscape = toEscape.replace("'", "&apos;");
        toEscape = toEscape.replace(" ", "&nbsp;");
        toEscape = toEscape.replace("<", "&lt;");
        toEscape = toEscape.replace(">", "&gt;");
        return toEscape;
    }

    @Override
    public String getExternalOperationCmdline() {
        return "";
    }

    @Override
    public String printToHtml() {
        return GlobalValues.HtmlFormattingText.TEXT_MESSAGE_HTML.replace("{TEXT_MESSAGE_CONTENT}", escapeHtml(content));
    }
}
