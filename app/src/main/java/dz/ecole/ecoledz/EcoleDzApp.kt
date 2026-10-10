package dz.ecole.ecoledz

import android.app.Application
import androidx.room.Room

class EcoleDzApp : Application() {
    lateinit var db: EcoleDatabase
        private set
    override fun onCreate() {
        super.onCreate()
        // 100% offline: local Room DB is the single source of truth. No network code anywhere.
        db = Room.databaseBuilder(this, EcoleDatabase::class.java, "ecoledz.db").build()
    }
}
