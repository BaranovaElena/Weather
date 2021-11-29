package com.example.weather.ui.citylist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.domain.model.City

class CityListAdapter(var onItemViewClickListener: CityListFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<CityListAdapter.CityListViewHolder>() {

    private var cityList: List<City> = listOf()

    fun setCityList(data: List<City>) {
        cityList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
        return CityListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CityListViewHolder, position: Int) {
        holder.bind(cityList[position])
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class CityListViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.city_list_item, parent, false)
    ) {

        fun bind(city: City) {
            itemView.apply {
                findViewById<TextView>(R.id.city_list_item_text_view).text = city.name
                setOnClickListener { onItemViewClickListener?.onItemViewClick(city) }

                val imageView = findViewById<ImageView>(R.id.city_image)
                Glide.with(this)
                    .load(city.image)
                    .centerCrop()
                    .into(imageView)

                val likeBtn = findViewById<ImageButton>(R.id.city_list_item_like_button)
                likeBtn.setOnClickListener {
                    when (likeBtn.isSelected) {
                        true -> {
                            likeBtn.isSelected = false
                            likeBtn.setImageResource(R.drawable.ic_not_liked)
                        }
                        false -> {
                            likeBtn.isSelected = true
                            likeBtn.setImageResource(R.drawable.ic_liked)
                        }
                    }
                }
            }
        }
    }
}


