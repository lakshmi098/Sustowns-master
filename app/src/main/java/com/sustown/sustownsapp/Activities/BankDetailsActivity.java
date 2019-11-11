package com.sustown.sustownsapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.sustownsapp.R;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class BankDetailsActivity extends AppCompatActivity {

    EditText bank_acc_name,bank_acc_address,bank_account_no,bank_name,bank_country,branch_name,branch_address,swift_number,iban_number,national_routing_no,
               business_number,tax_id_no;
    Button choose_file_bank,save_Details_bank;
    Integer REQUEST_CODE_DOC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bank_details);

        bank_acc_name = (EditText) findViewById(R.id.bank_acc_name);
        bank_acc_address = (EditText) findViewById(R.id.bank_acc_address);
        bank_account_no = (EditText) findViewById(R.id.bank_account_no);
        bank_name = (EditText) findViewById(R.id.bank_name);
        bank_country = (EditText) findViewById(R.id.bank_country);
        branch_name = (EditText) findViewById(R.id.branch_name);
        branch_address = (EditText) findViewById(R.id.branch_address);
        swift_number = (EditText) findViewById(R.id.swift_number);
        iban_number = (EditText) findViewById(R.id.iban_number);
        national_routing_no = (EditText) findViewById(R.id.national_routing_no);
        business_number = (EditText) findViewById(R.id.business_number);
        tax_id_no = (EditText) findViewById(R.id.tax_id_no);
        choose_file_bank = (Button) findViewById(R.id.choose_file_bank);
        save_Details_bank = (Button) findViewById(R.id.save_Details_bank);

        choose_file_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getDocument();
            }
        });
    }

        private void getDocument()
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/msword,application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
            startActivityForResult(intent, REQUEST_CODE_DOC);
        }



        @Override
        protected void onActivityResult(int req, int result, Intent data)
        {
            // TODO Auto-generated method stub
            super.onActivityResult(req, result, data);
            if (result == RESULT_OK)
            {
                Uri fileuri = data.getData();
                String docFilePath = getFileNameByUri(this, fileuri);
            }

// get file path

    }
    private String getFileNameByUri(Context context, Uri uri)
    {
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        if (uri.getScheme().toString().compareTo("content") == 0)
        {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION }, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        }
        else
        if (uri.getScheme().compareTo("file") == 0)
        {
            try
            {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            }
            catch (URISyntaxException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            filepath = uri.getPath();
        }
        return filepath;
    }

}
