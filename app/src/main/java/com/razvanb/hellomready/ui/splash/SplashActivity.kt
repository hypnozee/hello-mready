package com.razvanb.hellomready.ui.splash

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.razvanb.hellomready.di.module.SplashModule
import com.razvanb.hellomready.manager.ApplicationState
import com.razvanb.hellomready.ui.base.BaseActivity
import com.razvanb.hellomready.ui.base.factory.SplashPresenterFactory
import javax.inject.Inject


class SplashActivity : BaseActivity(), SplashView {

    @Inject
    internal lateinit var presenterFactory: SplashPresenterFactory

    private lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOrDismissProgress(true)
    }

    override fun injectDependencies() {
        ApplicationState
                .get()
                .getAppComponent()
                .inject(SplashModule())
                .inject(this)
    }

    override fun setUpPresenter() {
        presenter = ViewModelProviders.of(this, presenterFactory).get(SplashPresenter::class.java)
        presenter.attachView(this, lifecycle)
        lifecycle.addObserver(presenter)
    }


    private fun showOrDismissProgress(show: Boolean) {
    }

    override fun onGetReposStarted(isSuccessful: Boolean) {
        showOrDismissProgress(true)
    }

    override fun onGetReposFinished(isSuccessful: Boolean) {
        showOrDismissProgress(false)
    }
}
