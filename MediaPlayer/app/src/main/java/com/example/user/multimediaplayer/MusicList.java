package com.example.user.multimediaplayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.model.Audio;

import java.io.File;
import java.util.ArrayList;



public class MusicList extends Fragment {
    private String TAG = "MusicList";
    private Context context;
    // TODO: Rename and change types of parameters
    ArrayList<Audio> audioList;
    private ListView listMusic;
    String[] items;
    private View rootView;
    public MusicList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_music_list, container, false);
        context = container.getContext();
        listMusic = (ListView)rootView.findViewById(R.id.lvplayList);

        loadAudio();
        SoundAdapter soundAdapter = new SoundAdapter(context,audioList);
        listMusic.setAdapter(soundAdapter);
        listMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(),MDPlayer.class).putExtra("pos",position).putExtra("sound", audioList));
//                startActivity(new Intent(getActivity(),MDPlayer.class).putExtra("sound", songs.get(position).getAbsolutePath()));
            }
        });
        return rootView;
    }
    /**
     * Load audio files using {@link ContentResolver}
     *
     * If this don't works for you, load the audio files to audioList Array your oun way
     */
    private void loadAudio() {
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
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

                // Save to audioList
                audioList.add(new Audio(data, title, album, artist));
            }
        }
        if (cursor != null)
            cursor.close();
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




}
