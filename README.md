<br>
<div align="center">
    <img src="https://github.com/idorm/idorm-android/assets/103296628/9d4d7c9c-f7a8-48f5-8499-2d48220a76b9" width="300"></img><br>
    <h1>idorm</h1>
    <h3>인천대학교 기숙사생들을 위한 모든 것</h3>
    <h5>
        크롤링 기반 기숙사 공식 일정 알림&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        카드 스와이핑으로 룸메이트 매칭&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        룸메이트와 공유 캘린더&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        기숙사별 커뮤니티
    </h5>
    <h5>
        <a href="https://play.google.com/store/apps/details?id=org.appcenter.inudorm&hl=ko">📲 playstore 다운링크</a>
        &nbsp<a>|</a>&nbsp
        <a href="https://apps.apple.com/kr/app/아이돔/id1660001335">📲 appstore 다운링크 </a>
    </h5>
</div><br>

---

## idorm 기능
|로그인 이미지| <img src="https://github.com/idorm/idorm-android/assets/103296628/4bde6362-1d75-46c0-b65c-d2eb962a3a07" width="300"></img> |
| ------------ | ------------- |
| 자동 로그인 | 어플에서 확인하는 기숙사 공식 일정 |

|<img src="https://github.com/idorm/idorm-android/assets/103296628/b4664e47-f8c6-44cc-9443-c1b67df2140e" width="300"></img>| <img src="https://github.com/idorm/idorm-android/assets/103296628/d9c5350d-a4d4-4f94-8b53-9b04cdbaa9b8" width="300"></img> |
| ------------ | ------------- |
| 온보딩을 통한 내 카드 생성 | 필터와 스와이핑으로 룸메이트 매칭  |

|<img src="https://github.com/idorm/idorm-android/assets/103296628/47677acd-abda-48c5-aeae-2960e31b7482" width="300"></img>| <img src="https://github.com/idorm/idorm-android/assets/103296628/5c67abd7-667f-4c34-b189-43aca6bed73f" width="300"></img> |
| ------------ | ------------- |
| 기숙사 별로 확인하는 기숙사생 커뮤니티 | 룸메이트와 공유하는 우리 방 캘린더  |

<br>

---


## Project Setup
Sentry Key와 카카오 SDK 네이티브 앱 key가 필요합니다.
키는 프로젝트 관리자에게 요청하세요.

<br>

## Dependencies
``` 
dependencies {
    //sdkInfo
    compileSdk 33
    minSdk 26
    targetSdk 33

    //for codes
    implementation 'androidx.core:core-ktx:1.10.1'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'

    // Dependencies for MVVM
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$archLifecycleVersion"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$archLifecycleVersion"
    implementation 'androidx.activity:activity-ktx:1.7.2'
    implementation 'androidx.fragment:fragment-ktx:1.6.1'

    //for coroutines
    implementation('org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3')
    implementation('org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3')

    //for a testing codes
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    //for playstore In-App Update
    implementation 'com.google.android.play:app-update:2.1.0'
    implementation 'com.google.android.play:app-update-ktx:2.1.0'

    //for sentry
    implementation 'io.sentry:sentry-android:6.28.0'

    //for fireBase
    implementation platform('com.google.firebase:firebase-bom:32.2.2')
    implementation 'com.google.firebase:firebase-messaging-ktx:23.2.1'
    implementation 'com.google.firebase:firebase-core:21.1.1'
    implementation 'com.google.firebase:firebase-analytics-ktx:21.3.0'
    implementation 'com.google.firebase:firebase-config-ktx:21.4.1'

    //for share on KakaoTalk
    implementation "com.kakao.sdk:v2-share:2.13.0"

    //for suitable date and time
    implementation "joda-time:joda-time:$jodaTimeVersion"

    //for materialView
    implementation 'com.google.android.material:material:1.9.0'

    //for networking
    implementation "com.squareup.okhttp3:okhttp:4.9.0"
    implementation "com.squareup.retrofit2:retrofit:$retrofitVersion"
    implementation "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    implementation "com.google.code.gson:gson:$gsonVersion"

    //for network image loader
    implementation "com.github.bumptech.glide:glide:$glideVersion"

    //for dataStore
    implementation "androidx.datastore:datastore-preferences:1.0.0"

    //for appcompat
    implementation 'androidx.appcompat:appcompat:1.6.1'

    //for constraintlayout
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    //for splashscreen
    implementation 'androidx.core:core-splashscreen:1.1.0-alpha01'

    //for continuous slider
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'com.github.Jay-Goo:RangeSeekBar:v3.0.0'
    implementation "org.jetbrains.kotlin:kotlin-reflect"

    //for cardStackView(matching card)
    implementation "com.github.yuyakaido:CardStackView:$cardStackViewVersion"
    implementation "io.github.florent37:shapeofview:$shapeOfViewVersion"

    //for ImagePicker
    implementation "com.github.nguyenhoanglam:ImagePicker:$imagePickerVersion"

    //for circleImageView
    implementation 'de.hdodenhof:circleimageview:3.1.0' 

    //for calendarView
    implementation 'com.kizitonwose.calendar:view:2.3.0'
}
