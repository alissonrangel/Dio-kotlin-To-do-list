package life.alissonescorcio.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import life.alissonescorcio.todolist.databinding.ActivityMainBinding
import life.alissonescorcio.todolist.datasource.TaskDataSource
import life.alissonescorcio.todolist.model.Task
import life.alissonescorcio.todolist.ui.AddTaskActivity.Companion.TASK_ID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.rvTasks.adapter = adapter
        updateList()
        insertListeners()
    }

    override fun onStart() {
        super.onStart()
        updateList()
    }

    private fun insertListeners() {
        binding.fabAddTodo.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            Log.e("TAG", "listenerEdit - $it")
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK){
            updateList()
            Toast.makeText(this,"onActivityResult", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateList(){
        val list = TaskDataSource.getList()
        binding.include.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        if( list.isNotEmpty()) Toast.makeText(this,"${list[0].title}", Toast.LENGTH_LONG).show()


        adapter.submitList(TaskDataSource.getList())

    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}