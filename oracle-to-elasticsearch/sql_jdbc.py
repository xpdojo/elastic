query = """
SELECT CAR.ID            AS PRODUCT_ID,
       CAR.V_LISTING_DTM AS LISTING_DTM,
       CAR.V_REG_DTM     AS REG_DTM,
       CAR.V_UPDATE_DTM  AS UPDATE_DTM
FROM VEHICLE_CAR CAR
WHERE CAR.V_UPDATE_DTM > '20221006000000'
ORDER BY CAR.V_UPDATE_DTM ASC
"""