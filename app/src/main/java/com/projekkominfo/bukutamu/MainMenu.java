package com.projekkominfo.bukutamu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainMenu extends AppCompatActivity {

        // Folder path for Firebase Storage.
        String Storage_Path = "Scan_Surat_Buku_Tamu/";

        // Root Database Name for Firebase Database.
        String Database_Path = "Buku Tamu";

        // Creating button.
        Button gambar, submit, clear;

        // Creating EditText.
        EditText nik, nama, alamat, pihak, tujuan ;
        TextView tanggal;
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat mdformat = new SimpleDateFormat("EEEE, dd MMMM yyyy ");
        String strDate = mdformat.format(calendar.getTime());

        // Creating ImageView.
        ImageView IvImage;

        // Creating URI.
        Uri FilePathUri;

        // Creating StorageReference and DatabaseReference object.
        StorageReference storageReference;
        DatabaseReference databaseReference;

        StorageTask mUploadTask;

        // Image request code for onActivityResult() .
        int Image_Request_Code = 7;
        ProgressDialog progressDialog ;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mainmenu);

            // Assign FirebaseStorage instance to storageReference.
            storageReference = FirebaseStorage.getInstance().getReference();

            // Assign FirebaseDatabase instance with root database name.
            databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

            //Assign ID'S to button.
            gambar = findViewById(R.id.etGambar);
            submit = findViewById(R.id.etSubmit);
            clear = findViewById(R.id.etbersih);

            // Assign ID's to EditText.
            nik = findViewById(R.id.etNik);
            nama = findViewById(R.id.etNama);
            alamat = findViewById(R.id.etAlamat);
            pihak = findViewById(R.id.etPihak);
            tanggal = findViewById(R.id.tanggal);
            tujuan = findViewById(R.id.etTujuan);
            tanggal.setText(strDate);

            // Assign ID'S to image view.
            IvImage = findViewById(R.id.image_view);

            // Assigning Id to ProgressDialog.
            progressDialog = new ProgressDialog(MainMenu.this);

            // Adding click listener to Choose image button.
            gambar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Creating intent.
                    Intent intent = new Intent();
                    // Setting intent type as image to select image from phone storage.
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), Image_Request_Code);
                    }

            });


            // Adding click listener to Upload image button.
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(MainMenu.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                    } else {
                        UploadImageFileToFirebaseStorage();
                    }
                }
            });

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent c = new Intent(MainMenu.this, MainData.class);
                    startActivity(c);
                    finish();
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

                FilePathUri = data.getData();

                Picasso.with(this).load(FilePathUri).into(IvImage);

                try {
                    // Getting selected image into Bitmap.
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                    // Setting up bitmap selected image into ImageView.
                    IvImage.setImageBitmap(bitmap);
                    // After selecting image change choose button above text.
                    gambar.setText("Scan Surat");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }}
        }

        // Creating Method to get the selected image file Extension from File Path URI.
        public String GetFileExtension(Uri uri) {

            ContentResolver contentResolver = getContentResolver();

            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

            // Returning the file Extension.
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

        }

        // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
        public void UploadImageFileToFirebaseStorage() {

            if(TextUtils.isEmpty(nik.getText().toString())){
                Toast.makeText(this, "Masukkan NIK terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(nama.getText().toString())){
                Toast.makeText(this, "Masukkan nama terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(pihak.getText().toString())){
                Toast.makeText(this, "Masukkan pihak terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(tujuan.getText().toString())){
                Toast.makeText(this, "Masukkan tujuan terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Checking whether FilePathUri Is empty or not.
            if (FilePathUri != null) {

                // Setting progressDialog Title.
                progressDialog.setTitle("Sedang mengunggah data");

                // Showing progressDialog.
                progressDialog.show();

                // Creating second StorageReference.
                StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

                // Adding addOnSuccessListener to second StorageReference.
                storageReference2nd.putFile(FilePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                // Getting image name from EditText and store into string variable.
                                String TempImageName = nik.getText().toString().trim();
                                String TempNama = nama.getText().toString().trim();
                                String TempAlamat = alamat.getText().toString().trim();
                                String TempPihak = pihak.getText().toString().trim();
                                String TempTanggal = tanggal.getText().toString().trim();
                                String TempTujuan = tujuan.getText().toString().trim();

                                // Hiding the progressDialog after done uploading.
                                progressDialog.dismiss();

                                // Showing toast message after done uploading.
                                Toast.makeText(getApplicationContext(), "Berhasil diunggah", Toast.LENGTH_LONG).show();
                                nik.setText("");
                                nama.setText("");
                                alamat.setText("");
                                pihak.setText("");
                                tujuan.setText("");

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful());

                                Uri downloadUrl = uriTask.getResult();

                                @SuppressWarnings("VisibleForTests")
                                ImageUploadInfo imageUploadInfo;
                                imageUploadInfo = new ImageUploadInfo(TempImageName, TempNama, TempAlamat, TempPihak, TempTanggal, TempTujuan, downloadUrl.toString());

                                // Getting image upload ID.
                                String ImageUploadId = databaseReference.push().getKey();

                                // Adding image upload id s child element into databaseReference.
                                assert ImageUploadId != null;
                                databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                            }
                        })
                        // If something goes wrong .
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                                // Hiding the progressDialog.
                                progressDialog.dismiss();

                                // Showing exception erro message.
                                Toast.makeText(MainMenu.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })

                        // On progress change upload time.
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                // Setting progressDialog Title.
                                progressDialog.setTitle("Sedang mengunggah data");
                            }
                        });
            }
            else {
                Toast.makeText(MainMenu.this, "Mohon melampirkan gambar jika ada.", Toast.LENGTH_LONG).show();
            }
        }
    }
