package com.example.helloworld

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskAddActivity : AppCompatActivity() {

    private val taskDao = MyApplication.db.taskDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_add)

        val titleText = findViewById<EditText>(R.id.title)
        val descriptionText = findViewById<EditText>(R.id.description)
        val completeButton = findViewById<Button>(R.id.completeButton)

        completeButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val task = Task(
                    0,
                    titleText.text.toString(),
                    descriptionText.text.toString(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    false
                )
                taskDao.insertTask(task)
                withContext(Dispatchers.Main) { onBackPressed() }
            }
        }
    }

}