package com.example.filemanagerwithdownloadfunction;

import static android.app.DownloadManager.STATUS_RUNNING;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;

public class DUtils {

    private DownloadManager downloadManager;
    DownloadManager.Query myDownloadQuery = new DownloadManager.Query();

    public void DownloadStuff(Context context,String url,Model_file model_file){
        String file = Environment.getExternalStorageDirectory() + "/Download/OMG/" + model_file.getFileName() + "." + model_file.fileExt;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(model_file.getFileName());
        request.setDescription("Downloading");//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(new File(file)));
        request.setAllowedOverMetered(true);
        model_file.setdRef(downloadManager.enqueue(request));

        myDownloadQuery.setFilterById(model_file.getdRef());

        Cursor cursor = downloadManager.query(myDownloadQuery);
        if (cursor.moveToFirst()) {
            checkStatus(cursor,context);
        }
    }

    private void checkStatus(Cursor cursor,Context context) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

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


        Toast toast = Toast.makeText(context,
                statusText + "\n" +
                        reasonText,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();

    }

    public int checkDownloadStatus(Model_file model_file,Context context){
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        myDownloadQuery.setFilterById(model_file.getdRef());
        Cursor cursor = downloadManager.query(myDownloadQuery);
        if (cursor.moveToFirst())
        {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            return status;
        }


        return 0;
    }


}
