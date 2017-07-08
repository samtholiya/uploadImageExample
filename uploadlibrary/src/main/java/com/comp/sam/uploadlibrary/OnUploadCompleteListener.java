package com.comp.sam.uploadlibrary;

import okhttp3.Response;

/**
 * Created by shubh on 08-07-2017.
 */

public interface OnUploadCompleteListener {
    public void onUploadComplete(Response response);
}
