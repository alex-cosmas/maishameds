package org.maishameds.ui.views

import android.os.Bundle
import org.maishameds.R
import org.maishameds.databinding.ActivityDashboardBinding


class DashboardActivity : BindingActivity<ActivityDashboardBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    override val layoutResId: Int
        get() = R.layout.activity_dashboard
}