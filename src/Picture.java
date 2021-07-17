/**
 * QQChatHistoryExporter/
 * Description:
 *
 * @author:ZhangJun2017
 * @date:2021/7/9
 */

public class Picture {
    public String md5;
    public String localPath = "[图片未解析]";
    public String fileName;
    private RichMsgHandle.PicRec parser = null;

    /*
    public Picture(byte[] rawData) {
        try {
            parser = RichMsgHandle.PicRec.parseFrom(rawData);
            thumbHeight = parser.getUint32ThumbHeight();
            thumbWidth = parser.getUint32ThumbWidth();
            md5 = parser.getMd5();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
    */

    public Picture() {

    }

    public Picture(RichMsgHandle.PicRec parser) {
        this.parser = parser;
        md5 = parser.getMd5();
        localPath = parser.getLocalPath();
        String CRC64Value = String.valueOf(CRC64.crc64Long(("chatimg:" + md5)));
        if (CRC64Value.startsWith("-")) {
            CRC64Value.replace("-", "");
            fileName = "Cache_-" + Long.toHexString(Long.valueOf(CRC64Value));
        } else {
            fileName = "Cache_" + Long.toHexString(Long.valueOf(CRC64Value));
        }
    }

    @Override
    public String toString() {
        return GlobalValues.HtmlFormattingText.PICTURE_HTML.replace("{PICTURE_SRC}", GlobalValues.AssetsPath.PICTURE_PATH + fileName).replace("{PICTURE_ALT}", "[图片](" + localPath + ")");
    }

    public String getExternalOperationCmdline() {
        /*if (parser != null) {
            String[] minusOperator = {"", "-"};
            String[] valueArray = String.valueOf(CRC64.crc64Long(("chatimg:" + md5))).split("-");
            fileName = "Cache_" + minusOperator[valueArray.length - 1] + Long.toHexString(Long.valueOf(valueArray[valueArray.length - 1]));
            outputString = fileName;
        }*/
        return "copy /Y <sourceDir>\\" + fileName.substring(fileName.length() - 3) + "\\" + fileName + " <destDir>\\" + fileName + "\r\n";
    }

}

//copied from http://www.java2s.com/example/java/security/a-function-that-returns-a-64bit-crc-for-string.html
class CRC64 {
    private static final long[] CRCTable = new long[256];
    private static final long POLY64REV = 0x95AC9329AC4BC9B5L;
    private static final long INITIALCRC = 0xFFFFFFFFFFFFFFFFL;
    private static boolean init = false;

    /**
     * A function that returns a 64-bit crc for string
     *
     * @param in input string
     * @return 64-bit crc value
     */
    public static final long crc64Long(String in) {
        if (in == null || in.length() == 0) {
            return 0;
        }
        long crc = INITIALCRC, part;
        if (!init) {
            for (int i = 0; i < 256; i++) {
                part = i;
                for (int j = 0; j < 8; j++) {
                    int value = ((int) part & 1);
                    if (value != 0) {
                        part = (part >> 1) ^ POLY64REV;
                    } else {
                        part >>= 1;
                    }
                }
                CRCTable[i] = part;
            }
            init = true;
        }
        int length = in.length();
        for (int k = 0; k < length; ++k) {
            char c = in.charAt(k);
            crc = CRCTable[(((int) crc) ^ c) & 0xff] ^ (crc >> 8);
        }
        return crc;
    }
}