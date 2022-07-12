package org.appcenter.inudorm.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.appcenter.inudorm.repository.MainRepository

class MainViewModel : ViewModel() {

    private val mainRepository = MainRepository()

    // 데이터를 캡슐화하여 외부(뷰)에서 접근할 수 없도록하고
    // 외부 접근 프로퍼티는 immutable 타입으로 제한해 변경할 수 없도록 한다.
    // MutableLiveData의 Constructor에는 초기값을 넘겨준다.
    private val _data = MutableLiveData<String>("initial value")
    val data: LiveData<String>
        get() = _data

    // getData 쓰면 안됨. 이미 있는 메소드같음.
    fun getText() {
        _data.value = mainRepository.fetchData()
    }
}