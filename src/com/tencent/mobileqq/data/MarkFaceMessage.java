package com.tencent.mobileqq.data;

import java.io.Serializable;

public class MarkFaceMessage implements Serializable {
    public static final long serialVersionUID = 102222;
    public String faceName;
    public int dwTabID;
    public byte[] sbufID;
    public int imageHeight;
    public int imageWidth;
}
