package com.example.helloworld

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskModifyActivity : AppCompatActivity() {

    private val taskDao = MyApplication.db.taskDao()

    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_add)

        val titleText = findViewById<EditText>(R.id.title)
        val descriptionText = findViewById<EditText>(R.id.description)
        val completeButton = findViewById<Button>(R.id.completeButton)

        val id = intent.getIntExtra("id", 0)

        lifecycleScope.launch {
            task = taskDao.getTask(id)
            withContext(Dispatchers.Main) {
                titleText.setText(task.title)
                descriptionText.setText(task.description)
            }
        }

        completeButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val task = Task(
                    0,
                    titleText.text.toString(),
                    descriptionText.text.toString(),
                    task.createdAt,
                    System.currentTimeMillis(),
                    false
                )
                taskDao.updateTask(task)
                withContext(Dispatchers.Main) { onBackPressed() }
            }
        }
    }
}
