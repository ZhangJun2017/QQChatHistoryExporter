import com.google.protobuf.InvalidProtocolBufferException;

public class VoiceMessage extends Message {
    public Voice voice;

    public VoiceMessage(Person sender, long time, long uniseq, byte[] data) {
        super(sender, time, uniseq);
        try {
            voice = new Voice(RichMsgHandle.PttRec.parseFrom(data));
        } catch (InvalidProtocolBufferException e) {
            //this should not happen too
            voice = new Voice();
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "<VoiceMessage>" + sender.nickName + "(" + sender.uin + "):" + voice;
    }

    @Override
    public String printToHtml() {
        return GlobalValues.HtmlFormattingText.VOICE_MESSAGE_HTML.replace("{VOICE_MESSAGE_CONTENT}", voice.toString());
    }

    @Override
    public String getExternalOperationCmdline() {
        return voice.getExternalOperationCmdline();
    }
}
