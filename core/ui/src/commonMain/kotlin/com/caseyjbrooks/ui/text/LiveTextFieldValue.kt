package com.caseyjbrooks.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue

public class LiveTextFieldValue(
    private val textPosition: MutableState<TextFieldValue>,
    private val textString: MutableState<String>,
    private val onTextChange: (String) -> Unit
) {
    private val currentText: String get() = textString.value
    private val currentValue = derivedStateOf {
        textPosition.value.copy(
            text = textString.value
        )
    }

    public fun get(): TextFieldValue {
        return currentValue.value
    }
    public operator fun component1(): TextFieldValue {
        return currentValue.value
    }

    public fun update(newValue: TextFieldValue, sendUpdate: Boolean = true) {
        if (newValue.text != currentText && sendUpdate) {
            onTextChange(newValue.text)
        }

        textString.value = newValue.text
        textPosition.value = newValue
    }

    public operator fun component2(): (TextFieldValue) -> Unit {
        return { tfv: TextFieldValue -> update(tfv) }
    }
}

public fun LiveTextFieldValue.updateText(newValue: String, sendUpdate: Boolean = true) {
    update(get().copy(text = newValue), sendUpdate)
}

@Composable
public fun rememberLiveText(
    initialValue: String,
    onTextChange: (String) -> Unit
): LiveTextFieldValue {
    return rememberLiveText(mutableStateOf(initialValue), onTextChange)
}

@Composable
public fun rememberLiveText(
    inputState: State<String>,
    onTextChange: (String) -> Unit
): LiveTextFieldValue {
    val value by inputState
    val textPosition = remember { mutableStateOf(TextFieldValue()) }
    val textString = remember(value) { mutableStateOf(value) }

    return LiveTextFieldValue(
        textPosition,
        textString,
        onTextChange
    )
}

@Composable
public fun <T : Any> rememberLiveText(
    inputState: State<T>,
    mapStateToText: (T) -> String,
    onTextChange: (String) -> Unit,
): LiveTextFieldValue {
    val value: T by inputState
    val textPosition: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val textString: MutableState<String> = remember(value) { mutableStateOf(mapStateToText(value)) }

    return LiveTextFieldValue(
        textPosition,
        textString,
        onTextChange
    )
}
