package com.razvanb.hellomready.utils

import android.content.Context
import android.graphics.Typeface
import timber.log.Timber

object TypefaceUtil {

    fun overrideFont(context: Context, defaultFontNameToOverride: String, customFontFileNameInAssets: String) {
        try {
            val customFontTypeface = Typeface.createFromAsset(context.assets, customFontFileNameInAssets)
            val defaultFontTypefaceField = Typeface::class.java.getDeclaredField(defaultFontNameToOverride)
            defaultFontTypefaceField.isAccessible = true
            defaultFontTypefaceField.set(null, customFontTypeface)
        } catch (e: Exception) {
            Timber.e("Can not set custom font $customFontFileNameInAssets instead of $defaultFontNameToOverride")
        }
    }
}