package com.app.wordpress;

/**
 * Created by Sahdan on 19/12/2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sahdan on 06/11/2017.
 */

public class datapembelii extends Activity {
    Button kirim;
    EditText namaa,alamatt,nohpp,kodeposs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datapembeli);

        kirim = (Button)findViewById(R.id.pesantiket);

        namaa = (EditText)findViewById(R.id.namalengkap);
        alamatt = (EditText)findViewById(R.id.alamat);
        nohpp = (EditText)findViewById(R.id.nohp);
        kodeposs = (EditText)findViewById(R.id.kodepos);
        kirim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                kirimTeksPesan();
                pemesan();
            }
        });
    }

    protected void kirimTeksPesan(){
        Log.i("Kirim Pesan", "");
        String noHP = "082139140139";
        String isiNama = namaa.getText().toString();
        String isialamat = alamatt.getText().toString();
        String isiNoHP = nohpp.getText().toString();
        String isikodepos = kodeposs.getText().toString();



        try {
            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
            smsManager.sendTextMessage(noHP, null, "Pemberitahuan \n Anda Ada Pesanan Barang\n"+"Nama:\n"+isiNama+"\n Alamat Lengkap:\n"+isialamat+"\n Kode Pos:\n"+isikodepos+"\n No. HP:\n"+isiNoHP, null, null);
            Toast.makeText(getApplicationContext(), "Pesan Sukses", Toast.LENGTH_LONG).show();
        }

        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Pesan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    protected void pemesan(){
        Log.i("Kirim Pesan", "");
        String noHPP = nohpp.getText().toString();
        String isiNamaa = namaa.getText().toString();



        try {
            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
            smsManager.sendTextMessage(noHPP, null, "Pemesanan Barang Berhasil !\n"+"Nama:\n"+isiNamaa+"Silahkan Ditunggu Paketannya Datang Kurang Lebih 3-5 Hari Sampai Ke Tujuan :)", null, null);
            Toast.makeText(getApplicationContext(), "Pesan Sukses", Toast.LENGTH_LONG).show();
        }

        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Pesan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}