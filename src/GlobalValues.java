/**
 * QQChatHistoryExporter/
 * Description:
 *
 * @author:ZhangJun2017
 * @date:2021/7/17
 */

public class GlobalValues {
    public static class HtmlFormattingText {
        //HTML
        public static final String HTML_FILE_HEADER = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><link rel=\"stylesheet\" type=\"text/css\" href=\"" + AssetsPath.CSS_FILE_PATH + "\"><title>{HTML_TITLE}</title></head><body>\n";
        public static final String HTML_FILE_FOOTER = "</body></html>";
        public static final String HTML_TITLE = "{HOST_NICKNAME}({HOST_UIN}) 与 {OPPOSITE_NICKNAME}({OPPOSITE_UIN}) 的聊天记录";
        public static final String CSS_CONTENT = ".message-received .message-head{color:blue;}.message-sent .message-head{color:green;}.message-received,.message-sent{padding-bottom:10px;}.message-content{padding-left:8px;}.message-head{font-size:14px;}";
        //Message
        public static final String MESSAGE_SENT_HTML = "<div class=\"message-sent\"><div class=\"message-head\"><span>{MESSAGE_HEADER}</span></div>{MESSAGE_CONTENT}</div>\n";
        public static final String MESSAGE_RECEIVED_HTML = "<div class=\"message-received\"><div class=\"message-head\"><span>{MESSAGE_HEADER}</span></div>{MESSAGE_CONTENT}</div>\n";
        public static final String TEXT_MESSAGE_HTML = "<div class=\"message-content text-message\"><span>{TEXT_MESSAGE_CONTENT}</span></div>";
        public static final String PICTURE_MESSAGE_HTML = "<div class=\"message-content picture-message\"><span>{PICTURE_MESSAGE_CONTENT}</span></div>";
        public static final String PICTURE_HTML = "<img src=\"{PICTURE_SRC}\"  alt=\"{PICTURE_ALT}\"/>";
    }

    public static class BatchFormattingText {
        public static final String BATCH_FILE_BODY = "@echo off\r\ntitle 请等待外部操作...\r\n{BATCH_FILE_BODY}\r\necho 完成";
    }

    public static class AssetsPath {
        public static final String PICTURE_PATH = "./assets/img/";
        public static final String CSS_FILE_PATH = "./assets/css/main.css";
        //public static final String FORWARD_MESSAGE_CSS_FILE_PATH = "../css/main.css";
    }
}
