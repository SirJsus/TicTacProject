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

    fun insertNow(data: Activity) {
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

    fun deleteNow(id: Int, activity: Activity) {
        Log.d("tictac_DBHelper", "deleteNow, id: $id,\nactivity $activity")
        val whereClause = "$id_now = ?"
        val whereArgs = arrayOf("$id")
        deleteOnTable(TABLE_NAME_NOW, whereClause, whereArgs)
    }

    fun getNow(): Activity{
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
        Log.d("tictac_DBHelper", "insertTask: $data\n$result")
    }

    private fun getTask(cursor: Cursor): Task {
        val data: Task = if (cursor.moveToFirst()){
            val id    = cursor.getInt (cursor.getColumnIndex(id_task).toInt())
            val name    = cursor.getString (cursor.getColumnIndex(name_task).toInt())
            val desc    = cursor.getString (cursor.getColumnIndex(desc_task).toInt())
            val arch    = cursor.getInt (cursor.getColumnIndex(arch_task).toInt())
            val getArch = (arch == 1)
            Task(id, name, desc, getArch)
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
            dataList.add(Task(id, name, desc, getArch))
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
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ACTIVITY")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_REGISTER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_NOW")
        db?.execSQL("DROP TABLE ITABLE_NAM$TABLE_NAME_TASK")
        onCreate(db)
    }
}

/*
class SEAHPDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{

        private const val TABLE_NAME_USER = "usuario"
        private const val user_us = "user_us"
        private const val name_us = "name_us"
        private const val mail_us = "mail_us"
        private const val pass_us = "pass_us"

        private const val CREATE_USER_TABLE =
            "CREATE TABLE $TABLE_NAME_USER (" +
                    //"$user_id INTEGER NOT NULL," +
                    "$user_us TEXT NOT NULL," +
                    "$name_us TEXT NOT NULL," +
                    "$mail_us TEXT NOT NULL," +
                    "$pass_us BLOB NOT NULL," +
                    //"$type_us INTEGER NOT NULL," +
                    "PRIMARY KEY ($user_us)" +
                    ");"

        private const val TABLE_NAME_PJ_US = "participante"
        private const val id_project = "id_project"
        private const val us_user = "us_user"
        private const val type_us = "us_type"

        private const val CREATE_PJ_US_TABLE =
            "CREATE TABLE $TABLE_NAME_PJ_US (" +
                "$id_project INTEGER REFERENCES $TABLE_NAME_PROJECT($project_id)," +
                "$us_user INTEGER REFERENCES $TABLE_NAME_USER($user_us)," +
                "$type_us INTEGER NOT NULL," +
                "PRIMARY KEY ($id_project, $us_user)" +
            ");"

        private const val TABLE_NAME_MATRIX = "matriz"
        private const val matrix_id = "matrix_id"
        private const val name_mat = "name_mat"
        private const val description_mat = "description_mat"
        private const val row_mat = "row_mat"
        private const val column_mat = "column_mat"

        private const val CREATE_MATRIX_TABLE =
            "CREATE TABLE $TABLE_NAME_MATRIX (" +
                "$matrix_id INTEGER NOT NULL," +
                "$name_mat TEXT NOT NULL," +
                "$description_mat TEXT," +
                "$row_mat INTEGER NOT NULL," +
                "$column_mat INTEGER NOT NULL," +
                    "$us_user TEXT REFERENCES $TABLE_NAME_USER($user_us)," +
                "$id_project INTEGER REFERENCES $TABLE_NAME_PROJECT($project_id)," +
                    "$type_us INTEGER REFERENCES $TABLE_NAME_PJ_US($type_us)," +
                    "PRIMARY KEY($matrix_id, $id_project)" +
                ");"

        private const val TABLE_NAME_ELEMENT = "elemento"
        private const val id_matrix = "id_matrix"
        private const val row_ele = "row_ele"
        private const val column_ele = "column_ele"
        private const val name_ele = "name_ele"
        private const val description_ele = "description_ele"
        private const val scale_ele = "scale_ele"

        private const val CREATE_ELEMENT_TABLE =
        "CREATE TABLE $TABLE_NAME_ELEMENT(" +
            "$id_matrix INTEGER REFERENCES $TABLE_NAME_MATRIX($matrix_id)," +
            "$id_project INTEGER REFERENCES $TABLE_NAME_PROJECT($project_id)," +
            "$us_user TEXT REFERENCES $TABLE_NAME_USER($user_us)," +
            "$row_ele INTEGER NOT NULL," +
            "$column_ele INTEGER NOT NULL," +
            "$name_ele TEXT NOT NULL," +
            "$description_ele TEXT," +
            "$scale_ele REAL," +
            "PRIMARY KEY($id_matrix, $id_project, $row_ele, $column_ele, $us_user)" +
            ");"

        private const val TABLE_NAME_CRITERIA = "criterio"
        private const val criteria_id = "criteria_id"
        private const val name_cr = "name_cr"
        private const val description_cr = "description_cr"
        private const val sub_cr = "sub_cr"

        private const val CREATE_CRITERIA_TABLE =
        "CREATE TABLE $TABLE_NAME_CRITERIA (" +
            "$criteria_id INTEGER NOT NULL," +
            "$name_cr TEXT NOT NULL," +
            "$description_cr TEXT," +
            "$sub_cr INTEGER," +
            "$id_project INTEGER NOT NULL," +
                "PRIMARY KEY ($criteria_id, $id_project)," +
                "FOREIGN KEY ($sub_cr) REFERENCES $TABLE_NAME_CRITERIA($criteria_id)," +
                "FOREIGN KEY ($id_project) REFERENCES $TABLE_NAME_PROJECT($project_id)" +
            ");"

        private const val TABLE_NAME_CR_ELE = "cr_ele"
        private const val id_criteria = "id_criteria"
        private const val ele_row = "ele_row"
        private const val ele_column = "ele_column"

        private const val CREATE_CR_ELE_TABLE =
        "CREATE TABLE $TABLE_NAME_CR_ELE (" +
            "$id_criteria INTEGER REFERENCES $TABLE_NAME_CRITERIA($criteria_id)," +
            "$id_matrix INTEGER," +
            "$ele_row INTEGER," +
            "$ele_column INTEGER," +
            "PRIMARY KEY ($id_criteria, $id_matrix, $ele_row, $ele_column)," +
            "FOREIGN KEY ($id_matrix, $ele_row, $ele_column) REFERENCES $TABLE_NAME_ELEMENT($id_matrix, $row_ele, $column_ele)" +
            ");"

        private const val TABLE_NAME_ALTERNATIVE = "alternativa"
        private const val alternative_id = "alternative_id"
        private const val name_alt = "name_alt"
        private const val description_alt = "description_alt"

        private const val CREATE_ALTERNATIVE_TABLE =
        "CREATE TABLE $TABLE_NAME_ALTERNATIVE (" +
            "$alternative_id INTEGER NOT NULL," +
            "$name_alt TEXT NOT NULL," +
            "$description_alt TEXT," +
            "$id_project INTEGER NOT NULL," +
                "PRIMARY KEY ($alternative_id, $id_project)," +
                "FOREIGN KEY ($id_project) REFERENCES $TABLE_NAME_PROJECT($project_id)" +
                ");"

        private const val TABLE_NAME_ALT_ELE = "alt_ele"
        private const val id_alternative = "id_alternative"

        private const val CREATE_ALT_ELE_TABLE =
        "CREATE TABLE $TABLE_NAME_ALT_ELE (" +
            "$id_alternative INTEGER REFERENCES $TABLE_NAME_ALTERNATIVE($alternative_id)," +
            "$id_matrix INTEGER," +
            "$ele_row INTEGER," +
            "$ele_column INTEGER," +
            "FOREIGN KEY ($id_matrix, $ele_row, $ele_column) REFERENCES $TABLE_NAME_ELEMENT($id_matrix, $row_ele, $column_ele)" +
            ");"

        private const val TABLE_NAME_CONSISTENCY = "consistencia"
        private const val cr_id = "cr_id"
        private const val ci_cr = "ci_cr"
        private const val ri_cr = "ri_cr"
        private const val result_cr = "result_cr"

        private const val CREATE_CONSISTECY_TABLE =
        "CREATE TABLE $TABLE_NAME_CONSISTENCY (" +
            "$cr_id INTEGER PRIMARY KEY," +
            "$ci_cr REAL NOT NULL," +
            "$ri_cr REAL NOT NULL," +
            "$result_cr REAL NOT NULL," +
            "$id_matrix INTEGER REFERENCES $TABLE_NAME_MATRIX($matrix_id)" +
            ");"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_PROJECT_TABLE)
        db?.execSQL(CREATE_USER_TABLE)
        db?.execSQL(CREATE_PJ_US_TABLE)
        db?.execSQL(CREATE_MATRIX_TABLE)
        db?.execSQL(CREATE_ELEMENT_TABLE)
        db?.execSQL(CREATE_CRITERIA_TABLE)
        db?.execSQL(CREATE_CR_ELE_TABLE)
        db?.execSQL(CREATE_ALTERNATIVE_TABLE)
        db?.execSQL(CREATE_ALT_ELE_TABLE)
        db?.execSQL(CREATE_CONSISTECY_TABLE)
    }

    private fun isTableEmpty(db: SQLiteDatabase?, table: String): Boolean {
        if (db == null) return true
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $table", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count == 0
    }

    fun insertUser(data: User) {
        val values = ContentValues()
        with(values){
            put(user_us, data.user)
            put(name_us, data.name)
            put(mail_us, data.mail)
            put(pass_us, data.pass)
        }
        insertOnTable(TABLE_NAME_USER, null, values)
    }

    fun getDataUserByUser(user: String): User {
        val userCursor = readableDatabase.query(
            TABLE_NAME_USER, null,
            "$user_us = ?", arrayOf(user), null, null, null
        )
        return getDataUser(userCursor) ?: User()
    }

    fun getDataUserByMail(mail: String): User?{
        val userCursor = readableDatabase.query(
            TABLE_NAME_USER, null,
            "$mail_us = ?", arrayOf(mail),null, null, null)
        return getDataUser(userCursor)
    }

    private fun getDataUser(userCursor: Cursor): User? {
        val data: User? = if (userCursor.moveToFirst()){
            val user    = userCursor.getString (userCursor.getColumnIndex(user_us).toInt())
            val name    = userCursor.getString (userCursor.getColumnIndex(name_us).toInt())
            val mail    = userCursor.getString (userCursor.getColumnIndex(mail_us).toInt())
            val pass    = userCursor.getString (userCursor.getColumnIndex(pass_us).toInt())
            User(user, name, mail, pass)
        } else null
        userCursor.close()
        return data
    }

    fun getListUsers(): MutableList<User>{
        val columns = arrayOf(user_us, name_us, mail_us, pass_us)
        val cursor = readableDatabase.query(
            TABLE_NAME_USER, columns,
            null, null, null, null, null)
        val dataList = mutableListOf<User>()
        while (cursor.moveToNext()){
            val user    = cursor.getString (cursor.getColumnIndex(user_us).toInt())
            val name    = cursor.getString (cursor.getColumnIndex(name_us).toInt())
            val mail    = cursor.getString (cursor.getColumnIndex(mail_us).toInt())
            val pass    = cursor.getString (cursor.getColumnIndex(pass_us).toInt())
            dataList.add(User(/*id,*/ user, name, mail, pass/*, type*/))
        }
        cursor.close()
        return dataList
    }

    fun insertProject(data: Project){
        val values = ContentValues()
        with(values){
            put(project_id, data.idProject)
            put(name_pj, data.nameProject)
            put(description_pj, data.descriptionProject)
        }
        insertOnTable(TABLE_NAME_PROJECT, null, values)
    }

    fun updateProject (data: Project){
        val values = ContentValues()
        with(values){
            put(project_id, data.idProject)
            put(name_pj, data.nameProject)
            put(description_pj, data.descriptionProject)
        }
        updateOnTable(TABLE_NAME_PROJECT, values, "$project_id = ?", arrayOf("${data.idProject}"))
    }

    fun getDataProjectByID(idList: Long): Project {
        val userCursor = readableDatabase.query(
            TABLE_NAME_PROJECT, null,
            "$project_id = ?", arrayOf("$idList"), null, null, null
        )
        return getDataProject(userCursor) ?: Project()
    }

    private fun getDataProject(userCursor: Cursor): Project? {
        val data: Project? = if (userCursor.moveToFirst()){
            val id      = userCursor.getLong   (userCursor.getColumnIndex(project_id).toInt())
            val name    = userCursor.getString (userCursor.getColumnIndex(name_pj).toInt())
            val desc    = userCursor.getString (userCursor.getColumnIndex(description_pj).toInt())
            Project(id, name, desc)
        } else null
        userCursor.close()
        return data
    }

    fun insertParticipant(data: Participant){
        val values = ContentValues()
        with(values){
            put(id_project, data.project.idProject)
            put(us_user, data.user.user)
            put(type_us, data.type)
        }
        insertOnTable(TABLE_NAME_PJ_US, null, values)
    }

    fun getListOfParticipantsByUser(setUser: String): MutableList<Participant>{
        val cursor = readableDatabase.query(
            TABLE_NAME_PJ_US, null,
            "$us_user = ?", arrayOf(setUser), null, null, null)
        val dataList = mutableListOf<Participant>()
        while (cursor.moveToNext()){
            val id_project =   cursor.getLong(cursor.getColumnIndex(id_project).toInt())
            val us_user =      cursor.getString(cursor.getColumnIndex(us_user).toInt())
            val type =      cursor.getInt(cursor.getColumnIndex(type_us).toInt())
            val project = getDataProjectByID(id_project)
            val user = getDataUserByUser(us_user)
            if (user != User() || project != Project()) dataList.add(Participant(user, project, type))

        }
        cursor.close()
        return dataList
    }

    fun getListOfParticipantsByProject(setProject: Long): MutableList<Participant>{
        val cursor = readableDatabase.query(
            TABLE_NAME_PJ_US, null,
            "$id_project = ?", arrayOf("$setProject"), null, null, null)
        val dataList = mutableListOf<Participant>()
        while (cursor.moveToNext()){
            val id_project =   cursor.getLong(cursor.getColumnIndex(id_project).toInt())
            val us_user =      cursor.getString(cursor.getColumnIndex(us_user).toInt())
            val type =      cursor.getInt(cursor.getColumnIndex(type_us).toInt())
            val project = getDataProjectByID(id_project)
            val user = getDataUserByUser(us_user)
            if (user != User() || project != Project()) dataList.add(Participant(user, project, type))
        }
        cursor.close()
        return dataList
    }

    fun getDataProjectUserByUser(user: String, idProject: Long): Project_User?{
        val projectUserCursor = readableDatabase.query(
            TABLE_NAME_PJ_US, null,
            "$id_project = ? AND $us_user = ?", arrayOf("$idProject", user),
            null, null, null)
        return getDataProjectUser(projectUserCursor)
    }

    private fun getDataProjectUser(cursor: Cursor): Project_User?{
        val data: Project_User? = if (cursor.moveToFirst()){
            val project = cursor.getLong(cursor.getColumnIndex(id_project).toInt())
            val user = cursor.getString(cursor.getColumnIndex(us_user).toInt())
            val type = cursor.getInt(cursor.getColumnIndex(type_us).toInt())
            Project_User(project, user, type)
        } else null
        cursor.close()
        return data
    }

    fun updateProjectUser (data: Participant){
        val values = ContentValues()
        with(values){
            put(id_project, data.project.idProject)
            put(us_user, data.user.user)
            put(type_us, data.type)
        }
        updateOnTable(
            TABLE_NAME_PJ_US, values, "$id_project = ? AND $us_user = ?",
            arrayOf("${data.project.idProject}", data.user.user))
    }

    fun deleteProjectUser(data: Participant) {
        val whereClause = "$id_project = ? AND $us_user = ?"
        val whereArgs = arrayOf("${data.project.idProject}", data.user.user)
        deleteOnTable(TABLE_NAME_PJ_US, whereClause, whereArgs)
    }

    fun insertCriteria(data: Criteria){
        val values = ContentValues()
        with(values){
            put(criteria_id, data.idCriteria)
            put(name_cr, data.nameCriteria)
            put(description_cr, data.descriptionCriteria)
            put(sub_cr, data.subCriteria)
            put(id_project, data.idProject)
        }
        insertOnTable(TABLE_NAME_CRITERIA, null, values)
    }

    fun getDataCriteriaByID(idCriteria: Long, idProject: Long): Criteria?{
        val userCursor = readableDatabase.query(
            TABLE_NAME_CRITERIA, null,
            "$criteria_id = ? AND $id_project = ?", arrayOf("$idCriteria", "$idProject"),
            null, null, null)
        return getDataCriteria(userCursor)
    }

    private fun getDataCriteria(userCursor: Cursor): Criteria?{
        val data: Criteria? = if (userCursor.moveToFirst()){
            val id =    userCursor.getLong(userCursor.getColumnIndex(criteria_id).toInt())
            val name =  userCursor.getString(userCursor.getColumnIndex(name_cr).toInt())
            val desc =  userCursor.getString(userCursor.getColumnIndex(description_cr).toInt())
            val sub =   userCursor.getLong(userCursor.getColumnIndex(sub_cr).toInt())
            val idProject = userCursor.getLong(userCursor.getColumnIndex(id_project).toInt())
            Criteria(id, name, desc, sub, idProject)
        } else null
        userCursor.close()
        return data
    }

    fun getListCriteriaByIDProject(idProject: Long): MutableList<Criteria>{
        val cursor = readableDatabase.query(
            TABLE_NAME_CRITERIA, null,
            "$id_project = ?", arrayOf("$idProject"), null, null, null)
        val dataList = mutableListOf<Criteria>()
        while (cursor.moveToNext()){
            val id =    cursor.getLong(cursor.getColumnIndex(criteria_id).toInt())
            val name =  cursor.getString(cursor.getColumnIndex(name_cr).toInt())
            val desc =  cursor.getString(cursor.getColumnIndex(description_cr).toInt())
            val sub =   cursor.getLong(cursor.getColumnIndex(sub_cr).toInt())
            val project = cursor.getLong(cursor.getColumnIndex(id_project).toInt())
            dataList.add(Criteria(id, name, desc, sub, project))
        }
        cursor.close()
        return dataList
    }

    fun insertAlternative(data: Alternative){
        val values = ContentValues()
        with(values){
            put(alternative_id, data.idAlternative)
            put(name_alt, data.nameAlternative)
            put(description_alt, data.descriptionAlternative)
            put(id_project, data.idProject)
        }
        insertOnTable(TABLE_NAME_ALTERNATIVE, null, values)
    }

    fun getListAlternativeByIDProject(idProject: Long): MutableList<Alternative>{
        val cursor = readableDatabase.query(
            TABLE_NAME_ALTERNATIVE, null,
            "$id_project = ?", arrayOf("$idProject"), null, null, null)
        val dataList = mutableListOf<Alternative>()
        while (cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndex(alternative_id).toInt())
            val name = cursor.getString(cursor.getColumnIndex(name_alt).toInt())
            val desc = cursor.getString(cursor.getColumnIndex(description_alt).toInt())
            val project = cursor.getLong(cursor.getColumnIndex(id_project).toInt())
            dataList.add(Alternative(id, name, desc, project))
        }
        cursor.close()
        return dataList
    }

    fun getDataAlternativeByID(idAlternate: Long, idProject: Long): Alternative?{
        val alternativeCursor = readableDatabase.query(
            TABLE_NAME_ALTERNATIVE, null,
            "$alternative_id = ? AND $id_project = ?", arrayOf("$idAlternate", "$idProject"),
            null, null, null)
        return getDataAlternative(alternativeCursor)
    }

    private fun getDataAlternative(cursor: Cursor): Alternative?{
        val data: Alternative? = if (cursor.moveToFirst()){
            val id = cursor.getLong(cursor.getColumnIndex(alternative_id).toInt())
            val name = cursor.getString(cursor.getColumnIndex(name_alt).toInt())
            val desc = cursor.getString(cursor.getColumnIndex(description_alt).toInt())
            val project = cursor.getLong(cursor.getColumnIndex(id_project).toInt())
            Alternative(id, name, desc, project)
        } else null
        cursor.close()
        return data
    }

    fun insertMatrix(data: Matrix){
        val values = ContentValues()
        with(values){
            put(matrix_id, data.idMatrix)
            put(name_mat, data.nameMatrix)
            put(description_mat, data.descriptionMatrix)
            put(row_mat, data.rowMax)
            put(column_mat, data.columnMax)
            put(us_user, data.user.user)
            put(id_project, data.project.idProject)
            put(type_us, data.type)
        }
        insertOnTable(TABLE_NAME_MATRIX, null, values)
    }

    fun updateMatrix (data: Matrix){
        val values = ContentValues()
        with(values){
            put(matrix_id, data.idMatrix)
            put(name_mat, data.nameMatrix)
            put(description_mat, data.descriptionMatrix)
            put(row_mat, data.rowMax)
            put(column_mat, data.columnMax)
            put(us_user, data.user.user)
            put(id_project, data.project.idProject)
            put(type_us, data.type)
        }
        updateOnTable(TABLE_NAME_MATRIX, values, "$matrix_id = ?", arrayOf("${data.idMatrix}"))
    }

    fun getDataMatrixByID(idMatrix: Long, idProject: Long): Matrix {
        val matrixCursor = readableDatabase.query(
            TABLE_NAME_MATRIX, null,
            "$matrix_id = ? AND $id_project = ?", arrayOf("$idMatrix", "$idProject"),
            null, null, null)
        return getDataMatrix(matrixCursor) as Matrix
    }

    fun getListMatrixByIDProject(idProject: Long): MutableList<Matrix>{
        //
        val cursor = readableDatabase.query(
            TABLE_NAME_MATRIX, null,
            "$id_project = ?", arrayOf("$idProject"), null, null, null)
        val dataList = mutableListOf<Matrix>()
        while (cursor.moveToNext()){
            //
            val id = cursor.getLong(cursor.getColumnIndex(matrix_id).toInt())
            val name    = cursor.getString(cursor.getColumnIndex(name_mat).toInt())
            val desc    = cursor.getString(cursor.getColumnIndex(description_mat).toInt())
            val row     = cursor.getInt(cursor.getColumnIndex(row_mat).toInt())
            val column  = cursor.getInt(cursor.getColumnIndex(column_mat).toInt())
            val userID  = cursor.getString(cursor.getColumnIndex(us_user).toInt())
            val projectID = cursor.getLong(cursor.getColumnIndex(id_project).toInt())
            val type    = cursor.getInt(cursor.getColumnIndex(type_us).toInt())

            val setUser = getDataUserByUser(userID)

            val setProject = getDataProjectByID(projectID)

            dataList.add(Matrix(id, name, desc, row, column, setUser!!, setProject!!, type))
        }
        cursor.close()
        return dataList
    }

    private fun getDataMatrix(matrixCursor: Cursor): Matrix? {
        val data: Matrix? = if (matrixCursor.moveToFirst()){
            val id      =    matrixCursor.getLong   (matrixCursor.getColumnIndex(matrix_id).toInt())
            val name    =    matrixCursor.getString (matrixCursor.getColumnIndex(name_mat).toInt())
            val desc    =    matrixCursor.getString (matrixCursor.getColumnIndex(description_mat).toInt())
            val row     =    matrixCursor.getInt    (matrixCursor.getColumnIndex(row_mat).toInt())
            val column  =    matrixCursor.getInt    (matrixCursor.getColumnIndex(column_mat).toInt())
            val idUser =    matrixCursor.getString    (matrixCursor.getColumnIndex(us_user).toInt())
            val idProject = matrixCursor.getLong    (matrixCursor.getColumnIndex(id_project).toInt())
            val type       = matrixCursor.getInt    (matrixCursor.getColumnIndex(type_us).toInt())

            val setUser = if (getDataUserByUser(idUser) == null) User()
            else getDataUserByUser(idUser)

            val setProject = if (getDataProjectByID(idProject) == null) Project()
            else getDataProjectByID(idProject)
            Matrix(id, name, desc, row, column, setUser!!, setProject!!, type)
        } else null
        matrixCursor.close()
        return data
    }

    fun insertElement(data: Element){
        val values = ContentValues()
        with(values){
            put(id_matrix, data.idMatrix)
            put(id_project, data.project.idProject)
            put(us_user, data.user.user)
            put(column_ele, data.xElement)
            put(row_ele, data.yElement)
            put(name_ele, data.nameElement)
            put(description_ele, data.descriptionElement)
            put(scale_ele, data.scaleElement)
        }
        insertOnTable(TABLE_NAME_ELEMENT, null, values)
    }

    fun getListElementOnMatrix(idMatrix: Long, idProject: Long): MutableList<Element>{
        //
        val cursor = readableDatabase.query(
            TABLE_NAME_ELEMENT, null,
            "$id_matrix = ? AND $id_project = ?", arrayOf("$idMatrix", "$idProject"), null, null, null)
        val dataList = mutableListOf<Element>()
        val mat = getDataMatrixByID(idMatrix, idProject)
        while (cursor.moveToNext()){
            //
            val row     = cursor.getInt(cursor.getColumnIndex(row_ele).toInt())
            val column  = cursor.getInt(cursor.getColumnIndex(column_ele).toInt())
            val name    = cursor.getString(cursor.getColumnIndex(name_ele).toInt())
            val desc    = cursor.getString(cursor.getColumnIndex(description_ele).toInt())
            val scale   = cursor.getDouble(cursor.getColumnIndex(scale_ele).toInt())
            val user    = cursor.getString(cursor.getColumnIndex(us_user).toInt())

            val dataUser = getDataUserByUser(user)

            dataList.add(Element(column, row, name, desc, scale,
                mat.idMatrix, mat.nameMatrix, mat.descriptionMatrix, mat.rowMax, mat.columnMax,
                dataUser, mat.project, mat.type))
        }
        cursor.close()
        return dataList
    }

    fun updateElement (data: Element){
        val values = ContentValues()
        with(values){
            put(id_matrix, data.idMatrix)
            put(id_project, data.project.idProject)
            put(us_user, data.user.user)
            put(row_ele, data.yElement)
            put(column_ele, data.xElement)
            put(name_ele, data.nameElement)
            put(description_ele, data.descriptionElement)
            put(scale_ele, data.scaleElement)
        }
        updateOnTable(TABLE_NAME_ELEMENT, values, "$id_matrix = ? AND $id_project = ? AND $row_ele = ? AND $column_ele = ? AND $us_user = ?",
            arrayOf("${data.idMatrix}", "${data.project.idProject}","${data.yElement}", "${data.xElement}", data.user.user)
        )
    }




}
*/