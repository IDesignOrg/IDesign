# 🙌팀 IDESIGN

<img width="1468" alt="스크린샷 2024-09-23 오후 4 43 17" src="https://github.com/user-attachments/assets/cb01ae97-d77b-4bce-9979-74c9ea3cac9d">

## 목차

> 1. [ERD 구조](#erd-구조)
> 2. [시스템 아키텍처](#시스템-아키텍처)
> 3. [기술 스택](#기술스택)
> 4. [주요 기능](#주요기능)
> 5. [API 명세](#API명세)
> 6. [형상 관리](#형상관리)
> 7. [팀 문화](#팀문화)

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
> 3D 1명, 백엔드 2명
>
> [IDesign Swagger API 확인하러 가기](https://idesign.r-e.kr/swagger-ui/index.html)

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
                    <img src="https://example.com/yjh-image.png" alt="윤재환" style="max-width: 50%;"/>
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
            <td align = "center" style = "border: 1px solid black;">
                -
            </td>
            <td align = "center" style = "border: 1px solid black;">
                -
            </td>
            <td align = "center" style = "border: 1px solid black;">
                -
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
