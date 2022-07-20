package com.example.filemanagerwithdownloadfunction;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<model_file> model_files = new ArrayList<>();
    private Fetch fetch;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();


        model_file file = new model_file("1", "Adventure", "jpg", "https://media-cdn.tripadvisor.com/media/photo-s/0c/bb/a3/97/predator-ride-in-the.jpg");
        model_file file2 = new model_file("2", "html logo for image ", "jpg", "https://i.ytimg.com/vi/PDfWeUP09TA/hqdefault.jpg");
        model_file file3 = new model_file("3", "short stories for childern ", "pdf", "https://www.nipccd.nic.in/uploads/page/Short-stories-from-100-Selected-Storiespdf-958b29ac59dc03ab693cca052b4036e2.pdf");
        model_file file4 = new model_file("4", "National labour force projections: 2020(base)–2073 – CSV", "csv", "https://www.stats.govt.nz/assets/Uploads/National-labour-force-projections/National-labour-force-projections-2020base2073/Download-data/National-labour-force-projections-2020base-2073.csv.csv");


        model_files.add(file);
        model_files.add(file2);
        model_files.add(file3);
        model_files.add(file4);


        RecyclerView recyclerView = findViewById(R.id.recyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter_recyclerview adapter = new Adapter_recyclerview(getApplicationContext(), model_files);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter_recyclerview.OnItemClickListener() {
            @Override
            public void onItemClick(model_file model_file) {
//                Toast.makeText(MainActivity.this, model_file.getFileName(), Toast.LENGTH_SHORT).show();
                String url = model_file.fileUrl;
                String file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + model_file.getFileName() + "." + model_file.fileExt;

                if (isFileExist(file)) {
                    Toast.makeText(MainActivity.this, "exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(MainActivity.this)
                        .setDownloadConcurrentLimit(3)
                        .build();

                fetch = Fetch.Impl.getInstance(fetchConfiguration);


                final Request request = new Request(url, file);
                request.setPriority(Priority.HIGH);
                request.setNetworkType(NetworkType.ALL);

                fetch.enqueue(request, updatedRequest -> {
                    //Request was successfully enqueued for download.
                }, error -> {
                    //An error occurred enqueuing the request.
                });

                fetch.addListener(fetchListener);

//Remove listener when done.
            }
        });


    }

    private boolean isFileExist(String path) {
        File file = new File(path);
        return file.isFile();
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
            Toast.makeText(MainActivity.this, "errror", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
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


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Storage permission is requires,please allow from settings", Toast.LENGTH_SHORT).show();
        } else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
    }

}