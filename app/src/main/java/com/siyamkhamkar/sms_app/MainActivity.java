package com.siyamkhamkar.sms_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText no, msg;
    Button send_btn;
    ImageButton add_btn;

    private static final int RESULT_PICK_CONTACT=885;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        no = (EditText) findViewById(R.id.number_txtbx);
        msg = (EditText) findViewById(R.id.message_txtbx);

        add_btn = (ImageButton) findViewById(R.id.btn_addcntct);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cntct_picker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(cntct_picker,RESULT_PICK_CONTACT);
            }
        });

        send_btn = (Button) findViewById(R.id.btn_send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {
                    SmsManager sms_mnger = SmsManager.getDefault();
                    sms_mnger.sendTextMessage(no.getText().toString(), null, msg.getText().toString(), null, null );

                    Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_PICK_CONTACT && requestCode==RESULT_OK)
        {
            try{
                String phoneNo = null;
                Uri cntct_uri = data.getData();

                cursor = getContentResolver().query(cntct_uri, null, null, null, null);
                cursor.moveToFirst();

                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                phoneNo = cursor.getString(phoneIndex);
                no.setText(phoneNo);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}