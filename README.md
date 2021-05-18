# 작은 학습 도우미 Together
![together](https://user-images.githubusercontent.com/45118999/118665070-f85f5080-b82c-11eb-8e75-dfe05ed5df80.jpg)


## Togther 소개  
Together는 비대면 스터디를 돕기 위해 제작된 안드로이드 어플 입니다.  
COVID-19로 인해 저하된 학습 의욕을 향상시키기 위해 아래와 같은 기능을 제공합니다.  


### - 공부 시간 확인  
> 일간, 주간, 월간 공부시간 확인 및 평균 제공  
### - 스터디 그룹 가입  
> 본인의 목표에 맞는 스터디 그룹 가입  
> 스터디원들과 스터디 진행, 대화 가능  
### - 공부 집중 모드  
> 공부시간동안 얼굴을 인식해야 공부시간 측정 가능  
> 따라서 공부시간동안 휴대폰 사용 불가능  
### - 일정 관리  
> 달력 및 일정 관리 기능이 있어서 특정한 날 일정을 적을 수 있음  
> 개인 일정 관리 및 공부 일정을 본 앱에서 해결할 수 있음  
### - 백색소음  
> 원하는 백색소음을 선택해서 공부할 때 재생할 수 있음  
### - 공부 통계 확인(구현중)  
> 공부 기록을 바탕으로 공부 통계 확인 가능  
> 그래프로 쉽게 확인할 수 있음  


## 코드 실행 관련 주의사항  

#### 배포시에는 관련된 문제가 없겠지만, 개발중이라 실행을 위해 몇가지 절차가 필요합니다.  
#### 프로젝트 실행 전에 sdk를 미리 다운받는것을 권장 드립니다.


##### 본 프로젝트는 OpenCV를 사용하기 때문에 관련 sdk를 설치해야 합니다.  
#### 1. sdk 다운로드  
![image](https://user-images.githubusercontent.com/45118999/118667741-38273780-b82f-11eb-96e4-4d0d0dbf425b.png)  
git clone을 사용해 본 프로젝트를 받은 후, 위 그림과 같이 프로젝트 폴더와 같은 곳에 sdk를 배치합니다.  
sdk는 용량이 커서 구글 드라이브로 첨부합니다.  
저희에게 말씀해주시면 메일로 보내드리겠습니다.  
https://opencv.org/releases/ 에서도 다운이 가능합니다.(버전 4.4 다운을 권장 드립니다.)  
홈페이지 접속>OpenCV 4.4> Android 다운로드> sdk만 사용  
(sdk: https://drive.google.com/file/d/12efDuBtevuCf4gS_FyEvyGJqVpCJpFsA/view?usp=sharing)  


#### 2. Android Studio 기본 설정  
본 프로젝트를 실행하기 위해서 Android Studio 4.1.2, API 26 이상 사용을 권장합니다.  
API는 Tools>SDKManager 에서 확인 및 설치가 가능합니다.  
Tools>SDKManager에 들어가서 SDK Tools 클릭 후, NDK(Side by side), CMake도 설치를 진행합니다.  


![image](https://user-images.githubusercontent.com/45118999/118671353-5a6e8480-b832-11eb-8d45-308742c27951.png)


#### 3. sdk 설정  
settings.gradle에 들어가 아래 코드를 입력한 후, Sync Now를 클릭합니다.  
```java
def opencvsdk='../'
include ':opencv'
project(':opencv').projectDir = new File(opencvsdk + '/sdk')
```

이미 있다면 다음 단계로 넘어갑니다.

![image](https://user-images.githubusercontent.com/45118999/118676444-5f353780-b836-11eb-9321-298cdeef9d90.png)


다음으로 File>Project Structure로 들어가 Modules에 opencv가 있는지 확인합니다.  
없다면 위 사진과 같이 Modules의app 클릭> + 클릭> Module Dependency 클릭 후 opencv를 추가합니다.  

마지막으로 Gradle Scripts의 build.gradle(:app)으로 들어가 67번째 줄에 위치한 아래 코드를 추가합니다.  
있다면 지운 후 Sync Now를 클릭하고, 다시 넣은 후 Sync Now를 클릭 합니다.  


```java
    implementation project(':opencv')//opencv
```


OpenCV와 관련된 파일들이 추가되고, cpp 하위 폴더에 CMakeLists를 포함한 파일들이 생성된 것을 확인할 수 있습니다.  
혹시 문제가 발생한다면, 저희에게 개인적으로 연락을 주시거나 TogetherIdeal@gmail.com으로 보내주시면 도와드리겠습니다.  


### 구글 로그인을 위해 준비해야 하는 부분  
본 프로젝트는 개발중인 프로젝트입니다.  
구글 로그인을 위해 디버그 성명 인증서 SHA-1 값을 보내주셔야지 로그인이 가능합니다.  
저희에게 개인적으로 연락을 주시거나, TogetherIdeal@gmail.com으로 보내주시면 처리하겠습니다.  
메일의 경우 처리가 늦어질 수 있습니다. 양해 부탁 드립니다.  
SHA-1 값을 찾는 방법은 아래 티스토리 게시물의 5번을 참고해주시면 감사합니다.  
https://fjdkslvn.tistory.com/2  
