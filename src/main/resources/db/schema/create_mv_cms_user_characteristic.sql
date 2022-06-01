DROP MATERIALIZED VIEW mv_cms_user_characteristic
/

CREATE MATERIALIZED VIEW mv_cms_user_characteristic
   REFRESH FORCE ON DEMAND
   AS WITH w_cms_user_characteristic AS
      (  -- get pick list characteristics
         SELECT c.characteristic_id,
                ccv.locale,
                ccv.cms_code,
                ccv.cms_name
           FROM characteristic c,
                mv_cms_code_value ccv
          WHERE c.pl_name IS NOT NULL 
            AND ccv.asset_code = c.pl_name
          UNION --Bug # 50969
         -- get non-pick list characteristics
         SELECT uc.characteristic_id,
                dl.locale,
                uc.characteristic_value AS cms_code,
                uc.characteristic_value AS cms_name
           FROM user_characteristic uc,
                ( -- get default locales
                  SELECT DISTINCT ccv.locale
                    FROM mv_cms_code_value ccv
                   WHERE ccv.asset_code = 'default.locale'

                ) dl
             -- cartesian join to default locale
          WHERE uc.characteristic_id IN (SELECT characteristic_id FROM characteristic WHERE pl_name IS NULL)
          UNION
         -- get report user characteristics
         SELECT uc.characteristic_id,
                uc.locale,
                uc.cms_code,
                uc.cms_name
           FROM rpt_user_characteristic uc
      )
      SELECT rd.user_nt_id AS user_id,
             rd.locale,
             cuc1.cms_name AS pax_char1,
             cuc2.cms_name AS pax_char2,
             cuc3.cms_name AS pax_char3,
             cuc4.cms_name AS pax_char4,
             cuc5.cms_name AS pax_char5,
             cuc6.cms_name AS pax_char6,
             cuc7.cms_name AS pax_char7,
             cuc8.cms_name AS pax_char8,
             cuc9.cms_name AS pax_char9,
             cuc10.cms_name AS pax_char10,
             cuc11.cms_name AS pax_char11,
             cuc12.cms_name AS pax_char12,
             cuc13.cms_name AS pax_char13,
             cuc14.cms_name AS pax_char14,
             cuc15.cms_name AS pax_char15,
             cuc16.cms_name AS pax_char16,
             cuc17.cms_name AS pax_char17,
             cuc18.cms_name AS pax_char18,
             cuc19.cms_name AS pax_char19,
             cuc20.cms_name AS pax_char20
        FROM ( -- get raw data
               SELECT dl.locale,
                      rch.*
                 FROM rpt_characteristic rch,
                      ( -- get default locales
                        SELECT DISTINCT ccv.locale
                          FROM mv_cms_code_value ccv
                         WHERE ccv.asset_code = 'default.locale'

                      ) dl
                   -- cartesian join locale to each report record
                WHERE rch.characteristic_type = 'USER'
             ) rd,
             w_cms_user_characteristic cuc1,
             w_cms_user_characteristic cuc2,
             w_cms_user_characteristic cuc3,
             w_cms_user_characteristic cuc4,
             w_cms_user_characteristic cuc5,
             w_cms_user_characteristic cuc6,
             w_cms_user_characteristic cuc7,
             w_cms_user_characteristic cuc8,
             w_cms_user_characteristic cuc9,
             w_cms_user_characteristic cuc10,
             w_cms_user_characteristic cuc11,
             w_cms_user_characteristic cuc12,
             w_cms_user_characteristic cuc13,
             w_cms_user_characteristic cuc14,
             w_cms_user_characteristic cuc15,
             w_cms_user_characteristic cuc16,
             w_cms_user_characteristic cuc17,
             w_cms_user_characteristic cuc18,
             w_cms_user_characteristic cuc19,
             w_cms_user_characteristic cuc20
       WHERE rd.locale                     = cuc1.locale (+)
         AND rd.characteristic_id1         = cuc1.characteristic_id (+)
         AND rd.characteristic_charvalue1  = cuc1.cms_code (+)
         AND rd.locale                     = cuc2.locale (+)
         AND rd.characteristic_id2         = cuc2.characteristic_id (+)
         AND rd.characteristic_charvalue2  = cuc2.cms_code (+)
         AND rd.locale                     = cuc3.locale (+)
         AND rd.characteristic_id3         = cuc3.characteristic_id (+)
         AND rd.characteristic_charvalue3  = cuc3.cms_code (+)
         AND rd.locale                     = cuc4.locale (+)
         AND rd.characteristic_id4         = cuc4.characteristic_id (+)
         AND rd.characteristic_charvalue4  = cuc4.cms_code (+)
         AND rd.locale                     = cuc5.locale (+)
         AND rd.characteristic_id5         = cuc5.characteristic_id (+)
         AND rd.characteristic_charvalue5  = cuc5.cms_code (+)
         AND rd.locale                     = cuc6.locale (+)
         AND rd.characteristic_id6         = cuc6.characteristic_id (+)
         AND rd.characteristic_charvalue6  = cuc6.cms_code (+)
         AND rd.locale                     = cuc7.locale (+)
         AND rd.characteristic_id7         = cuc7.characteristic_id (+)
         AND rd.characteristic_charvalue7  = cuc7.cms_code (+)
         AND rd.locale                     = cuc8.locale (+)
         AND rd.characteristic_id8         = cuc8.characteristic_id (+)
         AND rd.characteristic_charvalue8  = cuc8.cms_code (+)
         AND rd.locale                     = cuc9.locale (+)
         AND rd.characteristic_id9         = cuc9.characteristic_id (+)
         AND rd.characteristic_charvalue9  = cuc9.cms_code (+)
         AND rd.locale                     = cuc10.locale (+)
         AND rd.characteristic_id10        = cuc10.characteristic_id (+)
         AND rd.characteristic_charvalue10 = cuc10.cms_code (+)
         AND rd.locale                     = cuc11.locale (+)
         AND rd.characteristic_id11        = cuc11.characteristic_id (+)
         AND rd.characteristic_charvalue11 = cuc11.cms_code (+)
         AND rd.locale                     = cuc12.locale (+)
         AND rd.characteristic_id12        = cuc12.characteristic_id (+)
         AND rd.characteristic_charvalue12 = cuc12.cms_code (+)
         AND rd.locale                     = cuc13.locale (+)
         AND rd.characteristic_id13        = cuc13.characteristic_id (+)
         AND rd.characteristic_charvalue13 = cuc13.cms_code (+)
         AND rd.locale                     = cuc14.locale (+)
         AND rd.characteristic_id14        = cuc14.characteristic_id (+)
         AND rd.characteristic_charvalue14 = cuc14.cms_code (+)
         AND rd.locale                     = cuc15.locale (+)
         AND rd.characteristic_id15        = cuc15.characteristic_id (+)
         AND rd.characteristic_charvalue15 = cuc15.cms_code (+)
         AND rd.locale                     = cuc16.locale (+)
         AND rd.characteristic_id16        = cuc16.characteristic_id (+)
         AND rd.characteristic_charvalue16 = cuc16.cms_code (+)
         AND rd.locale                     = cuc17.locale (+)
         AND rd.characteristic_id17        = cuc17.characteristic_id (+)
         AND rd.characteristic_charvalue17 = cuc17.cms_code (+)
         AND rd.locale                     = cuc18.locale (+)
         AND rd.characteristic_id18        = cuc18.characteristic_id (+)
         AND rd.characteristic_charvalue18 = cuc18.cms_code (+)
         AND rd.locale                     = cuc19.locale (+)
         AND rd.characteristic_id19        = cuc19.characteristic_id (+)
         AND rd.characteristic_charvalue19 = cuc19.cms_code (+)
         AND rd.locale                     = cuc20.locale (+)
         AND rd.characteristic_id20        = cuc20.characteristic_id (+)
         AND rd.characteristic_charvalue20 = cuc20.cms_code (+)
/

CREATE UNIQUE INDEX mv_cms_user_char_idx1
   ON mv_cms_user_characteristic
   ( user_id,
     locale
   )
/
