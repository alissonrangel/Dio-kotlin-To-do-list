package life.alissonescorcio.todolist.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import life.alissonescorcio.todolist.model.Task

class HelperDB (
    context: Context
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL){

    companion object{
        private val NOME_BANCO = "todo.db"
        private val VERSAO_ATUAL = 1
    }

    val TABLE_NAME = "todo"
    val COLUMNS_ID = "id"
    val COLUMNS_TITLE = "title"
    val COLUMNS_DESCRIPTION = "description"
    val COLUMNS_DATE = "date"
    val COLUMNS_HOUR = "hour"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMNS_ID INTEGER NOT NULL," +
            "$COLUMNS_TITLE TEXT NOT NULL," +
            "$COLUMNS_DESCRIPTION TEXT NOT NULL," +
            "$COLUMNS_DATE TEXT NOT NULL," +
            "$COLUMNS_HOUR TEXT NOT NULL," +
            "" +
            "PRIMARY KEY ($COLUMNS_ID AUTOINCREMENT)" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if ( oldVersion != newVersion){
            //update da sua table ou criar novas tabelas
            db?.execSQL(DROP_TABLE)
            onCreate(db)
        }
    }

    fun listarTarefas() : List<Task>{
        val db :SQLiteDatabase = readableDatabase ?: return arrayListOf()
        var lista : ArrayList<Task> = arrayListOf<Task>()

        /*
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_NOME LIKE ?"
        var buscaComPercentual = "%$busca%"
        var cursor :Cursor = db.rawQuery(sql, arrayOf(buscaComPercentual))
        */


        var cursor : Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)


        if (cursor == null){
            db.close()
            return arrayListOf()
        }
        while (cursor.moveToNext()){
            var todo = Task(
                cursor.getString(cursor.getColumnIndex(COLUMNS_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DATE)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_HOUR)),
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID))
            )
            lista.add(todo)
        }
        db.close()
        return lista
    }

    fun buscarContatoPorId(busca: String) : Task?{
        //salvarContato(ContatosVO(0,"TESTE 3", "TESTE 3"))

        val db :SQLiteDatabase = readableDatabase
        var lista :MutableList<Task> = mutableListOf<Task>()

        var where: String? = null
        var args: Array<String> = arrayOf()


        where = "$COLUMNS_ID = ?"
        args = arrayOf("$busca")



        var cursor : Cursor = db.query(TABLE_NAME, null, where, args, null, null, null)
        var task : Task? = null

        if (cursor == null){
            db.close()
            return null
        }
        while (cursor.moveToNext()){
            task = Task(
                cursor.getString(cursor.getColumnIndex(COLUMNS_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DATE)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_HOUR)),
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID))
            )
        }
        db.close()
        return task
    }

    fun salvarContato(task: Task){
        val db = writableDatabase ?: return

        //val sql = "INSERT INTO $TABLE_NAME ($COLUMNS_NOME, $COLUMNS_TELEFONE) VALUES (?, ?)"
        //var array: Array<String> = arrayOf(contato.nome, contato.telefone)
        //db.execSQL(sql, array)

        var content = ContentValues()

        content.put(COLUMNS_TITLE, task.title)
        content.put(COLUMNS_DESCRIPTION, task.description)
        content.put(COLUMNS_DATE, task.date)
        content.put(COLUMNS_HOUR, task.hour)
        db.insert(TABLE_NAME, null, content)

        db.close()
    }

    fun updateContato(task: Task){
        val db :SQLiteDatabase = writableDatabase ?: return

        /*
        val content = ContentValues()
        content.put(COLUMNS_NOME, contato.nome)
        content.put(COLUMNS_TELEFONE, contato.telefone)
        val where = "id = ?"
        var arg = arrayOf("${contato.id}")
        db.update(TABLE_NAME, content, where, arg)
        */

        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_TITLE = ?, $COLUMNS_DESCRIPTION = ?," +
                "$COLUMNS_DATE = ?, $COLUMNS_HOUR = ? WHERE $COLUMNS_ID = ?"
        var args = arrayOf(task.title, task.description, task.date, task.hour, task.id)

        db.execSQL(sql, args)

        db.close()
    }

    fun deletarContato(id: Int){
        val db :SQLiteDatabase = writableDatabase ?: return

        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMNS_ID = ?"
        val arg = arrayOf("$id")
        db.execSQL(sql, arg)
        db.close()
    }
}