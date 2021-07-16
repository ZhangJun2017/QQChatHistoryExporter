import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
            toAppend = toAppend.replaceAll("<MESSAGE_HEADER>", message.sender.nickName + " " + LocalDateTime.ofInstant(Instant.ofEpochSecond(message.time), ZoneOffset.of("+8")).format(dateTimeFormatter));
            toAppend = toAppend.replaceAll("<MESSAGE_CONTENT>", message.printToHtml());
            stringBuilder.append(toAppend);
        });
        return stringBuilder.toString();
    }

    public void printToConsole() {
        messages.sort(new SequenceOrderedComparator());
        messages.forEach(message -> System.out.println("[" + LocalDateTime.ofInstant(Instant.ofEpochSecond(message.time), ZoneOffset.of("+8")).format(dateTimeFormatter) + "]" + message));
    }
}
