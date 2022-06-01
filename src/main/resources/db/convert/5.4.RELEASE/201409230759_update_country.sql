update country set REQUIRE_POSTALCODE = 0 where COUNTRY_ID in ( '1824', '1836', '1846')
/
update country set REQUIRE_POSTALCODE = 0 where COUNTRY_CODE in ( 'ie','kw','lb' )
/
update country set CM_ASSET_CODE = 'country_data.country.rou',AWARDBANQ_COUNTRY_ABBREV = 'ROU' where COUNTRY_CODE = 'ro'
/