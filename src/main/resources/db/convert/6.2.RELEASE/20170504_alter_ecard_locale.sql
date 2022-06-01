--To remove the duplicate records from the table
DELETE FROM ecard_locale
      WHERE ROWID NOT IN (  SELECT MIN (ROWID)
                              FROM ecard_locale
                          GROUP BY card_id, locale, display_name)
/
--unique constraint for fields (card_id,locale, display_name)
ALTER TABLE ecard_locale ADD CONSTRAINT card_name_udk UNIQUE(card_id,locale,display_name)
/
