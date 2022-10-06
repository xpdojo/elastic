import configparser
import logging

import elastic
import oracle
import sql_jdbc

CONFIG_FILEPATH = 'config.ini'


def main():
    # Logging 설정
    logging.basicConfig(level=logging.INFO)

    es_logger = logging.getLogger('elasticsearch')
    es_logger.setLevel(logging.DEBUG)

    # 설정 파일 읽기
    config = configparser.ConfigParser()
    config.read(CONFIG_FILEPATH)

    oracle_result = oracle.select(sql=sql_jdbc.query,
                                  user=config['ORACLE']['user'],
                                  password=config['ORACLE']['password'],
                                  dsn=config['ORACLE']['dsn'])

    search_result = elastic.search(index=config['ELASTIC']['index'],
                                   updated_product=oracle_result)

    elastic.bulk_index(cloud_id=config['ELASTIC']['cloud_id'],
                       user=config['ELASTIC']['user'],
                       password=config['ELASTIC']['password'],
                       index=config['ELASTIC']['index'],
                       docs=search_result)


if __name__ == "__main__":
    main()
