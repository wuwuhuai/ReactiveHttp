package github.leavesc.reactivehttpsamples.store

import github.leavesc.reactivehttpsamples.store.AddressManager
import github.leavesc.reactivehttpsamples.store.User

class Webservice {
    fun getUser(uid: String) {

        //请求成功回来
        val response = ""
        val newUser = User(uid,"gaga","gagaga",24,0)
        AddressManager.instance.appDb.userDao().update(newUser)
    }
}