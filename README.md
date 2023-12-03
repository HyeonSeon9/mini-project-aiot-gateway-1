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

## Exception
### AlreadyExistsException
  - 
 
### AlreadyStartedException
  -
  
### InvalidArgumentException
  - 
  
### OutOfBoundsException
  - 
  
## gateway
### SettingNode.java
  - checkCommendLine 메서드
    - CommandLine에 Option과 Parse를 통해 lauch.json에 args의 values로부터 필터링에 사용될 옵션값을 저장한다.
    - 만약 "-c"옵션이 존재할 경우 CommandLine이 우선시 되며 없을 경우 setting.json에서 옵션값을 가져온다.
  - makeFlow 메서드
    - setting.json에서 각 노드들의 고유id와 type값을 가져와 reflection을 이용해 객체를 생성해준다.
    - 생성한 객체는 < Key : 고유id, value : ActiveNode(생성한 객체) > 형태의 HashMap변수 nodeList에 저장한다.
    - 생성한 객체의 setting.json의 "wire" key 값에 있는 배열에 size가 1이상일 경우 배열의 index를 key로 배열안에 있는 노드의 id들을 리스트로 만든다.
    - wireMap이라는 새로운 Map에 < 객체 노드의 id를  < wirePort, wireOutList > > 형태로 저장한다. 
  - connectWire 메서드
    - 
  - nodeStrat 메서드
  
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
  
### SplitNode.java (InOutputNode)
  -
  
### ReduceTopicNode.java (InOutputNode)
  -
  
### PlaceTranslatorNode.java (InOutputNode)
  -
  
### ReduceTopicNode.java (InOutputNode)
  -
  
### MqttOutNode.java (OutputNode)
  -
  
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
  
