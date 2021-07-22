import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * QQChatHistoryExporter/
 * Description:
 *
 * @author:ZhangJun2017
 * @date:2021/7/8
 */

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
            if (message.sender == host) {
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
        String[] messageArray = message.split("\u0014");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageArray[0]);
        for (int i = 1; i < messageArray.length; i++) {
            stringBuilder.append(GlobalValues.HtmlFormattingText.EMOTION_HTML.replace("{EMOTION_SRC}", GlobalValues.AssetsPath.EMOTION_PATH + "s" + configMap.get(String.valueOf(messageArray[i].codePointAt(0))).getQSid() + ".png").replace("{EMOTION_ALT}", configMap.get(String.valueOf(messageArray[i].codePointAt(0))).getQDes()) + messageArray[i].substring(1));
        }
        return stringBuilder.toString();
    }

    public void printToConsole() {
        messages.sort(new SequenceOrderedComparator());
        messages.forEach(message -> System.out.println("[" + LocalDateTime.ofInstant(Instant.ofEpochSecond(message.time), ZoneOffset.of("+8")).format(dateTimeFormatter) + "]" + message));
    }
}
