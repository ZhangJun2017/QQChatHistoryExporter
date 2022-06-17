public class GlobalValues {
    public static class HtmlFormattingText {
        //HTML
        public static final String HTML_FILE_HEADER = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><link rel=\"stylesheet\" type=\"text/css\" href=\"" + AssetsPath.CSS_FILE_PATH + "\"><title>{HTML_TITLE}</title></head><body>\n";
        public static final String HTML_FILE_FOOTER = "</body></html>";
        public static final String HTML_TITLE = "{HOST_NICKNAME}({HOST_UIN}) 与 {OPPOSITE_NICKNAME}({OPPOSITE_UIN}) 的聊天记录";
        public static final String CSS_CONTENT = ".message-received .message-head{color:blue;}.message-sent .message-head{color:green;}.message-received,.message-sent{padding-bottom:10px;}.message-content{padding-left:8px;}.message-head{font-size:14px;}.picture-message span img,.mixed-message span img{cursor:zoom-in;}.emotion{width:28px;height:28px;}.markface{width:150px;height:150px;}";
        public static final String ROUTER_HTML_CONTENT = "<meta http-equiv=\"refresh\" content=\"0;url={MAIN_HTML_FILE_URL}\">请等待重定向...";
        //Message
        public static final String MESSAGE_SENT_HTML = "<div class=\"message-sent\"><div class=\"message-head\"><span>{MESSAGE_HEADER}</span></div>{MESSAGE_CONTENT}</div>\n";
        public static final String MESSAGE_RECEIVED_HTML = "<div class=\"message-received\"><div class=\"message-head\"><span>{MESSAGE_HEADER}</span></div>{MESSAGE_CONTENT}</div>\n";
        public static final String TEXT_MESSAGE_HTML = "<div class=\"message-content text-message\"><span>{TEXT_MESSAGE_CONTENT}</span></div>";
        public static final String EMOTION_HTML = "<img class=\"emotion\" src=\"{EMOTION_SRC}\" alt=\"{EMOTION_ALT}\" title=\"{EMOTION_TITLE}\">";
        public static final String PICTURE_MESSAGE_HTML = "<div class=\"message-content picture-message\"><span>{PICTURE_MESSAGE_CONTENT}</span></div>";
        public static final String PICTURE_HTML = "<img src=\"{PICTURE_SRC}\" width=\"{PICTURE_WIDTH}\" height=\"{PICTURE_HEIGHT}\" alt=\"{PICTURE_ALT}\" title=\"{PICTURE_TITLE}\" onclick='window.open(\"{PICTURE_SRC}\")'/>";
        public static final String MIXED_MESSAGE_HTML = "<div class=\"message-content mixed-message\"><span>{MIXED_MESSAGE_CONTENT}</span></div>";
        public static final String POKE_MESSAGE_HTML = "<div class=\"message-content poke-message\"><span>{POKE_MESSAGE_CONTENT}</span></div>";
        public static final String FORWARD_MESSAGE_HTML = "<div class=\"message-content forward-message\"><span><a href=\"{FORWARD_MESSAGE_HTML_URL}\" target=\"_blank\">查看 {FORWARD_MESSAGE_COUNT} 条转发的消息</a></span></div>";
        public static final String VOICE_MESSAGE_HTML = "<div class=\"message-content voice-message\"><span>{VOICE_MESSAGE_CONTENT}</span></div>";
        public static final String VOICE_HTML = "{STT_TEXT}<br><audio src=\"{VOICE_SRC}\" controls title=\"{VOICE_TITLE}\"></audio>";
        public static final String MARKFACE_MESSAGE_HTML = "<div class=\"message-content markface-message\"><span>{MARKFACE_MESSAGE_CONTENT}</span></div>";
        public static final String MARKFACE_HTML = "<img class=\"markface\" src=\"{MARKFACE_SRC}\" alt=\"{MARKFACE_ALT}\" title=\"{MARKFACE_TITLE}\"/>";
    }

    public static class BatchFormattingText {
        public static final String BATCH_FILE_BODY = "@echo off\r\ntitle 请等待外部操作...\r\n{BATCH_FILE_BODY}\r\necho 完成";
    }

    public static class AssetsPath {
        //HTML formatting only,other usages may lead to unexpected behavior....
        public static final String PICTURE_PATH = "./../img/";
        public static final String CSS_FILE_PATH = "./../css/main.css";
        public static final String EMOTION_PATH = "./../emotion/";
        public static final String MAIN_HTML_FILE_PATH = "./assets/html/";
        public static final String HTML_FILE_PATH = "./";
        public static final String VOICE_PATH = "./../voice/";
    }
}
