package com.example.user.multimediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;



public class MusicList extends Fragment {
    private String TAG = "MusicList";
    private Context context;
    // TODO: Rename and change types of parameters

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
        final ArrayList<File> songs = findSong(Environment.getExternalStorageDirectory());

        items = new String[songs.size()];
        for(int i =0; i<songs.size();i++){
            items[i]= songs.get(i).getName().replace(".mp3","");
            Log.d(TAG,""+items[i]);
        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
//                R.layout.songs,R.id.txtviewLargetext,items);
//        listMusic.setAdapter(adapter);

        SoundAdapter soundAdapter = new SoundAdapter(context,songs);
        listMusic.setAdapter(soundAdapter);
        listMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(),MDPlayer.class).putExtra("pos",position).putExtra("sound", songs));
//                startActivity(new Intent(getActivity(),MDPlayer.class).putExtra("sound", songs.get(position).getAbsolutePath()));
            }
        });
        return rootView;
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
