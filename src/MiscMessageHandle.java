import com.google.protobuf.InvalidProtocolBufferException;

public class MiscMessageHandle {
    public static Message parse(Person sender, long time, long uniseq, String msgtype, byte[] msgData) {
        String msgStr = new String(msgData);
        String[] msgArr;
        Person system = new Person("10000", "系统提示");
        Message toReturn;
        switch (msgtype) {
            case "-2009":
                msgArr = msgStr.split("\\|");
                if (msgArr.length >= 4) {
                    toReturn = new TextMessage(sender, time, uniseq, (msgArr[3].equals("0") ? "[语音通话]" : msgArr[3].equals("1") ? "[视频通话]" : "[通话]") + msgArr[0].substring(1));
                } else {
                    toReturn = new TextMessage(sender, time, uniseq, "[通话]");
                }
                break;
            case "-5008":
                toReturn = new TextMessage(sender, time, uniseq, "[分享卡片]");
                break;
            case "-2005":
                msgArr = msgStr.split("\\|");
                toReturn = new TextMessage(sender, time, uniseq, "[文件]大小：" + (msgArr.length == 5 ? msgArr[1] + "字节" : "未知") + "，本地路径：" + (msgStr.startsWith("\u0016") ? msgArr[0].substring(1) : "未知"));
                break;
            case "-3008":
                toReturn = new TextMessage(system, time, uniseq, "对方已成功接收文件 \"" + msgStr + "\"");
                break;
            case "-2022":
                try {
                    RichMsgHandle.ShortVideo shortVideo = RichMsgHandle.ShortVideo.parseFrom(msgData);
                    toReturn = new TextMessage(sender, time, uniseq, "[短视频]大小：" + shortVideo.getFileSize() + "字节，本地路径：" + shortVideo.getLocalPath());
                } catch (InvalidProtocolBufferException e) {
                    toReturn = new TextMessage(sender, time, uniseq, "[短视频]解析失败");
                }
                break;
            case "-5040":
                try {
                    RichMsgHandle.Tip tip = RichMsgHandle.Tip.parseFrom(msgData);
                    toReturn = new TextMessage(system, time, uniseq, tip.getTip() + (tip.getRecallInfo().getRecallDetail().equals("") ? "" : "（" + tip.getRecallInfo().getRecallDetail() + "）"));
                } catch (InvalidProtocolBufferException e) {
                    toReturn = new TextMessage(system, time, uniseq, "解析失败");
                }
                break;
            default:
                toReturn = new TextMessage(sender, time, uniseq, "不支持的消息类型：" + msgtype);
                break;
        }
        return toReturn;
    }
}
