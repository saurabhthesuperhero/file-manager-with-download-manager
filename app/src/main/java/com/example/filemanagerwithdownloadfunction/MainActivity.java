package com.example.filemanagerwithdownloadfunction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Adapter_recyclerview adapter;
    ArrayList<model_file> model_files = new ArrayList<>();
    private Fetch fetch;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();


        model_file file = new model_file("1", "bird ", "jpg", "https://static.remove.bg/sample-gallery/graphics/bird-thumbnail.jpg");
        model_file file2 = new model_file("2", "mars", "jpg", "https://d2pn8kiwq2w21t.cloudfront.net/original_images/missionswebmars_sample_return_yKNCEnM.jpg");
        model_file file3 = new model_file("3", "short  english stories for childern ", "pdf", "https://www.really-learn-english.com/support-files/english-short-stories-free.pdf");
//        model_file file4 = new model_file("4", "National labour force projections: 2020(base)–2073 s – CSV", "csv", "https://www.stats.govt.nz/assets/Uploads/National-labour-force-projections/National-labour-force-projections-2020base2073/Download-data/National-labour-force-projections-2020base-2073.csv.csv");
//        model_file file5 = new model_file("5", "sample docx file", "docx", "https://www.google.com/url?https://file-examples.com/wp-content/uploads/2017/02/file-sample_100kB.docx");
//        model_file file6 = new model_file("6", "ppt file 1", "ppt", "https://file-examples.com/wp-content/uploads/2017/08/file_example_PPT_250kB.ppt");
//        model_file file7 = new model_file("7", "xls file", "xlsx", "https://file-examples.com/wp-content/uploads/2017/02/file_example_XLS_100.xls");
//        model_file file8 = new model_file("8", "sample pdf 2", "pdf", "https://file-examples.com/wp-content/uploads/2017/10/file-example_PDF_500_kB.pdf");
//        model_file file9 = new model_file("9", "png image", "png", "https://file-examples.com/wp-content/uploads/2017/10/file_example_PNG_500kB.png");
//        model_file file10 = new model_file("10", "zip file", "zip", "https://file-examples.com/wp-content/uploads/2017/02/zip_2MB.zip");
        model_file file11 = new model_file("11", "yaro aisa he song file", "mp3", "https://wapking.asia/siteuploads/files/sfd5/2480/Yaaron%20Aisa%20Hai-Salim%20Merchant(MyMp3Song).mp3");
        model_file file14 = new model_file("14", "mahabharat pdf", "pdf", "https://www.holybooks.com/wp-content/uploads/Mahabharata-VOL-1.pdf");
//        model_file file12 = new model_file("12", "mp4 file", "mp4", "https://file-examples.com/wp-content/uploads/2017/04/file_example_MP4_480_1_5MG.mp4");


        model_files.add(file);
        model_files.add(file2);
        model_files.add(file3);
//        model_files.add(file4);
//        model_files.add(file5);
//        model_files.add(file6);
//        model_files.add(file7);
//        model_files.add(file8);
//        model_files.add(file9);
//        model_files.add(file10);
        model_files.add(file11);
        model_files.add(file14);
//        model_files.add(file12);


        RecyclerView recyclerView = findViewById(R.id.recyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_recyclerview(getApplicationContext(), model_files);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter_recyclerview.OnItemClickListener() {
            @Override
            public void onItemClick(model_file model_file) throws IOException {
//                Toast.makeText(MainActivity.this, model_file.getFileName(), Toast.LENGTH_SHORT).show();
                String url = model_file.fileUrl;
                String file = Environment.getExternalStorageDirectory() + "/Download/OMG/"+ model_file.getFileName() + "." + model_file.fileExt;
                File ff=new
                        File(file);
                if (isFileExist(file)) {

                    openFile(MainActivity.this,file);

                    return;
                }
                FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(MainActivity.this)
                        .setDownloadConcurrentLimit(50)
                        .build();

                fetch = Fetch.Impl.getInstance(fetchConfiguration);


                final Request request = new Request(url, file);
                Log.e(TAG, "onItemClick: "+url+" "+file );
                request.setPriority(Priority.HIGH);
                request.setNetworkType(NetworkType.ALL);

                fetch.enqueue(request, updatedRequest -> {
                    //Request was successfully enqueued for download.
                    Log.e(TAG, "onItemClick: "+request.getUrl() );
                }, error -> {
                    Log.e(TAG, "onItemClick: error "+error.getValue()+" "+error.name()+" " );
                    //An error occurred enqueuing the request.
                });

                fetch.addListener(fetchListener);

//Remove listener when done.
            }
        });


    }

    private boolean isFileExist(String path) {
        File file = new File(path);
        Log.e(TAG, "isFileExist: "+file.exists() );
        return  (file.exists() && file.getAbsoluteFile().exists());
//        return file.exists();
    }


    private final FetchListener fetchListener = new FetchListener() {

        @Override
        public void onWaitingNetwork(@NonNull Download download) {
            Log.e(TAG, "onWaitingNetwork: ");
        }

        @Override
        public void onStarted(@NonNull Download download, @NonNull List<? extends DownloadBlock> list, int i) {
            Log.e(TAG, "onStarted: ");

        }

        @Override
        public void onResumed(@NonNull Download download) {
            Log.e(TAG, "onResumed: ");
        }

        @Override
        public void onRemoved(@NonNull Download download) {
            Log.e(TAG, "onRemoved: ");
        }

        @Override
        public void onQueued(@NonNull Download download, boolean b) {
            Toast.makeText(MainActivity.this, "Started", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProgress(@NonNull Download download, long l, long l1) {
            Log.e(TAG, "onProgress: ");
        }

        @Override
        public void onPaused(@NonNull Download download) {
            Log.e(TAG, "onPaused: ");
        }

        @Override
        public void onError(@NonNull Download download, @NonNull Error error, @Nullable Throwable throwable) {
            Log.e("TAG", "onError: " + error.name() + " " + error.getValue());

        }

        @Override
        public void onDownloadBlockUpdated(@NonNull Download download, @NonNull DownloadBlock downloadBlock, int i) {
            Log.e(TAG, "onDownloadBlockUpdated: " + downloadBlock.getDownloadId());
        }

        @Override
        public void onDeleted(@NonNull Download download) {
            Log.e(TAG, "onDeleted: ");
        }

        @Override
        public void onCompleted(@NonNull Download download) {
            adapter.notifyDataSetChanged();

            Log.e(TAG, "onCompleted: ");
        }

        @Override
        public void onCancelled(@NonNull Download download) {
            Log.e(TAG, "onCancelled: ");
        }

        @Override
        public void onAdded(@NonNull Download download) {
            Log.e(TAG, "onAdded: ");
        }
    };

    public static void openFile(Context context, String  url) throws IOException {
        // Create URI
        File f= new File(url);
        Log.e("TAG", "openFile: "+url );
        Uri uri = Uri.parse(f.getPath());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx") || url.toString().contains(".csv")) {
            // Excel file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/zip");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "image/*");
//            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "video/*");
        } else {

            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "*/*");
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Storage permission is requires,please allow from settings", Toast.LENGTH_SHORT).show();
        } else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 111);
    }

}