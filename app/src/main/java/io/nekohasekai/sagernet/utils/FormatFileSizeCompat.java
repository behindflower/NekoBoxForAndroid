package io.nekohasekai.sagernet.utils;

import android.content.Context;

public class FormatFileSizeCompat {

    public static String formatFileSize(Context context, long size, boolean iec) {
        return FormatFileSize.formatFileSize(context, size, iec);
    }

}