package github.leavesc.reactivehttpsamples.store


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import github.leavesc.reactivehttpsamples.store.Webservice
import github.leavesc.reactivehttpsamples.store.UserRepository


class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val userDao = AddressManager.instance.appDb.userDao()
        val webservice = Webservice()
        repository = UserRepository(webservice,userDao)
    }

    fun getUser(uid: String) {
        val user = repository.getUser(uid)
    }
}