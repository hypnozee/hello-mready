package com.razvanb.hellomready.ui.githubrepodetatils

import android.content.Context
import com.razvanb.hellomready.data.DataManager
import com.razvanb.hellomready.data.db.manager.RoomRepository
import com.razvanb.hellomready.data.network.pojo.Readme
import com.razvanb.hellomready.di.module.GitHubRepoDetailsModule
import com.razvanb.hellomready.manager.ApplicationState
import com.razvanb.hellomready.manager.CoroutinesManager
import com.razvanb.hellomready.ui.base.BasePresenterImpl
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

class GitHubRepoDetailsPresenter(coroutinesManager: CoroutinesManager, dataManager: DataManager) :
    BasePresenterImpl<GitHubRepoDetailsView>(coroutinesManager, dataManager) {

    @Inject
    internal lateinit var roomRepository: RoomRepository

    override fun onInjectDependencies() {
        ApplicationState
            .get()
            .getAppComponent()
            .inject(GitHubRepoDetailsModule())
            .inject(this)
    }

    fun getReadmeFromApi(owner: String, repo: String) {
        launchOnUI {
            val request = getReadme(owner = owner, repo = repo)
            val response = request.await()
            if (response.isSuccessful) {
                val reposResponse: Readme? = response.body()
                onSuccessRepos(reposResponse)
                Timber.d("Request successful")
            }
        }
    }

    private suspend fun onSuccessRepos(readMeResponse: Readme?) {
        view().onLoadRepoDetails(readMeResponse)
    }

    fun downLoadReadmeFromUrl(urlString: String?, context: Context, name: String?) {
        Thread {
            try {
                val url = URL(urlString)

                val urlConn = url.openConnection()
                urlConn.readTimeout = 30000
                urlConn.connectTimeout = 30000

                val inputStream = urlConn.getInputStream()
                val buffInStream = BufferedInputStream(inputStream, 1024 * 5)

                val file = File(context.getDir("readme", Context.MODE_PRIVATE), name)
                val outStream = FileOutputStream(file)
                val buff = ByteArray(10 * 1024)

                var endOfStream = buffInStream.read(buff)
                while (endOfStream != -1) {
                    outStream.write(buff, 0, endOfStream)
                    endOfStream = buffInStream.read(buff)
                }

                launchOnUI {
                    onSuccessReadmeDownLoad(file)
                }
                outStream.flush()
                outStream.close()
                buffInStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.d("Download finished with exception: ${e.message} <-")
            }
        }.start()

        Timber.d("-> Started downloading readme file from .. $urlString")

        Timber.d("Download finished $name <-")
    }

    private suspend fun onSuccessReadmeDownLoad(readMeFile: File?) {
        view().onLoadReadmeContent(readMeFile)
    }

}