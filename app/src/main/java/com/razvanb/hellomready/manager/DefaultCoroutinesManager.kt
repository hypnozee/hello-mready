package com.razvanb.hellomready.manager

import android.support.annotation.CallSuper
import com.razvanb.hellomready.utils.CoroutinesUtils.Companion.tryCatch
import com.razvanb.hellomready.utils.CoroutinesUtils.Companion.tryCatchFinally
import com.razvanb.hellomready.utils.CoroutinesUtils.Companion.tryFinally
import kotlinx.coroutines.*

open class DefaultCoroutinesManager : CoroutinesManager {

    private val coroutinesJobs: MutableList<Job> = mutableListOf()

    @Synchronized
    override fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        val job: Job = GlobalScope.launch(Dispatchers.Main) { block() }
        coroutinesJobs.add(job)
        job.invokeOnCompletion { coroutinesJobs.remove(job) }
    }

    @Synchronized
    override fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        launchOnUI { tryCatch(tryBlock, catchBlock, handleCancellationExceptionManually) }
    }

    @Synchronized
    override fun launchOnUITryCatchFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        launchOnUI { tryCatchFinally(tryBlock, catchBlock, finallyBlock, handleCancellationExceptionManually) }
    }

    @Synchronized
    override fun launchOnUITryFinally(
        tryBlock: suspend CoroutineScope.() -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        suppressCancellationException: Boolean
    ) {
        launchOnUI { tryFinally(tryBlock, finallyBlock, suppressCancellationException) }
    }

    @Synchronized
    override fun cancelAllCoroutines() {
        val coroutinesJobsSize = coroutinesJobs.size

        if (coroutinesJobsSize > 0) {
            for (i in coroutinesJobsSize - 1 downTo 0) {
                coroutinesJobs[i].cancel()
            }
        }
    }

    private val deferredObjects: MutableList<Deferred<*>> = mutableListOf()

    @CallSuper
    @Synchronized
    override suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        val deferred: Deferred<T> = GlobalScope.async(Dispatchers.Default) { block() }
        deferredObjects.add(deferred)
        deferred.invokeOnCompletion { deferredObjects.remove(deferred) }
        return deferred
    }

    @CallSuper
    @Synchronized
    override suspend fun <T> asyncAwait(block: suspend CoroutineScope.() -> T): T {
        return async(block).await()
    }

    @CallSuper
    @Synchronized
    override fun cancelAllAsync() {
        val deferredObjectsSize = deferredObjects.size

        if (deferredObjectsSize > 0) {
            for (i in deferredObjectsSize - 1 downTo 0) {
                deferredObjects[i].cancel()
            }
        }
    }

    @CallSuper
    @Synchronized
    override fun cleanup() {
        cancelAllAsync()
    }
}