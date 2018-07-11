package com.example.user.multimediaplayer;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.model.Audio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SoundAdapter extends ArrayAdapter<Audio> {
    private String TAG = "SoundAdapter";
    List<Audio> musicDefinitionList;
    public SoundAdapter(@NonNull Context context, List<Audio> sounds) {
        super(context,R.layout.sound_row, sounds);
        musicDefinitionList =sounds;
//        for (File sound: sounds) {
//            musicDefinitionList.add(readFileMusic(sound));
//        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.sound_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtTitleMusic = (TextView) convertView.findViewById(R.id.txtTitle);
            viewHolder.txtDetailMusic = (TextView) convertView.findViewById(R.id.txtDescription);
//            viewHolder.txtDuration = (TextView) convertView.findViewById(R.id.txtDuration);
            convertView.setTag(viewHolder);
        }else
        {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.txtDuration.setText(musicDefinitionList.get(position).getDuration());
        viewHolder.txtTitleMusic.setText(musicDefinitionList.get(position).getTitle());
        viewHolder.txtDetailMusic.setText(musicDefinitionList.get(position).getArtist());
        return convertView;
    }
    private static class ViewHolder {
        TextView txtTitleMusic;
        TextView txtDetailMusic;
        TextView txtDuration;
    }

    public MusicDefinition readFileMusic(File sound){
        MusicDefinition definition= new MusicDefinition();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(sound.getAbsolutePath());

//        definition.setDuration(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        definition.setDescription(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        definition.setTitle(sound.getName().replace(".mp3",""));
        String duration = getHRM(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        definition.setDuration(duration);

        Log.d(TAG,mmr.toString());
        Log.d(TAG,definition.toString());
        return definition;
    }
    private String getHRM(String mill) {

        int milliseconds =  Integer.parseInt(mill);
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        String aux = "";
        aux = ( (minutes < 10) ? "0" + minutes : minutes) + ":" + ((seconds < 10) ? "0" + seconds : seconds);
        return aux;
    }
}
