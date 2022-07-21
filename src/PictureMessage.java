import com.google.protobuf.InvalidProtocolBufferException;

public class PictureMessage extends Message {
    public Picture picture;

    public PictureMessage(Person sender, long time, long uniseq, long msgUid, byte[] data) {
        super(sender, time, uniseq, msgUid);
        try {
            picture = new Picture(RichMsgHandle.PicRec.parseFrom(data));
        } catch (InvalidProtocolBufferException e) {
            //this should not happen
            picture = new Picture();
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "<PictureMessage>" + sender.nickName + "(" + sender.uin + "):" + picture;
    }

    @Override
    public String printToHtml() {
        return GlobalValues.HtmlFormattingText.PICTURE_MESSAGE_HTML.replace("{PICTURE_MESSAGE_CONTENT}", picture.toString());
    }

    @Override
    public String getExternalOperationCmdline() {
        return picture.getExternalOperationCmdline();
    }
}
