package com.example.user.multimediaplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

public class MDPlayer extends AppCompatActivity implements View.OnClickListener{
    static MediaPlayer np;
    ArrayList<File> cns;
    ArrayList<File> nombrecns;
    Thread actseekbar;
    int posicion;
    Uri uri;
    String aux;

    ImageButton btnpy, btnfw, btnbw, btnbck;
    TextView nombrecancion,tTranscurrido, tRestante;
    SeekBar sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdplayer);
//        np= new MediaPlayer();
        // captura de accion de ImageButton
        btnpy = (ImageButton) findViewById(R.id.btnimgPlay);
        btnfw = (ImageButton) findViewById(R.id.btnimgForward);
        btnbw = (ImageButton) findViewById(R.id.btnimgBackWard);
        //btnbck = (ImageButton)findViewById(R.id.btnback);

        //captura de accion SeekBar
        sb = (SeekBar) findViewById(R.id.skBr);

        //captura de accion TextView
        tTranscurrido = (TextView) findViewById(R.id.tTranscurrido);
        tRestante     = (TextView) findViewById(R.id.tRestante);
        nombrecancion =(TextView)findViewById(R.id.txtVshowMusic);

        //Seteo de Eventos
        btnpy.setOnClickListener(this);
        btnfw.setOnClickListener(this);
        btnbw.setOnClickListener(this);

        //btnbck.setOnClickListener(this);
        sb.setOnClickListener(this);


        //logica
        playMusic();
    }

    private String getHRM(int milliseconds) {

        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        String aux = "";
        aux = ((hours < 10) ? "0" + hours : hours) + ":" + ((minutes < 10) ? "0" + minutes : minutes) + ":" + ((seconds < 10) ? "0" + seconds : seconds);
        return aux;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.btnimgPlay:
                System.out.println("IMPRIMIENDO");
                System.out.println(np.isPlaying());
                if (np.isPlaying()) {
                    btnpy.setImageResource(R.drawable.play);

                    np.pause();
                } else {

                    btnpy.setImageResource(R.drawable.pause);
                    try{
                        np.prepare();
                        np.start();
                    }catch (IllegalStateException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println(np.isPlaying());
                }
                break;
            /*case R.id.btnimgForward:
                np.seekTo(np.getCurrentPosition() + 5000);
                break;
            case R.id.btnimgBackWard:
                np.seekTo(np.getCurrentPosition() - 5000);
                break;*/
            case R.id.btnimgForward:
                NextCacion();
                break;
            case R.id.btnimgBackWard:
                AntriorCacion();
                break;
            /*case R.id.btn_palylist:
                startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("pos",posicion).putExtra("songList",mysongs) );
                break;*/
        }

    }
    public void NextCacion(){
        np.stop();
        //mp.release();

        posicion = (posicion + 1) % cns.size();
        nombrecancion.setText(cns.get(posicion).getName().toString());

        uri = Uri.parse(cns.get(posicion).toString());
        np = MediaPlayer.create(getApplicationContext(), uri);

        np.start();
        sb.setMax(0);//le envia el maximo a so portar seebark ok
        tTranscurrido.setText(getHRM(np.getDuration()));//mostrar el tiempo que dura la cancion
        try{
            sb.setMax(np.getDuration());//le envia el maximo a so portar seebark ok
        }catch (Exception e){
e.printStackTrace();
        }
    }

    public void AntriorCacion(){
        np.stop();
        //mp.release();
        //posicion=(posicion-1<0)? mysongs.size()-1: posicion-1;
        if (posicion - 1 < 0) {
            posicion = cns.size() - 1;
        } else {
            posicion = posicion - 1;
        }
        nombrecancion.setText(cns.get(posicion).getName().toString());
        uri = Uri.parse(cns.get(posicion).toString());
        np = MediaPlayer.create(getApplicationContext(), uri);
        np.start();
        sb.setMax(0);//le envia el maximo a so portar seebark ok
        tTranscurrido.setText( getHRM(np.getDuration()));//mostrar el tiempo que dura la cancion
        sb.setMax(np.getDuration());
    }


    public void playMusic()
    {

        actseekbar = new Thread() {
            @Override
            public void run() {
                super.run();
                int trtime = np.getDuration();
                sb.setMax(trtime);
                int posact = 0;
                int ejec = 0;
                boolean bool = false;
                while (posact < trtime) {
                    try {
                        sleep(500);
                        posact = np.getCurrentPosition();
                        sb.setProgress(posact);

                        ejec = sb.getProgress();
                        aux = getHRM(ejec);
                        tRestante.setText(aux.toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                        tRestante.setText(aux);
                    }
                }
            }
        };
        if (np != null) {
            np.stop();
        }
        try {
            Intent i = getIntent();
            Bundle b = i.getExtras();
            assert b != null;
            cns = (ArrayList) b.getParcelableArrayList("Canciones");
            posicion = (int) b.getInt("pos", 0);
            uri = Uri.parse(cns.get(posicion).toString());
            nombrecancion.setText(cns.get(posicion).getName().toString());
            np = MediaPlayer.create(getApplication(), uri);
            actseekbar.start();
            np.start();
            tRestante.setText(getHRM(np.getDuration()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                np.seekTo(seekBar.getProgress());
            }
        });
    }

}