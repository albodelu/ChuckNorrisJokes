package es.voghdev.chucknorrisjokes

import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.app.ResLocator
import org.mockito.ArgumentMatchers.any

fun ResLocator.hasString(id: Int, str: String) {
    whenever(getString(id)).thenReturn(str)
}

fun <T> anyCategory(): T = any()

fun <T> anyJoke(): T = any()

