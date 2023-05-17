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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
    import org.appcenter.inudorm.databinding.FragmentBaseInformationBinding
import org.appcenter.inudorm.model.*
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.adapter.OnboardRVAdapter
import org.appcenter.inudorm.presentation.matching.BaseInfoMutationEvent
import org.appcenter.inudorm.presentation.mypage.matching.MyMatchingProfileActivity
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.presentation.account.OnboardActivity
import org.appcenter.inudorm.util.*


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

    private val prefsRepository by lazy {
        PrefsRepository(requireContext())
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



    private suspend fun initInfo(purpose: BaseInfoPurpose) {
        if (purpose == BaseInfoPurpose.Edit) {
            val onboardInfo = prefsRepository.getMatchingInfo().firstOrNull()
            if(onboardInfo == null){
                OkDialog("저장된 정보가 없습니다.")
                return
            }
            binding.dormGroup.check(onboardInfo.dormCategory.elementId)
            binding.genderGroup.check(onboardInfo.gender.elementId)
            binding.joinPeriodGroup.check(onboardInfo.joinPeriod.elementId)
            binding.snoring.isChecked = onboardInfo.isSnoring
            binding.grinding.isChecked = onboardInfo.isGrinding
            binding.smoking.isChecked = onboardInfo.isSmoking
            binding.eatingInside.isChecked = onboardInfo.isAllowedFood
            binding.wearEarphones.isChecked = !onboardInfo.isWearEarphones
            binding.age1.setText(onboardInfo.age.toString()[0].toString())
            binding.age2.setText(onboardInfo.age.toString()[1].toString())
            (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[0].answer = onboardInfo.wakeupTime
            (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[1].answer = onboardInfo.cleanUpStatus
            (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[2].answer = onboardInfo.showerTime
            (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[3].answer = onboardInfo.openKakaoLink
            (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[4].answer = onboardInfo.mbti
            (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[5].answer = onboardInfo.wishText
        }
    }

    private fun checkInfo(age : Int, wakeupTime : String, cleanUpStatus : String, showerTime : String, openKakaoLink : String, mbti: String) : Boolean{

        if(age < 20) {
            OkDialog("나이는 20살 이상만 입력이 가능합니다").show(requireContext())
            return false}
        if(wakeupTime.isEmpty()) {
            OkDialog("기상 시간을 입력해 주세요" ).show(requireContext())
            return false}
        if(cleanUpStatus.isEmpty()) {
            OkDialog("정리 정돈 정도를 입력해 주세요").show(requireContext())
            return false}
        if(showerTime.isEmpty()) {
            OkDialog("샤워 시간을 입력해 주세요").show(requireContext())
            return false}
        if(openKakaoLink.isEmpty()) {
            OkDialog("룸메와 연락할 오픈 채팅 링크를 입력해 주세요").show(requireContext())
            return false}
        if(mbti.isNotEmpty() && mbti.length != 4) {
            OkDialog("MBTI를 확인해 주세요.").show(requireContext())
            return false}

        return true
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
            val dormCategory = Dorm.fromElementId(binding.dormGroup.checkedChipId)!!
            val gender = Gender.fromElementId(binding.genderGroup.checkedChipId)!!
            val joinPeriod = JoinPeriod.fromElementId(binding.joinPeriodGroup.checkedChipId)!!
            val isSnoring = binding.snoring.isChecked
            val isGrinding = binding.grinding.isChecked
            val isSmoking = binding.smoking.isChecked
            val isAllowedFood = binding.eatingInside.isChecked
            val isWearEarphones = !binding.wearEarphones.isChecked
            val age = (binding.age1.text.toString() + binding.age2.text.toString()).toInt()
            val wakeupTime = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[0].answer
            val cleanUpStatus = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[1].answer
            val showerTime = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[2].answer
            val openKakaoLink = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[3].answer
            val mbti = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[4].answer
            val wishText = (binding.baseInfoRecycler.adapter as OnboardRVAdapter).dataSet[5].answer

            if(checkInfo(age, wakeupTime, cleanUpStatus, showerTime, openKakaoLink, mbti)){
                viewModel.submit(OnboardInfo(
                    dormCategory = dormCategory,
                    gender = gender,
                    joinPeriod = joinPeriod,
                    isSnoring = isSnoring,
                    isGrinding = isGrinding,
                    isSmoking = isSmoking,
                    isAllowedFood = isAllowedFood,
                    isWearEarphones = isWearEarphones,
                    age = age,
                    wakeupTime = wakeupTime,
                    cleanUpStatus = cleanUpStatus,
                    showerTime = showerTime,
                    openKakaoLink = openKakaoLink,
                    mbti = mbti,
                    wishText = wishText,
                ))
            }
        }
        lifecycleScope.launch{
            viewModel.baseInfoMutationEvent.collect{
                initInfo(getPurposeFromBundle())
                when(it) {
                    is BaseInfoMutationEvent.CreateBaseInfo -> {
                        if(it.mutation.state.isSuccess()){
                            OkDialog("매칭 이미지가 저장되었습니다.", onOk = {
                                val intent = Intent(requireContext(), MyMatchingProfileActivity::class.java)
                                startActivity(intent)
                            }).show(requireContext())
                        }
                        if(it.mutation.state.isError()){
                            UIErrorHandler.handle(
                                requireContext(),
                                prefsRepository,
                                (it.mutation.state as State.Error).error
                            ){ e ->
                                when (e.error) {
                                    ErrorCode.FIELD_REQUIRED -> {
                                        OkDialog(
                                            e.error.message,
                                            onOk = {  },
                                            cancelable = false
                                        ).show(requireContext())
                                    }
                                    ErrorCode.DUPLICATE_MATCHINGINFO -> {
                                        OkDialog(
                                            e.error.message,
                                            onOk = {  },
                                            cancelable = false
                                        ).show(requireContext())
                                    }
                                else -> {
                                    OkDialog(getString(R.string.unknownError)).show(requireContext())
                                }
                                }
                            }
                        }

                    }
                    is BaseInfoMutationEvent.EditBaseInfo -> {
                        if(it.mutation.state.isSuccess())
                            OkDialog("매칭 이미지가 수정되었습니다.", onOk = {
                                val intent = Intent(requireContext(), MyMatchingProfileActivity::class.java)
                                startActivity(intent)
                            }).show(requireContext())
                        if(it.mutation.state.isError())
                            UIErrorHandler.handle(
                                requireContext(),
                                prefsRepository,
                                (it.mutation.state as State.Error).error
                            ){ e ->
                                when (e.error) {
                                    ErrorCode.FIELD_REQUIRED -> {
                                        OkDialog(
                                            e.error.message,
                                            onOk = {  },
                                            cancelable = false
                                        ).show(requireContext())
                                    }
                                    ErrorCode.MATCHINGINFO_NOT_FOUND -> {
                                        CustomDialog(
                                            titleText = "매칭 이미지가 아직 없어요. \uD83D\uDE05",
                                            text = "룸메이트 매칭을 위해\n우선 매칭 이미지를 만들어 주세요.",
                                            positiveButton = DialogButton(
                                                "프로필 이미지 만들기",
                                                ButtonType.Filled,
                                                onClick = {
                                                    createImage()
                                                })
                                        ).show(this@BaseInformationFragment.requireContext())
                                    }
                                    else -> {
                                        OkDialog(getString(R.string.unknownError)).show(requireContext())
                                    }
                                }
                            }
                    }
                    else -> {
                        OkDialog("알 수 없는 오류가 발생했습니다.")
                    }
                }
            }
        }
    }

    private fun createImage() {
        val intent = Intent(requireContext(), OnboardActivity::class.java)
        intent.putExtra("purpose", BaseInfoPurpose.Create)
        startActivity(intent)
    }
}
