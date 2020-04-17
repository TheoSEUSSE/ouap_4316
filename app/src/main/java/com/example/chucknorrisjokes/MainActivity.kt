package com.example.chucknorrisjokes

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewManager : RecyclerView.LayoutManager

    private val composite = CompositeDisposable()
    private lateinit var vA : JokeAdapter

    @UnstableDefault
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewAdapter = JokeAdapter()
        vA = viewAdapter

        viewManager = LinearLayoutManager(this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)


        fun display10jokes( viewAdapter : JokeAdapter) {
            val sub = JokeApiServiceFactory.factory().giveMeAJoke()
                .repeat(10)
                .delay(500,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doAfterNext { progressBar.visibility = View.INVISIBLE }
                .subscribeBy(
                    onError = {Log.d("ERROR", it.toString())},
                    onNext = {viewAdapter.listJoke = viewAdapter.listJoke.plus(it)},
                    onComplete = {} )
            composite.add(sub)
        }

        display10jokes(viewAdapter)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int ) {
                super.onScrollStateChanged(recyclerView, newState)
                if ( (viewManager as LinearLayoutManager).findLastVisibleItemPosition() == viewAdapter.itemCount - 1) {
                    display10jokes(viewAdapter)
                }
            }
        } )

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("size", vA.listJoke.size)
        var iterator = 0
        vA.listJoke.forEach {
            outState.putString("joke$iterator", Json(JsonConfiguration.Stable).stringify(Joke.serializer(), it))
            iterator++
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val size = savedInstanceState.getInt("size")
        var stock : List<Joke> = emptyList()
        for (i in 0 until size-1) {
            try {
                stock = stock.plus(Json(JsonConfiguration.Stable).parse(Joke.serializer(), savedInstanceState.getString("joke$i")!!))
            }
            catch (e : Exception) {
                Log.d("error", e.toString())
            }
        }
        vA.listJoke = stock
    }
}


