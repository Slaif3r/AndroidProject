package com.example.user.multimediaplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlayListCreate extends AppCompatActivity {
    private String TAG = "PlayListCreate";
    private String namePlayList;
   private ArrayAdapter<String> adaptador;
    ListView lista;
    ArrayList<String> list;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_play_list_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = new ArrayList<String>();
        retrieveAllPlaylists();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog  dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_playlist_dialog);
                dialog.setTitle("Crear nuevo playlist");
                final EditText edtxt_name = (EditText)dialog.findViewById(R.id.edtxt_name);
                Button btn_acept = (Button)dialog.findViewById(R.id.btn_aceptar);
                Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancelar);
                dialog.show();
                btn_acept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        namePlayList = edtxt_name.getText().toString();
                        Log.d(TAG,"nuevo playliste"+namePlayList);
                        validarPlayListName(namePlayList);

                        dialog.dismiss();
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });





        lista = (ListView)findViewById(R.id.lst_play);

        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);

        lista.setAdapter(adaptador);
    }

    private void validarPlayListName(String namePlayList) {
        if (list.contains(namePlayList))
        {
            alertDialog();
        }else{
            addPlaylist(namePlayList);
            list.clear();
            retrieveAllPlaylists();
            adaptador.notifyDataSetChanged();
        }
    }

    private void alertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Este playlist ya existe, ingrese otro nombre")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void addNewPlayList(String namePlayList){
//        ContentResolver contentResolver = context.getContentResolver();
//        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", 1);
//
//        ContentValues values = new ContentValues(1);
//        values.put(MediaStore.Audio.Playlists.NAME,namePlayList);
//        contentResolver.insert(uri,values);



    }

    public void addPlaylist(String pname) {

        ContentResolver resolver = context.getContentResolver();

        Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor c = resolver.query(playlists, new String[] { "*" }, null, null,
                null);
        long playlistId = 0;
//        c.moveToFirst();
        if( c != null && c.moveToFirst() ) {

            do {
                String plname = c.getString(c
                        .getColumnIndex(MediaStore.Audio.Playlists.NAME));
                if (plname.equalsIgnoreCase(pname)) {
                    playlistId = c.getLong(c
                            .getColumnIndex(MediaStore.Audio.Playlists._ID));
                    break;
                }
            } while (c.moveToNext());
            c.close();
        }

        if (playlistId != 0) {
            Uri deleteUri = ContentUris.withAppendedId(playlists, playlistId);
            Log.d(TAG, "REMOVING Existing Playlist: " + playlistId);

            // delete the playlist
            resolver.delete(deleteUri, null, null);
        }

        Log.d(TAG, "CREATING PLAYLIST: " + pname);
        ContentValues v1 = new ContentValues();
        v1.put(MediaStore.Audio.Playlists.NAME, pname);
        v1.put(MediaStore.Audio.Playlists.DATE_MODIFIED,
                System.currentTimeMillis());
        Uri newpl = resolver.insert(playlists, v1);
        Log.d(TAG, "Added PlayLIst: " + newpl);

    }

    public void retrieveAllPlaylists() {

        Uri tempPlaylistURI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        final String idKey = MediaStore.Audio.Playlists._ID;
        final String nameKey = MediaStore.Audio.Playlists.NAME;

        final String[] columns = { idKey, nameKey };

        Cursor playListCursor = context.getContentResolver().query(
                tempPlaylistURI, columns, null, null, null);

        if (playListCursor != null) {
            Log.d("playlist cursor count=", "" + playListCursor.getCount());

            for (boolean hasItem = playListCursor.moveToFirst(); hasItem; hasItem = playListCursor
                    .moveToNext()) {
              String  playlistName = playListCursor.getString(playListCursor
                        .getColumnIndex(nameKey));
                // noOfTracks = playListCursor.getInt(playListCursor
                // .getColumnIndex(tracksCountKey));
                Log.d(this.getClass().getName(), "playlistname=" + playlistName // returns only default playliststhe
                        + "tracks=");
                list.add(playlistName);
//                playlistModel.add(new PlaylistModel(playlistName, noOfTracks));
            }


        }

    }
}
