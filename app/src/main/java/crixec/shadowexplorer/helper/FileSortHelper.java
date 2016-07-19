package crixec.shadowexplorer.helper;

import java.util.Comparator;
import java.util.HashMap;

import crixec.shadowexplorer.bean.FileItem;

/**
 * Created by crixec on 16-7-15.
 */
public class FileSortHelper {

    private enum SortMethod {name, size, date, type}

    int BIG;
    int SMALL;

    private HashMap<SortMethod, Comparator<FileItem>> methods = new HashMap<SortMethod, Comparator<FileItem>>();

    public FileSortHelper(boolean isAscending) {
        setAscending(isAscending);
    }

    public FileSortHelper() {
        this(true);
        methods.put(SortMethod.name, new NameComparator());
        methods.put(SortMethod.date, new DateComparator());
        methods.put(SortMethod.size, new SizeComparator());
        methods.put(SortMethod.type, new TypeComparator());
    }

    private void setAscending(boolean isAscending) {
        if (isAscending) {
            BIG = -1;
            SMALL = 1;
        } else {
            BIG = 1;
            SMALL = -1;
        }
    }

    public Comparator getDefaultComparator() {
        setAscending(true);
        return methods.get(SortMethod.name);
    }

    class NameComparator implements Comparator<FileItem> {
        @Override
        public int compare(FileItem f1, FileItem f2) {
            if ((f1.isFile() && f2.isDirectory()) || (f2.isValid() && f1.isInvalid())) {
                return SMALL;
            } else if ((f2.isFile() && f1.isDirectory()) || (f1.isValid() && f2.isInvalid())) {
                return BIG;
            }
            return compareString(f1.getName(), f2.getName());
        }
    }

    public int compareString(String s1, String s2) {
        int result = s1.compareToIgnoreCase(s2);
        if (result > 0)
            return SMALL;
        if (result < 0)
            return BIG;
        return result;
    }

    class DateComparator implements Comparator<FileItem> {
        @Override
        public int compare(FileItem f1, FileItem f2) {
            return 0;
        }
    }

    class SizeComparator implements Comparator<FileItem> {
        @Override
        public int compare(FileItem f1, FileItem f2) {
            return 0;
        }
    }

    class TypeComparator implements Comparator<FileItem> {
        @Override
        public int compare(FileItem f1, FileItem f2) {
            return 0;
        }
    }

}
