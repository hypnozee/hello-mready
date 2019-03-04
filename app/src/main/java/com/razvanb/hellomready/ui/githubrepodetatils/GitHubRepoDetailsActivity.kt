package com.razvanb.hellomready.ui.githubrepodetatils

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.razvanb.hellomready.R
import com.razvanb.hellomready.data.DataManager
import com.razvanb.hellomready.data.network.pojo.ItemsItem
import com.razvanb.hellomready.data.network.pojo.Readme
import com.razvanb.hellomready.di.module.GitHubRepoDetailsModule
import com.razvanb.hellomready.manager.ApplicationState
import com.razvanb.hellomready.ui.base.BaseActivity
import com.razvanb.hellomready.ui.base.factory.GitHubRepoDetailsPresenterFactory
import kotlinx.android.synthetic.main.activity_repo_details.*
import java.io.File

import javax.inject.Inject

class GitHubRepoDetailsActivity : BaseActivity(),
    GitHubRepoDetailsView {

    @Inject
    internal lateinit var presenterFactory: GitHubRepoDetailsPresenterFactory
    private lateinit var presenter: GitHubRepoDetailsPresenter

    @Inject
    internal lateinit var mDataManager: DataManager

    private var repo: ItemsItem? = null
    private var readMeInfo: Readme? = null
    private var readMeFile: File? = null

    companion object {
        const val REPOS_EXTRAS = "repos_extras"
    }

    override fun injectDependencies() {
        ApplicationState
            .get()
            .getAppComponent()
            .inject(GitHubRepoDetailsModule())
            .inject(this)
    }

    override fun setUpPresenter() {
        presenter = ViewModelProviders.of(this, presenterFactory).get(GitHubRepoDetailsPresenter::class.java)
        presenter.attachView(this, lifecycle)
        lifecycle.addObserver(presenter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_details)
        setupDataFromBundle()

        collapsing_toolbar.title = repo?.name
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.title_activity_repos_list)

        setupRepo()

        presenter.getReadmeFromApi(repo?.owner?.login!!, repo?.name!!)
    }

    private fun setupDataFromBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            repo = bundle.getParcelable(REPOS_EXTRAS)
        }
    }

    private fun setupRepo() {
        repo?.owner?.avatarUrl?.let { loadRepoImage(it, backdrop) }

    }

    private fun loadRepoImage(url: String, imageView: ImageView) {

        Glide.with(this)
            .load(url)
            .apply(
                RequestOptions()
                    .fallback(R.drawable.ic_repo_placeholder_error)
                    .error(R.drawable.ic_repo_placeholder_error)
            )
            .transition(GenericTransitionOptions<Any>())
            .transition(withCrossFade(200))
            .thumbnail(0.1f)
            .into(imageView)
    }

    override fun onLoadRepoDetails(readmeResponse: Readme?) {
        readMeInfo = readmeResponse
        presenter.downLoadReadmeFromUrl(readmeResponse?.downloadUrl, getContext(), readmeResponse?.name)

    }

    override fun onLoadReadmeContent(readmeFile: File?) {
        readMeFile = readmeFile
        readme_content.text = readMeFile?.readText()
    }


}
