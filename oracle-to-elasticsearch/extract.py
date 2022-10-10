import logging

import oracledb

QUEUE_NAME = "VEHICLE_CAR_QUEUE"


def batch_extract(
        user: str,
        password: str,
        dsn: str,
        sql: str,
) -> list:
    connection = oracledb.connect(user=user,
                                  password=password,
                                  dsn=dsn)

    logging.info("oracle.dbms.version={0}".format(connection.version))
    logging.debug(sql)

    result = []

    # oracledb.exceptions.NotSupportedError:
    # DPY-3001: scrolling a scrollable cursor is only supported in python-oracledb thick mode
    # with connected_oracle.cursor(scrollable=True) as cursor:
    with connection.cursor() as cursor:
        cursor.execute(statement=sql)
        columns = [col[0] for col in cursor.description]
        cursor.rowfactory = lambda *args: dict(zip(columns, args))
        for row in cursor:
            result.append(row)

    # with connected_oracle.cursor() as cursor:
    #     # oracledb.exceptions.NotSupportedError:
    #     # DPY-3001: creating a queue is only supported in python-oracledb thick mode
    #     queue = connected_oracle.queue(QUEUE_NAME)
    #     queue.deqoptions.wait = oracledb.DEQ_NO_WAIT
    #     queue.deqoptions.navigation = oracledb.DEQ_FIRST_MSG

    return result
