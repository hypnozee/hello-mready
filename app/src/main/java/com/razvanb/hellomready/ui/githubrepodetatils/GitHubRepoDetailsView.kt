package com.razvanb.hellomready.ui.githubrepodetatils

import com.razvanb.hellomready.data.network.pojo.Readme
import com.razvanb.hellomready.ui.base.BaseView
import java.io.File

interface GitHubRepoDetailsView : BaseView {
    fun onLoadRepoDetails(readmeResponse: Readme?)
    fun onLoadReadmeContent(readmeFile: File?)
}