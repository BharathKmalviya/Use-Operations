package com.evince.useroperations.ui.user_details

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Patterns
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.BuildCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.evince.useroperations.R
import com.evince.useroperations.database.AppDatabase
import com.evince.useroperations.databinding.ActivityUserDetailsBinding
import com.evince.useroperations.enums.Status.ERROR
import com.evince.useroperations.enums.Status.LOADING
import com.evince.useroperations.enums.Status.SUCCESS
import com.evince.useroperations.models.UserModel
import com.evince.useroperations.ui.home.MainViewModel
import com.evince.useroperations.utils.isConnectedToInternet
import com.evince.useroperations.utils.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding

    private val mViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var appDatabase: AppDatabase

    var isChangesDone = false

    var downloadID: Long = -1

    companion object {
        const val ARG_MODEL = "ARG_MODEL"

        @JvmStatic
        fun start(context: Context?, userModel: UserModel) {
            val starter = Intent(context, UserDetailsActivity::class.java)
            starter.putExtra(ARG_MODEL, userModel)
            context?.startActivity(starter)
        }

        @JvmStatic
        fun getIntent(context: Context?, userModel: UserModel): Intent {
            val starter = Intent(context, UserDetailsActivity::class.java)
            starter.putExtra(ARG_MODEL, userModel)
            return starter
        }
    }

    private val mUserModel by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARG_MODEL, UserModel::class.java)
        } else {
            intent.getParcelableExtra(ARG_MODEL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupObservers()
        setupListeners()
        loadData()
    }

    private fun initView() {
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private fun setupObservers() {
        mViewModel.userUpdateResponse.observe(this) {
            when (it.status) {
                SUCCESS -> {
                    val model = UserModel(
                        id = mUserModel?.id ?: 0,
                        avatar = mUserModel?.avatar ?: "",
                        firstName = binding.etFirstName.text.toString(),
                        lastName = binding.etLastName.text.toString(),
                        email = binding.etEmail.text.toString()
                    )
                    lifecycleScope.launch(Dispatchers.IO) {
                        appDatabase.usersDao().updateUser(model)
                    }
                    showShortToast(getString(R.string.user_updated_successfully))
                    isChangesDone = true
                }

                ERROR -> {
                    showShortToast(it.message ?: getString(R.string.default_error_message))
                }

                LOADING -> {

                }
            }
        }
        mViewModel.userDeleteResponse.observe(this) {
            when (it.status) {
                SUCCESS -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val model = appDatabase.usersDao().getUser(mUserModel?.id ?: 0)
                        model?.let { it1 ->
                            appDatabase.usersDao().deleteUser(it1)
                        }
                    }
                    showShortToast(getString(R.string.user_deleted_successfully))
                    isChangesDone = true
                    onBackPressed()
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

        binding.btnDownload.setOnClickListener {
            onDownload()
        }

        binding.btnUpdate.setOnClickListener {
            if (binding.etFirstName.text.toString().isEmpty()) {
                binding.etFirstName.error = "Please enter first name"
                return@setOnClickListener
            }

            if (binding.etLastName.text.toString().isEmpty()) {
                binding.etLastName.error = "Please enter last name"
                return@setOnClickListener
            }

            if (binding.etEmail.text.toString().isEmpty()) {
                binding.etEmail.error = "Please enter email"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
                binding.etEmail.error = "Please enter valid email"
                return@setOnClickListener
            }

            mViewModel.updateUser(
                id = (mUserModel?.id ?: 0).toString(),
                firstName = binding.etFirstName.text.toString(),
                lastName = binding.etLastName.text.toString(),
                email = binding.etEmail.text.toString()
            )
        }

        binding.btnDelete.setOnClickListener {
            mViewModel.deleteUser(id = (mUserModel?.id ?: 0).toString())
        }
    }

    private fun loadData() {
        binding.model = mUserModel
    }

    private fun onDownload() {
        val name = mUserModel?.avatar?.split("/")?.last() ?: "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadFile(name, mUserModel?.avatar ?: "")
        } else {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    downloadFile(name, mUserModel?.avatar ?: "")
                }

                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    showShortToast("Please allow storage permission to download the file")
                }

                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private fun downloadFile(fileName: String, url: String) {
        // fileName -> fileName with extension
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription("User profile image")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val name =
                    mUserModel?.avatar?.split("/")?.last() ?: "${System.currentTimeMillis()}.jpg"
                downloadFile(name, mUserModel?.avatar ?: "")
            } else {
                showShortToast("Please allow storage permission to download the file")
            }
        }

    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                showShortToast("Download completed")
            }
        }
    }

    override fun onBackPressed() {
        if (isChangesDone) {
            setResult(Activity.RESULT_OK)
        }
        finish()
    }
}