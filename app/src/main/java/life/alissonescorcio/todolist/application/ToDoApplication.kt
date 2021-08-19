package life.alissonescorcio.todolist.application

import android.app.Application
import life.alissonescorcio.todolist.helpers.HelperDB

class ToDoApplication : Application(){
    var helperDB: HelperDB? = null
        private set

    companion object{
        lateinit var instance: ToDoApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }
}