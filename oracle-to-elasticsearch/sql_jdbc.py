def query(batch_size: int,
          last_run_metadata: str):
    return f"""
SELECT CAR.ID                                           AS PRODUCT_ID,
       TO_TIMESTAMP(CAR.UPDATE_DTM, 'YYYYMMDDHH24MISS') AS LAST_UPDATE_TS,
       CAR.REG_DTM                                      AS REG_DTM,
       CAR.UPDATE_DTM                                   AS UPDATE_DTM
FROM VEHICLE_CAR CAR
WHERE CAR.LAST_UPDATE_TS >= TIMESTAMP '{last_run_metadata}'
ORDER BY CAR.UPDATE_DTM ASC
OFFSET 0 ROWS FETCH NEXT {batch_size} ROWS ONLY
"""
