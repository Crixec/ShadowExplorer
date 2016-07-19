package crixec.shadowexplorer.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import crixec.shadowexplorer.R;
import crixec.shadowexplorer.explorer.AbsExplorer;
import crixec.shadowexplorer.explorer.Explorer;
import crixec.shadowexplorer.fragment.WindowFragment;
import crixec.shadowexplorer.util.StorageInfo;

public class MainActivity extends BaseActivity implements WindowFragment.OnDataChanged, View.OnClickListener {

    private WindowFragment leftWindow;
    private WindowFragment rightWindow;
    private AbsExplorer explorer;
    private String LEFT_WINDOW = "left";
    private String RIGHT_WINDOW = "right";
    private String CURRENT_WINDOW = LEFT_WINDOW;
    private TextView showPath;
    private TextView showSpace;
    private TextView showCount;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        explorer = new Explorer();
        explorer.init();
        showPath = (TextView) findViewById(R.id.topbar_path);
        showSpace = (TextView) findViewById(R.id.topbar_space_usage);
        showCount = (TextView) findViewById(R.id.topbar_files_count);
        leftWindow = WindowFragment.newInstance(explorer);
        rightWindow = WindowFragment.newInstance(explorer);
        leftWindow.setDefault(true);
        rightWindow.setDefault(false);
        leftWindow.setOnDataChanged(this);
        rightWindow.setOnDataChanged(this);
        leftWindow.setTag(LEFT_WINDOW);
        rightWindow.setTag(RIGHT_WINDOW);
        getFragmentManager().beginTransaction().add(R.id.left_layout, leftWindow).add(R.id.right_layout, rightWindow).commit();
        initBottomBar();
        refreshToolBars();
    }

    public WindowFragment getCurrentWindow() {
        if (CURRENT_WINDOW.equals(LEFT_WINDOW))
            return leftWindow;
        return rightWindow;
    }

    public void refreshToolBars() {
        showPath.setText(getCurrentWindow().getCurrentPath());
        showSpace.setText(getSpaceInfo(getCurrentWindow().getCurrentPath()));
        showCount.setText(String.format("Folders : %d,Files : %d", getCurrentWindow().getFoldersCount(), getCurrentWindow().getFilesCount()));
        if (CURRENT_WINDOW.equals(LEFT_WINDOW)) {
            bottomBar.sync.setIcon(R.drawable.sync_to_left);
        } else {
            bottomBar.sync.setIcon(R.drawable.sync_to_right);
        }
    }

    public void initBottomBar() {
        bottomBar = new BottomBar();
        bottomBar.bookmark = new BottomBarItem(R.id.bottombar_tool_bookmark).find();
        bottomBar.select = new BottomBarItem(R.id.bottombar_tool_sync).find();
        bottomBar.sync = new BottomBarItem(R.id.bottombar_tool_sync).find();
        bottomBar.menu = new BottomBarItem(R.id.bottombar_tool_menu).find();
        bottomBar.up = new BottomBarItem(R.id.bottombar_tool_up).find();
        bottomBar.bookmark.setOnClickListener(this);
        bottomBar.select.setOnClickListener(this);
        bottomBar.sync.setOnClickListener(this);
        bottomBar.menu.setOnClickListener(this);
        bottomBar.up.setOnClickListener(this);

    }

    public String getSpaceInfo(String path) {
        File file = new File(path);
        StorageInfo storageInfo = new StorageInfo(explorer, file);
        return String.format("%s used,%s free,%s", storageInfo.getUsedSpace(), storageInfo.getFreeSpace(), storageInfo.getPermission());
    }

    @Override
    public void onRefresh() {
        refreshToolBars();
    }

    @Override
    public void onTouch(View view) {
        String cur = (String) view.getTag();
        if (cur.equals(LEFT_WINDOW)) {
            leftWindow.setSelected(true);
            rightWindow.setSelected(false);
        } else {
            rightWindow.setSelected(true);
            leftWindow.setSelected(false);
        }
        CURRENT_WINDOW = cur;
        onRefresh();
    }

    @Override
    public void onBackPressed() {
        if (!getCurrentWindow().hasUpPath()) {
            super.onBackPressed();
        } else {
            getCurrentWindow().gotoUpPath();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottombar_tool_up: {
                getCurrentWindow().gotoUpPath();
                break;
            }
        }
    }

    public class BottomBar {
        BottomBarItem bookmark;
        BottomBarItem select;
        BottomBarItem sync;
        BottomBarItem menu;
        BottomBarItem up;
    }

    public class BottomBarItem {
        ViewGroup rootView;
        ImageView icon;
        TextView name;
        int id;

        private BottomBarItem(int id) {
            this.id = id;
        }

        public BottomBarItem find() {
            rootView = (ViewGroup) findViewById(id);
            icon = (ImageView) rootView.getChildAt(0);
            name = (TextView) rootView.getChildAt(1);
            return BottomBarItem.this;
        }

        public void setIcon(int resId) {
            icon.setImageResource(resId);
        }

        public void setName(CharSequence text) {
            name.setText(text);
        }

        public void setOnClickListener(View.OnClickListener ocl) {
            rootView.setOnClickListener(ocl);
        }

    }
}
