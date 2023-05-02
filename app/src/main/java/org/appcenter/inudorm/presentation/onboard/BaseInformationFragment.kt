package org.appcenter.inudorm.presentation.onboard

import android.content.Intent
import android.os.Bundle
import android.text.Editable
    import android.text.TextWatcher
import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
    import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
    import org.appcenter.inudorm.databinding.FragmentBaseInformationBinding
import org.appcenter.inudorm.model.*
import org.appcenter.inudorm.presentation.adapter.OnboardRVAdapter
import org.appcenter.inudorm.presentation.matching.BaseInfoMutationEvent
import org.appcenter.inudorm.presentation.mypage.matching.MyMatchingProfileActivity
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.OkDialog


enum class BaseInfoPurpose{
    Create,
    Edit
}

class BaseInformationFragment : Fragment() {

    companion object {
        fun newInstance() = BaseInformationFragment()
    }

    private lateinit var viewModel: BaseInformationViewModel
    private lateinit var binding: FragmentBaseInformationBinding
    lateinit var adapter: OnboardRVAdapter

    private fun getPurposeFromBundle(): BaseInfoPurpose { // Fragment가 전달받은 Bundle을 풀어해쳐 이메일 입력을 받는 목적을 가져와요..
        val bundle = this.arguments
        return if (bundle != null) {
            bundle.getSerializable("purpose") as BaseInfoPurpose
        } else {
            BaseInfoPurpose.Create
        }
    }



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
        val question1 = OnboardQuestion("기상시간을 알려주세요.","(필수)",View.GONE,)
        val question2 = OnboardQuestion("정리정돈은 얼마나 하시나요?","(필수)",View.GONE)
        val question3 = OnboardQuestion("샤워는 주로 언제/몇 분 동안 하시나요?","(필수)",View.GONE)
        val question4 = OnboardQuestion("룸메와 연락할 개인 오픈채팅 링크를 알려주세요.","(필수)",View.GONE, 300)
        val question5 = OnboardQuestion("MBTI를 알려주세요.",null,View.GONE, 4)
        val question6 = OnboardQuestion("미래의 룸메에게 하고 싶은 말은?",null,View.VISIBLE,100, 0)

        val list = arrayListOf<OnboardQuestion>(question1, question2, question3, question4, question5, question6)

        adapter = OnboardRVAdapter(list, requireContext())

        binding.baseInfoRecycler.layoutManager = LinearLayoutManager(this.context)
        with(binding){
            baseInfoRecycler.adapter = adapter
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            viewModelStore,
            BaseInfoViewModelFactory(getPurposeFromBundle())
        )[BaseInformationViewModel::class.java]

            binding.age1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) { // 한자리가 채워지면 age2로 focus 옮기기
                    binding.age2.requestFocus()
                }
            }
        })
        binding.doneButton.setOnClickListener {
            viewModel.submit(OnboardInfo(
                dormCategory = Dorm.fromElementId(binding.dormGroup.checkedChipId)!!,
                gender = Gender.fromElementId(binding.genderGroup.checkedChipId)!!,
                joinPeriod = JoinPeriod.fromElementId(binding.joinPeriodGroup.checkedChipId)!!,
                isSnoring = binding.snoring.isChecked,
                isGrinding = binding.grinding.isChecked,
                isSmoking = binding.smoking.isChecked,
                isAllowedFood = binding.eatingInside.isChecked,
                isWearEarphones = binding.wearEarphones.isChecked,
                age = (binding.age1.text.toString() + binding.age2.text.toString()).toInt(),
                wakeupTime = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[0].answer,
                cleanUpStatus = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[1].answer,
                showerTime = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[2].answer,
                openKakaoLink = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[3].answer,
                mbti = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[4].answer,
                wishText = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[5].answer,
            ))
        }
        lifecycleScope.launch{
            viewModel.baseInfoMutationEvent.collect{
                when(it) {
                    is BaseInfoMutationEvent.CreateBaseInfo -> {
                        if(it.mutation.state.isSuccess()){
                            OkDialog("매칭 이미지가 저장되었습니다.", onOk = {
                                val intent = Intent(requireContext(), MyMatchingProfileActivity::class.java)
                                startActivity(intent)
                            }).show(requireContext())
                        }
                        if(it.mutation.state.isError())
                            OkDialog("매칭 이미지 저장에 실패했습니다.", onOk = {
                                val intent = Intent(requireContext(), MyMatchingProfileActivity::class.java)
                                startActivity(intent)
                            }).show(requireContext())
                    }
                    is BaseInfoMutationEvent.EditBaseInfo -> {
                        if(it.mutation.state.isSuccess())
                            OkDialog("매칭 이미지가 수정되었습니다.", onOk = {
                                val intent = Intent(requireContext(), MyMatchingProfileActivity::class.java)
                                startActivity(intent)
                            }).show(requireContext())
                        if(it.mutation.state.isError())
                            OkDialog("매칭 이미지 수정에 실패했습니다", onOk = {
                                val intent = Intent(requireContext(), MyMatchingProfileActivity::class.java)
                                startActivity(intent)
                            }).show(requireContext())
                    }
                    else -> {
                        OkDialog("알 수 없는 오류가 발생했습니다.")
                    }
                }
            }
        }
    }
}
