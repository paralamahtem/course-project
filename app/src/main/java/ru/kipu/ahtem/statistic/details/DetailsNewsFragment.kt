
package ru.kipu.ahtem.statistic.details

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_news_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import ru.kipu.ahtem.statistic.R
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class DetailsNewsFragment : Fragment(), CoroutineScope {

    private var job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private lateinit var viewModel: DetailsNewsViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsNewsViewModel::class.java)

        job = launch(Dispatchers.IO) {
            getData()
        }
    }

    private fun getData() {
        try {
            val document = Jsoup.connect(arguments?.getString("link")).get()
            val elements = document.select("div[class=entryContentHeader]")
            val elements1 = document.select("div[class=photo]")
            val elements2 = document.select("div[class=entries-rows-wrapper]")
            val elements3 = document.select("div[id=playerStatistics]")

            val title = elements.select("h1").text()

            val description = elements2.select("span[class=entry-row]")
                    .select("div[class=entry-row-values]")
                    .text()
            val descriptionPlayers = elements3.select("div[class=entries-rows-wrapper]")
                    .select("div[class=entry-row-values]")
                    .text()

            val linkImage = elements1.select("img").attr("src")

            job = launch {
                det_title.text = title.toString()
                det_description.text = description.toString()
                det_description_players.text = descriptionPlayers.toString()
                Picasso.with(activity)
                        .load(linkImage)
                        .into(det_main_photo)
            }
        } catch (e: IOException) {
            Log.e("TEST) ", e.message.toString())
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}