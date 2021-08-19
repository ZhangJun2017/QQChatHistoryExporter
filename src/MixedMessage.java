import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

public class MixedMessage extends Message {
    private List<RichMsgHandle.Elem> elemList;

    public MixedMessage(Person sender, long time, long uniseq, byte[] data) {
        super(sender, time, uniseq);
        try {
            elemList = RichMsgHandle.Msg.parseFrom(data).getElemsList();
        } catch (InvalidProtocolBufferException e) {
            elemList = new ArrayList<>();
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "<MixedMessage>" + sender.nickName + "(" + sender.uin + "):" + printToHtml();
    }

    @Override
    public String printToHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        for (RichMsgHandle.Elem elem : elemList) {
            if (elem.hasPicMsg()) {
                stringBuilder.append(new Picture(elem.getPicMsg()).toString());
            } else {
                stringBuilder.append(TextMessage.escapeHtml(elem.getTextMsg()));
            }
        }
        return GlobalValues.HtmlFormattingText.MIXED_MESSAGE_HTML.replace("{MIXED_MESSAGE_CONTENT}", stringBuilder.toString());
    }

    @Override
    public String getExternalOperationCmdline() {
        StringBuilder stringBuilder = new StringBuilder();
        for (RichMsgHandle.Elem elem : elemList) {
            if (elem.hasPicMsg()) {
                stringBuilder.append(new Picture(elem.getPicMsg()).getExternalOperationCmdline());
            }
        }
        return stringBuilder.toString();
    }
}
