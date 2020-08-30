package com.se17.complainapp;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddComplainActivity extends AppCompatActivity {

    Context ctx = AddComplainActivity.this;

    private ImageView capturedImage;
    private Button captureImageBtn, submitComplainBtn;
    private Spinner cateogryInput, severityInput;
    private EditText descriptionInput;


    private static final int REQUEST_CODE_PERM = 1;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2;
    private String currentImagePath;

    private StorageReference mStorageRef;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complain);

        submitComplainBtn = findViewById(R.id.submitComplainBtn);
        cateogryInput = findViewById(R.id.categorySpinner);
        severityInput = findViewById(R.id.severitySpinner);
        descriptionInput = findViewById(R.id.description);

        //creating database
        mSQLiteHelper = new SQLiteHelper(this, "COMPLAIN.sqlite", null, 5);

        //creating table in database
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS COMPLAIN_RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, category VARCHAR, " +
                "severity VARCHAR, description VARCHAR, complainImage BLOB)");


        submitComplainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mSQLiteHelper.insertData(
                            cateogryInput.getSelectedItem().toString().trim(),
                            severityInput.getSelectedItem().toString().trim(),
                            descriptionInput.getText().toString().trim(),
                            imageViewToByte(capturedImage)
                    );
                    Toast.makeText(ctx, "Added successfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ctx,MainActivity.class));
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        //saving image in firebase storage
        mStorageRef = FirebaseStorage.getInstance().getReference("complain_uploads");

        capturedImage = findViewById(R.id.capturedimageIV);
        captureImageBtn = findViewById(R.id.captureimageBtn);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddComplainActivity.this, new String[]{
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERM
                    );
                }else {
                    dispatchCaptureImageIntent();
                }
            }
        });

    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void dispatchCaptureImageIntent(){
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(in.resolveActivity(getPackageManager())!= null){
            File imageFile = null;
            try {
                imageFile = createImageFile();
            }catch (IOException exception){
                Toast.makeText(ctx,exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(ctx, "com.se17.complainapp.fileprovider", imageFile);
                in.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(in, REQUEST_CODE_CAPTURE_IMAGE);
            }
        }
    }


    //method for saving image file in gallery
    private File createImageFile() throws IOException {

        String fileName = "IMAGE_" + new SimpleDateFormat(
                "yyy_MM_dd_MM_mm_ss", Locale.getDefault()).format(new Date());

        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(fileName,".jpg",directory);

        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERM && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchCaptureImageIntent();
            }else {
                Toast.makeText(ctx, "Not all permissions granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == RESULT_OK){
            try {
                //Display Image in ImageView
                 capturedImage.setImageBitmap(BitmapFactory.decodeFile(currentImagePath));

                //captured image file
                File capturedImageFile = new File(currentImagePath);


            }catch (Exception exception){
                Toast.makeText(ctx, "Not all permission granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private Bitmap getScaledBitmap(ImageView imageView){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int scaleFactor = Math.min(options.outWidth / imageView.getWidth(), options.outHeight / imageView.getHeight());
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        return BitmapFactory.decodeFile(currentImagePath, options);
    }


}
