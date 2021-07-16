import com.google.protobuf.InvalidProtocolBufferException;

/**
 * QQChatHistoryExporter/
 * Description:
 *
 * @author:ZhangJun2017
 * @date:2021/7/9
 */

public class PictureMessage extends Message {
    public Picture picture;

    public PictureMessage(Person sender, long time, long uniseq, byte[] data) {
        super(sender, time, uniseq);
        try {
            this.picture = new Picture(RichMsgHandle.PicRec.parseFrom(data));
        } catch (InvalidProtocolBufferException e) {
            this.picture = new Picture();
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "<PictureMessage>" + sender.nickName + "(" + sender.uin + "):" + picture;
    }

    @Override
    public String printToHtml() {
        return null;
    }

    @Override
    public String getExternalOperationCmdline() {
        return picture.getExternalOperationCmdline();
    }
}
