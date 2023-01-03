package org.appcenter.inudorm.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R



// Event 클래스를 상속 받는 두 클래스를 만들어준다. sealed를 쓰면 when ... is 를 통해 이벤트에 따라 분기시킬 수 있다.
sealed class Event {
    data class ShowToast(val text: String, val interval: Int) : Event()
    data class ShowDialog(
        val text: String,
        val positiveButton: DialogButton? = null,
        val negativeButton: DialogButton? = null,
    ) : Event()

    data class MergeBundleWithPaging(val bundle: Bundle) : Event()
}

open class ViewModelWithEvent : ViewModel() {
    private val _eventFlow: MutableSharedFlow<Event> = MutableSharedFlow()
    val eventFlow: SharedFlow<Event> = _eventFlow.asSharedFlow()

    private fun event(event: Event) {
        Log.d("Event", "event emitted")
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun showToast(text: String) = event(Event.ShowToast(text, 1000))
    fun showDialog(
        text: String,
        positiveButton: DialogButton? = null,
        negativeButton: DialogButton? = null,
    ) = event(Event.ShowDialog(text, positiveButton, negativeButton))

    fun mergeBundleWithPaging(bundle: Bundle) =
        event(Event.MergeBundleWithPaging(bundle))
}

fun eventHandler(context: Context, it: Event) {
    Log.d("Fragment", it.toString())
    when (it) {
        is Event.ShowToast -> {
            Toast.makeText(context, it.text, it.interval).show()
        }
        is Event.ShowDialog -> {
            CustomDialog(it.text, it.positiveButton, it.negativeButton).show(context)
        }
        is Event.MergeBundleWithPaging -> {
            (context as OnPromptDoneListener).onPromptDone(it.bundle)
        }
    }
}