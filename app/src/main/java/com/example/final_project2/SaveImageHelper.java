package com.example.final_project2;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveImageHelper implements Target {

    private Context context;
    private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private String desc;

    public SaveImageHelper(Context context, AlertDialog mDialogWeekReference, ContentResolver contentResolverWeakReference, String name, String desc) {
        this.context = context;
        this.alertDialogWeakReference = new WeakReference<AlertDialog>(mDialogWeekReference);
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolverWeakReference);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContentResolver r = contentResolverWeakReference.get();
        AlertDialog dialog = alertDialogWeakReference.get();
        if (r != null) {
            MediaStore.Images.Media.insertImage(r, bitmap, name, desc);
        }
        dialog.dismiss();

        //open galerry after download
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivity(Intent.createChooser(intent, "VIEW PICTURE"));
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
