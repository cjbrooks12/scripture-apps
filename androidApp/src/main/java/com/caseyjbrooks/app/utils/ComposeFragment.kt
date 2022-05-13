package com.caseyjbrooks.app.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import com.caseyjbrooks.app.utils.theme.BrandTheme

abstract class ComposeFragment : Fragment() {

    protected open val plugins: List<DefaultLifecycleObserver> by lazy {
        listOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                BrandTheme {
                    ScreenContent()
                }
            }
        }
    }

    override fun onStart() {
        plugins.forEach { lifecycle.addObserver(it) }
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        plugins.forEach { lifecycle.removeObserver(it) }
    }

    @Composable
    abstract fun ScreenContent()
}
