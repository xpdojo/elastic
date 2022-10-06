from datetime import datetime

from elastic import utc_str_timestamp


def test_elasticsearch_utc_timestamp():
    # 2014-12-24T04:41:04.000Z
    yyyymmddhh24miss = '20141224044104'

    date = datetime.strptime(yyyymmddhh24miss, '%Y%m%d%H%M%S')
    assert date == datetime(2014, 12, 24, 4, 41, 4)

    timestamp = date.strftime('%Y-%m-%dT%H:%M:%S.%f')[:-3] + 'Z'
    assert timestamp == '2014-12-24T04:41:04.000Z'


def test_utc_timestamp():
    timestamp = utc_str_timestamp('20141224044104')
    assert timestamp == '2014-12-24T04:41:04.000Z'
