# 8.x 버전

## elasticsearch-kibana

- 실행

```sh
cd elasticsearch-kibana
./start.sh
# ./stop.sh
```

- 열린데이터광장 공공데이터 적재
  - 2023-12-29 기준.
  - Linux + chrome으로 다운로드하면 csv는 한글이 깨지니 json으로 받음.
  - 간단하게 적재하기 위해 logstash 대신 python 사용.
  - [서울도서관 소장자료 현황정보 (524089건)](https://data.seoul.go.kr/dataList/OA-15483/S/1/datasetView.do)
  - [서울시 지하철 호선별 역별 시간대별 승하차 인원 정보 (63105건)](https://data.seoul.go.kr/dataList/OA-12252/S/1/datasetView.do)

```sh
cd demo
python3 load.py
```

## References

- [deviantony/docker-elk](https://github.com/deviantony/docker-elk)
