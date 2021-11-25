package github.leavesc.reactivehttpsamples.store


class UserRepository(
    private val webservice: Webservice,
    private val userDao: UserDao
) {
    fun getUser(userId: String): List<User> {
        refreshUser(userId)

        return if (hasUser(userId)) {
            val userIds = arrayOf(userId)
            userDao.loadAllByIds(userIds = userIds)
        } else {
            ArrayList()
        }

    }

    private fun refreshUser(userId: String) {
        webservice.getUser(userId)
    }

    private fun hasUser(uid: String): Boolean {
        val userIds = arrayOf(uid)
        val users = userDao.loadAllByIds(userIds = userIds)
        return !users.isEmpty()
    }

}