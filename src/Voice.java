public class Voice {
    public String localPath;
    public String sttText;
    public int voiceLength;
    public String filePath = "";
    public String fileName;
    private RichMsgHandle.PttRec parser = null;

    public Voice() {
        localPath = "";
        sttText = "无效语音消息";
    }

    public Voice(RichMsgHandle.PttRec parser) {
        this.parser = parser;
        localPath = parser.getLocalPath();
        sttText = parser.getSttText();
        voiceLength = parser.getVoiceLength();
        String[] pathArr = localPath.split("/");
        for (int i = pathArr.length - 2; i >= 0; i--) {
            if (pathArr[i].equals("ptt")) {
                for (int j = i + 1; j < pathArr.length - 1; j++) {
                    filePath += pathArr[j] + "\\";
                }
                fileName = pathArr[pathArr.length - 1].substring(0, pathArr[pathArr.length - 1].length() - 4);
                filePath += fileName;
                i = -1;
            }
        }
    }

    @Override
    public String toString() {
        return GlobalValues.HtmlFormattingText.VOICE_HTML.replace("{VOICE_SRC}", GlobalValues.AssetsPath.VOICE_PATH + fileName + ".mp3").replace("{VOICE_TITLE}", "时长 " + voiceLength + "秒 的语音消息").replace("{STT_TEXT}", sttText);
    }

    public String getExternalOperationCmdline() {
        return "copy /Y \"<sourceDir>\\ptt\\" + filePath + ".slk\" \"" + System.getProperty("java.io.tmpdir") + "\\" + fileName + ".slk\" >nul\r\n" + "<silkDecoder> \"" + System.getProperty("java.io.tmpdir") + "\\" + fileName + ".slk\" \"" + System.getProperty("java.io.tmpdir") + "\\" + fileName + ".pcm" + "\" -quiet>nul\r\n" + "<ffmpeg> -loglevel quiet -y -f s16le -ar 24000 -ac 1 -i \"" + System.getProperty("java.io.tmpdir") + "\\" + fileName + ".pcm" + "\" -ar 24000 -b:a 320k \"" + System.getProperty("java.io.tmpdir") + "\\" + fileName + ".mp3\" >nul\r\n" + "move /Y \"" + System.getProperty("java.io.tmpdir") + "\\" + fileName + ".mp3" + "\" \"<destDir>\\voice\\" + fileName + ".mp3" + "\" >nul\r\n" + "del \"" + System.getProperty("java.io.tmpdir") + "\\" + fileName + ".slk\" \"" + System.getProperty("java.io.tmpdir") + "\\" + fileName + ".pcm\" >nul\r\n";
    }
}
