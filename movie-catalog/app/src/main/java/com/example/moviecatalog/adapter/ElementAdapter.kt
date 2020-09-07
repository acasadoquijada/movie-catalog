package com.example.moviecatalog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.databinding.ElementImageLayoutBinding
import com.example.moviecatalog.model.Element
import com.squareup.picasso.Picasso

class ElementAdapter(var mItemClickListener: OnClickElementInterface, val type: String) :
    RecyclerView.Adapter<ElementAdapter.ElementHolder>() {

    companion object {
        const val TVSHOW = "tvShow"
        const val MOVIE = "movie"
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
        holder.bind(elementList[position].getPosterPathURL())
    }

    fun setElementList(movieList: List<Element>) {
        this.elementList = movieList
        this.notifyDataSetChanged()
    }

    inner class ElementHolder(private val binding: ElementImageLayoutBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            this.binding.root.setOnClickListener(this)
        }

        fun bind(imagePath: String?) {
            setImage(imagePath)
        }

        private fun setImage(imagePath: String?) {
            Picasso.get().load(imagePath).into(binding.image)
        }

        override fun onClick(v: View?) {
            mItemClickListener.onItemClick(adapterPosition, type)
        }
    }
}
