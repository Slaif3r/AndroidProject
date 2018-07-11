package com.example.user.multimediaplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.user.model.Audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MDPlayer extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "MDPlayer";
    static MediaPlayer np;
    ArrayList<Audio> cns;
    ArrayList<File> nombrecns;
//    Thread actseekbar;
    int posicion;
    Uri uri;
    String aux;
    public static int oneTimeOnly = 0;
    ImageButton btnpy, btnfw, btnbw, btnbck;
    TextView nombrecancion,tTranscurrido, tRestante;
    SeekBar sb;
    private Runnable updateSongTime;
    private Handler myHandler = new Handler();
    private double startTime = 0;
    private double finalTime = 0;

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
//        sb.setClickable(false);
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
        updateSongTime = getRunnable();
        //logica
        playMusic();
//        btnpy.setImageResource(R.drawable.pause);
    }

    private String getHRM(int milliseconds) {

        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        String aux = "";
        aux = ((minutes < 10) ? "0" + minutes : minutes) + ":" + ((seconds < 10) ? "0" + seconds : seconds);
        return aux;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.btnimgPlay:
                Log.d(TAG,"IMPRIMIENDO");
                Log.d(TAG,""+np.isPlaying());
                if (np.isPlaying()) {

                    np.pause();
                    btnpy.setImageResource(R.drawable.play);

                } else {

                    play();
                    btnpy.setImageResource(R.drawable.pause);

                    Log.d(TAG,""+np.isPlaying());
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

    private void play() {
        try {

            np.start();

            finalTime = np.getDuration();
            startTime = np.getCurrentPosition();
//            if (oneTimeOnly == 0) {
//                sb.setMax((int) finalTime);
//                oneTimeOnly = 1;
//            }
            sb.setMax(np.getDuration());
            sb.setProgress((int)startTime);
            myHandler.postDelayed(getRunnable(), 100);
            tRestante.setText(getHRM(np.getDuration()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void NextCacion(){
        if (np.isPlaying()) {
            np.stop();
        }
        np.reset();

        posicion = (posicion + 1) % cns.size();
        nombrecancion.setText(cns.get(posicion).getArtist());

        if (np == null)
            np = new MediaPlayer();

        np.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            np.setDataSource(cns.get(posicion).getData());
        } catch (IOException e) {
            e.printStackTrace();
        }


        try{
            np.prepare();
            play();
            sb.setMax(0);//le envia el maximo a so portar seebark ok
            tTranscurrido.setText(getHRM(np.getDuration()));//mostrar el tiempo que dura la cancion
            sb.setMax(np.getDuration());//le envia el maximo a so portar seebark ok
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void AntriorCacion(){
        if (np.isPlaying()) {
            np.stop();
        }
        np.reset();
        //posicion=(posicion-1<0)? mysongs.size()-1: posicion-1;
        if (posicion - 1 < 0) {
            posicion = cns.size() - 1;
        } else {
            posicion = posicion - 1;
        }
        nombrecancion.setText(cns.get(posicion).getArtist());
//        uri = Uri.parse(cns.get(posicion).toString());
//        np = MediaPlayer.create(getApplicationContext(), uri);
        if (np == null)
            np = new MediaPlayer();
//        np = MediaPlayer.create(getApplicationContext(), uri);
        np.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            np.setDataSource(cns.get(posicion).getData());
            np.prepare();
            play();
//            sb.setMax(0);//le envia el maximo a so portar seebark ok
            tTranscurrido.setText( getHRM(np.getDuration()));//mostrar el tiempo que dura la cancion
            sb.setMax(np.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @SuppressLint("DefaultLocale")
    public void playMusic()
    {

        if (np != null) {
            np.stop();
        }
        try {
            if (np == null)
                np = new MediaPlayer();
            np.reset();
            Intent i = getIntent();
            Bundle b = i.getExtras();
            cns = (ArrayList) b.getParcelableArrayList("sound");
            posicion = (int) b.getInt("pos", 0);
            uri = Uri.parse(cns.get(posicion).toString());
            nombrecancion.setText(cns.get(posicion).getArtist());

//            np = MediaPlayer.create(getApplication(), uri);
            np.setAudioStreamType(AudioManager.STREAM_MUSIC);

            np.setDataSource(cns.get(posicion).getData());

            np.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.d(TAG,"finalTime ==> "+np.getDuration()+" startTime "+startTime);
        if (oneTimeOnly == 0) {
            sb.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        sb.setProgress((int)startTime);


        tRestante.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        tTranscurrido.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );
    }

    @NonNull
    private Runnable getRunnable() {
        return new Runnable() {
                @SuppressLint("DefaultLocale")
                public void run() {
                    startTime = np.getCurrentPosition();
                    Log.d(TAG,"startTime"+startTime+ " - finalTime "+finalTime);
                    tTranscurrido.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
                    );
                    Log.d(TAG,"tTranscurrido ==>"+tTranscurrido.getText().toString());
                    sb.setProgress((int)startTime);
                    myHandler.postDelayed(this, 100);
                }
            };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_playliste,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_mn_add:
                newPlayList();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void newPlayList() {
        Intent intent = new Intent(MDPlayer.this, PlayListCreate.class);
        intent.putExtra("sound_id",cns.get(posicion).getId());
        startActivity(intent);
        onStop();
    }
}