package org.appcenter.inudorm.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.appcenter.inudorm.model.*
import java.util.*

val testMyInfo = MatchingInfo(
    35345345,
    234234,
    4234324,
    JoinPeriod.Short,
    Gender.Male,
    20,
    snoring = true,
    smoking = false,
    grinding = false,
    wearEarphones = true,
    allowedFood = true,
    wakeUpTime = "9~10시 기상하는 것 같아여",
    cleanUpStatus = "더러운게 눈에 띄면 청소하는 편이에요",
    showerTime = "아침/저녁 랜덤으로 10분 안에서 끝내려 하지만 피곤하면 정줄놓고 20분씩 있기도 해요",
    mbti = "INTJ",
    wishText = "휫바람을 불거나 하는 잡소음을 유발하지 않았으면.. 제발.. 정신 나갈 것 같아요",
    chatLink = "http://google.com"
)
val testMate =  Mate(
    234234,
    "앱센터 병아리",
    "https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png",
    myInfo = testMyInfo
)

class RoomMateRepository {
    suspend fun fetchRoomMates(params: RoomMateFilter): ArrayList<Mate> {
        return flow {
            kotlinx.coroutines.delay(3000)
            emit(
                arrayListOf(
                    Mate(
                        234234,
                        "앱센터 병아리",
                        "https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png",
                        MatchingInfo(
                            35345345,
                            234234,
                            4234324,
                            JoinPeriod.Short,
                            Gender.Male,
                            20,
                            snoring = true,
                            smoking = false,
                            grinding = false,
                            wearEarphones = true,
                            allowedFood = true,
                            wakeUpTime = "9~10시 기상하는 것 같아여",
                            cleanUpStatus = "더러운게 눈에 띄면 청소하는 편이에요",
                            showerTime = "아침/저녁 랜덤으로 10분 안에서 끝내려 하지만 피곤하면 정줄놓고 20분씩 있기도 해요",
                            mbti = "INTJ",
                            wishText = "휫바람을 불거나 하는 잡소음을 유발하지 않았으면.. 제발.. 정신 나갈 것 같아요",
                            chatLink = "http://google.com"
                        )
                    ),
                    testMate,
                    testMate
                )
            )
        }.first()
    }
}