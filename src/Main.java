import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
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
        final String DB_URL = "jdbc:sqlite:" + sourceDir + "\\" + dbFileName;
        final String DB_URL_slowtable = "jdbc:sqlite:" + sourceDir + "\\" + dbSlowTableFileName;
        ResultSet rs;
        try {
            String targetMD5 = new BigInteger(1, MessageDigest.getInstance("md5").digest(uinOpposite.getBytes())).toString(16).toUpperCase();
            Connection connection = DriverManager.getConnection(DB_URL);
            Connection connectionSlowTable = DriverManager.getConnection(DB_URL_slowtable);
            Statement statement = connection.createStatement();
            //Statement statementSlowTable = connectionSlowTable.createStatement();
            HashMap<String, Person> friendMap = fetchFriends(statement.executeQuery("SELECT uin,name,remark FROM Friends"), key);
            HashMap<String, HashMap<String, Person>> friendMapMultiMsg = fetchMultiMsgFriends(statement.executeQuery("SELECT uin,nick,uniseq FROM MultiMsgNick"), key);
            /*
            StringBuilder multiMsgQueryBuilder = new StringBuilder("(");

            rs = statement.executeQuery("SELECT uniseq FROM mr_friend_<TARGET>_New WHERE msgtype='-2011'".replace("<TARGET>", targetMD5));
            while (rs.next()) {
                multiMsgQueryBuilder.append("'" + rs.getString("uniseq") + "',");
            }
            try {
                rs = statementSlowTable.executeQuery("SELECT uniseq FROM mr_friend_<TARGET>_New WHERE msgtype='-2011'".replace("<TARGET>", targetMD5));
                while (rs.next()) {
                    multiMsgQueryBuilder.append("'" + rs.getString("uniseq") + "',");
                }
            } catch (SQLException e) {
            }
            rs = statement.executeQuery("SELECT uniseq FROM mr_multimessage WHERE msgtype='-2011'");
            while (rs.next()) {
                multiMsgQueryBuilder.append("'" + rs.getString("uniseq") + "',");
            }
            multiMsgQueryBuilder.replace(multiMsgQueryBuilder.length() - 1, multiMsgQueryBuilder.length(), ")");
            ResultSet multiMessageList = connection.createStatement().executeQuery("SELECT * FROM mr_multimessage WHERE msgseq IN " + multiMsgQueryBuilder.toString());
             */
            ResultSet multiMessageList = connection.createStatement().executeQuery("SELECT * FROM mr_multimessage");
            ResultSet messageList = connection.createStatement().executeQuery("SELECT * FROM mr_friend_<TARGET>_New".replace("<TARGET>", targetMD5));
            ResultSet messageListSlowTable = null;
            try {
                messageListSlowTable = connectionSlowTable.createStatement().executeQuery("SELECT * FROM mr_friend_<TARGET>_New".replace("<TARGET>", targetMD5));
            } catch (SQLException e) {
                //slowtable.db might not have that table since the regular database is enough to store messages
            }
            MessageStack topMessageStack = MessageStack.process(messageList, messageListSlowTable, MessageStack.parseMultiMsg(multiMessageList), friendMap, friendMapMultiMsg, key);
            writeFile(GlobalValues.HtmlFormattingText.ROUTER_HTML_CONTENT.replace("{MAIN_HTML_FILE_URL}", GlobalValues.AssetsPath.MAIN_HTML_FILE_PATH + uinOpposite + ".html"), destDir + "\\" + uinOpposite + ".html");
            writeFile(new MessageWrapper(topMessageStack, friendMap.get(uinSelf), friendMap.get(uinOpposite)).printToHtml(), destDir + "\\assets\\html\\" + uinOpposite + ".html");
            writeFile(GlobalValues.HtmlFormattingText.CSS_CONTENT, destDir + "\\assets\\css\\main.css");
            writeBatchFile(GlobalValues.BatchFormattingText.BATCH_FILE_BODY.replace("{BATCH_FILE_BODY}", topMessageStack.getExternalOperationCmdline().replace("<sourceDir>", assetDir).replace("<destDir>", destDir + "\\assets")), destDir + "\\exop.bat");
            Runtime.getRuntime().exec("cmd.exe /c start cmd.exe /c " + destDir + "\\exop.bat");  //show a dialog to user
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
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

    static void writeFile(String content, String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path, false);
        fileOutputStream.write(content.getBytes("UTF-8"));
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    static void writeBatchFile(String content, String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path, false);
        fileOutputStream.write(content.getBytes("GB2312"));
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    static HashMap<String, Person> fetchFriends(ResultSet rs, String key) throws SQLException {
        HashMap<String, Person> toReturn = new HashMap<>();
        while (rs.next()) {
            String uin = RawMessage.decryptChar(rs.getString("uin"), key);
            String name = RawMessage.decryptChar(rs.getString("name"), key);
            String remark = RawMessage.decryptChar(rs.getString("remark"), key);
            toReturn.put(uin, new Person(uin, remark.equals("") ? name : remark));
        }
        return toReturn;
    }

    static HashMap<String, HashMap<String, Person>> fetchMultiMsgFriends(ResultSet rs, String key) throws SQLException {
        HashMap<String, HashMap<String, Person>> toReturn = new HashMap<>();
        while (rs.next()) {
            String uin = RawMessage.decryptChar(rs.getString("uin"), key);
            String nick = RawMessage.decryptChar(rs.getString("nick"), key);
            String uniseq = rs.getString("uniseq");
            toReturn.putIfAbsent(uniseq, new HashMap<>());
            toReturn.get(uniseq).put(uin, new Person(uin, nick));
        }
        return toReturn;
    }
}
