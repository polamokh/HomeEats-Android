package com.example.homeeats;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class FilesStorageDatabase {
    private static FilesStorageDatabase singletonObject;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    FilesStorageDatabase(){}

    public static FilesStorageDatabase GetInstance(){
        if (singletonObject == null)
            singletonObject = new FilesStorageDatabase();
        return singletonObject;
    }

    public void uploadPhoto(Bitmap image, final String path, final RetrievalEventListener<String> retrievalEventListener){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] imageData = baos.toByteArray();
        final StorageReference imageReference = mStorageRef.child(path);

        UploadTask uploadTask = imageReference.putBytes(imageData);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        retrievalEventListener.OnDataRetrieved(uri.toString());
                    }
                });

            }
        });
    }
}
