# Oracle DB to Elastic Cloud

## 배경

Logstash로 데이터 적재 시 데이터가 누락(Loss)될 수 있다.

- [Queues and data resiliency](https://www.elastic.co/guide/en/logstash/7.17/resiliency.html) - Elastic 공식 문서

## 목적

Oracle DB의 데이터를 Elasticsearch로 적재하고,
Logstash는 데이터 누락을 감수할 수 있는 데이터만 적재한다. (ex: Log, Histroy, etc.)

## 준비

```shell
python3 -m pip install -r requirements.txt
```

## 실행

```shell
python3 main.py
```

## 테스트

```shell
python3 -m pytest
```
