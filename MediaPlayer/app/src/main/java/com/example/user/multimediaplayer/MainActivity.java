package com.example.user.multimediaplayer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView) findViewById(R.id.lvplayLists);
        final ArrayList<File> songs = findSong(Environment.getExternalStorageDirectory());

        items = new String[songs.size()];
        for(int i =0; i<songs.size();i++){
            items[i]= songs.get(i).getName().replace(".mp3","");

        }
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.songs,R.id.txtviewLargetext,items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),MDPlayer.class).putExtra("pos",position).putExtra("Canciones", songs));
            }
        });

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
