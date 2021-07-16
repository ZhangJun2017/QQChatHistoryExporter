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

    @Override
    public String printToHtml() {
        return GlobalValues.HtmlFormattingText.TEXT_MESSAGE_HTML.replaceAll("<TEXT_MESSAGE_CONTENT>", content);
    }

    @Override
    public String getExternalOperationCmdline() {
        return "";
    }

}
