package com.example.allmovies.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.allmovies.AppConstants
import com.example.allmovies.R
import com.example.allmovies.network.model.dto.MovieDTO
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_movie.view.cv_movie
import kotlinx.android.synthetic.main.list_item_movie.view.iv_moviePoster

class MoviesAdapter(val movieClick: OnMovieClick, val context: Context, private val movies: List<MovieDTO>) :
    RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false)
        return MoviesHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        movies.elementAt(position).apply {

            Picasso.get()
                .load("${AppConstants.TMDB_IMAGE_BASE_URL_W185}${poster_path}")
                .placeholder(R.drawable.placeholder)
                .into(holder.poster)

            holder.cardView.setOnClickListener {
                id.let { id -> movieClick.onClick(id) }
            }

        }
    }

    override fun getItemCount(): Int = movies.size

    class MoviesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView = itemView.cv_movie
        var poster: ImageView = itemView.iv_moviePoster
    }
}
