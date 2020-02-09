package com.coders.chatapplication.presentation.ui.searchfriends

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.coders.chatapplication.R
import com.coders.chatapplication.presentation.commons.bindView
import com.coders.chatapplication.presentation.commons.toastIt
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFriendsActivity : AppCompatActivity() {

	private val viewModel by viewModel<SearchFriendsViewModel>()
	private val friendsList by bindView<RecyclerView>(R.id.users_list)
	private val toolbar by bindView<Toolbar>(R.id.toolbar)
	private val swipeRefreshLayout by bindView<SwipeRefreshLayout>(R.id.refresh_layout)

	private val friendsAdapter = SearchFriendsAdapter()
	private lateinit var searchView: SearchView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_search_friends)

		setSupportActionBar(toolbar)

		friendsList.layoutManager = LinearLayoutManager(this)
		friendsList.adapter = friendsAdapter

		viewModel.failure.observe(this, Observer {
			swipeRefreshLayout.isRefreshing = false
			toastIt(it.exception.localizedMessage ?: "Error")
		})

		viewModel.searchedUsers.observe(this, Observer {
			swipeRefreshLayout.isRefreshing = false
			friendsAdapter.update(it)
		})

		swipeRefreshLayout.setOnRefreshListener {
			viewModel.searchUsers("")
		}

		viewModel.searchUsers("")
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.search_users, menu)

		val myActionMenuItem = menu!!.findItem(R.id.action_search)
		searchView = myActionMenuItem.actionView as SearchView
		searchView.setOnQueryTextListener(object :
			SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String): Boolean {
				viewModel.searchUsers(query)
				if (!searchView.isIconified) {
					searchView.isIconified = true
				}
				myActionMenuItem.collapseActionView()
				return false
			}

			override fun onQueryTextChange(s: String): Boolean {
				return false
			}
		})
		return true
	}
}