package com.example.chucknorrisjokes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class JokeAdapter : RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {
    var listJoke: List<Joke> = emptyList()

    set(list : List<Joke>) {
        field = list
        notifyDataSetChanged()}

    class JokeViewHolder(val tV : TextView) : RecyclerView.ViewHolder(tV)

    fun setList(list : List<Joke>) {listJoke = list}

    override fun getItemCount() = listJoke.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val tV = LayoutInflater.from(parent.context).inflate(R.layout.recycler_layout, parent, false) as TextView
        return JokeViewHolder(tV)
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.tV.text = listJoke[position].value
    }
}