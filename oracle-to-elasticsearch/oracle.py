import oracledb


def select(
        user: str,
        password: str,
        dsn: str,
        sql: str,
) -> list:
    oracle = oracledb.connect(user=user,
                              password=password,
                              dsn=dsn)

    result = []

    with oracle.cursor() as cursor:
        cursor.execute(sql)
        columns = [col[0] for col in cursor.description]
        cursor.rowfactory = lambda *args: dict(zip(columns, args))
        for row in cursor:
            result.append(row)

    result_length = len(result)
    if result_length == 0:
        print("No data found")
        exit(1)
    else:
        print(result_length)

    return result
