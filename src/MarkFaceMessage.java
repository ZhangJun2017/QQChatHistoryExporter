import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MarkFaceMessage extends Message {
    public MarkFace markFace;

    public MarkFaceMessage(Person sender, long time, long uniseq, byte[] data) {
        super(sender, time, uniseq);
        try {
            markFace = new MarkFace((com.tencent.mobileqq.data.MarkFaceMessage) new ObjectInputStream(new ByteArrayInputStream(data)).readObject());
        } catch (IOException | ClassNotFoundException e) {
            markFace = new MarkFace(hanziExtract(new String(data)));
            e.printStackTrace();
        }
    }

    public static String hanziExtract(String str) {
        return str.replaceAll("[^\u4e00-\u9fa5]", "");
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
