import com.tencent.mobileqq.data.MarkFaceMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MarkFace {
    public String faceName;
    public int dwTabID;
    public byte[] sbufID;
    public int imageHeight;
    public int imageWidth;
    public String url;

    public MarkFace(byte[] data) {
        try {
            MarkFaceMessage markFaceMessage = (MarkFaceMessage) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
            faceName = markFaceMessage.faceName;
            dwTabID = markFaceMessage.dwTabID;
            sbufID = markFaceMessage.sbufID;
            imageHeight = markFaceMessage.imageHeight;
            imageWidth = markFaceMessage.imageWidth;
            try {
                url = "https://i.gtimg.cn/club/item/parcel/item/" + toHexString(sbufID).substring(0, 2) + "/" + toHexString(sbufID) + "/" + imageWidth + "x" + imageHeight + ".png";
            } catch (StringIndexOutOfBoundsException e) {
                //reply to some specific messages in PCQQ using markface will lead to invalid markface message
                faceName = "[表情]" + markFaceMessage.faceName;
                dwTabID = markFaceMessage.dwTabID;
                url = "";
                e.printStackTrace();
            }
        } catch (ClassNotFoundException | IOException e) {
            faceName = "[表情]" + hanziExtract(new String(data));
            dwTabID = -1;
            url = "";
            e.printStackTrace();
        }
    }

    public static String hanziExtract(String str) {
        return str.replaceAll("[^\u4e00-\u9fa5]", "");
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
