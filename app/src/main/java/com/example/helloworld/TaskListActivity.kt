package com.example.helloworld

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskListActivity : AppCompatActivity() {

    private val taskDao = MyApplication.db.taskDao()

    private var taskList: List<Task> = listOf()

    private val adapter = TaskListAdapter()

    private val dialogList = arrayOf(
        "編集する",
        "削除する"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        findViewById<RecyclerView>(R.id.recyclerView).let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        }

        findViewById<FloatingActionButton>(R.id.fab).let {
            it.setOnClickListener {
                startActivity(Intent(this, TaskAddActivity::class.java))
            }
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.Default) {
            taskList = taskDao.getAll()
            withContext(Dispatchers.Main) { adapter.notifyDataSetChanged() }
        }
    }

    private inner class TaskListAdapter : RecyclerView.Adapter<TaskItemViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): TaskItemViewHolder = TaskItemViewHolder(
            LayoutInflater.from(this@TaskListActivity)
                .inflate(R.layout.item_task_list, parent, false)
        )

        override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
            holder.run {
                titleTextView.text = taskList[position].title
                container.setOnClickListener {
                    Intent(this@TaskListActivity, TaskDetailActivity::class.java).run {
                        putExtra("id", taskList[position].id)
                        startActivity(this)
                    }
                }
                container.setOnLongClickListener {
                    showAlertDialog(position)
                    true
                }
            }
        }

        override fun getItemCount(): Int = taskList.size
    }

    private inner class TaskItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: FrameLayout = itemView.findViewById(R.id.container)
        val titleTextView: TextView = itemView.findViewById(R.id.title)
    }

    fun showAlertDialog(position: Int) {
        AlertDialog.Builder(this@TaskListActivity)
            .setItems(dialogList) { _, which ->
                when (which) {
                    0 -> Intent(
                        this@TaskListActivity,
                        TaskModifyActivity::class.java
                    ).run {
                        putExtra("id", taskList[position].id)
                        startActivity(this)
                    }
                    1 -> lifecycleScope.launch(Dispatchers.Default) {
                        taskDao.deleteTask(taskList[position])
                        taskList = taskDao.getAll()
                        withContext(Dispatchers.Main) {
                            adapter.notifyItemRemoved(position)
                        }
                    }
                }
            }.show()
    }
}
