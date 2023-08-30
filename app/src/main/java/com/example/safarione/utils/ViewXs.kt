package com.example.safarione.utils

import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.dismissKeyboard() {
    view?.apply {
        val target = activity?.currentFocus ?: this
        ContextCompat.getSystemService(context, InputMethodManager::class.java)
            ?.hideSoftInputFromWindow(
                target.windowToken, 0
            )
    }
}