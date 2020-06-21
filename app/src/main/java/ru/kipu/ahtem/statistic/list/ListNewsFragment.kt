package ru.kipu.ahtem.statistic.list

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_news_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import ru.kipu.ahtem.statistic.R
import java.io.IOException

class ListNewsFragment : Fragment() {

    private val url = "https://www.cfu2015.com/teams"
    private val url1 = "https://www.cfu2015.com"
    private val listNews = mutableListOf<News>()
    private lateinit var adapter: DataAdapter

    companion object {
        fun newInstance() = ListNewsFragment()
    }

    private lateinit var viewModel: ListNewsViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListNewsViewModel::class.java)
        // TODO: Use the ViewModel

        adapter = DataAdapter()
        val llm = LinearLayoutManager(this.context)
        rv.layoutManager = llm
        rv.adapter = adapter

        GlobalScope.launch {
            getData()
        }
    }

    private fun getData() {
        try {
            val document = Jsoup.connect(url).get()
            val document1 = Jsoup.connect(url1).get()
            val elements = document.select("div[class=entry-box]")

            for (i in 0 until elements.size) {
                val title = elements .select("a")
                        .select("div[class=entry-content]")
                        .select("div[class=entry-title]")
                        .eq(i)
                        .text()

                val linkImage = elements.select("a").select("div[class=entry-logo]")
                                        .select("img")
                                        .eq(i)
                                        .attr("src")

                val linkDetails =
                        document1.baseUri() +
                                elements.select("a")
                                        .eq(i)
                                        .attr("href")

                listNews.add(News(title, linkImage , linkDetails))
            }
            GlobalScope.launch(Dispatchers.Main) {
                adapter.set(listNews)
            }
        } catch (e: IOException) {
            Log.e("TEST) exception", e.message.toString())
        }
    }

}
