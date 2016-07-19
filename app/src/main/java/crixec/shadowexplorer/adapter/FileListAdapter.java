package crixec.shadowexplorer.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import crixec.shadowexplorer.R;
import crixec.shadowexplorer.bean.FileItem;

/**
 * Created by crixe on 2016/7/12.
 */
public class FileListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<FileItem> lists = new ArrayList<FileItem>();
    private boolean hasUp;

    public FileListAdapter(Context mContext, ArrayList<FileItem> lists) {
        this.mContext = mContext;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setHasUp(boolean hasUp) {
        this.hasUp = hasUp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.file_item, parent, false);
            holder = new ViewHolder();
            holder.fileIcon = (ImageView) convertView.findViewById(R.id.file_item_file_icon);
            holder.fileName = (TextView) convertView.findViewById(R.id.file_item_file_name);
            holder.fileDetail = (TextView) convertView.findViewById(R.id.file_item_detail);
            holder.isSymlink = (TextView) convertView.findViewById(R.id.file_item_is_symlink);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FileItem file = lists.get(position);
        if (position == 0 && hasUp) {
            holder.fileName.setText("..");
            holder.fileDetail.setText("Up Directory");
            holder.fileIcon.setImageResource(R.drawable.fi_dir_up);
            holder.isSymlink.setVisibility(TextView.INVISIBLE);
        } else {
            if (file.isDirectory()) {
                holder.fileIcon.setImageResource(R.drawable.fi_dir);
            } else {
                if (file.isFile()) {
                    holder.fileIcon.setImageResource(R.drawable.fi_text);
                } else {
                    holder.fileIcon.setImageResource(R.drawable.fi_file);
                }
            }
            holder.fileName.setText(file.getName());
            holder.isSymlink.setVisibility(TextView.INVISIBLE);
            if (file.isSymlink()) {
                holder.isSymlink.setVisibility(TextView.VISIBLE);
            }
            String date = DateFormat.format("yy-MM-dd H:mm", new Date(file.lastModified())).toString();
            if (file.isFile())
                holder.fileDetail.setText(date + "  " + file.getFileSize());
            else
                holder.fileDetail.setText(date);

        }

        return convertView;
    }

    public static class ViewHolder {
        ImageView fileIcon;
        TextView fileName;
        TextView fileDetail;
        TextView isSymlink;
    }
}
