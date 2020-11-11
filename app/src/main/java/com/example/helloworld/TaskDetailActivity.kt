package com.example.helloworld

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailActivity : AppCompatActivity() {

    private val taskDao = MyApplication.db.taskDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val titleText = findViewById<TextView>(R.id.title)
        val descriptionText = findViewById<TextView>(R.id.description)
        val createdAtText = findViewById<TextView>(R.id.createdAt)
        val updatedAtText = findViewById<TextView>(R.id.updatedAt)

        val id = intent.getIntExtra("id", 0)
        lifecycleScope.launch(Dispatchers.Default) {
            val task = taskDao.getTask(id)
            withContext(Dispatchers.Main) {
                titleText.text = task.title
                descriptionText.text = task.description
                createdAtText.text = task.createdAt.toDateText()
                updatedAtText.text = task.updatedAt.toDateText()
            }
        }
    }
}