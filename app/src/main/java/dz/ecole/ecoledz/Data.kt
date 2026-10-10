package dz.ecole.ecoledz

import androidx.room.*

@Entity(tableName = "classes")
data class SchoolClass(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val year: String = ""
)

@Entity(
    tableName = "pupils",
    foreignKeys = [ForeignKey(entity = SchoolClass::class, parentColumns = ["id"], childColumns = ["classId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("classId")]
)
data class Pupil(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val classId: Long,
    val firstName: String,
    val lastName: String
)

@Entity(tableName = "grades",
    foreignKeys = [ForeignKey(entity = Pupil::class, parentColumns = ["id"], childColumns = ["pupilId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("pupilId")]
)
data class Grade(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pupilId: Long,
    val subject: String,
    val term: String = "T1",
    val value: Double,
    val coefficient: Double = 1.0
)

@Entity(tableName = "attendance",
    foreignKeys = [ForeignKey(entity = Pupil::class, parentColumns = ["id"], childColumns = ["pupilId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("pupilId")]
)
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pupilId: Long,
    val date: String, // yyyy-MM-dd
    val status: String // PRESENT, ABSENT, LATE, EXCUSED
)

@Dao
interface EcoleDao {
    @Insert suspend fun insertClass(c: SchoolClass): Long
    @Query("SELECT * FROM classes ORDER BY name") fun observeClasses(): kotlinx.coroutines.flow.Flow<List<SchoolClass>>
    @Query("SELECT * FROM classes ORDER BY name") suspend fun listClasses(): List<SchoolClass>
    @Delete suspend fun deleteClass(c: SchoolClass)

    @Insert suspend fun insertPupil(p: Pupil): Long
    @Query("SELECT * FROM pupils WHERE classId = :classId ORDER BY lastName, firstName") fun observePupils(classId: Long): kotlinx.coroutines.flow.Flow<List<Pupil>>
    @Query("SELECT * FROM pupils WHERE classId = :classId ORDER BY lastName, firstName") suspend fun listPupils(classId: Long): List<Pupil>
    @Delete suspend fun deletePupil(p: Pupil)

    @Insert suspend fun insertGrade(g: Grade): Long
    @Query("SELECT * FROM grades WHERE pupilId = :pupilId ORDER BY subject") fun observeGrades(pupilId: Long): kotlinx.coroutines.flow.Flow<List<Grade>>
    @Query("SELECT * FROM grades WHERE pupilId = :pupilId") suspend fun listGrades(pupilId: Long): List<Grade>
    @Query("DELETE FROM grades WHERE id = :id") suspend fun deleteGrade(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun markAttendance(a: Attendance)
    @Query("SELECT * FROM attendance WHERE pupilId = :pupilId ORDER BY date DESC") fun observeAttendance(pupilId: Long): kotlinx.coroutines.flow.Flow<List<Attendance>>
    @Query("SELECT * FROM attendance WHERE date = :date AND pupilId IN (SELECT id FROM pupils WHERE classId = :classId)") suspend fun attendanceForClassDate(classId: Long, date: String): List<Attendance>
}

@Database(entities = [SchoolClass::class, Pupil::class, Grade::class, Attendance::class], version = 1, exportSchema = false)
abstract class EcoleDatabase : RoomDatabase() {
    abstract fun dao(): EcoleDao
}
