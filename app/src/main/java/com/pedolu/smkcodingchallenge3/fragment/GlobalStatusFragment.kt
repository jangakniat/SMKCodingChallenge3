package com.pedolu.smkcodingchallenge3.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pedolu.smkcodingchallenge3.R
import com.pedolu.smkcodingchallenge3.adapter.GlobalViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class GlobalStatusFragment : Fragment() {
    private val menuTeks = arrayOf("Confirmed", "Recovered", "Death")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_global_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter =
            GlobalViewPagerAdapter(this.requireActivity())
        viewPager.adapter = adapter
        TabLayoutMediator(tabsLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = menuTeks[position]
            }).attach()

        tabsLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.text) {
                    "Confirmed" -> {
                        tabsLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorYellow
                            )
                        )
                    }
                    "Recovered" -> {
                        tabsLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorGreen
                            )
                        )
                    }
                    "Death" -> {
                        tabsLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorRed
                            )
                        )
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }


}
