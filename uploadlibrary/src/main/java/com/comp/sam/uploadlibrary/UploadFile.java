package com.comp.sam.uploadlibrary;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by shubh on 08-07-2017.
 */

public class UploadFile {
    private static String TAG = "comp.sam.uploadlibrary";
    private OnUploadExceptionListener mOnUploadExceptionListener;
    private String mUrl;
    private MediaType mMediaType;
    private String mName;
    private String mFileName;
    private File mFile;
    private OnUploadCompleteListener mOnUploadCompleteListener;

    public UploadFile(){}

    public OnUploadExceptionListener getOnUploadExceptionListener() {
        return mOnUploadExceptionListener;
    }

    public String getUrl() {
        return mUrl;
    }

    public MediaType getMediaType() {
        return mMediaType;
    }

    public String getName() {
        return mName;
    }

    public String getFileName() {
        return mFileName;
    }

    public File getFile() {
        return mFile;
    }

    public OnUploadCompleteListener getOnUploadCompleteListener() {
        return mOnUploadCompleteListener;
    }

    public UploadFile(String url){
        mUrl = url;
    }

    public static UploadFile Builder(){
        return new UploadFile();
    }

    public UploadFile setContentType(MediaType mediaType){
        mMediaType = mediaType;
        return this;
    }

    public UploadFile setContentType(String mediaType){
        mMediaType = MediaType.parse(mediaType);
        return this;
    }

    public UploadFile setUrl(String url){
        mUrl = url;
        return this;
    }

    public UploadFile setName(String name){
        mName = name;
        return this;
    }

    public UploadFile setFileName(String fileName){
        mFileName = fileName;
        return this;
    }

    public UploadFile addOnUploadExceptionListener(OnUploadExceptionListener onUploadExceptionListener){
        mOnUploadExceptionListener = onUploadExceptionListener;
        return this;
    }

    public UploadFile addOnUploadCompleteListener(OnUploadCompleteListener onUploadCompleteListener){
        mOnUploadCompleteListener = onUploadCompleteListener;
        return this;
    }

    public UploadFile setFile(File file) {
        mFile = file;
        return this;
    }

    public void start(){

    }

    private class AsyncUpload extends AsyncTask<Void,Exception,Response>{

        @Override
        protected void onProgressUpdate(Exception... values) {
            super.onProgressUpdate(values);
            mOnUploadExceptionListener.onUploadError(values[0]);
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response!=null){
                mOnUploadCompleteListener.onUploadComplete(response);
            }
        }

        @Override
        protected Response doInBackground(Void... params) {
            try {
                RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(mName,mFileName, RequestBody.create(mMediaType, mFile)).build();

                Request request = new Request.Builder()
                        .url(mUrl)
                        .post(req)
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                return response;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
                publishProgress(e);
            } catch (Exception e) {
                Log.e(TAG, "Other Error: " + e);
                publishProgress(e);
            }
            return null;
        }
    }
}
