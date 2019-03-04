package com.razvanb.hellomready.ui.callbacks

import com.razvanb.hellomready.data.network.pojo.ItemsItem

interface OnItemClickListener {
    fun onItemClick(repo: ItemsItem)
}