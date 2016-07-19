package crixec.shadowexplorer.bean;

import java.io.File;
import java.io.IOException;

import crixec.shadowexplorer.util.FileUtils;

/**
 * Created by crixec on 2016/7/14.
 */
public class FileItem {
    private File file;

    public FileItem(File file) {
        this.file = file;
    }

    public File get() {
        return file;
    }


    public boolean isSymlink() {
        return FileUtils.isSymlink(file);
    }

    public boolean isSymlinkDirectory() {
        return FileUtils.isSymlinkDirectory(file);
    }

    public boolean isSymlinkFile() {
        return FileUtils.isSymlinkFile(file);
    }

    public boolean isDirectory() {
        if (isSymlink()) {
            if (!isSymlinkAvaiable()) {
                return false;
            }
        }
        if (isSymlink()) {
            return isSymlinkDirectory();
        }
        return file.isDirectory();
    }

    public boolean isFile() {
        if (isSymlink()) {
            if (!isSymlinkAvaiable()) {
                return true;
            }
        }
        if (isSymlink()) {
            return isSymlinkFile();
        }
        return file.isFile();
    }

    public boolean isSymlinkAvaiable() {
        if (isSymlink() && !getRealFile().isDirectory() && !getRealFile().isFile()) {
            return false;
        }
        return true;
    }

    public String getName() {
        return file.getName();
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public String getRealPath() {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public File getRealFile() {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getType() {
        if (isFile())
            return "FILE";
        if (isDirectory())
            return "DIRECTORY";
        return "NULL";
    }

    public boolean isValid() {
        if (!isFile() && !isDirectory()) {
            return false;
        }
        return true;
    }

    public boolean isInvalid() {
        return !isValid();
    }

    public long lastModified() {
        return file.lastModified();
    }

    public String getFileSize() {
        return FileUtils.formatFileSize(file.length(), "#.00");
    }
}
