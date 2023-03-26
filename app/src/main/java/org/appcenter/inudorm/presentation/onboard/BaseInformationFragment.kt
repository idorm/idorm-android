package org.appcenter.inudorm.presentation.onboard

import android.os.Bundle
import android.text.Editable
    import android.text.TextWatcher
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.databinding.DataBindingUtil
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.recyclerview.widget.LinearLayoutManager
    import org.appcenter.inudorm.R
    import org.appcenter.inudorm.databinding.FragmentBaseInformationBinding
    import org.appcenter.inudorm.model.OnboardQuestion
    import org.appcenter.inudorm.presentation.adapter.OnboardRVAdapter

    class BaseInformationFragment : Fragment() {

    companion object {
        fun newInstance() = BaseInformationFragment()
    }

    private val viewModel: BaseInformationViewModel by viewModels()
    private lateinit var binding: FragmentBaseInformationBinding
    lateinit var adapter: OnboardRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_base_information, container, false)

        initBind()

        return binding.root
    }

    private fun initBind(){
        val question1 = OnboardQuestion("기상시간을 알려주세요.","(필수)",View.GONE, )
        val question2 = OnboardQuestion("정리정돈은 얼마나 하시나요?","(필수)",View.GONE)
        val question3 = OnboardQuestion("샤워는 주로 언제/몇 분 동안 하시나요?","(필수)",View.GONE)
        val question4 = OnboardQuestion("룸메와 연락할 개인 오픈채팅 링크를 알려주세요.","(필수)",View.GONE)
        val question5 = OnboardQuestion("MBTI를 알려주세요.",null,View.GONE)
        val question6 = OnboardQuestion("미래의 룸메에게 하고 싶은 말은?",null,View.VISIBLE,"100", "0")

        val list = arrayListOf<OnboardQuestion>(question1, question2, question3, question4, question5, question6)

        adapter = OnboardRVAdapter(list)

        binding.baseInfoRecycler.layoutManager = LinearLayoutManager(this.context)
        with(binding){
            baseInfoRecycler.adapter = adapter
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.age1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) { // 한자리가 채워지면 age2로 focus 옮기기
                    binding.age2.requestFocus()
                }
            }
        })
    }
}