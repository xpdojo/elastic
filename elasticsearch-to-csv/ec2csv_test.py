import csv
import os
import unittest

import ec2csv

search_result = {
    '_shards': {
        'failed': 0,
        'skipped': 0,
        'successful': 1,
        'total': 1
    },
    'hits': {
        'hits': [
            {
                '_id': '1111',
                '_index': 'vehicle-car',
                '_score': 2.8701992,
                '_source': {
                    '@timestamp': '2022-10-06T15:55:09.000Z',
                    'last_update_ts': '2022-10-06T15:55:16.000Z',
                    'product_id': 'abc123',
                    'product_price': 5596},
                '_type': '_doc'}
        ],
        'max_score': 2.8701992,
        'total': {
            'relation': 'eq',
            'value': 6521
        }
    }
}

test_file = './test.csv';

headers = ['product_id', 'product_price']

expected_result = [
    headers,
    ['abc123', '5596'],
]


class TestCsv(unittest.TestCase):

    def setUp(self):
        ec2csv.write_csv(search_result, test_file, headers)

    def tearDown(self):
        os.remove(test_file)

    def test_read_line(self):
        with open(test_file, 'r') as csv_file:
            reader = csv.reader(csv_file, dialect='excel')
            self.assertEqual(next(reader), expected_result[0])
            self.assertEqual(next(reader), expected_result[1])


if __name__ == "__main__":
    unittest.main()
