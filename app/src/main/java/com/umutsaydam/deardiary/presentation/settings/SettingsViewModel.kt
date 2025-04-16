package com.umutsaydam.deardiary.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.entity.FontFamilySealed
import com.umutsaydam.deardiary.domain.entity.FontSizeSealed
import com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase.GetFontFamilyUseCase
import com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase.GetFontSizeUseCase
import com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase.SetFontFamilyUseCase
import com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase.SetFontSizeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getFontFamilyUseCase: GetFontFamilyUseCase,
    private val setFontFamilyUseCase: SetFontFamilyUseCase,
    private val getFontSizeUseCase: GetFontSizeUseCase,
    private val setFontSizeUseCase: SetFontSizeUseCase
) : ViewModel() {

    private val _defaultFontFamily = MutableStateFlow<FontFamilySealed>(FontFamilySealed.Serif)
    val defaultFontFamily: StateFlow<FontFamilySealed> = _defaultFontFamily

    private val _defaultFontSize = MutableStateFlow<FontSizeSealed>(FontSizeSealed.BodyMedium)
    val defaultFontSize: StateFlow<FontSizeSealed> = _defaultFontSize

    init {
        getFontFamilyAndSize()
    }

    fun setFontFamilyAndSize(
        defaultFontFamily: FontFamilySealed,
        defaultTextStyle: FontSizeSealed
    ) {
        viewModelScope.launch {
            setFontFamilyUseCase(defaultFontFamily.id)
            setFontSizeUseCase(defaultTextStyle.id)
        }
    }

    private fun getFontFamilyAndSize() {
        viewModelScope.launch {
            getFontFamilyUseCase().collectLatest { family ->
                Log.i("R/T", "family in viewmodel $family")
                _defaultFontFamily.value = FontFamilySealed.fromLabel(family)
            }

            getFontSizeUseCase().collectLatest { size ->
                _defaultFontSize.value = FontSizeSealed.fromLabel(size)
            }
        }
    }

    fun setSizeAndFamily(style: FontSizeSealed, font: FontFamilySealed) {
        _defaultFontSize.value = style
        _defaultFontFamily.value = font
    }
}