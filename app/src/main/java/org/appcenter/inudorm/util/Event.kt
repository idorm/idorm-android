package org.appcenter.inudorm.util

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class DialogButton(val text: String, val textColor: Int?, val onClick: () -> Unit)


// Event 클래스를 상속 받는 두 클래스를 만들어준다. sealed를 쓰면 when ... is 를 통해 이벤트에 따라 분기시킬 수 있다.
sealed class Event {
    data class ShowToast(val text: String, val interval: Int) : Event()
    data class ShowDialog(
        val text: String,
        val positiveButton: DialogButton?,
        val negativeButton: DialogButton?
    ) : Event()

    data class MergeBundleWithPaging<T>(val key: String, val data: T) : Event()
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
        positiveButton: DialogButton?,
        negativeButton: DialogButton?
    ) = event(Event.ShowDialog(text, positiveButton, negativeButton))

    fun mergeBundleWithPaging(key: String, data: Any) =
        event(Event.MergeBundleWithPaging(key, data))
}
