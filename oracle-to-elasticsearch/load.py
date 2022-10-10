import logging
from pprint import pprint
from typing import Generator, Any, Collection

from elasticsearch.client import Elasticsearch

from transform import utc_str_timestamp


def batch_fetch(
        iterable: Collection,
        batch_size: int = 1_000,
) -> Generator[Any, Any, None]:
    batch_size *= 2
    length = len(iterable)
    for i in range(0, length, batch_size):  # 0~len(iterable) 범위를 batch_size만큼씩 반복
        yield iterable[i:min(i + batch_size, length)]  # [0:1000] -> 0~999, [1000:2000] -> 1000~1999, ...


def bulk_index(
        cloud_id: str = None,
        user: str = 'elastic',
        password: str = None,
        index: str = None,
        docs: list = None,
        last_run_metadata_path: str = '.meta/.vehicle-car-last-run-metadata',
        batch_size: int = 1_000,
):
    """
    bulk index
    """
    # Elastic Cloud 접속
    es = Elasticsearch(cloud_id=cloud_id, http_auth=(user, password))
    pprint(es.info())

    for batch_docs in batch_fetch(docs, batch_size):
        bulk = es.bulk(index=index, body=batch_docs)
        logging.info("batch_docs {0}".format(len(bulk['items'])))

        last_tracking_value = utc_str_timestamp(batch_docs[len(batch_docs) - 1]['last_update_ts'])
        pprint("last_tracking_value {0}".format(last_tracking_value))
        # w+ : 읽기/쓰기 모드로 파일을 연다. 파일이 존재하지 않으면 생성한다. (디렉토리는 생성해야 함)
        with open(last_run_metadata_path, 'w+') as file:
            file.write('--- {0}'.format(last_tracking_value))
