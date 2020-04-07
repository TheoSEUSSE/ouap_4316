package com.example.chucknorrisjokes

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

        val button = findViewById<Button>(R.id.joke_button)
            button?.setOnClickListener() {
                val jokeService : Single<Joke> = JokeApiServiceFactory.factory().giveMeAJoke()

                val sub = jokeService.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                    onError = {Log.d("ERROR", it.toString())},
                    onSuccess = {viewAdapter.listJoke = viewAdapter.listJoke.plus(it)}
                )

                composite.add(sub)
            }


        //composite.clear()
        Log.d("TEST", viewAdapter.listJoke.toString())
    }
}
