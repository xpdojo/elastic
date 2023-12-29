import json
import os
import time
from pprint import pprint

from elasticsearch import Elasticsearch, helpers


def main():
    # https://www.elastic.co/guide/en/elasticsearch/client/python-api/current/getting-started-python.html#_connecting
    es = Elasticsearch(
        hosts="localhost:9200",
        # Stack Management > API keys > Create API key 혹은 POST /_security/api_key
        api_key="UUxYQnRJd0Jlbl9rekJfQVJKTEg6SXVhbU5oaHpTZTJES3E2NE13cEtOdw=="  # base64 encoded
    )
    print(es.info())

    # 인덱스 이름 설정
    # GET /_cat/indices?v
    # GET /test_index/_search
    index_name = "test_index"

    # 인덱스 존재 여부 확인 및 생성
    if not es.indices.exists(index=index_name):
        es.indices.create(index=index_name)

    # 문서 인덱싱
    # index(es, index_name)

    count = es.count(index=index_name)
    print(f"인덱스 '{index_name}'의 문서 수: {count['count']}개")

    random_search_result = random_search(es, index_name)
    print(random_search_result)

    start = time.time()
    search_result = search(es, index_name, '땅')
    end = time.time()

    pprint(search_result)
    print(f"검색 시간: {end - start}초")


def search(es, index_name: str, term: str):
    # 검색 쿼리 실행
    query = {
        "size": 10,
        "query": {
            "term": {
                "title": term
            }
        }
    }

    response = es.search(index=index_name, body=query)

    # 조회된 문서 처리
    return [doc['_source'] for doc in response['hits']['hits']]  # list comprehension


def random_search(es, index_name: str):
    # 랜덤 쿼리 실행
    query = {
        "size": 10,
        "query": {
            "function_score": {
                "functions": [
                    {"random_score": {}}
                ]
            }
        }
    }

    response = es.search(index=index_name, body=query)

    # 조회된 문서 처리
    # for doc in response['hits']['hits']:
    #     print(doc['_source'])
    return [doc['_source'] for doc in response['hits']['hits']]  # list comprehension


def index(es, index_name):
    # JSON 파일 읽기
    json_file_path = "./서울도서관 소장자료 현황정보.json"
    if os.path.exists(json_file_path):
        with open(json_file_path, 'r') as file:
            json_data = json.load(file)
            # print(json_data)

            # Elasticsearch에 데이터 저장
            actions = [
                {
                    "_index": index_name,
                    "_source": data,
                }
                for data in json_data['DATA']
            ]
            helpers.bulk(es, actions)
        result = "JSON 파일이 Elasticsearch 인덱스에 저장되었습니다."
    else:
        result = "JSON 파일이 존재하지 않습니다."
    print(result)


if __name__ == '__main__':
    main()
