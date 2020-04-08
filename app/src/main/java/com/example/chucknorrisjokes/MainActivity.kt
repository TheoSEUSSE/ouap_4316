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
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewManager : RecyclerView.LayoutManager

    private val composite = CompositeDisposable()


    @UnstableDefault
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewAdapter = JokeAdapter()

        viewManager = LinearLayoutManager(this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val button = findViewById<Button>(R.id.joke_button)

        button?.setOnClickListener() {
            val jokeService : Single<Joke> = JokeApiServiceFactory.factory().giveMeAJoke()

            jokeService.doOnSubscribe{ progressBar.visibility = View.VISIBLE }

            val sub = jokeService
                .repeat(10)
                .delay(5,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doAfterNext { progressBar.visibility = View.INVISIBLE }
                .subscribeBy(
                    onError = {Log.d("ERROR", it.toString())},
                    onNext = {viewAdapter.listJoke = viewAdapter.listJoke.plus(it)},
                    onComplete = {Log.d("COMPLETED", it.toString())}
            )
            composite.add(sub)
        }
        Log.d("TEST", viewAdapter.listJoke.toString())
    }
}
