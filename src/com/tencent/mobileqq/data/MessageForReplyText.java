package com.tencent.mobileqq.data;

import java.io.Serializable;

public class MessageForReplyText {
    public class SourceMsgInfo implements Serializable {
        public static final long serialVersionUID = 1;
        private byte[] mSourceMessageByte;
        public long mSourceMsgSenderUin;
        public long mSourceMsgSeq;
        public String mSourceMsgText;
        public int mSourceMsgTime;
        public long mSourceMsgToUin;
        public String mSourceMsgTroopName;
        public int mSourceSummaryFlag = 1;
        public int mType = 0;
        public int oriMsgType;
        public long origUid;
        public int replyPicHeight;
        public int replyPicWidth;
        private long uniseq = 0;
    }
}
