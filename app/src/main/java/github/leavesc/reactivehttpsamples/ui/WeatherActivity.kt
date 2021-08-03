package github.leavesc.reactivehttpsamples.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import github.leavesc.reactivehttpsamples.R
import github.leavesc.reactivehttpsamples.adapter.WeatherAdapter
import github.leavesc.reactivehttpsamples.base.BaseActivity
import github.leavesc.reactivehttpsamples.core.bean.CastsBean
import github.leavesc.reactivehttpsamples.core.bean.ForecastsBean
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * @Author: leavesC
 * @Date: 2020/10/26 15:30
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
class WeatherActivity : BaseActivity() {

    private val castsBeanList = mutableListOf<CastsBean>()

    private val weatherAdapter = WeatherAdapter(castsBeanList)

    private val adCode by lazy {
        intent.getStringExtra("adCode") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        rv_dailyForecast.layoutManager = LinearLayoutManager(this)
        rv_dailyForecast.adapter = weatherAdapter

        iv_place.setOnClickListener {
            startActivity<MapActivity>()
            finish()
        }
    }

    private fun showWeather(forecastsBean: ForecastsBean) {
        tv_city.text = forecastsBean.city
        castsBeanList.clear()
        castsBeanList.addAll(forecastsBean.casts)
        weatherAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
    }

}