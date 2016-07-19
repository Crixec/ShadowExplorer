package crixec.shadowexplorer.explorer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import crixec.shadowexplorer.util.FileUtils;

/**
 * Created by crixe on 2016/7/11.
 */
public class Explorer extends AbsExplorer {
    @Override
    public void init() {

    }

    @Override
    public File[] listFiles(File file) {
        File[] files = file.listFiles();
        return files == null ? new File[0] : files;
    }

    @Override
    public File[] listFiles(File file, FileFilter fileFilter) {
        File[] files = file.listFiles(fileFilter);
        return files == null ? new File[0] : files;
    }


    @Override
    public String[] list(File file) {
        if (file.isDirectory()) return new String[0];
        String[] files = file.list();
        return files == null ? new String[0] : files;
    }

    @Override
    public boolean delete(File file) {
        return file.delete();
    }

    @Override
    public boolean deleteAll(File file) {
        for (File wfile : listFiles(file)) {
            if (wfile.isDirectory() && listFiles(wfile).length > 0) {
                if (!deleteAll(wfile)) {
                    return false;
                }
            } else {
                if (!delete(wfile)) {
                    return false;
                }
            }
        }
        return delete(file);
    }

    @Override
    public boolean create(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean renameTo(File file, File newFile) {
        return file.renameTo(newFile);
    }

    @Override
    public boolean move(File file, File newFile) {
        if (file.isFile() || list(file).length == 0) {
            newFile.getParentFile().mkdirs();
            return renameTo(file, newFile);
        } else {
            for (File wfile : listFiles(file)) {
                if (!move(wfile, new File(newFile, wfile.getName()))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean copy(File from, File to) {
        try {
            FileUtils.copyFile(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean copyAll(File from, File to) {
        if (from.isDirectory()) {
            if (!from.exists()) {
                from.mkdir();
            }
            String files[] = from.list();
            for (String file : files) {
                File srcFile = new File(from, file);
                File destFile = new File(to, file);
                copyAll(srcFile, destFile);
            }
        } else {
            return copy(from, to);
        }
        return false;
    }

    // the two methods chmodXX cannot be archive
    // it need root permission
    @Override
    public boolean chmod(File file, String modeCode) {
        return false;
    }

    @Override
    public boolean chmodAll(File file, String modeCode) {
        return false;
    }

    @Override
    public boolean exists(File file) {
        return file.exists();
    }

    @Override
    public boolean canExecute(File file) {
        return file.canExecute();
    }

    @Override
    public boolean canRead(File file) {
        return file.canRead();
    }

    @Override
    public boolean canWrite(File file) {
        return file.canWrite();
    }
}
