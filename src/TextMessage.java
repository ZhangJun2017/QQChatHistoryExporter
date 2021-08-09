public class TextMessage extends Message {
    public final String content;

    public TextMessage(Person sender, long time, long uniseq, String content) {
        super(sender, time, uniseq);
        this.content = content;
    }

    public static String escapeHtml(String toEscape) {
        if (!toEscape.contains("\u0014")) {
            toEscape = toEscape.replace("&", "&amp;");
            toEscape = toEscape.replace("\"", "&quot;");
            toEscape = toEscape.replace("'", "&apos;");
            toEscape = toEscape.replace(" ", "&nbsp;");
            toEscape = toEscape.replace("<", "&lt;");
            toEscape = toEscape.replace(">", "&gt;");
            toEscape = toEscape.replace("\n", "<br>");
            return toEscape;
        } else {
            if (toEscape.equals("\u0014\u0014")) {
                return toEscape;
            }
            String[] toEscapeArray = toEscape.split("\u0014");
            StringBuilder message = new StringBuilder(escapeHtml(toEscapeArray[0]));
            for (int i = 0; i < toEscapeArray.length - 1; i++) {
                message.append('\u0014');
                if (toEscapeArray[i + 1].length() > 0) {
                    message.append(toEscapeArray[i + 1].charAt(0));
                    message.append(escapeHtml(toEscapeArray[i + 1].substring(1)));
                } else {
                    // a standalone \u0014 ??
                    message.append('\u0034');
                }
            }
            message.append(toEscapeArray.length < 3 || toEscapeArray[toEscapeArray.length - 1].length() < 2 ? "" : escapeHtml(toEscapeArray[toEscapeArray.length - 1]));
            return message.toString();
        }
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
