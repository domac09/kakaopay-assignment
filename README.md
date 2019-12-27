# kakaopay-assignment
## Development environment
- IDE : IntelliJ 2019.3.1
- framework : springboot 2.2.2.RELEASE 
- build tool : gradle v5.6.2
- test : JUnit 5
- database : H2 database

## How to solve an assignment
- Entity
	- Region(지역), Support (지자체 협약 지원 정보)의 연관관계를 OneToOne으로 설정.
	- Region의 지역코드는 임시로 지정.
	- 이차보전의 경우 1%~2%와 같이 범위 산정되어 있는 부분 때문에 MIN, MAX 항목을 나누어 관리.
		- 평균값을 구하기 쉽게 하려면 나누는게 좋다고 판단.
	- JpaAuditing 적용하여 createDate, modifiedDate를 관리.
- Authentication
	- `com.auth0.java-jwt` library 사용.
	- OncePerRequestFilter를 상속하여 인증 프로세스를 추가한 클래스를 두고 filter로 등록.
- DTO
	- 외부로 보여지는 response view는 DTO를 이용하여 과제 설명의 출력예제와 동일하게 표현

## build & run
- Git clone and move kakaopay-assignment directory.
```
$ git clone https://github.com/domac09/kakaopay-assignment.git
$ cd kakaopay-assignment
```

- Build

`$ ./gradlew clean build`

- Run 

`$ java -jar agreement-support-api/build/libs/agreement-support-api.jar`
