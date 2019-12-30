# kakaopay-assignment
- 지자체 협약 지원 API
## Development environment
- IDE : IntelliJ 2019.3.1
- framework : springboot 2.2.2.RELEASE 
- build tool : gradle v5.6.2
- test : JUnit 5
- database : H2 database

## How to solve an assignment
- Entity
	- Region(지역), Support (지자체 협약 지원 정보)의 연관관계를 OneToOne으로 설정.
	- 이차보전의 경우 1%~2%와 같이 범위 산정되어 있는 부분 때문에 MIN, MAX 항목을 나누어 관리.
		- 평균값을 구하기 쉽게 하려면 나누는게 좋다고 판단.
	- JpaAuditing 적용하여 createDate, modifiedDate를 관리.
- Authentication
	- `com.auth0.java-jwt` library 사용.
	- OncePerRequestFilter를 상속하여 인증 프로세스를 추가한 클래스를 두고 filter로 등록.
- DTO
	- 외부로 보여지는 response view는 DTO를 이용하여 과제 설명의 출력예제와 동일하게 표현
- Cache by `EhCache`
    - findAll()을 해야 하는 로직에는 데이터의 양이 많을 경우 상당히 부담이 되기 때문에 cache를 통하여 부담을 감소. 
    - data update event가 발생 시, cache evict가 되어 cache data 동기화 됨.

## Build & run
- Git clone and move kakaopay-assignment directory.
```
$ git clone https://github.com/domac09/kakaopay-assignment.git
$ cd kakaopay-assignment
```

- Build

`$ ./gradlew clean build`

- Run 

`$ java -jar agreement-support-api/build/libs/agreement-support-api.jar`

## API List
0.인증

```
{"error": "unauthorized"} // 토큰인증 실패했을 경우.
{"error": "token_expired"} // 토큰이 만료되었을 경우, ttl은 30분
```

1.회원가입

```
request:
curl -X POST \
  http://localhost:5000/v1/members \
  -d '{
        "id": "test",
        "password": "test1234"
      }'


response:
{
    "token": "{token}",
    "message": "OK"
}
```

2.로그인

```
request:
curl -X POST \
  http://localhost:000/v1/members/login \
  -d '{
        "id": "test",
        "password": "test1234"
      }'

response: 
{
    "token": "{token}",
    "message": "OK"
}
```

3.토큰 refresh

```
request:
curl -X POST \
  http://localhost:5000/v1/token/refresh \
  -H 'Authorization: Bearer {token}'

response:
{
    "token": "{token}",
    "message": "OK"
}
```

4.지자체 협약 지원 데이터 입력

```
request:
curl -X POST \
  http://localhost:5000/v1/supports \
  -H 'Authorization: Bearer {token}' \
  -d '{
        "region": "강릉시",
        "target": "강릉시 소재 중소기업으로서 강릉시장이 추천한 자",
        "usage": "운전",
        "limit": "0",
        "maximumRate": "5.00",
        "minimumRate": "3.00",
        "institute": "강릉시",
        "mgmt": "강릉지점",
        "reception": "강릉시 소재 영업점"
      }'

response:
HTTP/1.1 200 
Content-Length: 0

<Response body is empty>
```

5.지자체 협약 지원 데이터 조회

```
request:
curl -X GET \
  http://localhost:5000/v1/supports?region=경기도 \
  -H 'Authorization: Bearer {token}'

reseponse:
[
  {
    "region": "경기도",
    "target": "경기도 소재 중소기업으로서 경기도지사가 추천한 자",
    "limit": "30,000,000,000 이내",
    "rate": "0.3%~2.0%",
    "usage": "운전 및 시설",
    "institute": "경기신용보증재단",
    "mgmt": "경수지역본부",
    "reception": "전 영업점"
  }
]
```

6.지자체 협약 지원 데이터 수정

```
request:
curl -X PUT \
  http://localhost:5000/v1/supports \
  -H 'Authorization: Bearer {token}' \
  -d '{
          "region": "경기도",
          "target": "경기도 소재 중소기업으로서 경기도지사가 추천한 자",
          "limit": "30,000,000,000 이내",
          "rate": "0.3%~2.0%",
          "usage": "운전 및 시설",
          "institute": "경기신용보증재단",
          "mgmt": "경수지역본부",
          "reception": "전 영업점"
        }'

response:
HTTP/1.1 200 
Content-Length: 0

<Response body is empty>
```

7.지원금액으로 내림차순 정렬(지원금액이 동일하면 이차보전 평균 비율이 적은 순서) size 만큼 조회 

```
request:
curl -X GET \
  http://localhost:5000/v1/supports/limit?size=3 \
  -H 'Authorization: Bearer {token}'

response:
[
  "경기도",
  "제주도",
  "국토교통부"
]
```

8.보전 비율이 가장 작은 추천 기관명 조회 

```
request:
curl -X GET \
  http://localhost:5000/v1/supports/rate \
  -H 'Authorization: Bearer {token}' 

response:
[
  "안양상공회의소"
]
```