public class Picture {
    public String md5;
    public String localPath;
    public String fileName;
    public String width;
    public String height;
    private RichMsgHandle.PicRec parser = null;

    public Picture() {
        localPath = "[图片未解析]";
        fileName = "Cache_" + Long.toHexString(0);
        width = "auto";
        height = "auto";
    }

    public Picture(RichMsgHandle.PicRec parser) {
        this.parser = parser;
        md5 = parser.getMd5();
        localPath = parser.getLocalPath();
        printOptimize();
        String CRC64Value = String.valueOf(CRC64.crc64Long(("chatimg:" + md5)));
        fileName = "Cache_" + (CRC64Value.startsWith("-") ? "-" : "") + Long.toHexString(Long.valueOf(CRC64Value.replace("-", "")));
    }

    @Override
    public String toString() {
        return GlobalValues.HtmlFormattingText.PICTURE_HTML.replace("{PICTURE_SRC}", GlobalValues.AssetsPath.PICTURE_PATH + fileName).replace("{PICTURE_ALT}", "[图片]").replace("{PICTURE_TITLE}", localPath).replace("{PICTURE_WIDTH}", width).replace("{PICTURE_HEIGHT}", height);
    }

    public String getExternalOperationCmdline() {
        return "copy /Y \"<sourceDir>\\chatpic\\chatimg\\" + fileName.substring(fileName.length() - 3) + "\\" + fileName + "\" \"<destDir>\\img\\" + fileName + "\" >nul\r\n";
    }

    private void printOptimize() {
        int mode = 0;
        switch (mode) {
            case 0: {
                int tempWidth = parser.getUint32Width();
                int tempHeight = parser.getUint32Height();
                if (tempWidth == 0 || tempHeight == 0) {
                    if (tempWidth == tempHeight) {
//                      :(
                        width = "auto";
                        height = "144";
                    } else {
                        width = tempWidth == 0 ? "auto" : "144";
                        height = tempHeight == 0 ? "auto" : "144";
                    }
                } else {
                    int shorterSide = Math.min(tempWidth, tempHeight);
                    if (shorterSide <= 72) {
                        width = String.valueOf(tempWidth);
                        height = String.valueOf(tempHeight);
                    } else if (shorterSide < 720) {
                        width = tempWidth == shorterSide ? "72" : "auto";
                        height = tempHeight == shorterSide ? "72" : "auto";
                    } else {
                        width = tempWidth == shorterSide ? "144" : "auto";
                        height = tempHeight == shorterSide ? "144" : "auto";
                    }
                }
                //muddy but it works....
                break;
            }
            case 1: {
                int tempWidth = parser.getUint32ThumbWidth();
                int tempHeight = parser.getUint32ThumbHeight();
                width = tempWidth == 0 ? "auto" : String.valueOf(tempWidth);
                height = tempHeight == 0 ? "auto" : String.valueOf(tempHeight);
                break;
            }
        }
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
