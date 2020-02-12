package com.gabriel.rememberme.ui.home.memory

import android.util.Log
import com.gabriel.rememberme.R
import com.gabriel.rememberme.data.database.entities.memory.Memory
import com.gabriel.rememberme.databinding.MemoryItemBinding
import com.gabriel.rememberme.util.hide
import com.gabriel.rememberme.util.toast
import com.gabriel.rememberme.util.toggle
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem
import java.lang.Exception

class MemoryItem(
    private val memory: Memory
) : BindableItem<MemoryItemBinding>() {

    override fun getLayout() = R.layout.memory_item

    override fun bind(viewBinding: MemoryItemBinding, position: Int) {
        viewBinding.memory = memory

        Picasso.get()
            .load(memory.image_url)
            .into(viewBinding.memoryImage, object : Callback {
                override fun onSuccess() {
                    viewBinding.progressBar.hide()
                }

                override fun onError(e: Exception?) {
                    Log.println(Log.ERROR, "ERROR", e!!.stackTrace.toString())
                    viewBinding.root.context.toast("Failed to load image, try again later")
                }
            })
    }

    fun getMemoryId(): Long {
        return memory.id
    }

}