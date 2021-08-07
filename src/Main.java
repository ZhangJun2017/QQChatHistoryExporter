import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello QQ!");
        Scanner scanner = new Scanner(System.in);
        System.out.print("数据库文件所在路径：");
        String sourceDir = scanner.nextLine().replace("/", "\\");
        System.out.print("外置资源所在路径：");
        String assetDir = scanner.nextLine().replace("/", "\\");
        System.out.print("密钥：");
        String key = scanner.nextLine();
        System.out.print("自己的QQ号：");
        String uinSelf = scanner.nextLine();
        System.out.print("对方的QQ号：");
        String uinOpposite = scanner.nextLine();
        System.out.println("请等待...");
        String destDir = sourceDir;
        String dbFileName = uinSelf + ".db";
        String dbSlowTableFileName = "slowtable_" + dbFileName;
        final String Class_Name = "org.sqlite.JDBC";
        final String DB_URL = "jdbc:sqlite:" + sourceDir + "\\" + dbFileName;
        final String DB_URL_slowtable = "jdbc:sqlite:" + sourceDir + "\\" + dbSlowTableFileName;
        Connection connection;
        HashMap<String, String> friendMap = new HashMap<>();
        MessageStack topMessageStack = new MessageStack();
        try {
            Class.forName(Class_Name);
            connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("SELECT uin,name,remark FROM Friends");
            while (rs.next()) {
                friendMap.put(decryptChar(rs.getString("uin"), key), (decryptChar(rs.getString("remark"), key).equals("") ? decryptChar(rs.getString("name"), key) : decryptChar(rs.getString("remark"), key)));
            }
            Person me = new Person(uinSelf, friendMap.get(uinSelf));
            Person opposite = new Person(uinOpposite, friendMap.get(uinOpposite));
            Person toStore;
            for (int i = 0; i < 2; i++) {
                connection = DriverManager.getConnection(i == 0 ? DB_URL : DB_URL_slowtable);
                statement = connection.createStatement();
                try {
                    rs = statement.executeQuery("SELECT msgData,senderuin,time,msgtype,uniseq FROM mr_friend_<TARGET>_New".replace("<TARGET>", new BigInteger(1, MessageDigest.getInstance("md5").digest(uinOpposite.getBytes())).toString(16).toUpperCase()));
                } catch (SQLException e) {
                    //slowtable.db might not have that table since the regular database is enough to store messages
                }
                while (rs.next()) {
                    toStore = decryptString(rs.getBytes("senderuin"), key).equals(uinSelf) ? me : opposite;
                    switch (rs.getString("msgtype")) {
                        case "-1000":
                        case "-1049":
                        case "-1051":
                            topMessageStack.add(new TextMessage(toStore, Long.valueOf(rs.getString("time")), Long.valueOf(rs.getString("uniseq")), decryptString(rs.getBytes("msgData"), key)));
                            break;
                        case "-2000":
                            topMessageStack.add(new PictureMessage(toStore, Long.valueOf(rs.getString("time")), Long.valueOf(rs.getString("uniseq")), decryptProtobuf(rs.getBytes("msgData"), key)));
                            break;
                        case "-1035":
                            topMessageStack.add(new MixedMessage(toStore, Long.valueOf(rs.getString("time")), Long.valueOf(rs.getString("uniseq")), (decryptProtobuf(rs.getBytes("msgData"), key))));
                            break;
                        case "-5012":
                        case "-5018":
                            topMessageStack.add(new PokeMessage(toStore, Long.valueOf(rs.getString("time")), Long.valueOf(rs.getString("uniseq")), decryptString(rs.getBytes("msgData"), key)));
                            break;
                        default:
                            topMessageStack.add(new TextMessage(toStore, Long.valueOf(rs.getString("time")), Long.valueOf(rs.getString("uniseq")), "不支持的消息类型：" + rs.getString("msgtype")));
                            break;
                    }
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(destDir + "\\exop.bat", false);
            fileOutputStream.write(GlobalValues.BatchFormattingText.BATCH_FILE_BODY.replace("{BATCH_FILE_BODY}", topMessageStack.getExternalOperationCmdline().replace("<sourceDir>", assetDir).replace("<destDir>", destDir + "\\assets")).getBytes("GB2312"));
            fileOutputStream.flush();
            fileOutputStream.close();
            //Runtime.getRuntime().exec("cmd.exe /c start cmd.exe /c " + destDir + "\\exop.bat");  //show a dialog to user
            fileOutputStream = new FileOutputStream(destDir + "\\assets\\css\\main.css", false);
            fileOutputStream.write(GlobalValues.HtmlFormattingText.CSS_CONTENT.getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();
            fileOutputStream = new FileOutputStream(destDir + "\\" + uinOpposite + ".html", false);
            StringBuilder htmlBuilder = new StringBuilder();
            String htmlTitle = GlobalValues.HtmlFormattingText.HTML_TITLE.replace("{HOST_NICKNAME}", me.nickName).replace("{HOST_UIN}", me.uin).replace("{OPPOSITE_NICKNAME}", opposite.nickName).replace("{OPPOSITE_UIN}", opposite.uin);
            htmlBuilder.append(GlobalValues.HtmlFormattingText.HTML_FILE_HEADER.replace("{HTML_TITLE}", htmlTitle));
            ArrayList emotionConfig = new Gson().fromJson(new JsonParser().parse(new JsonReader(new InputStreamReader(Main.class.getResourceAsStream("face_config.json")))).getAsJsonObject().get("sysface").getAsJsonArray(), new TypeToken<List<QEmotion>>() {
            }.getType());
            htmlBuilder.append(topMessageStack.replaceEmotion(topMessageStack.printToHtml(me), emotionConfig));
            htmlBuilder.append(GlobalValues.HtmlFormattingText.HTML_FILE_FOOTER);
            fileOutputStream.write(htmlBuilder.toString().getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (ClassNotFoundException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.err.println("未知错误");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("数据库异常");
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

    static String decryptString(byte[] data, String key) {
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
