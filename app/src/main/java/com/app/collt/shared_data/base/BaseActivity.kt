package com.app.collt.shared_data.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.app.collt.databinding.ItemToolbarBinding
import com.app.collt.domain.models.UserHolder
import com.app.collt.extension.gone
import com.app.collt.extension.resToast
import com.app.collt.extension.visible
import com.app.collt.ui.auth.login.LoginActivity
import com.app.collt.ui.auth.welcome.WelcomeActivity
import com.app.collt.utils.PermissionHelper
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import org.koin.android.ext.android.inject


abstract class BaseActivity : AppCompatActivity(), BaseView {
    lateinit var dialog: Dialog
    protected abstract val binding: ViewDataBinding
    open lateinit var mDisposable: CompositeDisposable
    var isInternetConnected: Boolean = true
    private lateinit var mToolbar: Toolbar
    lateinit var permissionHelper: PermissionHelper
    val mUserHolder: UserHolder by inject()
    var isDialogInitialized: Boolean = ::dialog.isInitialized

    abstract fun handleListener()
    abstract fun initObserver()
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDisposable()
        initConnectivity()
        setView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
        mDisposable.dispose()
    }

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }

    private fun setView() = try {
        initView()
        initObserver()
        handleListener()
    } catch (e: Exception) {
        resToast(e.localizedMessage)
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
            finish()
        }

    }

    private fun initConnectivity() {
        val settings = InternetObservingSettings.builder()
            .host("www.google.com")
            .strategy(SocketInternetObservingStrategy())
            .interval(3000)
            .build()

        ReactiveNetwork.observeInternetConnectivity(settings)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isConnectedToHost ->
                isInternetConnected = isConnectedToHost
            }.addTo(mDisposable)
    }

    private fun initDisposable() {
        mDisposable = CompositeDisposable()
    }

    override fun internalServer() {

    }

    override fun onUnknownError(error: String?) {

    }

    override fun onTimeout() {

    }

    override fun onNetworkError() {

    }

    override fun onConnectionError() {

    }

    override fun generalErrorAction() {

    }

    override fun onServerDown() {

    }

    //this method needs to be call only when you get response from the logout API
    override fun autoLogout() {
        mUserHolder.clear()
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
        finishAffinity()
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