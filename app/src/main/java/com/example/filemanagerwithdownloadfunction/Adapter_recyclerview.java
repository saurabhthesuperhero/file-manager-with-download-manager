package com.example.filemanagerwithdownloadfunction;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_recyclerview extends RecyclerView.Adapter<Adapter_recyclerview.ViewHolder> {
    Context context;
    private OnItemClickListener listener;

    ArrayList<model_file> model_files;

    public Adapter_recyclerview(Context context, ArrayList<model_file> model_files) {
        this.context = context;
        this.model_files = model_files;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_recyclerview.ViewHolder holder, int position) {

        model_file selectedFile = model_files.get(position);
        holder.textView.setText(selectedFile.getFileName() + "." + selectedFile.fileExt);

        String file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + selectedFile.getFileName() + "." + selectedFile.fileExt;
        if (isFileExist(file)) {
            //check for different filetypes
            File f = new File(file);
            Date lastModDate = new Date(f.lastModified());
            holder.date.setText(lastModDate.toString());
            holder.date.setVisibility(View.VISIBLE);
            holder.imageView.setImageURI(Uri.parse(file));
        } else {
            holder.imageView.setImageResource(R.drawable.download_icon);
        }


    }


    private boolean isFileExist(String path) {
        File file = new File(path);
        return file.isFile();
    }

    @Override
    public int getItemCount() {
        return model_files.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView,date;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.file_name_text_view);
            date = itemView.findViewById(R.id.file_date);
            imageView = itemView.findViewById(R.id.icon_view);
            imageView = itemView.findViewById(R.id.icon_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(model_files.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(model_file model_file);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}