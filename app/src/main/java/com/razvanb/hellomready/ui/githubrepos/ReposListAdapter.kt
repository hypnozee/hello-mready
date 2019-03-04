package com.razvanb.hellomready.ui.githubrepos

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.razvanb.hellomready.R
import com.razvanb.hellomready.data.network.pojo.ItemsItem
import com.razvanb.hellomready.ui.callbacks.OnItemClickListener
import com.razvanb.hellomready.utils.Constants.REPO_LIST_LAYOUT
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

class ReposListAdapter internal constructor(
    private val mReposList: List<ItemsItem?>?,
    private val mListener: OnItemClickListener)
    : RecyclerView.Adapter<ReposListAdapter.RepoHolder>() {

    private var mLayout: Int = 0

    fun setLayout(layout: Int) {
        this.mLayout = layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        return RepoHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    if (mLayout == REPO_LIST_LAYOUT) {
                        R.layout.repo_list_item
                    }
                    else {
                        R.layout.repo_grid_item
                    },
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        holder.bind(mReposList!![position]!!, mListener)
    }

    override fun getItemCount(): Int {
        return mReposList?.size ?: 0
    }

    inner class RepoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mThumbnail: ImageView = itemView.findViewById(R.id.repo_img)
        private val mMake: TextView = itemView.findViewById(R.id.repo_name)
        private val mModel: TextView = itemView.findViewById(R.id.repo_description)
        private val mPrice: TextView = itemView.findViewById(R.id.repo_stars)

        fun bind(repo: ItemsItem, listener: OnItemClickListener) {
            loadRepoImage(repo.owner?.avatarUrl ?: "", mThumbnail)
            mMake.text = repo.name
            mModel.text = repo.description
            mPrice.text = repo.stargazersCount.toString()

            itemView.setOnClickListener { listener.onItemClick(repo) }
        }

        private fun loadRepoImage(url: String, imageView: ImageView) {

            Glide.with(imageView)
                .load(url)
                .apply(
                    RequestOptions()
                        .fallback(R.drawable.ic_repo_placeholder_error)
                        .error(R.drawable.ic_repo_placeholder_error)
                )
                //                    .transition(new GenericTransitionOptions<>())
                .transition(withCrossFade(200))
                .thumbnail(0.1f)
                .into(imageView)
        }
    }
}