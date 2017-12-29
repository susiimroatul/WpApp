package com.app.wordpress;
import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sms extends Activity {
    Button kirim;
    EditText isiPesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);

        kirim = (Button)findViewById(R.id.kirim);

        isiPesan = (EditText)findViewById(R.id.editText2);

        kirim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                kirimTeksPesan();
            }
        });
    }

    protected void kirimTeksPesan(){
        Log.i("Kirim Pesan", "");
        String noHP = "081215798520";
        String isiTeksPesan = isiPesan.getText().toString();

        try {
            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
            smsManager.sendTextMessage(noHP, null, isiTeksPesan, null, null);
            Toast.makeText(getApplicationContext(), "Pesan Terkirim", Toast.LENGTH_LONG).show();
        }

        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Pesan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}