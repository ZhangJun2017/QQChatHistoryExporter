public class MarkFace {
    public String faceName;
    public int dwTabID;
    public byte[] sbufID;
    public int imageHeight;
    public int imageWidth;
    public String url;

    public MarkFace(String faceName) {
        this.faceName = "[表情]" + faceName;
        dwTabID = -1;
        url = "";
    }

    public MarkFace(com.tencent.mobileqq.data.MarkFaceMessage markFaceMessage) {
        faceName = markFaceMessage.faceName;
        dwTabID = markFaceMessage.dwTabID;
        sbufID = markFaceMessage.sbufID;
        imageHeight = markFaceMessage.imageHeight;
        imageWidth = markFaceMessage.imageWidth;
        url = "https://i.gtimg.cn/club/item/parcel/item/" + toHexString(sbufID).substring(0, 2) + "/" + toHexString(sbufID) + "/" + imageWidth + "x" + imageHeight + ".png";
    }

    public static String toHexString(byte[] arr) {
        StringBuffer toReturn = new StringBuffer();
        for (byte b : arr) {
            String byteHex = "00" + Integer.toHexString(b & 0xff);
            toReturn.append(byteHex.substring(byteHex.length() - 2));
        }
        return toReturn.toString();
    }

    @Override
    public String toString() {
        return GlobalValues.HtmlFormattingText.MARKFACE_HTML.replace("{MARKFACE_SRC}", url).replace("{MARKFACE_ALT}", faceName).replace("{MARKFACE_TITLE}", dwTabID + " -> " + faceName);
    }
}
