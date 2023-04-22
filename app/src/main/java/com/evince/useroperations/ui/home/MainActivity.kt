package com.evince.useroperations.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.evince.useroperations.R
import com.evince.useroperations.adapters.UsersAdapter
import com.evince.useroperations.database.AppDatabase
import com.evince.useroperations.databinding.ActivityMainBinding
import com.evince.useroperations.enums.Status.ERROR
import com.evince.useroperations.enums.Status.LOADING
import com.evince.useroperations.enums.Status.SUCCESS
import com.evince.useroperations.ui.user_details.UserDetailsActivity
import com.evince.useroperations.utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var userDetailLauncher: ActivityResultLauncher<Intent>

    private val mUserAdapter by lazy {
        UsersAdapter {
            userDetailLauncher.launch(UserDetailsActivity.getIntent(this, it))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupObservers()
        setupListeners()
        loadData()
    }


    private fun initView() {
        binding.recUsers.adapter = mUserAdapter
    }

    private fun setupObservers() {
        userDetailLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val list = appDatabase.usersDao().getAllUsers()
                        withContext(Dispatchers.Main) {
                            mUserAdapter.submitList(list)
                        }
                    }
                }
            }

        mViewModel.userListResponse.observe(this) {
            when (it.status) {
                SUCCESS -> {
                    mUserAdapter.submitList(it.data)
                }

                ERROR -> {
                    showShortToast(it.message ?: getString(R.string.default_error_message))
                }

                LOADING -> {
                }
            }
        }
    }

    private fun setupListeners() {
    }

    private fun loadData() {
        mViewModel.getUsersList("1")
    }
}