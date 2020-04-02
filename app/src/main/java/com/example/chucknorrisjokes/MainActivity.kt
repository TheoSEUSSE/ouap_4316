package com.example.chucknorrisjokes

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var viewManager : RecyclerView.LayoutManager

    private val composite = CompositeDisposable()


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("JokeList", JokeList.jokes.toString())

        viewManager = LinearLayoutManager(this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = viewManager
            adapter = JokeAdapter()
        }
        val jokeService : Single<Joke> = JokeApiServiceFactory.factory().giveMeAJoke()

        jokeService.subscribeOn(Schedulers.io())

        val sub = jokeService.subscribeBy(
                    onError = {Log.d("ERROR", it.toString())},
                    onSuccess = {Log.d("SUCCESS", it.toString())}
            )

        composite.add(sub)
        composite.clear()

    }
}
