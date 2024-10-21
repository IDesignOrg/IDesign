# 🙌팀 IDESIGN.

<img width="1468" alt="스크린샷 2024-09-23 오후 4 43 17" src="https://github.com/user-attachments/assets/cb01ae97-d77b-4bce-9979-74c9ea3cac9d">

## 목차

> 1. [ERD 구조](#erd-구조)
> 2. [시스템 아키텍처](#시스템-아키텍처)
> 3. [기술 스택](#기술-스택)
> 4. [주요 기능](#주요-기능)
> 5. [API 명세](#api-명세)
> 6. [형상 관리](#형상-관리)
> 7. [팀 문화](#팀-문화)
> 8. [버전](#버전)

## 프로젝트 소개

> IDesign은 주로 가구 및 인테리어에 중점을 둔 온라인 쇼핑몰입니다.
> 사용자들이 2D 및 3D 인테리어 디자인 도구를 사용하여 가구 배치 및 스타일을
> 시뮬레이션 할 수 있으며, 소품 SHOP을 통해 다양한 상품들을 구매할 수 있습니다.
>
> SpringBoot와 SpringData JPA를 사용해 기본적인 REST API를 구현하고,
> Docker, AWS, Github Actions를 사용해 서버를 배포했습니다.
> [IDesign 사이트로 이동](https://idesign.r-e.kr/)

### 개발 기간 및 인원

> 24.06.29 ~ 24.09.21 (11주)
>
> 3D 기능 개발 1명, 백엔드 2명

## 팀원

<markdown-accessibly-table data-catalyst>
<table align="center" tabindex="0" style ="border-collapse: collapse; width:100%;">
    <tbody>
        <tr>
            <th style = "border: 1px solid black; width:33%"> Backend 한민욱 </th>
            <th style = "border: 1px solid black; width:33%"> Backend 윤재환 </th>
            <th style = "border: 1px solid black; width:33%"> Frontend 이준희 </th>
        </tr>
        <tr>
            <td align="center" style = "border: 1px solid black;">
                <a target="_blank" rel="noopener noreferrer" href="https://github.com/user-attachments/assets/829537ed-bce3-4bb1-bf06-c99e5d5a86e4">
                    <img src="https://github.com/user-attachments/assets/829537ed-bce3-4bb1-bf06-c99e5d5a86e4" alt="한민욱" style="max-width: 50%;"/>
                </a>
            </td>
            <td align="center" style = "border: 1px solid black;">
                <a target="_blank" rel="noopener noreferrer" href="https://example.com">
                    <img src="https://github.com/user-attachments/assets/5a2486ac-9b4f-4793-b93f-79699d61c278" alt="윤재환" style="max-width: 50%;"/>
                </a>
            </td>
            <td align="center" style = "border: 1px solid black;">
                <a target="_blank" rel="noopener noreferrer" href="https://github.com/user-attachments/assets/17a06858-d5b0-446a-ad69-fd7ce7f5429c">
                    <img src="https://github.com/user-attachments/assets/17a06858-d5b0-446a-ad69-fd7ce7f5429c" alt="이준희" style="max-width: 50%;"/>
                </a>
            </td>
        </tr>
        <tr>
            <td align = "center" style = "border: 1px solid black;">
                <a href= "https://github.com/wookie3739">
                    <img alt="GitHub" src ="https://img.shields.io/badge/GitHub-181717.svg?&style=for-the-badge&logo=GitHub&logoColor=white"/>
                </a>
                <br>
                <a href="https://velog.io/@dnlsemtmf/posts">
                    <img alt = "velog" src = "https://camo.githubusercontent.com/28b1c3444eba03014c29da3c5d83a948c4497a4e710f4efbc4a39d6e9f0f833a/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f5465636820626c6f672d3230433939373f7374796c653d666c61742d737175617265266c6f676f3d56656c6f6726266c6f676f436f6c6f723d7768697465"
                    data-canonical-src="https://img.shields.io/badge/Tech blog-20C997?style=flat-square&logo=Velog&&logoColor=white" style = "max-width:100%;"/>
                </a>
                <br>
                <a href = "mailto:dnlsemtmf@gmail.com">
                    <img alt = "gmail" height = "25" src = "https://camo.githubusercontent.com/af3effc481f296b8b2ec1da6bf48d9d42aafae6dee7d3ffd966729c432d7d73d/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f456d61696c2d6431343833363f7374796c653d666c61742d737175617265266c6f676f3d476d61696c26266c6f676f436f6c6f723d7768697465">
                </a>
            </td>
            <td align = "center" style = "border: 1px solid black;">
                <a href= "https://github.com/DaRamGGi">
                    <img alt="GitHub" src ="https://img.shields.io/badge/GitHub-181717.svg?&style=for-the-badge&logo=GitHub&logoColor=white"/>
                </a>
                <br>
                <a href="https://daramggi.github.io/">
                    <img alt = "velog" src = "https://camo.githubusercontent.com/28b1c3444eba03014c29da3c5d83a948c4497a4e710f4efbc4a39d6e9f0f833a/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f5465636820626c6f672d3230433939373f7374796c653d666c61742d737175617265266c6f676f3d56656c6f6726266c6f676f436f6c6f723d7768697465"
                    data-canonical-src="https://img.shields.io/badge/Tech blog-20C997?style=flat-square&logo=Velog&&logoColor=white" style = "max-width:100%;"/>
                </a>
                <br>
                <a href = "mailto:jelly0379@gmail.com/">
                    <img alt = "gmail" height = "25" src = "https://camo.githubusercontent.com/af3effc481f296b8b2ec1da6bf48d9d42aafae6dee7d3ffd966729c432d7d73d/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f456d61696c2d6431343833363f7374796c653d666c61742d737175617265266c6f676f3d476d61696c26266c6f676f436f6c6f723d7768697465">
                </a>
            </td>
            <td align = "center" style = "border: 1px solid black;">
                <a href= "https://github.com/junheeLee96">
                    <img alt="GitHub" src ="https://img.shields.io/badge/GitHub-181717.svg?&style=for-the-badge&logo=GitHub&logoColor=white"/>
                </a>
                <br>
                <a href="https://junheelab.tistory.com/">
                    <img alt = "velog" src = "https://camo.githubusercontent.com/28b1c3444eba03014c29da3c5d83a948c4497a4e710f4efbc4a39d6e9f0f833a/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f5465636820626c6f672d3230433939373f7374796c653d666c61742d737175617265266c6f676f3d56656c6f6726266c6f676f436f6c6f723d7768697465"
                    data-canonical-src="https://img.shields.io/badge/Tech blog-20C997?style=flat-square&logo=Velog&&logoColor=white" style = "max-width:100%;"/>
                </a>
                <br>
                <a href = "mailto:dlwnsgml203@gmail.com">
                    <img alt = "gmail" height = "25" src = "https://camo.githubusercontent.com/af3effc481f296b8b2ec1da6bf48d9d42aafae6dee7d3ffd966729c432d7d73d/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f456d61696c2d6431343833363f7374796c653d666c61742d737175617265266c6f676f3d476d61696c26266c6f676f436f6c6f723d7768697465">
                </a>
            </td>
        </tr>
        <tr>
            <td align = "left" style = "border: 1px solid black; padding-left:10px; font-size:14px">
                <div>- 로그인, 회원가입 API</div>
                <div>- 쿠폰 API</div>
                <div>- 장바구니 및 결제 API</div>
                <div>- 3D 대시보드 API</div>
                <div>- 채팅 API</div>
                <div>- 주문, 배송 API</div>
                <div>- 고객센터 API</div>
                <div>- 배포 및 CICD</div>
            </td>
            <td align = "left" style = "border: 1px solid black; padding-left:10px; font-size:14px">
                <div>- Google, Kakao, Naver Login API</div>
                <div>- 상품 API</div>
                <div>- 이벤트 API</div>
                <div>- 리뷰 API</div>
                <div>- 관리자 페이지 API</div>
                <div>- 마이페이지 API</div>
                <div>- GCS</div>
                <div>- 배포 및 CICD</div>
            </td>
            <td align = "left" style = "border: 1px solid black; padding-left:10px; font-size:14px">
                <div>- 3D 기능개발</div>
                <div>- 대쉬보드 페이지 개발</div>
                <div>- 배포 및 CICD</div>
            </td>
        </tr>
    </tbody>
</table>

</markdown-accessibly-table>

## Installation

    #build webpack
    ```
    cd src/main/resources
    npm install
    npm run build
    ```
    ```
    #build springBoot
    gradlew build
    ```

## Deployment

- EC2 Instance를 이용합니다.
- Docker Container와 GitActions를 이용해 무중단 배포(blue-green)를 사용합니다.

<br>
<br>

# ERD 구조

<img src = "https://github.com/user-attachments/assets/4da63246-d8f2-4dbc-860f-99b56e139ff0" alt ="13차 ERD">

[👉ERD 구조 보러가기](https://dbdiagram.io/d/66587f0eb65d933879196226)

# 시스템 아키텍처

<img src = "https://github.com/user-attachments/assets/e2b17cb5-a49b-4f58-bec4-692ceab83422"/>
<br>
<br>

# 기술 스택

> [BackEnd](https://swamp-gull-171.notion.site/IDesign-BackEnd-Skill-Tools-10e6f41133c880459f21d2be9d3ce28b?pvs=4)

# 주요 기능

### 요약

<markdown-accessibly-table data-catalyst>
<table align="center" tabindex="0" style="border-collapse: collapse; width:100%;">
    <tbody>
        <tr>
            <th style="border: 1px solid black; width:33%; text-align: center;"> 공통 </th>
            <th style="border: 1px solid black; width:33%; text-align: center;"> 사용자 </th>
            <th style="border: 1px solid black; width:33%; text-align: center;"> 관리자 </th>
        </tr>
        <tr>
            <td style="border: 1px solid black; width:33%; text-align: left;">
                - 회원가입, 로그인<br>
                - 아이디 및 비밀번호 찾기<br>
                - 상품 목록 및 상품 상세 조회<br>
                - 2D 및 3D 인테리어 디자인<br>
                - 고객센터 문의 및 1:M 관리자 채팅<br>
                - 리뷰 작성 및 이벤트 참여
            </td>
            <td style="border: 1px solid black; width:33%; text-align: left;">
                - 개인정보 관리<br>
                - 장바구니 관리<br>
                - 결제 및 주문 관리<br>
                - 3D 인테리어 관리<br>
                - 쿠폰 관리<br>
                - 리뷰 관리
            </td>
            <td style="border: 1px solid black; width:33%; text-align: left;">
                - 상품 및 상품 카테고리 관리<br>
                - 옵션 및 옵션 카테고리 관리<br>
                - 회원 관리 및 쿠폰 관리<br>
                - 이벤트 및 리뷰 관리<br>
                - 관리자 채팅 관리
            </td>
        </tr>
    </tbody>
</table>
</markdown-accessibly-table>

> [👉세부 기능 확인하러 가기](https://lightning-scent-c7f.notion.site/IDESIGN-10ebd9219c6880fd8a6cfe17b6a2dbf2?pvs=4)

# API 명세

> [👉 API 명세 확인하러 가기(Swagger)](https://idesign.r-e.kr/swagger-ui/index.html)

# 형상 관리

<markdown-accessibly-table data-catalyst>
<table align="center" tabindex="0" style="border-collapse: collapse; width:100%;">
    <tbody>
        <tr>
          <th style="border: 1px solid black; width:50%; text-align: center;"> [Notion]<br>
            컨벤션, 팀 규칙, 기능 분담 등<br>
            프로젝트 진행에 필요한 전반적인 사항 기록
           </th>
          <th style="border: 1px solid black; width:50%; text-align: center;"> [Jira]<br>
                개발을 진행하며 Sprint 단위로 계획한 사항의 담당자, 마감일, 진행 상황, 세부 사항 기록
           </th>
        </tr>
        <tr>
            <td style = "height:100%;">
                <img src = "https://github.com/user-attachments/assets/25a89641-7bbe-48b6-8e27-78820a738a1d" alt = "IDESIGN Notion"/>
            </td>
            <td style = "height:100%;">
                <img src = "https://github.com/user-attachments/assets/50e4f233-63d7-4c54-af3d-41a50981aa38" alt = "IDESIGN Jira"/>
            </td>
        </td>
    </tbody>
</table>
</markdown-accessibly-table>
<br>
<br>

# 팀 문화

1. 상호 존중과 화합
- 질문할 때나 리뷰할 때나 미팅할 때나 늘 상호존중! 즐거운 협업이 될 수 있도록 노력합니다.
2. 바쁜 하루를 마무리하는 미팅<br>
❤️‍ Daily Scrum 🔥
- 주중 저녁, 매일 서로 어떤 하루를 보냈는지를 알기 위해 Discord에서 데일리 스크럼을 진행합니다.
- 무엇을 하고 있었는지, 무엇을 새로 시작했는지, 앞으로 무엇을 할 것인지, 그리고 그 날의 Trouble Shooting과 PR 확인 여부 등을 공유합니다.
3. 한 주를 마무리하는 미팅
✈️ Sprint Plan ✈️
- 일주일을 시작하는 월요일에 한 주 동안 어떤 일을 할지 계획을 세웁니다.
* 지난 Sprint Plan에서 세웠던 계획을 얼마나 이행했는지에 대해 돌아봅니다.
4. 즐거운 개발 시간, 🕜 Core Time 🕜
- 협업 효율을 올리기 위해 주 2번 이상 카페에 모여서 다 같이 개발하는 시간을 가지거나<br>
Discord 화면 공유를 통해 코어 타임을 가집니다.
* 주중 2 ~ 9시는 필수, 주말은 자유입니다.
5. 내가 아는 건 모두가!
- Notion을 통해 프로젝트 전반의 결정 사항과 회의 내용, 회의 결과 그리고 Jira, GitHub과의 프로젝트 PR을 통해 즉각적인 피드백이 이루어질 수 있도록 합니다.
- Jira를 통해 서로의 작업 계획, 진척도, 현재 어떠한 작업을 하고 있는지에 대해 파악할 수 있도록 합니다.
6. 모두의 Pull Request
- 2인 이상의 팀원들에게 확인을 받은 뒤 PR을 Merge 합니다.

<br>
<br>

# 버전

- version 1.0.0(240921) : 최초 배포
- version 1.0.1(-) : 톰캣 추가(한민욱/윤재환)
