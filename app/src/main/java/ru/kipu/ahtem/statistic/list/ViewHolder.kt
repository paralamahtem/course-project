package ru.kipu.ahtem.statistic.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item.view.*

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.row_tv_title
    private val image: ImageView = itemView.row_img
    private var link = ""

    init {
        itemView.setOnClickListener {
            itemView
                .findNavController()
                .navigate(ListNewsFragmentDirections.actionListNewsFragmentToDetailsNewsFragment().setLink(link))
        }
    }

    fun bind(news: News) {
        title.text = news.title
            Picasso.with(itemView.context)
                    .load(news.linkImage)
                    .into(image)
        link = news.linkDetails
    }
}