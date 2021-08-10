package github.leavesc.reactivehttpsamples

import android.os.Bundle
import android.view.View
import github.leavesc.reactivehttpsamples.base.BaseActivity
import github.leavesc.reactivehttpsamples.constant.MMKVConst.Companion.KEY_REFUSED_LOCATION_PERMISSION
import github.leavesc.reactivehttpsamples.databinding.ActivityPermissionBinding
import kotlinx.android.synthetic.main.activity_permission.view.*

class PermissionActivity : BaseActivity() {
    private lateinit var binding: ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (MainApplication.app.kv.decodeBool(KEY_REFUSED_LOCATION_PERMISSION, false)) {
            // 显示tips
            binding.llMockTips.visibility = View.VISIBLE
            binding.llMockTips.let { layout ->
                layout.close.setOnClickListener {
                    layout.visibility = View.GONE
                }
                layout.turn_on.setOnClickListener {
                    requestLocation()
                }
            }
        } else {
            requestLocation()
        }

    }

    override fun requestLocationSuccess() {
        // TODO: 2021/8/10 do something

    }
}