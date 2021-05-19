package com.loong.ihms.adapter

import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.loong.ihms.R
import com.loong.ihms.activity.AlbumDetailsActivity
import com.loong.ihms.databinding.ViewAlbumItemBinding
import com.loong.ihms.model.CuratorAlbum
import com.loong.ihms.utils.ConstantDataUtil
import com.loong.ihms.utils.GeneralUtil.createAlertDialog
import com.loong.ihms.utils.UserRelatedUtil

class AlbumPlaylistListAdapter(val context: Context, private var itemList: ArrayList<CuratorAlbum>) : RecyclerViewBaseAdapter() {
    override fun setBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val binding: ViewAlbumItemBinding = viewHolder.binding as ViewAlbumItemBinding
        val itemData: CuratorAlbum = itemList[position]

        Glide.with(context)
            .load(itemData.art)
            .into(binding.albumItemImg)

        binding.albumItemTitleTv.text = itemData.name
        binding.albumItemDescTv.text = itemData.getArtistNames()

        binding.albumItemCv.setOnClickListener {
            val intent = Intent(context, AlbumDetailsActivity::class.java)
            intent.putExtra(ConstantDataUtil.ALBUM_PLAYLIST_DETAILS_ID_PARAMS, itemData.id.toInt())
            context.startActivity(intent)
        }

        binding.albumItemCv.setOnLongClickListener {
            context.createAlertDialog(
                "Delete playlist",
                "Are you sure to delete this playlist item?",
                "Yes",
                {
                    val curatorAlbum = UserRelatedUtil.getCuratorAlbumList()
                    curatorAlbum.removeIf { it.id == itemData.id }

                    UserRelatedUtil.saveCuratorAlbumList(curatorAlbum)
                    itemList = curatorAlbum
                    notifyDataSetChanged()
                },
                "No"
            )

            true
        }
    }

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.view_album_item
    }

    override fun getItemTotalCount(): Int {
        return itemList.size
    }
}