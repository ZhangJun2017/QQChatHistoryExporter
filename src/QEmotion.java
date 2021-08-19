public class QEmotion {
    private final String QSid;
    private final String QDes;
    private final String IQLid;
    private final String AQLid;
    private final String EMCode;

    public QEmotion() {
        QSid = "9999";
        QDes = "未知表情";
        IQLid = "9999";
        AQLid = "9999";
        EMCode = "9999";
    }

    public String getQSid() {
        return QSid;
    }

    public String getQDes() {
        return QDes;
    }

    public String getIQLid() {
        return IQLid;
    }

    public String getAQLid() {
        return AQLid;
    }

    public String getEMCode() {
        return EMCode;
    }
}
