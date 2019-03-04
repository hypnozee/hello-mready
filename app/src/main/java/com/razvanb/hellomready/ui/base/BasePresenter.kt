package com.razvanb.hellomready.ui.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver

interface BasePresenter<V> : LifecycleObserver {
    fun attachView(view: V, viewLifecycle: Lifecycle)
    fun addStickyContinuation(continuation: StickyContinuation<*>, block: V.(StickyContinuation<*>) -> Unit)
    fun removeStickyContinuation(continuation: StickyContinuation<*>): Boolean
    fun isAttached(): Boolean
}