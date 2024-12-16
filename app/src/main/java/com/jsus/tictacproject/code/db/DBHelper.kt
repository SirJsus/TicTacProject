package com.jsus.tictacproject.code.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.Register
import com.jsus.tictacproject.code.objects.Task
import java.time.LocalDateTime

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "TicTacDatabase"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME_ACTIVITY = "actividad"
        private const val id_ac = "activity_id"
        private const val name_ac = "name_ac"
        private const val desc_ac = "description_ac"
        private const val arch_ac = "archived_ac"

        private const val CREATE_ACTIVITY_TABLE =
            "CREATE TABLE $TABLE_NAME_ACTIVITY (" +
                    "$id_ac INTEGER NOT NULL," +
                    "$name_ac TEXT NOT NULL," +
                    "$desc_ac TEXT," +
                    "$arch_ac INTEGER NOT NULL," +
                    "PRIMARY KEY ($id_ac)" +
                    ");"

        private const val TABLE_NAME_REGISTER = "registro"
        private const val id_rg = "register_id"
        private const val desc_rg = "desc_rg"
        private const val start_rg = "star_rg"
        private const val end_rg = "end_rg"

        private const val CREATE_REGISTER_TABLE =
            "CREATE TABLE $TABLE_NAME_REGISTER (" +
                    "$id_rg INTEGER NOT NULL," +
                    "$id_ac INTEGER NOT NULL," +
                    "$desc_rg TEXT," +
                    "$start_rg TEXT," +
                    "$end_rg TEXT," +
                    "PRIMARY KEY ($id_rg, $id_ac)," +
                    "FOREIGN KEY ($id_ac) REFERENCES $TABLE_NAME_ACTIVITY($id_ac)" +
                    ");"

        private const val TABLE_NAME_NOW = "ahora"
        private const val id_now = "now_id"
        private const val start_now = "star_now"

        private const val CREATE_NOW_TABLE =
            "CREATE TABLE $TABLE_NAME_NOW (" +
                    "$id_now INTEGER NOT NULL," +
                    "$id_ac INTEGER NOT NULL," +
                    "$start_now TEXT," +
                    "PRIMARY KEY ($id_now)," +
                    "FOREIGN KEY ($id_ac) REFERENCES $TABLE_NAME_ACTIVITY($id_ac)" +
                    ");"

        private const val TABLE_NAME_TASK = "tareas"
        private const val id_task = "now_task"
        private const val name_task = "name_task"
        private const val desc_task = "description_task"
        private const val arch_task = "archived_task"

        private const val CREATE_TASK_TABLE =
            "CREATE TABLE $TABLE_NAME_TASK (" +
                    "$id_task INTEGER NOT NULL," +
                    "$name_task TEXT NOT NULL," +
                    "$desc_task TEXT," +
                    "$arch_task INTEGER NOT NULL," +
                    "PRIMARY KEY ($id_task)" +
                    ");"

        private const val TABLE_NAME_TSK_ACT = "tarea_act"
        private const val position_tsk_act = "position_tsk_act"

        private const val CREATE_TSK_ACT_TABLE =
            "CREATE TABLE $TABLE_NAME_TSK_ACT (" +
                    "$position_tsk_act INTEGER NOT NULL," +
                    "$id_task INTEGER NOT NULL," +
                    "$id_ac INTEGER NOT NULL," +
                    "PRIMARY KEY ($position_tsk_act,$id_task,$id_ac)," +
                    "FOREIGN KEY ($id_task) REFERENCES $TABLE_NAME_TASK($id_task)," +
                    "FOREIGN KEY ($id_ac) REFERENCES $TABLE_NAME_ACTIVITY($id_ac)" +
                    ");"
    }

    fun insertActivity(data: Activity) {
        val values = ContentValues()
        with(values){
            put(id_ac, data.id)
            put(name_ac, data.name)
            put(desc_ac, data.description)
            val arch = if (data.archived) 1
                        else 0
            put(arch_ac, arch)
        }
        val result = insertOnTable(TABLE_NAME_ACTIVITY, null, values)
        Log.d("tictac_DBHelper", "insertActivity: $data\n$result")
    }

    private fun getActivity(cursor: Cursor): Activity {
        val data: Activity = if (cursor.moveToFirst()){
            val id    = cursor.getInt (cursor.getColumnIndex(id_ac).toInt())
            val name    = cursor.getString (cursor.getColumnIndex(name_ac).toInt())
            val desc    = cursor.getString (cursor.getColumnIndex(desc_ac).toInt())
            val arch    = cursor.getInt (cursor.getColumnIndex(desc_ac).toInt())
            val getArch = (arch == 1)
            Activity(id, name, desc, getArch)
        } else Activity()
        cursor.close()
        return data
    }

    fun getActivityCount(): Int{
        val query = "SELECT COUNT(*) FROM $TABLE_NAME_ACTIVITY"
        Log.d("tictac_DBHelper", "getActivityCount query: $query")
        val cursor = readableDatabase.rawQuery(query, null)
        val count = if (cursor.moveToFirst()){
            cursor.getInt(0)
        } else 0
        Log.d("tictac_DBHelper", "getActivityCount count: $count")
        cursor.close()
        return count
    }

    fun getActivityByID(id: Int): Activity{
        val query = "SELECT * FROM $TABLE_NAME_ACTIVITY WHERE $id_ac = $id"
        Log.d("tictac_DBHelper", "getActivityByID query: $query")
        val cursor = readableDatabase.rawQuery(query, null)
        val result = getActivity(cursor)
        Log.d("tictac_DBHelper", "getActivityByID result: $result")
        return result
    }

    fun getActivityList(): MutableList<Activity>{
        val columns = arrayOf(id_ac, name_ac, desc_ac)
        val cursor = readableDatabase.query(
            TABLE_NAME_ACTIVITY, columns,
            null, null, null, null, null)
        val dataList = mutableListOf<Activity>()
        while (cursor.moveToNext()){
            val id    = cursor.getInt (cursor.getColumnIndex(id_ac).toInt())
            val name    = cursor.getString (cursor.getColumnIndex(name_ac).toInt())
            val desc    = cursor.getString (cursor.getColumnIndex(desc_ac).toInt())
            dataList.add(Activity(id, name, desc))
        }
        cursor.close()
        Log.d("tictac_DBHelper", "getActivityList dataList: $dataList")
        return dataList
    }

    fun insertRegister(data: Register) {
        val values = ContentValues()
        with(values){
            put(id_rg, data.id)
            put(id_ac, data.activity.id)
            put(desc_rg, data.description)
            put(start_rg, data.start.toString())
            put(end_rg, data.end.toString())
        }
        val result = insertOnTable(TABLE_NAME_REGISTER, null, values)
        Log.d("tictac_DBHelper", "insertRegister: $data")
    }

    private fun getRegister(cursor: Cursor): Register {
        val data: Register = if (cursor.moveToFirst()){
            val id    = cursor.getInt (cursor.getColumnIndex(id_rg).toInt())
            val id_ac    = cursor.getInt (cursor.getColumnIndex(id_ac).toInt())
            val desc    = cursor.getString (cursor.getColumnIndex(desc_rg).toInt())
            val start    = cursor.getString (cursor.getColumnIndex(start_rg).toInt())
            val end    = cursor.getString (cursor.getColumnIndex(end_rg).toInt())

            val activity = getActivityByID(id_ac)
            if (activity == Activity()) return Register()
            Register(id, desc, activity, LocalDateTime.parse(start), LocalDateTime.parse(end))
        } else Register()
        cursor.close()
        return data
    }

    fun getRegisterByID(id: Int, activity: Activity): Register{
        val query = "SELECT * FROM $TABLE_NAME_REGISTER WHERE $id_rg = $id AND $id_ac = ${activity.id}"
        Log.d("tictac_DBHelper", "getActivityByID query: $query")
        val cursor = readableDatabase.rawQuery(query, null)
        val result = getRegister(cursor)
        Log.d("tictac_DBHelper", "getActivityByID result: $result")
        return result
    }

    fun getRegisterList(): MutableList<Register>{
        val columns = arrayOf(id_rg, id_ac, desc_rg, start_rg, end_rg)
        val cursor = readableDatabase.query(
            TABLE_NAME_REGISTER, columns,
            null, null, null, null, null)
        val dataList = mutableListOf<Register>()
        while (cursor.moveToNext()){
            val id    = cursor.getInt (cursor.getColumnIndex(id_rg).toInt())
            val id_ac    = cursor.getInt (cursor.getColumnIndex(id_ac).toInt())
            val desc    = cursor.getString (cursor.getColumnIndex(desc_rg).toInt())
            val start    = cursor.getString (cursor.getColumnIndex(start_rg).toInt())
            val end    = cursor.getString (cursor.getColumnIndex(end_rg).toInt())

            val activity = getActivityByID(id_ac)
            val getRegister = if (activity == Activity()) Register()
                            else Register(id, desc, activity,
                                    LocalDateTime.parse(start), LocalDateTime.parse(end))
            dataList.add(getRegister)
        }
        cursor.close()
        Log.d("tictac_DBHelper", "getRegisterList: $dataList")
        return dataList
    }

    fun insertNowActivity(data: Activity) {
        val values = ContentValues()
        with(values){
            put(id_now, 1)
            put(id_ac, data.id)
            put(start_now, data.timer.start!!.toString())
        }
        val result = insertOnTable(TABLE_NAME_NOW, null, values)
        Log.d("tictac_DBHelper", "insertNow: $result")
    }

    fun updateNow(data: Activity){
        val values = ContentValues()
        with(values){
            put(id_now, 1)
            put(id_ac, data.id)
            put(start_now, data.timer.start!!.toString())
        }
        val result = updateOnTable(TABLE_NAME_NOW, values, "$id_now = ?", arrayOf("1"))
        Log.d("tictac_DBHelper", "updateNow: $result")
    }

    fun deleteNowActivity(id: Int, activity: Activity) {
        Log.d("tictac_DBHelper", "deleteNow, id: $id,\nactivity $activity")
        val whereClause = "$id_now = ?"
        val whereArgs = arrayOf("$id")
        deleteOnTable(TABLE_NAME_NOW, whereClause, whereArgs)
    }

    fun getNowActivity(): Activity{
        val query = "SELECT * FROM $TABLE_NAME_NOW WHERE $id_now = 1"
        Log.d("tictac_DBHelper", "getNow query: $query")
        val cursor = readableDatabase.rawQuery(query, null)

        val data: Activity = if (cursor.moveToFirst()){
            val id_ac    = cursor.getInt (cursor.getColumnIndex(id_ac).toInt())
            val start    = cursor.getString (cursor.getColumnIndex(start_now).toInt())

            val activity = getActivityByID(id_ac)
            if (activity == Activity()) return Activity()
            val now = Activity(activity.id, activity.name, activity.description)
            now.timer.start = LocalDateTime.parse(start)
            now
        } else Activity()
        cursor.close()
        Log.d("tictac_DBHelper", "getNow result: $data")
        return data
    }

    fun insertTask(data: Task) {
        val values = ContentValues()
        with(values){
            put(id_task, data.id)
            put(name_task, data.name)
            put(desc_task, data.desc)
            val arch = if (data.arch) 1
            else 0
            put(arch_task, arch)
        }
        val result = insertOnTable(TABLE_NAME_TASK, null, values)
        insertTskAct(data)
        Log.d("tictac_DBHelper", "insertTask: $data\n$result")
    }

    private fun getTask(cursor: Cursor): Task {
        val data: Task = if (cursor.moveToFirst()){
            val id    = cursor.getInt (cursor.getColumnIndex(id_task).toInt())
            val name    = cursor.getString (cursor.getColumnIndex(name_task).toInt())
            val desc    = cursor.getString (cursor.getColumnIndex(desc_task).toInt())
            val arch    = cursor.getInt (cursor.getColumnIndex(arch_task).toInt())
            val getArch = (arch == 1)
            val listTskAct = getTskActList(id)
            Task(id, name, desc, listTskAct, getArch)
        } else Task()
        cursor.close()
        return data
    }

    fun getTaskList(): MutableList<Task>{
        val columns = arrayOf(id_task, name_task, desc_task, arch_task)
        val cursor = readableDatabase.query(
            TABLE_NAME_TASK, columns,
            null, null, null, null, null)
        val dataList = mutableListOf<Task>()
        while (cursor.moveToNext()){
            val id    = cursor.getInt (cursor.getColumnIndex(id_task).toInt())
            val name    = cursor.getString (cursor.getColumnIndex(name_task).toInt())
            val desc    = cursor.getString (cursor.getColumnIndex(desc_task).toInt())
            val arch    = cursor.getInt (cursor.getColumnIndex(arch_task).toInt())
            val getArch = (arch == 1)

            val listTskAct = getTskActList(id)

            dataList.add(Task(id, name, desc, listTskAct, getArch))
        }
        cursor.close()
        Log.d("tictac_DBHelper", "getTaskList dataList: $dataList")
        return dataList
    }

    fun getTaskByID(id: Int): Task{
        val query = "SELECT * FROM $TABLE_NAME_TASK WHERE $id_task = $id"
        Log.d("tictac_DBHelper", "getTaskByID query: $query")
        val cursor = readableDatabase.rawQuery(query, null)
        val result = getTask(cursor)
        Log.d("tictac_DBHelper", "getTaskByID result: $result")
        return result
    }

    fun insertTskAct(task: Task) {
        for ((position, data) in task.listActivity.withIndex()){
            val values = ContentValues()
            with(values){
                put(position_tsk_act, position)
                put(id_task, task.id)
                put(id_ac, data.id)
            }
            val result = insertOnTable(TABLE_NAME_TSK_ACT, null, values)
            Log.d("tictac_DBHelper", "insertTskAct: ($position, ${data.id}, ${task.id})\n$result")
        }
    }

    fun getTskActList(id: Int): MutableList<Activity>{
        val columns = arrayOf(position_tsk_act, id_task, id_ac)
        val select = "$id_task = ?"
        val arg = arrayOf(id.toString())
        val cursor = readableDatabase.query(
            TABLE_NAME_TSK_ACT, columns,
            select, arg, null, null, null)
        val dataList = mutableListOf<Activity>()
        while (cursor.moveToNext()){
            val position    = cursor.getInt (cursor.getColumnIndex(position_tsk_act).toInt())
            val id_ac    = cursor.getInt (cursor.getColumnIndex(id_ac).toInt())
            Log.d("tictac_DBHelper", "getTskActList position: $position")
            val getActivity = getActivityByID(id_ac)
            dataList.add(getActivity)
        }
        cursor.close()
        Log.d("tictac_DBHelper", "getTskActList dataList: $dataList")
        return dataList
    }

    private fun insertOnTable (table: String, column: String?, values: ContentValues){
        writableDatabase.insert(table, column, values)
        writableDatabase.close()
    }
    private fun updateOnTable (table: String, values: ContentValues, clause: String, things: Array<String>){
        writableDatabase.update(table, values, clause, things)
        writableDatabase.close()
    }

    private fun deleteOnTable (tableName: String, whereClause: String, whereArgs: Array<String>){
        writableDatabase.delete(tableName, whereClause, whereArgs)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_ACTIVITY_TABLE)
        db?.execSQL(CREATE_REGISTER_TABLE)
        db?.execSQL(CREATE_NOW_TABLE)
        db?.execSQL(CREATE_TASK_TABLE)
        db?.execSQL(CREATE_TSK_ACT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ACTIVITY")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_REGISTER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_NOW")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_TASK")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_TSK_ACT")
        onCreate(db)
    }
}