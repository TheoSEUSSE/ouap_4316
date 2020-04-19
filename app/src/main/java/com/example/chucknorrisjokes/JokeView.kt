package com.example.chucknorrisjokes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.view.*


class JokeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var view: TextView
    private lateinit var share: ImageView
    private lateinit var like: ImageView

    data class Model (val joke : Joke, var bool : Boolean)

    fun setupView(model : Model) {
        view.text = model.joke.toString()
        if(model.bool){
            like.setImageResource(R.drawable.like)
        }
        else {
            like.setImageResource(R.drawable.nolike)
        }
    }

    fun inflate(layout: Int) : JokeView {
        View.inflate(context, layout, this)
        view = findViewById(R.id.my_text_view)
        share = findViewById(R.id.share)
        like = findViewById(R.id.like)
        return this
    }
}