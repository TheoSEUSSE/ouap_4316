package com.example.chucknorrisjokes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView


class JokeAdapter : RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {
    var listJoke: List<Joke> = emptyList()

    set(list : List<Joke>) {
        field = list
        notifyDataSetChanged()}

    class JokeViewHolder(val jV : JokeView) : RecyclerView.ViewHolder(jV)

    fun setList(list : List<Joke>) {listJoke = list}

    override fun getItemCount() = listJoke.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val jV = JokeView(parent.context).inflate(R.layout.recycler_layout)
        return JokeViewHolder(jV)
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        val model = JokeView.Model(listJoke[position], false)
        holder.jV.setupView(model)
    }

    fun moveAJoke ( start : Int, end : Int) : Boolean {
        val joke = listJoke[start]
        listJoke = listJoke.subList(0, start).plus(listJoke.subList(start+1, listJoke.size))
        listJoke = listJoke.subList(0, end).plus(joke).plus(listJoke.subList(end+1, listJoke.size))
        return joke.equals(listJoke[end])
    }

    fun removeJoke (i : Int) {
        listJoke = listJoke.subList(0, i).plus(listJoke.subList(i+1, listJoke.size))
    }
}