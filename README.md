### mini-project-aiot-gateway-1
# 팀원
- 양현성
- 윤인섭
- 이동민
- 김지윤
- 허시영

# 클래스 구조도
![클래스 구조도](img/ClassDiagram.png)

# 클래스 역할

## gateway
### SettingNode.java
  - checkCommendLine 메서드
    - CommandLine에 Option과 Parse를 통해 lauch.json에 args의 values로부터 필터링에 사용될 옵션값을 저장한다.
    - CommandLine에 "-c" 옵션이 있을 경우 CommandLine 옵션값을 가져온다. 없을 경우 setting.json에서 옵션값을 가져온다.
      
  - makeFlow 메서드
    - setting.json에서 각 노드들의 고유id와 type값을 가져와 reflection을 이용해 객체를 생성해준다.
    - 생성한 객체는 < Key : 고유id, value : ActiveNode(생성한 객체) > 형태의 HashMap변수 nodeList에 저장한다.
    - 생성한 객체의 setting.json의 "wire" key 값에 있는 배열에 size가 1이상일 경우 배열의 index를 key로 배열안에 있는 노드의 id들을 리스트로 만든다.
    - wireMap이라는 새로운 Map에 < 객체 노드의 id를  < wirePort, wireOutList > > 형태로 저장한다.
      
  - connectWire 메서드
    - wireMap 변수를 이용해서 각각 노드들에서 필요한 wire연결을 해주는 메서드이다.
    - < 노드 id , < 포트 번호, outPut와 연결할 노드들의 List> > 를 이용
    - 노드 id : wireMap의 key값이며 Message를 outPutWire에 put해주는 노드의 id
    - 포트 번호 : 노드(노드 id)가 가지고 있는 출력 포트의 번호이다. ex) 출력 포트가 2개면 0, 1번 포트가 존재한다.
    - 노드들의 List : 해당되는 포트 번호로 부터 Output되는 Message를 받을 Input노드들의 리스트다.
      
  - nodeStrat 메서드
    - reflection을 통해 생성된 노드들의 실행을 담당하는 메서드이다.
    - nodeList Map에서 노드들을 하나씩 꺼내 실행시키는데 Split노드의 경우 옵션값을 넘겨줘야 한다.
    - 꺼내온 노드중 Split노드일 경우 Split노드의 setOption메서드를 이용해 값을 넘겨주고 실행한다.
  
### SimpleNodeRed.java
  - Setting.java 객체를 생성한다.
  - Setting 객체의 메서드를 통해 노드를 생성하고 실행, wire를 연결해주는 담당을 한다.
  
## message
### JsonMessage.java
  - JsonObject를 담을 수 있는 메시지 객체
  
### Message.java
  - 노드들 간의 정보를 보내줄 때 메시지를 역할하는 추상클래스
  - 메시지를 생성할 때 고유의 id와 메시지 갯수를 증가하며 생성한다.
  - getId, getCreationTime(), getCount() 메서드가 있다.
  
## node
### MqttInNode.java (InputNode)
  - ems 서버에서 모든 토픽("#")을 받아오는 역할을 하는 클래스
  - connectServer() 메서드에서 ems 서버와 연결한다.
  - serverSubscribe() 메서드에서 새로운 JSONObject를 생성한 후 "#" 필터로 받아 온 topic과 payload를 추가한다.
  - 다음노드로 JSONObject를 담은 JSONMessage를 보낸다.
  
### SplitNode.java (InOutputNode)
  - 옵션값을 통해 받은 applicatoinName과 Sensors를 이용해 이전 노드로 부터 받은 Message에서 원하는 정보만 추출하는 역할을 하는 클래스
  - 이전 노드에서 받은 JSONObject의 topic 중 applicationName과 일치하는 JSONObject만 splitSensor메서드를 호출한다.
  - splitSensor 메서드를 통해 만들어진 새로운 JSONObject를 sendNode메서드를 이용해 다음 노드로 보내준다.
  
  - splitSensor(JSONObject) 메서드
    - JSONObject 중 Sensors에 있는 값(temperature, humidity...)을 가지고 있는 JSONObject에서만 값을 추출한다.
   
  - sendNode(JSONObject) 메서드
    - OutputWire에 JSONMessage로 만들어 넣어준다.
  
### ReduceTopicNode.java (InOutputNode)
  - topic을 재구성하는 클래스
  - 재구성한 topic을 JSONObject에 추가한 후 JSONMessage를 OutputWire에 넣어준다.
  
  - makeTopic(JSONObject) 메서드
    - JSONObject에서 값을 추출해 topic을 재구성해준다.
  
### PlaceTranslatorNode.java (InOutputNode)
  - place 값을 한글로 바꾸는 메서드
  - JSONObject값 중 key가 "place"의 value와 setting과정에서 받은 placeInfo Map의 key 중 일치하는 value로 변경한다. 
  
### MqttOutNode.java (OutputNode)
  - local의 브로커에 지금까지 재구성한 topic과 MqttMessage를 publish해준다.
  
## setting
### nodeSetting.json
  - 각각의 노드들의 정보 (id, type, wire(다음을 노드의 주소))
  - 필터링 하고 싶은 topic과 sensors
  
## wire
### BufferedWire.java
  - Wire 인터페이스를 구현한다.
  - Message 큐가 필드로 존재한다.
  - Message에 대한 put, get, hasMessage메서드가 있다.
  
### Wire.java
  - Wire의 인터페이스
  
