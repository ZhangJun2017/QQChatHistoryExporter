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

    public RawMessage(ResultSet rs) {
        try {
            msgData = rs.getBytes("msgData");
            msgseq = rs.getString("msgseq");
            msgtype = rs.getString("msgtype");
            senderuin = rs.getString("senderuin");
            selfuin = rs.getString("selfuin");
            time = rs.getString("time");
            uniseq = rs.getString("uniseq");
        } catch (SQLException e) {
            System.err.println("读取消息时出现问题");
            e.printStackTrace();
        }
    }

    public RawMessage(byte[] msgData, String msgseq, String msgtype, String selfuin, String senderuin, String time, String uniseq) {
        this.msgData = msgData;
        this.msgseq = msgseq;
        this.msgtype = msgtype;
        this.selfuin = selfuin;
        this.senderuin = senderuin;
        this.time = time;
        this.uniseq = uniseq;
    }

    public Message parse(String key, HashMap<String, HashMap<String, Person>> multiMsgFriendMap, HashMap<String, Person> friendMap, HashMap<String, ArrayList<RawMessage>> rawMultiMsgList) {
        long time = Long.valueOf(this.time);
        long uniseq = Long.valueOf(this.uniseq);
        String senderuin = decryptChar(this.senderuin, key);
        Person sender = multiMsgFriendMap.getOrDefault(this.msgseq, friendMap).getOrDefault(senderuin, new Person(senderuin, senderuin));
        Message toReturn;
        switch (msgtype) {
            case "-1000":
            case "-1049":
            case "-1051":
                toReturn = new TextMessage(sender, time, uniseq, decryptString(msgData, key));
                break;
            case "-2000":
                toReturn = new PictureMessage(sender, time, uniseq, decryptProtobuf(msgData, key));
                break;
            case "-1035":
                toReturn = new MixedMessage(sender, time, uniseq, decryptProtobuf(msgData, key));
                break;
            case "-5012":
            case "-5018":
                toReturn = new PokeMessage(sender, time, uniseq, decryptString(msgData, key));
                break;
            case "-2011":
                if (ForwardMessage.isForwardMessage(decryptProtobuf(msgData, key))) {
                    toReturn = new ForwardMessage(sender, time, uniseq, key, multiMsgFriendMap, friendMap, rawMultiMsgList);
                } else {
                    toReturn = new TextMessage(sender, time, uniseq, "[分享消息]");
                }
                break;
            default:
                toReturn = new TextMessage(sender, time, uniseq, "不支持的消息类型：" + msgtype);
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
}
