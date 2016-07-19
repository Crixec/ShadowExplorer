package crixec.shadowexplorer.explorer;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by crixe on 2016/7/11.
 */
public abstract class AbsExplorer {


    public static final int TYPE_DIRECTORY = 8001;
    public static final int TYPE_FILE = 8002;
    public static final int TYPE_UNKNOW = 8003;


    abstract public void init();

    abstract public File[] listFiles(File file);

    abstract public File[] listFiles(File file, FileFilter fileFilter);

    abstract public String[] list(File file);

    abstract public boolean delete(File file);

    abstract public boolean deleteAll(File file);

    abstract public boolean create(File file);

    abstract public boolean renameTo(File file, File newFile);

    abstract public boolean move(File file, File newFile);

    abstract public boolean copy(File from, File to);

    abstract public boolean copyAll(File from, File to);

    abstract public boolean chmod(File file, String modeCode);

    abstract public boolean chmodAll(File file, String modeCode);

    abstract public boolean exists(File file);

    abstract public boolean canExecute(File file);

    abstract public boolean canRead(File file);

    abstract public boolean canWrite(File file);
}
