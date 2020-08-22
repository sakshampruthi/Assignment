package com.example.assignment

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.adapters.ListAdapter
import com.example.assignment.datamodels.Item
import com.example.assignment.network.ApiCall
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    val arrayList = arrayListOf<Item>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        apicall()

        swipeRefreshLayout.setOnRefreshListener {
            apicall()
        }
    }
    private fun apicall(){
        swipeRefreshLayout.isRefreshing = true
        network.visibility = View.GONE
        arrayList.clear()
        if (isNetworkConnected()) {
            val call = ApiCall().item
            call.enqueue(object : Callback<List<Item>> {
                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@MainActivity,
                            "Code: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val itembody = response.body()
                    if (itembody != null) {
                        for (i in itembody) {
                            val item: Item = if (i.subCategory != null)
                                Item(i.id, i.name, i.parent_id, i.subCategory)
                            else
                                Item(i.id, i.name, i.parent_id)
                            arrayList.add(item)
                        }
                        recyclerView.setHasFixedSize(true)
                        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                        val listAdapter = ListAdapter(this@MainActivity, arrayList)
                        recyclerView.adapter = listAdapter
                        swipeRefreshLayout.isRefreshing = false
                    }
                }

                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    swipeRefreshLayout.isRefreshing = false
                }

            })
        }
        else{
            swipeRefreshLayout.isRefreshing = false
            network.visibility = View.VISIBLE
        }
    }
    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        } else {
            try {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            } catch (e: Exception) {
                Log.i("update_status", "" + e.message)
            }
        }
        return false
    }
}