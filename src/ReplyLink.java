import com.tencent.mobileqq.data.MessageForReplyText;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReplyLink {
    public long mSourceMsgSenderUin;
    public String mSourceMsgText;
    public long mSourceMsgTime;
    public long origUid;
    public String replyToName;

    public ReplyLink(byte[] data, FriendManager friendManager) {
        try {
            MessageForReplyText.SourceMsgInfo sourceMsgInfo = (MessageForReplyText.SourceMsgInfo) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
            mSourceMsgSenderUin = sourceMsgInfo.mSourceMsgSenderUin;
            mSourceMsgText = sourceMsgInfo.mSourceMsgText;
            mSourceMsgTime = sourceMsgInfo.mSourceMsgTime;
            origUid = sourceMsgInfo.origUid;
            replyToName = friendManager.getPersonByUinPrefill(String.valueOf(mSourceMsgSenderUin)).nickName;
        } catch (IOException | ClassNotFoundException e) {
            mSourceMsgSenderUin = -1;
            mSourceMsgText = "";
            mSourceMsgTime = 0;
            origUid = -1;
            replyToName = "未知";
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return GlobalValues.HtmlFormattingText.REPLY_LINK_HTML.replace("{LINK_MSGUID}", String.valueOf(origUid)).replace("{REPLY_LINK_TITLE}", "回复 #" + origUid + " 的消息").replace("{SOURCE_MSG_INFO}", replyToName + " " + MessageStack.generalTimeFormat(mSourceMsgTime)).replace("{SOURCE_MSG_BRIEF}", TextMessage.escapeHtml(mSourceMsgText));
    }
}
