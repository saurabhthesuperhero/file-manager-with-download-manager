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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_recyclerview extends RecyclerView.Adapter<Adapter_recyclerview.ViewHolder> {
    Context context;
    private OnItemClickListener listener;

    ArrayList<Model_file> Model_files;

    public Adapter_recyclerview(Context context, ArrayList<Model_file> Model_files) {
        this.context = context;
        this.Model_files = Model_files;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_recyclerview.ViewHolder holder, int position) {

        Model_file selectedFile = Model_files.get(position);
        holder.textView.setText(selectedFile.getFileName() + "." + selectedFile.fileExt);
        String file = Environment.getExternalStorageDirectory() + "/Download/OMG/"+ selectedFile.getFileName() + "." + selectedFile.fileExt;


        if (isFileExist(file)) {
            //check for different filetypes
            File f = new File(file);
            Date lastModDate = new Date(f.lastModified());
            holder.date.setText(lastModDate.toString());
            holder.date.setVisibility(View.VISIBLE);
            String ext = selectedFile.fileExt;
            if (ext.equals("pdf")) {
                holder.imageView.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24);
            } else if (selectedFile.fileExt.equals("jpg") || selectedFile.fileExt.equals("png") || selectedFile.fileExt.equals("jpeg") || selectedFile.fileExt.equals("gif")) {
                holder.imageView.setImageURI(Uri.parse(file));
            } else if (ext.contains("mp3") || ext.contains("wav")) {
                holder.imageView.setImageResource(R.drawable.ic_baseline_music_note_24);

            } else if (ext.contains("3gp") || ext.toString().contains("mpg") || ext.toString().contains("mpeg") || ext.toString().contains("mpe") || ext.toString().contains("mp4") || ext.toString().contains("avi")) {
                holder.imageView.setImageResource(R.drawable.ic_baseline_videocam_24);

            } else {
                holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);

            }

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
        return Model_files.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView, date;
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
                        try {
                            listener.onItemClick(Model_files.get(position));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Model_file model_file) throws IOException;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}