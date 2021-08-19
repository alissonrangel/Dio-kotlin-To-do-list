package life.alissonescorcio.todolist.datasource

import life.alissonescorcio.todolist.application.ToDoApplication
import life.alissonescorcio.todolist.model.Task
import life.alissonescorcio.todolist.ui.TaskListAdapter

object TaskDataSource {
    private var list = arrayListOf<Task>()

    fun getList() = list.toList()

    fun insertTask(task: Task) {
        if ( task.id == 0 ) {
            list.add(task.copy(id = list.size + 1))
        } else {
            var id = task.id
            for (i in 0..(list.size -1)){
                if (list[i].id == id ){
                    list.removeAt(i)
                    list.add(task)
                }
            }

        }
    }
    fun insertTaskSqlLite(task: Task) {
        if ( task.id == 0 ) {
            //list.add(task.copy(id = list.size + 1))
            ToDoApplication.instance.helperDB?.salvarContato(task)
        } else {
            ToDoApplication.instance.helperDB?.updateContato(task)
        }
    }


    fun findById(taskId: Int) : Task?{
        return list.find { it.id == taskId }
    }

    fun findByIdSqlLite(taskId: Int) : Task?{
        var task = ToDoApplication.instance.helperDB?.buscarContatoPorId(taskId.toString())
        return task
    }

    fun deleteTask(task: Task) {
        list.remove(task)
    }

    fun deleteTaskSqlLite(task: Task) {
        ToDoApplication.instance.helperDB?.deletarContato(task.id)
    }

}