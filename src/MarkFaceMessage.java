public class MarkFaceMessage extends Message {
    public MarkFace markFace;

    public MarkFaceMessage(Person sender, long time, long uniseq, long msgUid, byte[] data) {
        super(sender, time, uniseq, msgUid);
        markFace = new MarkFace(data);
    }

    @Override
    public String toString() {
        return "<MarkFaceMessage>" + sender.nickName + "(" + sender.uin + "):" + markFace;
    }

    @Override
    public String printToHtml() {
        return GlobalValues.HtmlFormattingText.MARKFACE_MESSAGE_HTML.replace("{MARKFACE_MESSAGE_CONTENT}", markFace.toString());
    }
}
