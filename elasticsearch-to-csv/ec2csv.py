#!/usr/bin/env python3

import configparser
import csv
import os.path
import sys
from pprint import pprint

from elasticsearch import Elasticsearch

SIZE = 10_000
QUERY = {
    "bool": {
        "must": [
            {
                "term": {
                    "status.keyword": "active"
                }
            },
            {
                "term": {
                    "is_deleted.keyword": False
                }
            },
            {
                "range": {
                    "last_update_ts": {
                        "from": "2022-10-06"
                    }
                }
            }
        ]
    }
}

_argv = (sys.argv)
config_filepath = os.path.join(os.path.dirname(__file__), 'config.ini')
if len(_argv) > 1:
    config_filepath = str(_argv[1])
    if not os.path.isfile(config_filepath):
        print('Config file not found: {}'.format(config_filepath))
        sys.exit(1)

csv_path = os.path.join(os.path.dirname(config_filepath), 'search.csv')
if len(_argv) > 2:
    csv_path = str(_argv[2])
    print('CSV Path: {}'.format(config_filepath))


# CSV 파일 저장 함수
def write_csv(search, csv_path, headers):
    with open(csv_path, 'w') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=headers)
        writer.writeheader()

        for document in [hit['_source'] for hit in search['hits']['hits']]:
            csv_row = {}
            for header in headers:
                if header in document:
                    csv_row[header] = document[header]

            # writer.writerow({header: document[header]})
            writer.writerow(csv_row)


# Elastic Cloud 설정 파일 읽기
config = configparser.ConfigParser()
config.read(config_filepath)

# Elastic Cloud 접속
es = Elasticsearch(
    cloud_id=config['ELASTIC']['cloud_id'],
    http_auth=(config['ELASTIC']['user'], config['ELASTIC']['password'])
)

print("Elasticsearch Cluster Info")
pprint(es.info())

INDEX = config['ELASTIC']['index']

# count 쿼리
count_result = es.count(index=INDEX, body={"query": QUERY})
if count_result['count'] <= 0:
    print("No data found")
    exit()

pprint(str.format("query count >>> {0}", count_result['count']))

# search 쿼리
search_result = es.search(
    index=INDEX,
    size=SIZE,
    query=QUERY
)

# pprint(search_result)

csv_headers = config['ELASTIC']['csv_headers'].split(',')
write_csv(
    search_result,
    csv_path,
    csv_headers
)
