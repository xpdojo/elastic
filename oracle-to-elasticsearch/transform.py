from datetime import datetime


def utc_timestamp(yyyymmddhh24miss) -> str:
    # 2022-07-12T09:42:44.000Z
    date = datetime.strptime(yyyymmddhh24miss, '%Y%m%d%H%M%S')
    return date.strftime('%Y-%m-%dT%H:%M:%S.%f')[:-3] + 'Z'


def utc_str_timestamp(date) -> str:
    # 2022-09-05 09:55:52.000000 +00:00
    return date.strftime('%Y-%m-%d %H:%M:%S.%f +00:00')


def lower_dict_keys(d) -> dict:
    return {k.lower(): v for k, v in d.items()}


def transform(
        index: str,
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
        lower_src['@timestamp'] = utc_timestamp(lower_src['listing_dtm'])
        lower_src['reg_dtm'] = utc_timestamp(lower_src['reg_dtm'])

        # remove_field
        remove_fields = ['listing_dtm', 'update_dtm']
        for field in remove_fields:
            del lower_src[field]

        docs.append({"index": {"_index": index, "_id": lower_src['product_id']}})
        docs.append(lower_src)

    return docs
