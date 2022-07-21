import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RawMessage {
    public byte[] msgData;
    public String msgseq;
    public String msgtype;
    public String selfuin;
    public String senderuin;
    public String time;
    public String uniseq;
    public byte[] extStr;
    public String msgUid;

    public RawMessage(ResultSet rs) {
        try {
            msgData = rs.getBytes("msgData");
            msgseq = rs.getString("msgseq");
            msgtype = rs.getString("msgtype");
            senderuin = rs.getString("senderuin");
            selfuin = rs.getString("selfuin");
            time = rs.getString("time");
            uniseq = rs.getString("uniseq");
            extStr = rs.getBytes("extStr");
            msgUid = rs.getString("msgUid");
        } catch (SQLException e) {
            System.err.println("读取消息时出现问题");
            e.printStackTrace();
        }
    }

    public RawMessage(byte[] msgData, String msgseq, String msgtype, String selfuin, String senderuin, String time, String uniseq, byte[] extStr, String msgUid) {
        this.msgData = msgData;
        this.msgseq = msgseq;
        this.msgtype = msgtype;
        this.selfuin = selfuin;
        this.senderuin = senderuin;
        this.time = time;
        this.uniseq = uniseq;
        this.extStr = extStr;
        this.msgUid = msgUid;
    }

    public Message parse(String key, FriendManager friendManager, HashMap<String, ArrayList<RawMessage>> rawMultiMsgList, boolean isMultiMsg) {
        long time = Long.valueOf(this.time);
        long uniseq = Long.valueOf(this.uniseq);
        long msgseq = Long.valueOf(this.msgseq);
        long msgUid = Long.valueOf(this.msgUid);
        String senderuin = decryptChar(this.senderuin, key);
        friendManager.isMultiMsgMode = isMultiMsg;
        friendManager.multiMsgUniseq = this.msgseq;
        Person sender = friendManager.getPersonByUinPrefill(senderuin);
        Message toReturn;
        switch (msgtype) {
            case "-1000":
            case "-1051":
                toReturn = new TextMessage(sender, time, uniseq, msgUid, decryptString(msgData, key));
                break;
            case "-1049":
                toReturn = new ReplyMessage(sender, time, uniseq, msgUid, decryptString(msgData, key), decryptString(extStr, key), friendManager);
                break;
            case "-2000":
                toReturn = new PictureMessage(sender, time, uniseq, msgUid, decryptProtobuf(msgData, key));
                break;
            case "-1035":
                toReturn = new MixedMessage(sender, time, uniseq, msgUid, decryptProtobuf(msgData, key));
                break;
            case "-5012":
            case "-5018":
                toReturn = new PokeMessage(sender, time, uniseq, msgUid, decryptString(msgData, key));
                break;
            case "-2011":
                if (ForwardMessage.isForwardMessage(decryptProtobuf(msgData, key))) {
                    toReturn = new ForwardMessage(sender, time, uniseq, msgseq, msgUid, key, friendManager, rawMultiMsgList, msgData, isMultiMsg);
                } else {
                    toReturn = new TextMessage(sender, time, uniseq, msgUid, "[分享消息]");
                }
                break;
            case "-2002":
                toReturn = new VoiceMessage(sender, time, uniseq, msgUid, decryptProtobuf(msgData, key));
                break;
            case "-2007":
                toReturn = new MarkFaceMessage(sender, time, uniseq, msgUid, decryptProtobuf(msgData, key));
                break;
            default:
                toReturn = MiscMessageHandle.parse(sender, time, uniseq, msgUid, msgtype, decryptProtobuf(msgData, key));
                break;
        }
        return toReturn;
    }

    public static String decryptChar(String data, String key) {
        if (data == null) {
            return "";
        } else {
            char[] keyArr = key.toCharArray();
            char[] dataArr = data.toCharArray();
            String dataStr = "";
            for (int i = 0; i < data.length(); i++) {
                dataStr += Character.toString((char) (dataArr[i] ^ keyArr[i % keyArr.length]));
            }
            return dataStr;
        }
    }

    public static String decryptString(byte[] data, String key) {
        if (data == null) {
            return "";
        } else {
            char[] keyArr = key.toCharArray();
            byte[] dataByte = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                dataByte[i] = (byte) (data[i] ^ keyArr[i % keyArr.length]);
            }
            return new String(dataByte, StandardCharsets.UTF_8);
        }
    }

    public static byte[] decryptProtobuf(byte[] data, String key) {
        if (data == null) {
            return new byte[0];
        } else {
            char[] keyArr = key.toCharArray();
            byte[] dataByte = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                dataByte[i] = (byte) (data[i] ^ keyArr[i % keyArr.length]);
            }
            return dataByte;
        }
    }

    //copied from https://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java/140861#140861
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
