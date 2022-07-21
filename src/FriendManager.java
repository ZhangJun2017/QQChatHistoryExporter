import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class FriendManager {
    public HashMap<String, String> friendNameMap;
    public HashMap<String, String> friendRemarkMap;
    public HashMap<String, HashMap<String, String>> friendMapMultiMsg;
    public boolean isMultiMsgMode;
    public String multiMsgUniseq;

    public FriendManager() {
        friendNameMap = new HashMap<>();
        friendRemarkMap = new HashMap<>();
        friendMapMultiMsg = new HashMap<>();
        isMultiMsgMode = false;
        multiMsgUniseq = "";
    }

    public Person getPersonByUinPrefill(String uin) {
        if (isMultiMsgMode) {
            return getPersonByUinMultiMsg(uin, multiMsgUniseq);
        } else {
            return getPersonByUin(uin);
        }
    }

    public Person getPersonByUinMultiMsg(String uin, String uniseq) {
        if (!getNameByUinMultiMsg(uin, uniseq).equals("")) {
            return new Person(uin, getNameByUinMultiMsg(uin, uniseq));
        } else if (!getNameByUin(uin).equals("")) {
            return new Person(uin, getNameByUin(uin));
        } else {
            return new Person(uin, uin);
        }
    }

    public String getNameByUinMultiMsg(String uin, String uniseq) {
        if (friendMapMultiMsg.containsKey(uniseq)) {
            if (friendMapMultiMsg.get(uniseq).containsKey(uin)) {
                return friendMapMultiMsg.get(uniseq).get(uin);
            }
        }
        return "";
    }

    public Person getPersonByUin(String uin) {
        if (!getRemarkIfExistByUin(uin).equals("")) {
            return new Person(uin, getRemarkIfExistByUin(uin));
        } else {
            return new Person(uin, uin);
        }
    }

    public String getRemarkIfExistByUin(String uin) {
        if (!getRemarkByUin(uin).equals("")) {
            return getRemarkByUin(uin);
        } else {
            return getNameByUin(uin);
        }
    }

    public String getNameByUin(String uin) {
        return friendNameMap.getOrDefault(uin, "");
    }

    public String getRemarkByUin(String uin) {
        return friendRemarkMap.getOrDefault(uin, "");
    }

    public void fetchFriends(ResultSet rs, String key) throws SQLException {
        while (rs.next()) {
            String uin = RawMessage.decryptChar(rs.getString("uin"), key);
            friendNameMap.put(uin, RawMessage.decryptChar(rs.getString("name"), key));
            friendRemarkMap.put(uin, RawMessage.decryptChar(rs.getString("remark"), key));
        }
    }

    public void fetchMultiMsgFriends(ResultSet rs, String key) throws SQLException {
        while (rs.next()) {
            String uin = RawMessage.decryptChar(rs.getString("uin"), key);
            String nick = RawMessage.decryptChar(rs.getString("nick"), key);
            String uniseq = rs.getString("uniseq");
            friendMapMultiMsg.putIfAbsent(uniseq, new HashMap<>());
            friendMapMultiMsg.get(uniseq).put(uin, nick);
        }
    }
}
