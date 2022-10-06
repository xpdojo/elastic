from datetime import datetime
from pprint import pprint

from elasticsearch.client import Elasticsearch


def utc_str_timestamp(yyyymmddhh24miss):
    date = datetime.strptime(yyyymmddhh24miss, '%Y%m%d%H%M%S')
    return date.strftime('%Y-%m-%dT%H:%M:%S.%f')[:-3] + 'Z'


def lower_dict_keys(d):
    return {k.lower(): v for k, v in d.items()}


def search(index: str,
           updated_product: list,
           ) -> list:
    """
    like logstash filter
    """
    docs = []
    for src in updated_product:
        # mutate
        lower_src = lower_dict_keys(src)

        # date
        lower_src['@timestamp'] = utc_str_timestamp(lower_src['listing_dtm'])
        lower_src['reg_dtm'] = utc_str_timestamp(lower_src['reg_dtm'])

        # remove_field
        remove_fields = ['listing_dtm', 'update_dtm']
        for field in remove_fields:
            del lower_src[field]

        docs.append([
            {"index": {"_index": index, "_id": lower_src['product_id']}},
            lower_src
        ])

    return docs


def bulk_index(
        cloud_id: str,
        user: str,
        password: str,
        index: str,
        docs: list,
):
    """
    bulk index
    """
    # Elastic Cloud 접속
    es = Elasticsearch(cloud_id=cloud_id, http_auth=(user, password))
    pprint(es.info())

    for doc in docs:
        es.bulk(index=index, body=doc)
