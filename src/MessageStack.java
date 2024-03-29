import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageStack {
    public final ArrayList<Message> messages;

    public MessageStack() {
        messages = new ArrayList<>();
    }

    public void add(Message message) {
        messages.add(message);
    }

    public String getExternalOperationCmdline() {
        StringBuilder stringBuilder = new StringBuilder();
        messages.forEach(messages -> stringBuilder.append(messages.getExternalOperationCmdline()));
        return stringBuilder.toString();
    }

    public String printToHtml(Person host) {
        StringBuilder stringBuilder = new StringBuilder();
        messages.sort(new SequenceOrderedComparator());
        messages.forEach(message -> {
            String toAppend;
            if (message.sender.uin.equals(host.uin)) {
                toAppend = GlobalValues.HtmlFormattingText.MESSAGE_SENT_HTML;
            } else {
                toAppend = GlobalValues.HtmlFormattingText.MESSAGE_RECEIVED_HTML;
            }
            toAppend = toAppend.replace("{MESSAGE_HEADER}", message.sender.nickName + " " + generalTimeFormat(message.time));
            toAppend = toAppend.replace("{MESSAGE_CONTENT}", message.printToHtml());
            toAppend = toAppend.replace("{MSGUID}", String.valueOf(message.msgUid));
            stringBuilder.append(toAppend);
        });
        return stringBuilder.toString();
    }

    public static String generalTimeFormat(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String replaceEmotion(String message, List<QEmotion> configList) {
        HashMap<String, QEmotion> configMap = new HashMap<>();
        for (QEmotion emotion : configList) {
            configMap.put(emotion.getAQLid(), emotion);
        }
        StringBuffer messageBuffer = new StringBuffer(message);
        for (int i = 0; i < messageBuffer.length(); i++) {
            if (messageBuffer.charAt(i) == '\u0014') {
                QEmotion emotion = configMap.getOrDefault(String.valueOf(messageBuffer.codePointAt(i + 1)), new QEmotion(String.valueOf(i + 1)));
                messageBuffer.insert(i + 2, GlobalValues.HtmlFormattingText.EMOTION_HTML.replace("{EMOTION_SRC}", GlobalValues.AssetsPath.EMOTION_PATH + "s" + emotion.getQSid() + ".png").replace("{EMOTION_ALT}", emotion.getQDes()).replace("{EMOTION_TITLE}", emotion.getAQLid() + " -> " + emotion.getQDes()));
                messageBuffer.delete(i, i + 2);
            }
        }
        return messageBuffer.toString();
    }

    public static MessageStack join(MessageStack stack1, MessageStack stack2) {
        MessageStack toReturn = new MessageStack();
        toReturn.messages.addAll(stack1.messages);
        toReturn.messages.addAll(stack2.messages);
        return toReturn;
    }

    public static MessageStack process(ResultSet messageList, ResultSet messageListSlowTable, HashMap<String, ArrayList<RawMessage>> multiMessageList, FriendManager friendManager, String key) throws SQLException {
        return MessageStack.join(MessageStack.parse(messageList, multiMessageList, friendManager, key), MessageStack.parse(messageListSlowTable, multiMessageList, friendManager, key));
    }

    public static MessageStack parse(ResultSet messageList, HashMap<String, ArrayList<RawMessage>> multiMessageList, FriendManager friendManager, String key) throws SQLException {
        MessageStack toReturn = new MessageStack();
        if (messageList == null) {
            return toReturn;
        }
        while (messageList.next()) {
            toReturn.add(new RawMessage(messageList).parse(key, friendManager, multiMessageList, false));
        }
        return toReturn;
    }

    public static HashMap<String, ArrayList<RawMessage>> parseMultiMsg(ResultSet rs) throws SQLException {
        HashMap<String, ArrayList<RawMessage>> toReturn = new HashMap<>();
        while (rs.next()) {
            RawMessage message = new RawMessage(rs);
            toReturn.putIfAbsent(message.msgseq, new ArrayList<>());
            toReturn.get(message.msgseq).add(message);
        }
        return toReturn;
    }

    public void printToConsole() {
        messages.sort(new SequenceOrderedComparator());
        messages.forEach(message -> System.out.println("[" + generalTimeFormat(message.time) + "]" + message));
    }
}
