package com.razvanb.hellomready.ui.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import com.razvanb.hellomready.data.DataManager
import com.razvanb.hellomready.manager.CoroutinesManager
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume

/**
 * Base presenter any presenter of the application must extend. It provides initial injections and
 * required methods.
 * @param V the type of the View the presenter is based on
 * @property viewInstance the viewInstance the presenter is based on
 * @constructor Injects the required dependencies
 */
abstract class BasePresenterImpl<V : BaseView>
constructor(coroutinesManager: CoroutinesManager, dataManager: DataManager) : ViewModel(),
    CoroutinesManager by coroutinesManager,
    DataManager by dataManager,
    BasePresenter<V> {

    private var viewInstance: V? = null
    private var viewLifecycle: Lifecycle? = null
    private val isViewResumed = AtomicBoolean(false)
    private val viewContinuations: MutableList<Continuation<V>> = mutableListOf()
    private val stickyContinuations: MutableMap<StickyContinuation<*>, V.(StickyContinuation<*>) -> Unit> =
        mutableMapOf()
    private var mustRestoreStickyContinuations: Boolean = false


    init {
        injectDependencies()
    }

    /**
     * This method may be called when the presenter viewInstance is created
     */
    open fun onViewCreated() {}

    /**
     * This method may be called when the presenter viewInstance is stopped
     */
    open fun onViewStoped() {}

    /**
     * This method may be called when the presenter viewInstance is paused
     */
    open fun onViewPaused() {}

    /**
     *Verify lollipop permissions
     */
    open fun onVerifyPermissions() {}

    /**
     * Injects the required dependencies
     */
    private fun injectDependencies() {
        onInjectDependencies()
    }

    protected open fun onInjectDependencies() {
        // Nothing to do here. This is an event handled by the subclasses.
    }

    @Synchronized
    protected suspend fun view(): V {
        if (isViewResumed.get()) {
            viewInstance?.let { return it }
        }

        // Wait until the viewInstance is ready to be used again
        return suspendCoroutine { continuation -> viewContinuations.add(continuation) }
    }

    @Synchronized
    override fun attachView(view: V, viewLifecycle: Lifecycle) {
        viewInstance = view
        this.viewLifecycle = viewLifecycle

        onViewAttached(view)
    }

    protected open fun onViewAttached(view: V) {
        // Nothing to do here. This is an event handled by the subclasses.
    }

    override fun isAttached(): Boolean {
        return viewInstance != null
    }

    @Synchronized
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onViewStateChanged() {
        isViewResumed.set(viewLifecycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED) ?: false)
    }

    @Synchronized
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onViewReadyForContinuations() {
        val view = viewInstance

        if (view != null) {
            val viewContinuationsIterator = viewContinuations.listIterator()

            while (viewContinuationsIterator.hasNext()) {
                val continuation = viewContinuationsIterator.next()

                // The viewInstance was not ready when the presenter needed it earlier,
                // but now it's ready again so the presenter can continue
                // interacting with it.
                viewContinuationsIterator.remove()
                continuation.resume(view)
            }
        }
    }

    @Synchronized
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onViewReadyForStickyContinuations() {
        val view = viewInstance

        if (mustRestoreStickyContinuations && view != null) {
            mustRestoreStickyContinuations = false

            val stickyContinuationsIterator = stickyContinuations.iterator()

            while (stickyContinuationsIterator.hasNext()) {
                val stickyContinuationBlockMap = stickyContinuationsIterator.next()
                val stickyContinuation = stickyContinuationBlockMap.key
                val stickyContinuationBlock = stickyContinuationBlockMap.value
                view.stickyContinuationBlock(stickyContinuation)
            }
        }
    }

    @Synchronized
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onViewDestroyed() {
        //    viewInstance = null
        viewLifecycle = null
        mustRestoreStickyContinuations = true
    }

    override fun onCleared() {
        cleanup()
        super.onCleared()
    }

    @Synchronized
    override fun addStickyContinuation(
        continuation: StickyContinuation<*>,
        block: V.(StickyContinuation<*>) -> Unit
    ) {
        stickyContinuations[continuation] = block
    }

    @Synchronized
    override fun removeStickyContinuation(continuation: StickyContinuation<*>): Boolean {
        return stickyContinuations.remove(continuation) != null
    }

    /**
     * Executes the given block on the viewInstance. The block is executed again
     * every time the viewInstance instance changes and the new viewInstance is resumed.
     * This, for example, is useful for dialogs that need to be persisted
     * across orientation changes.
     *
     * @param block code that has to be executed on the viewInstance
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun <ReturnType> V.stickySuspension(
        block: V.(StickyContinuation<ReturnType>) -> Unit
    ): ReturnType {
        return suspendCoroutine { continuation ->
            val stickyContinuation: StickyContinuation<ReturnType> =
                StickyContinuation(continuation, this@BasePresenterImpl)
            addStickyContinuation(stickyContinuation, block as V.(StickyContinuation<*>) -> Unit)
            block(stickyContinuation)
        }
    }

    @CallSuper
    @Synchronized
    override fun cleanup() {
        cancelAllCoroutines()
    }
}