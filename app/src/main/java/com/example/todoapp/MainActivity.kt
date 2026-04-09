package com.example.todoapp

import TaskAdapter
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addBtn = findViewById<Button>(R.id.addTaskBtn)
        recyclerView = findViewById(R.id.recyclerView)

        // Load saved tasks
        loadTasks()

        adapter = TaskAdapter(taskList,
            onEdit = { position -> showEditDialog(position) },
            onDelete = { position -> deleteTask(position) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addBtn.setOnClickListener {
            showAddDialog()
        }
    }

    // ➕ Add Task
    private fun showAddDialog() {
        val input = EditText(this)

        AlertDialog.Builder(this)
            .setTitle("Add Task")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val text = input.text.toString()
                if (text.isNotEmpty()) {
                    taskList.add(Task(text))
                    adapter.notifyDataSetChanged()
                    saveTasks()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // ✏️ Edit Task
    private fun showEditDialog(position: Int) {
        val input = EditText(this)
        input.setText(taskList[position].title)

        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(input)
            .setPositiveButton("Update") { _, _ ->
                taskList[position].title = input.text.toString()
                adapter.notifyDataSetChanged()
                saveTasks()
            }
            .show()
    }

    // ❌ Delete Task
    private fun deleteTask(position: Int) {
        taskList.removeAt(position)
        adapter.notifyDataSetChanged()
        saveTasks()
    }

    // 💾 Save Tasks
    private fun saveTasks() {
        val prefs = getSharedPreferences("todo", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val gson = Gson()
        val json = gson.toJson(taskList)

        editor.putString("tasks", json)
        editor.apply()
    }

    // 📥 Load Tasks
    private fun loadTasks() {
        val prefs = getSharedPreferences("todo", Context.MODE_PRIVATE)
        val gson = Gson()

        val json = prefs.getString("tasks", null)
        val type = object : TypeToken<MutableList<Task>>() {}.type

        if (json != null) {
            val savedList: MutableList<Task> = gson.fromJson(json, type)
            taskList.clear()
            taskList.addAll(savedList)
        }
    }
}