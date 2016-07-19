package crixec.shadowexplorer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * Created by crixe on 2016/7/12.
 */
public class FileUtils {
    public static String getFileContent(File file) throws IOException {
        StringBuilder str = new StringBuilder();
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = br.readLine();
        if (line != null) {
            str.append(line);
            line = null;
        }
        while ((line = br.readLine()) != null) {
            str.append("\n" + line);
        }
        br.close();
        return str.toString();
    }

    public static void copyFile(File from, File to) throws IOException {
        InputStream in = new FileInputStream(from);
        OutputStream out = new FileOutputStream(to);
        byte[] buffer = new byte[1024 * 4096];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.close();
    }

    public static String formatFileSize(long fileS) {
        return formatFileSize(fileS, "#");
    }

    public static String formatFileSize(long fileS, String format) {
        DecimalFormat df = new DecimalFormat(format);
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static boolean isSymlink(File file) {
        boolean result = false;
        try {
            File canon = null;
            if (file == null) return false;
            if (file.getParent() == null) {
                canon = file;
            } else {
                File canonDir = file.getParentFile().getCanonicalFile();
                canon = new File(canonDir, file.getName());
            }
            result = !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isSymlinkFile(File file) {
        if (isSymlink(file)) {
            try {
                return file.getCanonicalFile().isFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.isFile();
    }

    public static boolean isSymlinkDirectory(File file) {
        if (isSymlink(file)) {
            try {
                return file.getCanonicalFile().isDirectory();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.isDirectory();
    }
}
