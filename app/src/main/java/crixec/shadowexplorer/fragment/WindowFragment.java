package crixec.shadowexplorer.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import crixec.shadowexplorer.R;
import crixec.shadowexplorer.adapter.FileListAdapter;
import crixec.shadowexplorer.bean.FileItem;
import crixec.shadowexplorer.explorer.AbsExplorer;
import crixec.shadowexplorer.helper.FileSortHelper;

/**
 * Created by crixec on 16-7-16.
 */
public class WindowFragment extends Fragment implements ListView.OnItemClickListener {
    private static final int MAX_HISTORY = 10;
    private View rootView;
    private ListView listView;
    private View selectedView;
    private ArrayList<FileItem> fileItems = new ArrayList<FileItem>();
    private static AbsExplorer explorer;
    private File currentFile = Environment.getExternalStorageDirectory();
    private FileListAdapter adapter;
    private FileSortHelper fileSortHelper;
    private Comparator comparator;
    private boolean isDefault = false;
    private OnDataChanged onDataChanged;
    public String tag = "";
    private int foldersCount = 0;
    private int filesCount = 0;
    private HashMap<String, Parcelable> pathHistory = new HashMap<String, Parcelable>(MAX_HISTORY);
    private static final int PRE_EXECUTE = 1001;
    private static final int POST_EXECUTE = 1002;


    public WindowFragment() {
    }

    public static WindowFragment newInstance(AbsExplorer sExplorer) {
        explorer = sExplorer;
        return new WindowFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_window, container, false);
        findViews();
        fileSortHelper = new FileSortHelper();
        comparator = fileSortHelper.getDefaultComparator();
        refresh();
        return rootView;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private void findViews() {
        listView = (ListView) findViewById(R.id.window_listview);
        selectedView = findViewById(R.id.window_selected_view);
        if (isDefault)
            selectedView.setVisibility(View.VISIBLE);
        else
            selectedView.setVisibility(View.INVISIBLE);
        adapter = new FileListAdapter(getActivity(), fileItems);
        adapter.setHasUp(hasUpPath());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setTag(tag);
                onDataChanged.onTouch(view);
                return false;
            }
        });
    }

    public void setSelected(boolean selected) {
        if (selectedView != null) {
            if (selected)
                selectedView.setVisibility(View.VISIBLE);
            else
                selectedView.setVisibility(View.INVISIBLE);
        }
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean hasUpPath() {
        return !getCurrentPath().equals("/");
    }

    public void gotoUpPath() {
        if (hasUpPath()) {
            gotoPath(getCurrentFile().getParentFile());
        }
    }


    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getActivity());
    }

    public View findViewById(int id) {
        return rootView.findViewById(id);
    }

    public OnDataChanged getOnDataChanged() {
        return onDataChanged;
    }

    public void setOnDataChanged(OnDataChanged onDataChanged) {
        this.onDataChanged = onDataChanged;
    }

    public View getRootView() {
        return rootView;
    }

    public String getCurrentPath() {
        return currentFile.getPath();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (hasUpPath() && i == 0) {
            gotoPath(getCurrentFile().getParentFile());
        } else {
            FileItem fileItem = fileItems.get(i);
            if (fileItem.isDirectory()) {
                gotoPath(fileItem.get());
            } else if (fileItem.isFile()) {
                openFile(fileItem.get());
            }
        }
    }

    private void gotoPath(File parentFile) {
        if (parentFile == null) return;
        int size = pathHistory.size();
        if (size == MAX_HISTORY) {
            pathHistory.remove(0);
        }
        pathHistory.put(getCurrentPath(), listView.onSaveInstanceState());
        currentFile = parentFile;
        refresh();
    }

    private void openFile(File file) {

    }

    private void refresh() {
        new UpdateList().execute();
    }

    public static interface OnDataChanged {
        public void onRefresh();

        public void onTouch(View view);
    }

    public int getFilesCount() {
        return filesCount;
    }

    public int getFoldersCount() {
        return foldersCount;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PRE_EXECUTE) {
                fileItems.clear();
            } else if (msg.what == POST_EXECUTE) {
                adapter.setHasUp(hasUpPath());
                adapter.notifyDataSetChanged();
                onDataChanged.onRefresh();
                listView.setSelection(0);
                if (pathHistory.containsKey(getCurrentPath())) {
                    listView.onRestoreInstanceState(pathHistory.get(getCurrentPath()));
                }
            }
        }
    };

    class UpdateList implements Runnable {

        public void execute() {
            new Thread(this).start();
        }

        @Override
        public void run() {

            Looper.prepare();
            handler.sendEmptyMessage(PRE_EXECUTE);
            foldersCount = 0;
            filesCount = 0;
            File[] files = explorer.listFiles(getCurrentFile());
            if (files == null)
                files = new File[]{};
            for (File f : files) {
                FileItem item = new FileItem(f);
                fileItems.add(item);
                if (item.isDirectory()) {
                    foldersCount++;
                } else {
                    filesCount++;
                }
            }
            Collections.sort(fileItems, comparator);
            if (hasUpPath()) {
                fileItems.add(0, new FileItem(getCurrentFile()));
            }
            handler.sendEmptyMessage(POST_EXECUTE);
        }

    }

}
