package com.kardify.app.db

import android.content.Context
import androidx.room3.ColumnInfo
import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Delete
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "front_side") val frontSide: String?,
    @ColumnInfo(name = "back_side") val backSide: String?
)

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question)

    @Update
    suspend fun update(question: Question)

    @Delete
    suspend fun delete(question: Question)

    @Query("SELECT id FROM questions ORDER BY id")
    suspend fun getAllIds(): List<Int>

    @Query("SELECT id FROM questions ORDER BY id DESC LIMIT 1")
    suspend fun getLastId(): Int

    @Query("SELECT * FROM questions ORDER BY id")
    fun getAllQuestions(): Flow<List<Question>>


    @Query("DELETE FROM questions")
    suspend fun deleteAll()


    @Query("""
    DELETE FROM questions
    WHERE id = :id
""")
    suspend fun deletetPairById(id: Int)

    @Query("""
    SELECT front_side FROM questions
    WHERE id = :id
""")
    suspend fun getFrontSideById(id: Int): String?

    @Query("""
    SELECT back_side FROM questions
    WHERE id = :id
""")
    suspend fun getBacktSideById(id: Int): String?
}

@Database(entities = [Question::class], version = 1)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: QuestionDatabase? = null

        fun getDatabase(context: Context): QuestionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuestionDatabase::class.java,
                    "questions_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class DatabaseManager(private val context: Context) {
    private val instances = mutableMapOf<String, QuestionDatabase>()

    fun getDatabase(dbConfig: DatabaseConfig): QuestionDatabase {
        return instances.getOrPut(dbConfig.name) {
            Room.databaseBuilder(
                context.applicationContext,
                QuestionDatabase::class.java,
                "${dbConfig.name}.db"
            ).build()
        }
    }

    fun closeDatabase(name: String) {
        instances[name]?.close()
        instances.remove(name)
    }

    fun closeAll() {
        instances.values.forEach { it.close() }
        instances.clear()
    }
}

data class DatabaseConfig(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val displayName: String,
    val description: String = ""
)

class DatabaseRepository(
    private val manager: DatabaseManager,
    private val context: Context
) {
    private val _configs = MutableStateFlow<List<DatabaseConfig>>(emptyList())
    val configs: StateFlow<List<DatabaseConfig>> = _configs.asStateFlow()

    private val _activeConfig = MutableStateFlow<DatabaseConfig?>(null)
    val activeConfig: StateFlow<DatabaseConfig?> = _activeConfig.asStateFlow()

    fun addDatabase(config: DatabaseConfig) {
        _configs.update { it + config }
    }

    fun removeDatabase(config: DatabaseConfig) {
        manager.closeDatabase(config.name)
        _configs.update { it - config }
        if (_activeConfig.value?.id == config.id) _activeConfig.value = null
    }

    fun switchTo(config: DatabaseConfig) {
        _activeConfig.value = config
    }

    fun activeDao(): QuestionDao? {
        val config = _activeConfig.value ?: return null
        return manager.getDatabase(config).questionDao()
    }
}