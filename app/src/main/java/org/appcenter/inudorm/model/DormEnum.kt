package org.appcenter.inudorm.model

import org.appcenter.inudorm.R

/**
 * First: 1긱, ...
 */
enum class Dorm(val text: String, val elementId: Int) {
    DORM1("1", R.id.firstDorm),
    DORM2("2", R.id.secondDorm),
    DORM3("3", R.id.thirdDorm);

    companion object {
        private val elementIdToDorm = values().associateBy { it.elementId }

        /**
         * layout id를 이용해 Dorm을 가져옵니다.
         */
        infix fun fromElementId(value: Int) = elementIdToDorm[value]
    }
}

enum class Gender(val text:String) {
    MALE("남성"),
    FEMALE("여성")
}

enum class JoinPeriod(val text:String, val elementId: Int) {
    WEEK16("16", R.id.sixteenWeeks),
    WEEK24("24", R.id.twentyFourWeeks);

    companion object {
        private val elementIdToJoinPeriod = values().associateBy { it.elementId }

        /**
         * layout id를 이용해 JoinPeriod 를 가져옵니다.
         */
        infix fun fromElementId(value: Int) = elementIdToJoinPeriod[value]
    }
}


enum class Taste(
    val elementId: Int,
    val key: String,
) {
    AllowFood(R.id.eatingInside, "isAllowedFood"),
    Grinding(R.id.grinding, "isGrinding"),
    Smoking(R.id.smoking, "isSmoking"),
    Snoring(R.id.snoring, "isSnoring"),
    WearEarphones(R.id.wearEarphones, "isWearEarphones");

    companion object {
        private val elementIdToTaste = values().associateBy { it.elementId }
        val tastes = values()

        /**
         * layout id를 이용해 Taste 를 가져옵니다.
         */
        infix fun fromElementId(value: Int) = elementIdToTaste[value]
    }
}
