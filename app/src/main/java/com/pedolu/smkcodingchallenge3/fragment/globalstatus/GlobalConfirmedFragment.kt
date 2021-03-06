package com.pedolu.smkcodingchallenge3.fragment.globalstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedolu.smkcodingchallenge3.R
import com.pedolu.smkcodingchallenge3.adapter.GlobalStatusAdapter
import com.pedolu.smkcodingchallenge3.adapter.StatusRoomAdapter
import com.pedolu.smkcodingchallenge3.data.httpClient
import com.pedolu.smkcodingchallenge3.data.mathdroidApiRequest
import com.pedolu.smkcodingchallenge3.data.model.global.GlobalStatusSummaryItem
import com.pedolu.smkcodingchallenge3.data.model.room.StatusSummaryModel
import com.pedolu.smkcodingchallenge3.data.service.CovidMathdroidService
import com.pedolu.smkcodingchallenge3.util.dismissLoading
import com.pedolu.smkcodingchallenge3.util.showLoading
import com.pedolu.smkcodingchallenge3.util.tampilToast
import com.pedolu.smkcodingchallenge3.viewmodel.StatusSummaryViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_global_status_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GlobalConfirmedFragment : Fragment() {
    private val statusSummaryViewModel by viewModels<StatusSummaryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_global_status_summary, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retriveRoomStatusSummary()
        swipeRefreshLayout.setOnRefreshListener {
            callGlobalConfirmedSummary()
        }
    }

    private fun retriveRoomStatusSummary() {
        statusSummaryViewModel.init(requireContext(), "confirmed")
        statusSummaryViewModel.statusSummary.observe(
            viewLifecycleOwner,
            Observer { statusSummary ->
                if (statusSummary != null) {
                    listGlobalStatus.layoutManager = LinearLayoutManager(context)
                    listGlobalStatus.adapter =
                        StatusRoomAdapter(
                            requireContext(),
                            statusSummary,
                            "confirmed"
                        ) {
                            val country = it
                            tampilToast(requireContext(), country.combined_key)
                        }
                } else {
                    callGlobalConfirmedSummary()
                }
            })
    }


    private fun callGlobalConfirmedSummary() {
        setInvisible()
        showLoading(requireContext(), swipeRefreshLayout)
        val httpClient = httpClient()
        val apiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
        val call = apiRequest.getGlobalConfirmed()
        call.enqueue(object : Callback<List<GlobalStatusSummaryItem>> {
            override fun onFailure(call: Call<List<GlobalStatusSummaryItem>>, t: Throwable) {
                tampilToast(requireContext(), "Gagalds")
                setVisible()
                dismissLoading(swipeRefreshLayout)
            }

            override fun onResponse(
                call: Call<List<GlobalStatusSummaryItem>>, response:
                Response<List<GlobalStatusSummaryItem>>
            ) {
                dismissLoading(swipeRefreshLayout)
                setVisible()
                when {
                    response.isSuccessful -> {
                        when {
                            response.body()?.size != 0 -> {
                                statusSummaryViewModel.init(requireContext(), "confirmed")
                                val statusList: MutableList<StatusSummaryModel> = ArrayList()
                                for (status in response.body()!!) {
                                    statusList.add(
                                        StatusSummaryModel(
                                            status.confirmed,
                                            "confirmed",
                                            status.combinedKey + "confirmed"
                                        )
                                    )
                                }
                                statusSummaryViewModel.addAllData(statusList)
                                showGlobalConfirmedSummary(response.body()!!)
                            }
                            else -> {
                                tampilToast(requireContext(), "Berhasil")
                            }
                        }
                    }
                    else -> {
                        tampilToast(requireContext(), "Coba Lagi")
                    }
                }
            }
        })
    }

    private fun showGlobalConfirmedSummary(globalStatus: List<GlobalStatusSummaryItem>) {
        listGlobalStatus.layoutManager = LinearLayoutManager(context)
        listGlobalStatus.adapter =
            GlobalStatusAdapter(
                requireContext(),
                globalStatus,
                "confirmed"
            ) {
                val country = it
                tampilToast(requireContext(), country.combinedKey)
            }
    }

    private fun setVisible() {
        listGlobalStatus.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        listGlobalStatus.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}
