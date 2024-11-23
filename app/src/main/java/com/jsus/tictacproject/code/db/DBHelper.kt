package com.jsus.tictacproject.code.db

class DBHelper {
}

/*
class SEAHPDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "SEAHP Database"
        private const val DATABASE_VERSION = 2

        private const val TABLE_NAME_PROJECT = "proyecto"
        private const val project_id = "project_id"
        private const val name_pj = "name_pj"
        private const val description_pj = "description_pj"

        private const val CREATE_PROJECT_TABLE =
            "CREATE TABLE $TABLE_NAME_PROJECT (" +
                    "$project_id INTEGER NOT NULL," +
                    "$name_pj TEXT NOT NULL," +
                    "$description_pj TEXT," +
                    "PRIMARY KEY ($project_id)" +
                    ");"

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

    private fun insertOnTable (table: String, columnNull: String?, values: ContentValues){
        writableDatabase.insert(table, columnNull, values)
        writableDatabase.close()
    }
    private fun updateOnTable (table: String, values: ContentValues, clause: String, things: Array<String>){
        writableDatabase.update(table, values, clause, things)
        writableDatabase.close()
    }

    private fun deleteOnTable (tableName: String, whereClause: String, whereArgs: Array<String>){
        writableDatabase.delete(tableName, whereClause, whereArgs)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_PROJECT")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_USER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_PJ_US")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_MATRIX")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ELEMENT")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_CRITERIA")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_CR_ELE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ALTERNATIVE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ALT_ELE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_CONSISTENCY")
        onCreate(db)
    }
}
*/