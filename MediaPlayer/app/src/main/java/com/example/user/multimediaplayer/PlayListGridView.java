package com.example.user.multimediaplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayListGridView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayListGridView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListGridView extends Fragment implements AdapterView.OnItemClickListener {
    private String TAG = "PlayListGridView";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<String> list;
    private ArrayAdapter<String> adaptador;
    ListView lista;
    private OnFragmentInteractionListener mListener;

    public PlayListGridView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayListGridView.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListGridView newInstance(String param1, String param2) {
        PlayListGridView fragment = new PlayListGridView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_music_list, container, false);
        context = container.getContext();
        list = new ArrayList<String>();
        retrieveAllPlaylists();
        lista = (ListView)rootView.findViewById(R.id.lvplayList);

        adaptador = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);

        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(this);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG,list.get(position)+" <====== ");
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("playlist_name",list.get(position));
        intent.putExtra("playlistid",getPlayListId(list.get(position)));
        startActivity(intent);
    }

   private long getPlayListId(String playlistName){

        //get all playlists
        @SuppressLint("Recycle") Cursor playListCursor = context.getContentResolver().query(
                MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, new String[]{"*"}, null, null,
                null);

        long playlistId = 0;

        assert playListCursor != null;
        playListCursor.moveToFirst();

        do {

            //check if selected playlsit already exist
            if (playListCursor.getString(playListCursor
                    .getColumnIndex(MediaStore.Audio.Playlists.NAME)).
                    equalsIgnoreCase(playlistName)) {

                playlistId = playListCursor.getLong(playListCursor
                        .getColumnIndex(MediaStore.Audio.Playlists._ID));
                break;
            }
        } while (playListCursor.moveToNext());
        return playlistId;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
