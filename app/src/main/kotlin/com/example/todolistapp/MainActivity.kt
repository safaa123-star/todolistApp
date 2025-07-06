package com.example.todolistapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.SharedPreferences
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val tasks = mutableListOf<String>()
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private val PREFS_NAME = "ToDoPrefs"
    private val TASKS_KEY = "tasks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskInput = findViewById<EditText>(R.id.taskInput)
        val addButton = findViewById<Button>(R.id.addButton)
        recyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)

        // تحميل المهام المحفوظة
        loadTasks()

        taskAdapter = TaskAdapter(tasks) { position ->
            tasks.removeAt(position)
            saveTasks()
            taskAdapter.notifyDataSetChanged()
        }
        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addButton.setOnClickListener {
            val task = taskInput.text.toString()
            if (task.isNotEmpty()) {
                tasks.add(task)
                saveTasks()
                taskAdapter.notifyDataSetChanged()
                taskInput.text.clear()
                Toast.makeText(this, "Task added: $task", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteTask(view: View) {
        val position = recyclerView.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION) {
            tasks.removeAt(position)
            saveTasks()
            taskAdapter.notifyDataSetChanged()
        }
    }

    private fun saveTasks() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putStringSet(TASKS_KEY, tasks.toHashSet())
        editor.apply()
    }

    private fun loadTasks() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedTasks = prefs.getStringSet(TASKS_KEY, mutableSetOf()) ?: mutableSetOf()
        tasks.clear()
        tasks.addAll(savedTasks)
    }
}
