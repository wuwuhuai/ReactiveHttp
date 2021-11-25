package github.leavesc.reactivehttpsamples

import android.os.Bundle
import android.view.View
import github.leavesc.reactivehttpsamples.base.BaseActivity
import github.leavesc.reactivehttpsamples.store.AddressManager

class StoreActivity : BaseActivity(), View.OnClickListener {
    private val tvData:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        tvData = findViewById<TextView>(R.id.tv_data)

        setupListener()

    }

    private fun setupListener() {
        observeUserTable()

    }
    private fun observeUserTable() {
        AddressManager.instance.appDb.userDao().getAll().observe(this, {
            tvData.text  = it.toString()
        })
    }

    override fun onClick(view: View) {
//        when (view.id) {
//            R.id.tv_add -> {
//                AddressManager.instance.appDb.userDao().insertAll(User.mock())
//            }
//            R.id.tv_delete -> {
//                AddressManager.instance.appDb.userDao().delete(User.mock())
//            }
//            R.id.tv_update -> {
//                AddressManager.instance.appDb.userDao().update(User.mockUpdate())
//            }
//            R.id.tv_read -> {
//                val users = AddressManager.instance.appDb.userDao().getAll()
//                binding.tvData.text  = users.toString()
//            }
//        }
    }
}