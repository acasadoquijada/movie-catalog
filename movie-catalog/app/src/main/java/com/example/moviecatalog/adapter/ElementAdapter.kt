package com.example.moviecatalog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.R
import com.example.moviecatalog.databinding.ElementImageLayoutBinding
import com.example.moviecatalog.model.Element
import com.squareup.picasso.Picasso

class ElementAdapter(var mItemClickListener: OnClickElementInterface, var type: String) :
    RecyclerView.Adapter<ElementAdapter.ElementHolder>() {

    companion object {
        const val TVSHOW = "tvShow"
        const val MOVIE = "movie"
        const val WATCHLIST = "watchlist"
    }

    private var elementList: List<Element> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = ElementImageLayoutBinding.inflate(inflater, parent, false)

        return ElementHolder(binding)
    }

    interface OnClickElementInterface {
        fun onItemClick(clickedItem: Int, type: String)
    }

    override fun getItemCount(): Int {
        return elementList.size
    }

    override fun onBindViewHolder(holder: ElementHolder, position: Int) {
        holder.bind(elementList[position].getPosterPathURL(), elementList[position].type)
    }

    fun setElementList(movieList: List<Element>) {
        this.elementList = movieList
        this.notifyDataSetChanged()
    }

    inner class ElementHolder(private val binding: ElementImageLayoutBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            this.binding.root.setOnClickListener(this)
        }

        fun bind(imagePath: String?, type: String) {
            setImage(imagePath)
            setLogo(type)
        }

        private fun setImage(imagePath: String?) {
            Picasso.get().load(imagePath).into(binding.image)
        }

        private fun setLogo(type: String) {
            val drawableLogo = if (type == MOVIE) {
                R.drawable.ic_cinema
            } else {
                R.drawable.ic_television
            }

            binding.typeLogo.setImageResource(drawableLogo)
        }

        override fun onClick(v: View?) {
            mItemClickListener.onItemClick(adapterPosition, type)
        }
    }
}
