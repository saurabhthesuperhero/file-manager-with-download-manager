package com.example.filemanagerwithdownloadfunction;

import static android.app.DownloadManager.STATUS_PAUSED;
import static android.app.DownloadManager.STATUS_RUNNING;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Adapter_recyclerview adapter;
    ArrayList<Model_file> Model_files = new ArrayList<>();
    private final String TAG = "MainActivity";

    private static MainActivity instance;
    private  DUtils dUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        instance = this;
        dUtils=new DUtils();
        Model_file file = new Model_file("1", "bird ", "jpg", "https://static.remove.bg/sample-gallery/graphics/bird-thumbnail.jpg");
        Model_file file2 = new Model_file("2", "mars", "jpg", "https://d2pn8kiwq2w21t.cloudfront.net/original_images/missionswebmars_sample_return_yKNCEnM.jpg");
        Model_file file3 = new Model_file("3", "short  english stories for childern ", "pdf", "https://www.really-learn-english.com/support-files/english-short-stories-free.pdf");
        Model_file file4 = new Model_file("4", "pdf file", "pdf", "http://www.lancsngfl.ac.uk/cmsmanual/getfile.php?src=2/PhotoStory3+info.pdf");
        Model_file file5 = new Model_file("5", "pdf file 22 ", "pdf", "http://www.lancsngfl.ac.uk/cmsmanual/getfile.php?src=2/PhotoStory3+info.pdf");
        Model_file file6 = new Model_file("6", "ppt file 323", "ppt", "https://filesamples.com/samples/document/ppt/sample2.ppt");
        Model_file file9 = new Model_file("9", "doc file ", "docx", "https://filesamples.com/samples/document/doc/sample2.doc");
        Model_file file11 = new Model_file("11", "yaro aisa he song file", "mp3", "https://wapking.asia/siteuploads/files/sfd5/2480/Yaaron%20Aisa%20Hai-Salim%20Merchant(MyMp3Song).mp3");
        Model_file file14 = new Model_file("14", "mahabharat pdf", "pdf", "https://www.holybooks.com/wp-content/uploads/Mahabharata-VOL-1.pdf");
        Model_file file12 = new Model_file("12", "2 yaro aisa he song file 2 2 ", "mp3", "https://wapking.asia/siteuploads/files/sfd5/2480/Yaaron%20Aisa%20Hai-Salim%20Merchant(MyMp3Song).mp3");
        Model_file file13 = new Model_file("13", "apk he ye", "apk", "https://www.djjubeemedia.appboxes.co/Apks/Tools/AlonsZee%20v1.0.3.apk");
        Model_file file18 = new Model_file("18", "zip ", "zip", "https://www.wa4e.com/code/crud.zip");
        Model_file file19 = new Model_file("19", "exe file he ye  ksdnjk ", "exe", "http://pub.agrarix.net/Windows/Acrobat/sgc10_rdr80_DLM_en_US.exe");
        Model_file file20 = new Model_file("20", "html file ", "html", "https://filesamples.com/samples/code/html/sample2.html");
        Model_file file21 = new Model_file("21", "pata nahi kya ye", "pfb", "https://filesamples.com/formats/pfb");


        Model_files.add(file);
        Model_files.add(file2);
        Model_files.add(file3);
        Model_files.add(file4);
        Model_files.add(file5);
        Model_files.add(file6);

        Model_files.add(file9);
        Model_files.add(file11);
        Model_files.add(file14);
        Model_files.add(file12);
        Model_files.add(file13);
        Model_files.add(file18);
        Model_files.add(file19);
        Model_files.add(file20);
        Model_files.add(file21);


        RecyclerView recyclerView = findViewById(R.id.recyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_recyclerview(getApplicationContext(), Model_files);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter_recyclerview.OnItemClickListener() {
            @Override
            public void onItemClick(Model_file model_file) throws IOException {
//                Toast.makeText(MainActivity.this, model_file.getFileName(), Toast.LENGTH_SHORT).show();
                String url = model_file.fileUrl;
                String file = Environment.getExternalStorageDirectory() + "/Download/OMG/" + model_file.getFileName() + "." + model_file.fileExt;

                if (isFileExist(file)) {

                        int status = dUtils.checkDownloadStatus(model_file,MainActivity.this);
                        Log.e(TAG, "onItemClick: status "+status );
                        if (status == STATUS_RUNNING || status==STATUS_PAUSED) {
                            Toast.makeText(MainActivity.this, "Download in progress", Toast.LENGTH_SHORT).show();

                        }else {
                            try{

                           openFile(MainActivity.this, file);

                            } catch (ActivityNotFoundException e) {
                                                Toast.makeText(MainActivity.this, "Cant open file check via file Manager", Toast.LENGTH_SHORT).show();

                            }
                        }
                } else {
                    dUtils.DownloadStuff(MainActivity.this,url,model_file);

                }
            }
        });


    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void updateView() {
        adapter.notifyDataSetChanged();
    }

    private boolean isFileExist(String path) {
        File file = new File(path);
        Log.e(TAG, "isFileExist: " + file.exists());
        Log.e(TAG, "isFileFile: " + file.isFile());
        return (file.exists() && file.getAbsoluteFile().exists());
//        return file.exists();
    }

    private void checkStatus(Cursor cursor) {

        //column for status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
        String filename = cursor.getString(filenameIndex);

        String statusText = "";
        String reasonText = "";

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch (reason) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch (reason) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "Download_started";
                break;
            case STATUS_RUNNING:
                statusText = "Download_Running";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "Successful download";
                reasonText = "Filename:\n" + filename;
                break;
        }


        Toast toast = Toast.makeText(MainActivity.this,
                statusText + "\n" +
                        reasonText,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();

    }


    public static void openFile(Context context, String url) throws IOException {
        // Create URI
        File f = new File(url);
        Log.e("TAG", "openFile: " + url);
        Uri uri = Uri.parse(f.getPath());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx") || url.toString().contains(".csv")) {
            // Excel file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/zip");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".webp") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "image/*");
//            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "video/*");
        } else {

            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
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
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 111);
    }

}