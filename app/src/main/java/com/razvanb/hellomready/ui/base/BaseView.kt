package com.razvanb.hellomready.ui.base

import android.content.Context

/**
 * Base viewInstance any viewInstance must implement.
 */

interface BaseView {
    /**
     * Returns the Context in which the application is running.
     * @return the Context in which the application is running
     */
    fun getContext(): Context
}