import configparser
import logging

import sql_prod_init
from extract import batch_extract
from load import bulk_index
from transform import transform

CONFIG_FILEPATH = 'config.ini'


def main():
    # Logging 설정
    logging.basicConfig(level=logging.DEBUG)

    es_logger = logging.getLogger('elasticsearch')
    es_logger.setLevel(logging.INFO)

    # 설정 파일 읽기
    config = configparser.ConfigParser()
    config.read(CONFIG_FILEPATH)

    last_run_metadata_path = config['ELASTIC']['last_run_metadata_path']
    with open(last_run_metadata_path, 'r') as f:
        last_run_metadata = f.read()

    last_run_timestamp = last_run_metadata.removeprefix('--- ').removesuffix("\n")
    query = sql_prod_init.query(int(config['ORACLE']['batch_size']), last_run_timestamp)
    # Extract
    oracle_result = batch_extract(sql=query,
                                  user=config['ORACLE']['user'],
                                  password=config['ORACLE']['password'],
                                  dsn=config['ORACLE']['dsn'])

    result_length = len(oracle_result)
    if result_length == 0:
        print("No data found")
        exit(1)
    else:
        logging.info("[oracle] result_length {}".format(result_length))

    # Transform
    transformed_result = transform(index=config['ELASTIC']['index'],
                                   updated_product=oracle_result)

    # Load
    bulk_index(cloud_id=config['ELASTIC']['cloud_id'],
               user=config['ELASTIC']['user'],
               password=config['ELASTIC']['password'],
               index=config['ELASTIC']['index'],
               docs=transformed_result,
               last_run_metadata_path=last_run_metadata_path,
               batch_size=int(config['ELASTIC']['batch_size']))


if __name__ == "__main__":
    main()
