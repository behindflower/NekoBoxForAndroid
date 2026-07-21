package io.nekohasekai.sagernet.utils;

import android.content.Context;

import java.text.DecimalFormat;

public class FormatFileSize {

    public static String formatFileSize(Context context, long size, boolean iec) {
        if (size <= 0) return "0 B";
        final String[] units = iec
                ? new String[]{"B", "KiB", "MiB", "GiB", "TiB", "PiB"}
                : new String[]{"B", "kB", "MB", "GB", "TB", "PB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        if (digitGroups >= units.length) digitGroups = units.length - 1;
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}