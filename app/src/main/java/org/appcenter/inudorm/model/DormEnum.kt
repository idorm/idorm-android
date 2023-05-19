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
        private val dormToElementId = values().associateBy { it.text }

        /**
         * layout id를 이용해 Dorm을 가져옵니다.
         */
        infix fun fromElementId(value: Int) = elementIdToDorm[value]
        infix fun fromDorm(value: String) = dormToElementId[value]
    }
}

enum class Gender(val text: String, val elementId: Int) {
    MALE("남성", R.id.man),
    FEMALE("여성", R.id.woman);

    companion object {
        private val elementIdToGender = Gender.values().associateBy { it.elementId }
        infix fun fromElementId(value: Int) = elementIdToGender[value]
    }

}

enum class JoinPeriod(val text: String, val elementId: Int) {
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

enum class BooleanStrings(
    val trueText: String,
    val falseText: String
) {
    Existence("있음", "없음"),
    Act("함", "안함"),
    Possibility("가능", "불가능"),
    Wearability("착용 가능", "착용 불가능")
}

enum class Taste(
    val elementId: Int,
    val key: String,
    val _name: String,
    val isFit: BooleanStrings
) {
    AllowFood(R.id.eatingInside, "isAllowedFood", "실내 음식 섭취", BooleanStrings.Act),
    Grinding(R.id.grinding, "isGrinding", "이갈이", BooleanStrings.Existence),
    Smoking(R.id.smoking, "isSmoking", "흡연", BooleanStrings.Act),
    Snoring(R.id.snoring, "isSnoring", "코골이", BooleanStrings.Existence),
    WearEarphones(R.id.wearEarphones, "isWearEarphones", "이어폰", BooleanStrings.Wearability);

    companion object {
        private val elementIdToTaste = values().associateBy { it.elementId }
        private val keyToTaste = values().associateBy { it.key }

        val tastes = values()

        /**
         * layout id를 이용해 Taste 를 가져옵니다.
         */
        infix fun fromElementId(value: Int) = elementIdToTaste[value]
        infix fun fromKey(key: String) = keyToTaste[key]
    }
}
