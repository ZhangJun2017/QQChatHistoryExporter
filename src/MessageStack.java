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
    private final ArrayList<Message> messages;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            toAppend = toAppend.replace("{MESSAGE_HEADER}", message.sender.nickName + " " + LocalDateTime.ofInstant(Instant.ofEpochSecond(message.time), ZoneOffset.of("+8")).format(dateTimeFormatter));
            toAppend = toAppend.replace("{MESSAGE_CONTENT}", message.printToHtml());
            stringBuilder.append(toAppend);
        });
        return stringBuilder.toString();
    }

    public String replaceEmotion(String message, List<QEmotion> configList) {
        HashMap<String, QEmotion> configMap = new HashMap<>();
        for (QEmotion emotion : configList) {
            configMap.put(emotion.getAQLid(), emotion);
        }
        StringBuffer messageBuffer = new StringBuffer(message);
        for (int i = 0; i < messageBuffer.length(); i++) {
            if (messageBuffer.charAt(i) == '\u0014') {
                messageBuffer.insert(i + 2, GlobalValues.HtmlFormattingText.EMOTION_HTML.replace("{EMOTION_SRC}", GlobalValues.AssetsPath.EMOTION_PATH + "s" + configMap.get(String.valueOf(messageBuffer.codePointAt(i + 1))).getQSid() + ".png").replace("{EMOTION_ALT}", configMap.get(String.valueOf(messageBuffer.codePointAt(i + 1))).getQDes()));
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

    public static MessageStack process(ResultSet messageList, ResultSet messageListSlowTable, ResultSet multiMessageList, HashMap<String, Person> friendMap, HashMap<String, HashMap<String, Person>> friendMapMultiMsg, String key) throws SQLException {
        return MessageStack.join(MessageStack.parse(messageList, multiMessageList, friendMap, friendMapMultiMsg, key), MessageStack.parse(messageListSlowTable, multiMessageList, friendMap, friendMapMultiMsg, key));
    }

    public static MessageStack parse(ResultSet messageList, ResultSet multiMessageList, HashMap<String, Person> friendMap, HashMap<String, HashMap<String, Person>> friendMapMultiMsg, String key) throws SQLException {
        MessageStack toReturn = new MessageStack();
        if (messageList == null) {
            return toReturn;
        }
        while (messageList.next()) {
            String senderuin = Main.decryptString(messageList.getBytes("senderuin"), key);
            long time = Long.valueOf(messageList.getString("time"));
            long uniseq = Long.valueOf(messageList.getString("uniseq"));
            String msgtype = messageList.getString("msgtype");
            byte[] msgData = messageList.getBytes("msgData");
            Person sender = friendMap.get(senderuin);
            if (sender == null) {
                sender = new Person(senderuin, senderuin);
            }
            switch (msgtype) {
                case "-1000":
                case "-1049":
                case "-1051":
                    toReturn.add(new TextMessage(sender, time, uniseq, Main.decryptString(msgData, key)));
                    break;
                case "-2000":
                    toReturn.add(new PictureMessage(sender, time, uniseq, Main.decryptProtobuf(msgData, key)));
                    break;
                case "-1035":
                    toReturn.add(new MixedMessage(sender, time, uniseq, Main.decryptProtobuf(msgData, key)));
                    break;
                case "-5012":
                case "-5018":
                    toReturn.add(new PokeMessage(sender, time, uniseq, Main.decryptString(msgData, key)));
                    break;
                case "-2011":
                    if (ForwardMessage.isForwardMessage(Main.decryptProtobuf(msgData, key))) {
                        toReturn.add(new ForwardMessage(sender, time, uniseq, multiMessageList, friendMapMultiMsg.get(uniseq), key));
                    } else {
                        toReturn.add(new TextMessage(sender, time, uniseq, "[分享消息]"));
                    }
                    break;
                default:
                    toReturn.add(new TextMessage(sender, time, uniseq, "不支持的消息类型：" + msgtype));
                    break;
            }
        }
        return toReturn;
    }

    public void printToConsole() {
        messages.sort(new SequenceOrderedComparator());
        messages.forEach(message -> System.out.println("[" + LocalDateTime.ofInstant(Instant.ofEpochSecond(message.time), ZoneOffset.of("+8")).format(dateTimeFormatter) + "]" + message));
    }
}
