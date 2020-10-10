package com.example.basicmusicplayerv3

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_music_item.*

class MainActivity : AppCompatActivity() {
    private var repository = Repository()
    private var musicList = repository.fetchData()
    private var mp: MediaPlayer? = null
    private var currentSong = musicList[0]
    private lateinit var adapter: CustomMusicAdapter

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        // Don't use android.R... resources!  They can change without warning.
        //toolbar.setNavigationIcon(android.R.drawable.ic_menu_gallery);
        toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp)
        toolbar.title = "Basic Player"
        toolbar.setTitleTextColor(Color.BLACK)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        controlSound()
        adapter = CustomMusicAdapter(this)
        adapter.addAll(musicList)
        adapter.notifyDataSetChanged()
        theListView.adapter = adapter
        theListView.setOnItemClickListener {
                parent, _/*view*/, position, _/*id*/ ->
            var toPlay = parent.getItemAtPosition(position) as Music
            val name = toPlay.name
            val albumPicked = "You selected $position ${name}"
            currentSong = toPlay
            textName.text = currentSong.name
            textSinger.text = currentSong.singer
            if(mp!=null) {
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = MediaPlayer.create(this, toPlay.song)
                initializeSeekBar()
                mp?.start()
            }
            else {
                playSong(toPlay.song)
            }
            Toast.makeText(this@MainActivity,
                albumPicked, Toast.LENGTH_SHORT).show()
        }
    }

    private fun controlSound() {
        playBt.setOnClickListener {
            textName.text = currentSong.name
            textSinger.text = currentSong.singer
            playSong(currentSong.song)
        }

        pauseBt.setOnClickListener {
            if(mp != null){
                mp?.pause()
                Log.d("Main Activity","Paused at ${mp!!.currentPosition/1000} seconds")
            }
        }

        stopBt.setOnClickListener {
            if(mp != null){
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
                Log.d("Main Activity","Stopped")
            }
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun playSong(songId: Int) {
        if (mp == null) {
            mp = MediaPlayer.create(this, songId)
            Log.d("Main Activity", "ID ${mp!!.audioSessionId}")
            initializeSeekBar()
        }
        mp?.start()
        Log.d("Main Activity", "Duration ${mp!!.duration / 1000} seconds")
    }

    private fun initializeSeekBar() {
        seekbar.max = mp!!.duration
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object:Runnable {
            override fun run() {
                try {
                    seekbar.progress = mp!!.currentPosition
                    handler.postDelayed(this, 1000)
                }catch (e: Exception){
                    seekbar.progress = 0
                }
            }
        },0)
    }
}