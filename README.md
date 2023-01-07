<img src="https://avatars.githubusercontent.com/u/108882058?s=200&v=4"></img>

# idorm
룸메이트 매칭, idorm과 함께해요.

## Project Setup
따로 할 것은 없고, Sentry Key 만 local.properties 에 넣어주시면 됩니다. 

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