package io.socket.com.example.mohamedabdelhady.tasks.souqcomadmin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Connection;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    EditText txttitle,txtprice,txtpriceafterdisc,txtdesc;
    ImageView imgProduct;
    Button addProduct;
    private Uri pathphoto;
    private StorageReference mStorageRef;
    Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txttitle = findViewById(R.id.product_title);
        txtprice = findViewById(R.id.product_price);
        txtpriceafterdisc = findViewById(R.id.product_after_disc);
        txtdesc = findViewById(R.id.product_desc);
        imgProduct = findViewById(R.id.product_image);
        addProduct = findViewById(R.id.add_product);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = txttitle.getText().toString();
                String price = txtprice.getText().toString();
                String priceafterdisc = txtpriceafterdisc.getText().toString();
                String desc = txtdesc.getText().toString();

                Database db = new Database();
                Connection conn = db.ConnectDB();
                if(conn != null){
                    int i = db.InsertUpdateDelete("insert into Product (tilte,Description,Price,PriceAfterDisc,Image)" +
                            " values ('"+title+"','"+desc+"','"+price+"','"+priceafterdisc+"','"+downloadUrl.toString()+"')");
                    if (i==1){
                        Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!= null){
            pathphoto = data.getData();
            imgProduct.setImageURI(pathphoto);
            upload(pathphoto);
        }
    }
    private void upload(Uri file){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference riversRef = mStorageRef.child("images/"+UUID.randomUUID().toString());

        riversRef.putFile(file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    downloadUrl = task.getResult();
                    Log.d("", "onComplete: Url: "+ downloadUrl.toString());
                }
            }
        });
    }

}
