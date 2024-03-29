import java.util.Comparator;

public abstract class Message {
    public final Person sender;
    public final long time;
    public final long uniseq;
    public final long msgUid;

    public Message(Person sender, long time, long uniseq, long msgUid) {
        this.sender = sender;
        this.time = time;
        this.uniseq = uniseq;
        this.msgUid = msgUid;
    }

    @Override
    public abstract String toString();
    //return "<Message>[" + time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "]" + sender.nickName + "(" + sender.uin + "):";

    public void printToConsole() {
        System.out.println(this);
    }

    public abstract String printToHtml();

    public String getExternalOperationCmdline() {
        return "";
    }
}

class SequenceOrderedComparator implements Comparator<Message> {
    public int compare(Message o1, Message o2) {
        if (o1.time > o2.time) {
            return 1;
        } else if (o1.time < o2.time) {
            return -1;
        } else {
            if (o1.uniseq > o2.uniseq) {
                return 1;
            } else return -1;
        }
    }
}
