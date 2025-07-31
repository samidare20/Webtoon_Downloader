package com.myapp.webtoon_downloader

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.myapp.webtoon_downloader.databinding.DownloaderWebtoonTilesBinding

class WebtoonTiles constructor(
        context: Context
) : LinearLayout(context) {
    private val binding: DownloaderWebtoonTilesBinding
    init {
        binding = DownloaderWebtoonTilesBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setData(title: String, link: String, comic: String, mbookmark: Boolean) {
        binding.titlename.text = title
        binding.bookmark.isSelected = mbookmark
        val db = Room_Database.getInstance(context)

        try {
            Glide.with(context).load(link).into(binding.thumbnail)
        } catch (e: Exception) {
            Log.d("mydebug", e.toString())
            Log.d("mydebug", binding.thumbnail.toString())
        }
        this.setOnClickListener {
            val intent = Intent(context, Episode::class.java)
            intent.putExtra("link", comic)
            intent.putExtra("title", title)
            intent.putExtra("thumbnail", link)
            startActivity(context, intent, null)
        }

        binding.bookmark.setOnClickListener {
            Thread(Runnable {
                binding.bookmark.isSelected = !binding.bookmark.isSelected
                val data = db.Room_DAO().selectTitle(title)
                data.bookmark = binding.bookmark.isSelected
                db.Room_DAO().update(data)
            }).start()
        }
    }
}