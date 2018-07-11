package com.example.user.multimediaplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.model.Audio;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] items;
    private String playlist_name;
    private Long playlistid;
    private Context context;
    ArrayList<Audio> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView) findViewById(R.id.lvplayLists);
        context = this;
        audioList = new ArrayList<>();
        playlist_name = getIntent().getExtras().getString("playlist_name");
        playlistid = getIntent().getExtras().getLong("playlistid");
        Log.d(MainActivity.class.getName(),playlistid + " <====> "+playlist_name);
        loadAudio(playlistid);
//
//
//        final ArrayList<File> songs = findSong(Environment.getExternalStorageDirectory());
//
//        items = new String[songs.size()];
//        for(int i =0; i<songs.size();i++){
//            items[i]= songs.get(i).getName().replace(".mp3","");
//
//        }
        AdapterPL adapterPL = new AdapterPL(context,audioList);
//        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(),
//                R.layout.songs,R.id.txtviewLargetext,items);
        lv.setAdapter(adapterPL);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),MDPlayer.class).putExtra("pos",position).putExtra("sound", audioList));
            }
        });

//        listMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(getActivity(),MDPlayer.class).putExtra("pos",position).putExtra("sound", audioList));
////                startActivity(new Intent(getActivity(),MDPlayer.class).putExtra("sound", songs.get(position).getAbsolutePath()));
//            }
//        });

    }
    public ArrayList<File> findSong(File root){
        ArrayList<File>songs = new ArrayList<File>();
        File[]archivos=root.listFiles();
        for (File lista: archivos)
        {
            if(lista.isDirectory() && !lista.isHidden()){
                songs.addAll(findSong(lista));
            }
            else{
                if(lista.getName().endsWith(".mp3")){
                    songs.add(lista);
                }
            }
        }
        return songs;
    }


    private void loadAudio(Long playlist_id) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external",(playlist_id));
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                // Save to audioList
                audioList.add(new Audio(id,data, title, album, artist));
            }
        }
        if (cursor != null)
            cursor.close();
    }
}
