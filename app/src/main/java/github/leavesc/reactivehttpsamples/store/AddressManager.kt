package github.leavesc.reactivehttpsamples.store

import androidx.room.Room
import github.leavesc.reactivehttpsamples.MainApplication


class AddressManager {
    val appDb by lazy {
        Room.databaseBuilder(MainApplication.app, AppDatabase::class.java, "addresses")
            .allowMainThreadQueries()
            .addMigrations(*(RoomMigration.instance.migrations.toTypedArray()))
            .build()
    }


    companion object {
        val instance: AddressManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AddressManager()
        }
    }
}

