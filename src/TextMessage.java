import java.util.ArrayList;

public class TextMessage extends Message {
    public final String content;

    public TextMessage(Person sender, long time, long uniseq, long msgUid, String content) {
        super(sender, time, uniseq, msgUid);
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
            ArrayList<Integer> markIndex = new ArrayList<>();
            StringBuilder toReturn = new StringBuilder("");
            for (int i = 0; i < toEscape.length(); i++) {
                if (toEscape.charAt(i) == '\u0014' && !markIndex.contains(i - 1)) {  //sometimes there are two consecutive \u0014
                    markIndex.add(i);
                }
            }
            toReturn.append(escapeHtml(toEscape.substring(0, markIndex.get(0))));  //handle first \u0014 separately
            for (int i = 0; i < markIndex.size() - 1; i++) {
                toReturn.append(toEscape.substring(markIndex.get(i), markIndex.get(i) + 2));
                toReturn.append(escapeHtml(toEscape.substring(markIndex.get(i) + 2, markIndex.get(i + 1))));
            }
            if (markIndex.get(markIndex.size() - 1) == toEscape.length() - 1) {  //handle last \u0014 separately
                //seems like there's a standalone \u0014 appear at the end of the message....
                toReturn.append('\u0014');
                toReturn.append((char) 9999);
            } else {
                //normal case
                toReturn.append(toEscape.substring(markIndex.get(markIndex.size() - 1), markIndex.get(markIndex.size() - 1) + 2));
                toReturn.append(escapeHtml(toEscape.substring(markIndex.get(markIndex.size() - 1) + 2, toEscape.length())));
            }
            return toReturn.toString();
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
