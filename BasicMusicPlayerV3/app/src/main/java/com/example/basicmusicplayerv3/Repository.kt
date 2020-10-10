package com.example.basicmusicplayerv3

data class Music(var name: String, var singer: String, var song: Int)

class Repository {
    companion object {
        private var initialDataList = mutableListOf(
            Music("base_after_base","someone",R.raw.base_after_base),
            Music("big_digits_excerpt","someone",R.raw.big_digits_excerpt),
            Music("Cant let go","someone",R.raw.cant_let_go)
        )
    }

    fun fetchData(): List<Music> {
        return initialDataList
    }
}