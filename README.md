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



## idorm 기능
|로그인 이미지| <img src="https://github.com/idorm/idorm-android/assets/103296628/4bde6362-1d75-46c0-b65c-d2eb962a3a07" width="300"></img> |
| ------------ | ------------- |
| 자동 로그인 | 어플에서 확인하는 기숙사 공식 일정 |

|<img src="https://github.com/idorm/idorm-android/assets/103296628/b4664e47-f8c6-44cc-9443-c1b67df2140e" width="300"></img>| <img src="https://github.com/idorm/idorm-android/assets/103296628/d9c5350d-a4d4-4f94-8b53-9b04cdbaa9b8" width="300"></img> |
| ------------ | ------------- |
| 온보딩을 통한 내 카드 생성 | 필터와 스와이핑으로 룸메이트 매칭  |

|<img src="https://github.com/idorm/idorm-android/assets/103296628/47677acd-abda-48c5-aeae-2960e31b7482" width="300"></img>| <img src="https://github.com/idorm/idorm-android/assets/103296628/5c67abd7-667f-4c34-b189-43aca6bed73f" width="300"></img> |
| ------------ | ------------- |
| 기숙사 별로 확인하는 커뮤니티 | 룸메이트와 공유하는 우리 방 캘린더  |
   

<br>
<br>
<br>
<br>
<br>
<br>

# idorm
룸메이트 매칭, idorm과 함께해요.

## Project Setup
따로 할 것은 없고, Sentry Key와 카카오 SDK 네이티브 앱 key 만 local.properties 에 넣어주시면 됩니다. 

키는 프로젝트 관리자에게 요청하세요.

## Code Convention
### File Name
- Set of Classes/Objects/Function: PascalCase
- Layout XML: view_type_component_type_domain_or_subject.xml
    - ex) fragment_bottom_sheet_agreement.xml 은 약관 동의를 하는 바텀시트 프래그먼트 레이아웃
    - ex) item_bottom_sheet_agreement.xml 은 약관 동의를 하는 바텀시트에 속한 RecyclerView 의 아이템 레이아웃
### Variables: camelCase (mostly)
### Resource: Various (not have been set)

## Commit Convention
지켜지지 않을지도 모르지만.. 일단은 적어보아요
- FEAT : 새로운 기능 추가
- FIX : 버그 수정
- DOCS : 문서 수정
- STYLE : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
- REFACTOR : 코드 리펙토링
- TEST : 테스트 코드, 리펙토링 테스트 코드 추가
- CHORE : 빌드 업무 수정, 패키지 매니저 수정
