import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * QQChatHistoryExporter/
 * Description:
 *
 * @author:ZhangJun2017
 * @date:2021/7/16
 */

public class MixedMessage extends Message {
    private List<RichMsgHandle.Elem> elemList = null;

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
                stringBuilder.append(new Picture(elem.getPicMsg()));
            } else {
                stringBuilder.append(elem.getTextMsg());
            }
        }
        return stringBuilder.toString();
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
