package github.leavesc.reactivehttpsamples.store


import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class RoomMigration {
    val migrations = ArrayList<Migration>()
    init {
        addMigration()
    }
    private fun addMigration() {
        migrations.add(object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                with(database) { execSQL("alter table user add column age integer") }
            }
        })

        migrations.add(object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                with(database) { execSQL("alter table user add column sex integer") }
            }
        })
    }

    companion object {
        val instance: RoomMigration by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RoomMigration()
        }
    }
}