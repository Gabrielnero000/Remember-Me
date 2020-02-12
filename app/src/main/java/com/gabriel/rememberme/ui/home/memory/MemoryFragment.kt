@file:Suppress("DEPRECATION")

package com.gabriel.rememberme.ui.home.memory


import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.rememberme.R
import com.gabriel.rememberme.data.database.entities.memory.Memory
import com.gabriel.rememberme.util.*
import com.google.android.gms.tasks.Task
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_memory.*
import kotlinx.android.synthetic.main.memory_item.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class MemoryFragment : Fragment(), KodeinAware, UploadListener {

    override val kodein by kodein()

    private val factory: MemoryViewModelFactory by instance()

    private var menuVisible: Boolean = false

    private lateinit var viewModel: MemoryViewModel

    private lateinit var progressDialog: ProgressDialog

    override fun onComplete(task: Task<Uri>) {
        if (task.isSuccessful) {
            val url = task.result.toString()
            createMemory(url)
            context?.toast("Upload successful")
        } else
            context?.toast("Upload failed")

        progressDialog.dismiss()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_memory, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(MemoryViewModel::class.java)
        setupButtons()
        bindUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constants.GET_IMAGE_INTENT) {
            val imageUri = data?.data!!

            try {
                startUpload(imageUri)
            } catch (e: Throwable){
                Log.println(Log.ERROR, "ERROR", e.toString())
                context?.toast("Failed to load image")
            }
        }
    }

    private fun bindUI() = Coroutines.main {
        progress_bar.show()
        viewModel.memories.await().observe(this, Observer {
            progress_bar.hide()
            initRecyclerView(it.toMemoryItem())
        })
    }

    private fun initRecyclerView(memoryItems: List<MemoryItem>) {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(memoryItems)
            setOnItemClickListener { item, view ->
                val memoryItem = item as MemoryItem
                view.setOnClickListener { view.memory_text.toggle() }
                view.setOnLongClickListener {
                    viewModel.removeMemory(memoryItem.getMemoryId())
                    true
                }
            }

        }

        memory_recycler_view.apply {
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun startUpload(uri: Uri) {
        progressDialog = ProgressDialog.show(context, "", "Uploading image...", true)
        progressDialog.show()
        viewModel.uploadImage(uri, this)
    }

    private fun setupButtons() {
        toggle_menu_button.setOnClickListener { toggleMenu() }
        add_button.setOnClickListener { launchImageGetter() }
        logout_button.setOnClickListener { viewModel.logout(context!!) }


    }

    private fun createMemory(url: String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Enter a name to your memory :)")

        val input = EditText(context!!)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val text = input.text.toString()
            viewModel.addMemory(text, url)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun toggleMenu() {
        if(menuVisible) {
            val translationFactor = toggle_menu_button.translationY

            toggle_menu_button.animate().setDuration(100).translationY(translationFactor + 400f).start()
            toggle_menu_button.animate().setDuration(100).rotation(0f).start()
            logout_button.animate().setDuration(100).translationY(translationFactor + 200f).start()

            add_button.hide()
            logout_button.hide()

            menuVisible = false

        } else {
            add_button.show()
            logout_button.show()

            val translationFactor = toggle_menu_button.translationY

            toggle_menu_button.animate().setDuration(100).translationY(translationFactor - 400f).start()
            toggle_menu_button.animate().setDuration(100).rotation(180f).start()
            logout_button.animate().setDuration(100).translationY(translationFactor - 200f).start()

            menuVisible = true
        }

    }

    private fun List<Memory>.toMemoryItem() : List<MemoryItem> {
        return this.map {
            MemoryItem(it)
        }
    }


}
