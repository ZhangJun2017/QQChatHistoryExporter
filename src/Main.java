import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;

/**
 * QQChatHistoryExporter/
 * Description:
 *
 * @author:ZhangJun2017
 * @date:2021/7/2
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello QQ!");
        /*HashMap config = new HashMap();
        config.put("dateTimeFormatter",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        config.put("printToFile",new FileOutputStream("123"));*/
        /*
        String data = "-";
        char[] dataArr = data.toCharArray();
        String key = "-";
        char[] keyArr = key.toCharArray();
        //start decrypt
        String msg = "";
        for (int i = 0; i < data.length(); i++) {
            msg += Character.toString((char) (dataArr[i] ^ keyArr[i % keyArr.length]));
        }
        System.out.println(msg);
        */

        /*
        final String Class_Name = "org.sqlite.JDBC";
        //final String DB_URL = "jdbc:sqlite:-";
        final String DB_URL = "jdbc:sqlite:-";
        Connection connection;
        try {
            Class.forName(Class_Name);
            connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //ResultSet rs = statement.executeQuery("SELECT uin, remark FROM Friends");
            ResultSet rs = statement.executeQuery("select msgData,senderuin,time,msgtype from mr_friend_-_New order by time");
            while (rs.next()) {
                //System.out.println(decrypt(rs.getString("uin")) + " | " + decrypt(rs.getString("remark")));
                System.out.println(decryptByte(rs.getBytes(1)) + " | " + decryptByte(rs.getBytes(2)) + " | " + rs.getString(3) + " | " + rs.getString(4));
                byte[] msg = rs.getBytes(1);
                byte[] sender = rs.getBytes(2);
                String time = rs.getString(3);
                String type = rs.getString(4);
                if (time.equals("1625591633")) {
                    FileOutputStream fileOutputStream = new FileOutputStream("-", false);
                    fileOutputStream.write(decryptToRaw(msg));
                    RichMsgHandle.mixmsgtxthead mixmsgtxthead = RichMsgHandle.mixmsgtxthead.parseFrom(decryptToRaw(msg));
                    //mixmsgtxtheadmixmsgtxthead.getPlainmsgnested().getUnknownFields().toByteArray();
                    System.out.println(mixmsgtxthead);
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

         */

        /*
        MessageStack topMessageStack = new MessageStack();
        final String Class_Name = "org.sqlite.JDBC";
        final String DB_URL = "jdbc:sqlite:-";
        Connection connection;
        Person sender = new Person("-1", "Unknown"), receiver;
        HashMap<String, String> friendMap = new HashMap<>();
        try {
            Class.forName(Class_Name);
            connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select uin,name,remark from Friends");
            while (rs.next()) {
                friendMap.put(decryptAsChar(rs.getString(1)), decryptAsChar(rs.getString(3)).equals("") ? decryptAsChar(rs.getString(2)) : decryptAsChar(rs.getString(3)));
            }
            //-
            //rs = statement.executeQuery("select msgData,senderuin,time,msgtype,uniseq from mr_friend_-_New");
            //-
            //rs = statement.executeQuery("select msgData,senderuin,time,msgtype,uniseq from mr_friend_-_New");
            //-
            rs = statement.executeQuery("select msgData,senderuin,time,msgtype,uniseq from mr_friend_-_New");
            String meUin = "-";
            String oppositeUin = "-";
            Person me = new Person(meUin, friendMap.get(meUin));
            Person opposite = new Person(oppositeUin, friendMap.get(oppositeUin));
            Person toStore;
            while (rs.next()) {
                if (decryptAsByte(rs.getBytes(2)).equals(meUin)) toStore = me;
                else toStore = opposite;
                if (rs.getString(4).equals("-1000")) {
                    topMessageStack.put(new TextMessage(toStore, Long.valueOf(rs.getString(3)), Long.valueOf(rs.getString(5)), decryptAsByte(rs.getBytes(1))));
                } else if (rs.getString(4).equals("-2000")) {
                    topMessageStack.put(new PictureMessage(toStore, Long.valueOf(rs.getString(3)), Long.valueOf(rs.getString(5)), decryptToRaw(rs.getBytes(1))));
                } else if (rs.getString(4).equals("-1035")) {
                    topMessageStack.put(new MixedMessage(toStore, Long.valueOf(rs.getString(3)), Long.valueOf(rs.getString(5)), decryptToRaw(rs.getBytes(1))));
                } else if (rs.getString(3).equals("1625591663")) {
                    FileOutputStream fileOutputStream = new FileOutputStream("-", false);
                    fileOutputStream.write(decryptToRaw(rs.getBytes(1)));
                    RichMsgHandle.Msg test = RichMsgHandle.Msg.parseFrom(decryptToRaw((rs.getBytes(1))));
                    //RichMsgHandle.PttRec.parseFrom(decryptToRaw(rs.getBytes(1)));
                    MixedMessage message = new MixedMessage(toStore, Long.valueOf(rs.getString(3)), Long.valueOf(rs.getString(5)), decryptToRaw(rs.getBytes(1)));
                    System.out.println(message);
                    System.out.println();
                } else {
                    topMessageStack.put(new TextMessage(toStore, Long.valueOf(rs.getString(3)), Long.valueOf(rs.getString(5)), "[" + rs.getString(4) + "]"));
                }
            }
            topMessageStack.getExternalOperationCmdline();
            topMessageStack.printToConsole();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    */
        String destDir = "-";
        String key = "-";
        String uinSelf = "-";
        //-
        //String uinOpposite="-";
        //-
        String uinOpposite = "-";
        //-
        //String uinOpposite = "-";
        final String Class_Name = "org.sqlite.JDBC";
        final String DB_URL = "jdbc:sqlite:-";
        final String DB_URL_slowtable = "jdbc:sqlite:-";
        Connection connection;
        HashMap<String, String> friendMap = new HashMap<>();
        MessageStack topMessageStack = new MessageStack();
        try {
            Class.forName(Class_Name);
            connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select uin,name,remark from Friends");
            while (rs.next()) {
                friendMap.put(decryptChar(rs.getString("uin"), key), (decryptChar(rs.getString("remark"), key).equals("") ? decryptChar(rs.getString("name"), key) : decryptChar(rs.getString("remark"), key)));
            }
            Person me = new Person(uinSelf, friendMap.get(uinSelf));
            Person opposite = new Person(uinOpposite, friendMap.get(uinOpposite));
            Person toStore;
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    connection = DriverManager.getConnection(DB_URL);
                } else {
                    connection = DriverManager.getConnection(DB_URL_slowtable);
                }
                statement = connection.createStatement();
                try {
                    rs = statement.executeQuery("select msgData,senderuin,time,msgtype,uniseq from mr_friend_<TARGET>_New".replace("<TARGET>", new BigInteger(1, MessageDigest.getInstance("md5").digest(uinOpposite.getBytes())).toString(16).toUpperCase()));
                } catch (SQLException e) {
                    //slowtable.db might not have that table since the common table is enough to store messages
                }
                while (rs.next()) {
                    if (decryptString(rs.getBytes(2), key).equals(uinSelf)) toStore = me;
                    else toStore = opposite;
                    switch (rs.getString("msgtype")) {
                        case "-1000":
                            topMessageStack.add(new TextMessage(toStore, Long.valueOf(rs.getString("time")), Long.valueOf(rs.getString("uniseq")), decryptString(rs.getBytes("msgData"), key)));
                            break;
                        case "-2000":
                            topMessageStack.add(new PictureMessage(toStore, Long.valueOf(rs.getString("time")), Long.valueOf(rs.getString("uniseq")), decryptProtobuf(rs.getBytes("msgData"), key)));
                            break;
                        default:
                            topMessageStack.add(new TextMessage(toStore, Long.valueOf(rs.getString("time")), Long.valueOf(rs.getString("uniseq")), rs.getString("msgtype")));
                            break;
                    }
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(destDir + "exop.bat", false);
            fileOutputStream.write(GlobalValues.BatchFormattingText.BATCH_FILE_BODY.replace("{BATCH_FILE_BODY}", topMessageStack.getExternalOperationCmdline()).getBytes("GB2312"));
            fileOutputStream.flush();
            fileOutputStream.close();
            //Runtime.getRuntime().exec(destDir + "exop.bat");
            fileOutputStream = new FileOutputStream(destDir + GlobalValues.AssetsPath.CSS_FILE_PATH, false);
            fileOutputStream.write(GlobalValues.HtmlFormattingText.CSS_CONTENT.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
            fileOutputStream.close();
            fileOutputStream = new FileOutputStream(destDir + uinOpposite + ".html", false);
            StringBuilder htmlBuilder = new StringBuilder();
            String htmlTitle = GlobalValues.HtmlFormattingText.HTML_TITLE.replace("{HOST_NICKNAME}", me.nickName).replace("{HOST_UIN}", me.uin).replace("{OPPOSITE_NICKNAME}", opposite.nickName).replace("{OPPOSITE_UIN}", opposite.uin);
            htmlBuilder.append(GlobalValues.HtmlFormattingText.HTML_FILE_HEADER.replace("{HTML_TITLE}", htmlTitle));
            htmlBuilder.append(topMessageStack.printToHtml(me));
            htmlBuilder.append(GlobalValues.HtmlFormattingText.HTML_FILE_FOOTER);
            fileOutputStream.write(htmlBuilder.toString().getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.err.println("未知错误");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("文件写出失败");
            e.printStackTrace();
        }
    }

    static String decryptChar(String data, String key) {
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

    static String decryptString(byte[] data, String key) throws UnsupportedEncodingException {
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

    static byte[] decryptProtobuf(byte[] data, String key) {
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
