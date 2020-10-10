package com.example.basicmusicplayerv3

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaMetadataRetriever
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class CustomMusicAdapter(context: Context) :
    BaseAdapter() {

    private var songList = mutableListOf<Music>()
    private val inflater = LayoutInflater.from(context)
    private val someContext = context

    override fun getCount(): Int {
        return songList.size
    }

    override fun getItem(position: Int): Music {
        return songList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addAll(newList: List<Music>){
        songList.addAll(newList)
    }
    fun removeAt(position: Int) {
        songList.removeAt(position)
    }

    // Next optimization is the ViewHolder pattern that
    // minimizes calls to findViewById
    private fun bindView(position: Int, view: View): View {
        // We retrieve the item from the array
        val music = getItem(position)
        // Now we bind the item, by copying the important parts into the view
        //SSS
        val songNameView = view.findViewById<TextView>(R.id.songText)
        songNameView.text = music.name

        val singerView = view.findViewById<TextView>(R.id.songSinger)
        singerView.text = music.singer

        var resourceId = music.song
        var metadataRetriever = MediaMetadataRetriever()
        someContext.resources.getResourceName(music.song)
        someContext.resources.getResourcePackageName(music.song)
        val afd: AssetFileDescriptor = someContext.resources.openRawResourceFd(music.song)
        metadataRetriever.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        afd.close()
//        metadataRetriever.setDataSource(
//            someContext.resources.openRawResourceFd(music.song).fileDescriptor)
        var duration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        Log.d("CustomMusic - ", "Song duration is " + duration)

        return view
    }

    // A ViewGroup are invisible containers that hold a bunch of views and
    // define their layout properties.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Maybe change this to a spinner?
        val optLevel = 1
        // The most important optimization, only create as many views
        // as fit on the screen.
        //SSS
        if(convertView == null) {
            Log.d("XXX", "$position NULL")
        } else {
            Log.d("XXX", "$position not null")
        }
        var returnView: View = convertView ?:
                inflater.inflate(R.layout.row, parent, false)
        return bindView(position, returnView)
    }
}