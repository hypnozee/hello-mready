package com.razvanb.hellomready.ui.githubrepos

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.razvanb.hellomready.R
import com.razvanb.hellomready.data.DataManager
import com.razvanb.hellomready.data.network.pojo.ItemsItem
import com.razvanb.hellomready.data.network.pojo.RepositoriesResponse
import com.razvanb.hellomready.di.module.GitHubReposModule
import com.razvanb.hellomready.manager.ApplicationState
import com.razvanb.hellomready.ui.base.BaseActivity
import com.razvanb.hellomready.ui.base.factory.GitHubReposPresenterFactory
import com.razvanb.hellomready.ui.callbacks.OnItemClickListener
import com.razvanb.hellomready.ui.githubrepodetatils.GitHubRepoDetailsActivity
import com.razvanb.hellomready.utils.Constants.REPO_GRID_LAYOUT
import com.razvanb.hellomready.utils.Constants.REPO_LIST_LAYOUT
import kotlinx.android.synthetic.main.activity_repo_list.*

import javax.inject.Inject

class GitHubReposActivity : BaseActivity(),
        GitHubReposView,
        OnItemClickListener {

    @Inject
    internal lateinit var presenterFactory: GitHubReposPresenterFactory
    private lateinit var presenter: GitHubReposPresenter

    @Inject
    internal lateinit var mDataManager: DataManager
    private var mRepoItems: List<ItemsItem?>? = null
    private var mListAdapter: ReposListAdapter? = null
    private lateinit var mItemDecor: DividerItemDecoration
    private var mLayout: Int = 0

    override fun injectDependencies() {
        ApplicationState
            .get()
            .getAppComponent()
            .inject(GitHubReposModule())
            .inject(this)
    }

    override fun setUpPresenter() {
        presenter = ViewModelProviders.of(this, presenterFactory).get(GitHubReposPresenter::class.java)
        presenter.attachView(this, lifecycle)
        lifecycle.addObserver(presenter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.title_activity_repos_list)


        presenter.getReposFromApi()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.repo_list_menu, menu)
        return true
    }

    override fun onGetReposFinished(isSuccessful: Boolean) {
    }

    override fun onGetReposStarted(isSuccessful: Boolean) {
    }

    override fun onLoadLayout(layout: Int) {
    }

    override fun onLayoutChanged(layout: Int) {
        mLayout = layout
        setupListLayout()
    }

    override fun onLoadRepos(repositoriesResponse: RepositoriesResponse?) {
        mRepoItems = repositoriesResponse?.items
        setupList()
    }

    private fun setupList() {
        mListAdapter = ReposListAdapter(this.mRepoItems, this)

        mItemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        mItemDecor.setDrawable(resources.getDrawable(R.drawable.list_item_divider))

        setupListLayout()
    }

    private fun setupListLayout() {
        mListAdapter?.setLayout(mLayout)
        if (mLayout == REPO_LIST_LAYOUT) {
            repo_list_rw.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            repo_list_rw.addItemDecoration(mItemDecor)
        } else {
            repo_list_rw.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            repo_list_rw.removeItemDecoration(mItemDecor)
        }
        repo_list_rw.adapter = mListAdapter
    }

    override fun onItemClick(repo: ItemsItem) {
        val intent = Intent(this, GitHubRepoDetailsActivity::class.java)
        intent.putExtra(GitHubRepoDetailsActivity.REPOS_EXTRAS, repo)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_list -> {

                if (mLayout == REPO_LIST_LAYOUT)
                    presenter.changeLayout(REPO_GRID_LAYOUT)
                else
                    presenter.changeLayout(REPO_LIST_LAYOUT)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
