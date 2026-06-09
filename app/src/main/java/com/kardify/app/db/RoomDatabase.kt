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

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "front_side") val frontSide: String?,
    @ColumnInfo(name = "back_side") val backSide: String?
)


@Dao
interface QuestionDao {
    @Insert
    suspend fun insert(question: Question)

    @Update
    suspend fun update(question: Question)

    @Delete
    suspend fun delete(question: Question)


    @Query("SELECT id FROM questions ORDER BY id ")
    suspend fun getAllIds(): List<Int>

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): Flow<List<Question>>
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