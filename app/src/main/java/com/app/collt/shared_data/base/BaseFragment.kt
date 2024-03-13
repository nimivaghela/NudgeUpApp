package com.app.collt.shared_data.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.app.collt.R
import com.app.collt.databinding.ItemToolbarBinding
import com.app.collt.domain.models.UserHolder
import com.app.collt.extension.gone
import com.app.collt.extension.snackBarWithAction
import com.app.collt.extension.visible
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import org.koin.android.ext.android.inject
import timber.log.Timber


abstract class BaseFragment<VB : ViewDataBinding> : Fragment(), BaseView {
    val mUserHolder: UserHolder by inject()

    /**
     * to get Fragment resource file
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * to set fragment option menu
     */
    protected open fun hasOptionMenu(): Boolean = false

    /**
     * to display error message
     */
    abstract fun displayMessage(message: String)

    /**
     * to initialize variables
     */
    abstract fun initView()

    /**
     * to call API or bind adapter
     */
    abstract fun postInit()

    /**
     * initialize live data observer
     */
    abstract fun initObserver()

    /**
     * to define all listener
     */
    abstract fun handleListener()

    lateinit var mDisposable: CompositeDisposable
    lateinit var binding: VB
    var isInternetConnected: Boolean = true
//    val mUserHolder: UserHolder by inject()


    override fun onDestroyView() {
        super.onDestroyView()
        mDisposable.clear()
        mDisposable.dispose()
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDisposable = CompositeDisposable()
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        setHasOptionsMenu(hasOptionMenu())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
//        observeApiCallbacks()
        initObserver()
        handleListener()
//        initDisposable()
        postInit()
    }

    override fun onResume() {
        super.onResume()
        mDisposable = CompositeDisposable()
        initConnectivity()
        initView()
        initObserver()
        handleListener()
        postInit()
    }

//    private lateinit var mToolbar: Toolbar

    /*  @SuppressLint("RestrictedApi")
      protected fun setToolbar(
          @NotNull toolbar: Toolbar, @NotNull title: String,
          isBackEnabled: Boolean = false, isLeft1Enabled: Boolean = false,
          isLeft2Enabled: Boolean = false, backgroundColor: Int = R.color.transparent
      ) {
          this.mToolbar = toolbar
          (activity as AppCompatActivity).setSupportActionBar(toolbar)
          toolbar.setBackgroundColor(
              ContextCompat.getColor(
                  (activity as AppCompatActivity).applicationContext,
                  backgroundColor
              )
          )
          (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

          *//*
        * Add your toolbar here
        * *//*
    }*/

    /*fun setNavigationIcon(navigationIconResId: Int) {
        if (::mToolbar.isInitialized) {
            mToolbar.setNavigationIcon(navigationIconResId)
        }
    }*/


    private fun initConnectivity() {
        val settings = InternetObservingSettings.builder()
            .host("www.google.com")
            .strategy(SocketInternetObservingStrategy())
            .interval(3000)
            .build()

        ReactiveNetwork
            .observeInternetConnectivity(settings)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isConnectedToHost ->
                isInternetConnected = isConnectedToHost
            }.addTo(mDisposable)
    }


    override fun onUnknownError(error: String?) {
        error?.let {
            Timber.d("Base Activity $error")
            displayMessage(error)
        }
        generalErrorAction()
    }

    override fun internalServer() {
        Timber.d("Base Activity API Internal server")
        displayErrorMessage(getString(R.string.text_error_internal_server))
        generalErrorAction()
    }

    override fun onTimeout() {
        Timber.d("Base Activity API Timeout")
        displayErrorMessage(getString(R.string.text_error_timeout))
        generalErrorAction()
    }

    override fun onNetworkError() {
        Timber.d("Base Activity network error")
        displayErrorMessage(getString(R.string.text_error_network))
        generalErrorAction()
    }

    override fun onConnectionError() {
        Timber.d("Base Activity internet issue")
        displayErrorMessage(getString(R.string.text_error_connection))
        generalErrorAction()
    }

    override fun generalErrorAction() {
        Timber.d("This method will use in child class for performing common task for all error")
    }

    override fun onServerDown() {
        Timber.d("Base Activity Server Connection issue")
        displayErrorMessage(getString(R.string.text_server_connection))
        generalErrorAction()
    }

    //this method needs to be call only when you get response from the logout API
    override fun autoLogout() {
        (activity as BaseActivity).autoLogout()
    }

    fun showLoadingIndicator(progressBar: View, isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun displayErrorMessage(message: String) {
        binding.root.snackBarWithAction(requireActivity(), message)
    }

    fun setToolBar(
        @NotNull toolbar: ItemToolbarBinding,
        isBackEnable: Boolean = false,
        isTitleEnable: Boolean,
        isTitleTroubleEnable: Boolean,
        titleText: String = "",
        titleTrouble: String = "",
        isSkipVisible: Boolean,
        titleSkipText: String,
        isFriendsVisible: Boolean,
        isLogoVisible: Boolean,
        isAddGroup: Boolean
    ) {
        if (isBackEnable) toolbar.ivBack.visible() else toolbar.ivBack.gone()
        if (isTitleEnable) toolbar.tvTitle.visible() else toolbar.tvTitle.gone()
        if (isTitleTroubleEnable) toolbar.tvTroubleSignUp.visible() else toolbar.tvTroubleSignUp.gone()
        if (isSkipVisible) toolbar.tvSkip.visible() else toolbar.tvSkip.gone()
        if (isFriendsVisible) toolbar.tvFriend.visible() else toolbar.tvFriend.gone()
        if (isLogoVisible) toolbar.ivLogo.visible() else toolbar.ivLogo.gone()
        if (isAddGroup) toolbar.ivAddGroup.visible() else toolbar.ivAddGroup.gone()

        toolbar.tvTitle.text = titleText
        toolbar.tvTroubleSignUp.text = titleTrouble
        toolbar.tvSkip.text = titleSkipText

        toolbar.ivBack.setOnClickListener {
            requireActivity().finish()
        }

    }

    override fun forbidden(error: String?) {

    }

    override fun onOTPExpire(error: String?) {

    }

    override fun onSubscribeRequired(error: String?) {

    }

    override fun onVerificationError(): Unit? {
        return null
    }

    override fun loginFirst() {

    }
}