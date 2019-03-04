package com.razvanb.hellomready.ui.base

import kotlin.coroutines.Continuation

class StickyContinuation<ReturnType>
constructor(
    private val continuation: Continuation<ReturnType>,
    private val presenter: BasePresenter<*>
) : Continuation<ReturnType> by continuation {

    private var _resumeValue: ReturnType? = null
    val resumeValue: ReturnType?
        get() = _resumeValue

    override fun resumeWith(result: Result<ReturnType>) {
        _resumeValue = result.getOrNull()
        presenter.removeStickyContinuation(this)
        continuation.resumeWith(result)
    }
}

