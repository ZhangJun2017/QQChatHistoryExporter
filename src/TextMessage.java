public class TextMessage extends Message {
    public final String content;

    public TextMessage(Person sender, long time, long uniseq, String content) {
        super(sender, time, uniseq);
        this.content = content;
    }

    public static String escapeHtml(String toEscape) {
        toEscape = toEscape.replace("&", "&amp;");
        toEscape = toEscape.replace("\"", "&quot;");
        toEscape = toEscape.replace("'", "&apos;");
        toEscape = toEscape.replace(" ", "&nbsp;");
        toEscape = toEscape.replace("<", "&lt;");
        toEscape = toEscape.replace(">", "&gt;");
        toEscape = toEscape.replace("\n", "<br>");
        return toEscape;
    }

    @Override
    public String toString() {
        return "<TextMessage>" + sender.nickName + "(" + sender.uin + "):" + content;
    }

    @Override
    public String printToHtml() {
        return GlobalValues.HtmlFormattingText.TEXT_MESSAGE_HTML.replace("{TEXT_MESSAGE_CONTENT}", escapeHtml(content));
    }
}
