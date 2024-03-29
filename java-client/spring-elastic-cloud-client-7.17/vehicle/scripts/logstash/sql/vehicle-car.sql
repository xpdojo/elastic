-- sql_last_value는 numeric, timestamp 데이터만 비교 가능하다.
SELECT CAR.*
FROM (SELECT TO_TIMESTAMP(V_UPDATE_DTM, 'YYYYMMDDHH24MISS') AS LAST_UPDATE_TS,
             CAR.*
      FROM VEHICLE_CAR CAR
      ORDER BY CAR.UPDATE_DTM) CAR
WHERE CAR.UPDATE_DTM <= TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')
  AND :sql_last_value < LAST_UPDATE_TS
