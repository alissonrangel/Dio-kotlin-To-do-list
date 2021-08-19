package life.alissonescorcio.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import life.alissonescorcio.todolist.application.ToDoApplication
import life.alissonescorcio.todolist.databinding.ActivityAddTaskBinding
import life.alissonescorcio.todolist.datasource.TaskDataSource
import life.alissonescorcio.todolist.extensions.format
import life.alissonescorcio.todolist.extensions.text
import life.alissonescorcio.todolist.model.Task
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (intent.hasExtra(TASK_ID)){
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findByIdSqlLite(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
                binding.tilDescription.text = it.description
            }
        }

        insertListener()
    }

    private fun insertListener() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute < 10) "0"+timePicker.minute else timePicker.minute
                val hour = if (timePicker.hour < 10) "0"+timePicker.hour else timePicker.hour
                binding.tilHour.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.btNewTask.setOnClickListener{
            val task = Task(
                title = binding.tilTitle.text,
                description = binding.tilDescription.text,
                hour = binding.tilHour.text,
                date = binding.tilDate.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTaskSqlLite(task)

            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.btCancel.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }

}