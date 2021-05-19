package com.loong.ihms.adapter

import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.loong.ihms.R
import com.loong.ihms.activity.AlbumDetailsActivity
import com.loong.ihms.databinding.ViewAlbumItemBinding
import com.loong.ihms.model.Album
import com.loong.ihms.utils.ConstantDataUtil

class AlbumListAdapter(val context: Context, private val itemList: ArrayList<Album>) : RecyclerViewBaseAdapter() {
    override fun setBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val binding: ViewAlbumItemBinding = viewHolder.binding as ViewAlbumItemBinding
        val itemData: Album = itemList[position]

        Glide.with(context)
            .load(itemData.art)
            .into(binding.albumItemImg)

        binding.albumItemTitleTv.text = itemData.name
        binding.albumItemDescTv.text = itemData.artist.name

        binding.albumItemCv.setOnClickListener {
            val intent = Intent(context, AlbumDetailsActivity::class.java)
            intent.putExtra(ConstantDataUtil.ALBUM_DETAILS_ID_PARAMS, itemData.id.toInt())
            context.startActivity(intent)
        }
    }

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.view_album_item
    }

    override fun getItemTotalCount(): Int {
        return itemList.size
    }
}