package github.leavesc.reactivehttpsamples.store


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    val age: Int? = null,
    val sex: Int? = null
){
    companion object{
        fun mock():User{
            return User("123","林","武淮",24,0)
        }

        fun mockUpdate(): User {
            return User("123", "林", "Vinci",24,0)
        }
    }
}
