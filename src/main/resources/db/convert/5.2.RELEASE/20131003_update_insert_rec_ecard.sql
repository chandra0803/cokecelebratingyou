update ECARD set IS_TRANSLATABLE = 1  where card_id = 2009
/
update ECARD set IS_TRANSLATABLE = 1  where card_id = 2010
/
update ECARD set IS_TRANSLATABLE = 1  where card_id = 2011
/
update ECARD set IS_TRANSLATABLE = 1  where card_id = 2012
/
update ECARD set IS_TRANSLATABLE = 1  where card_id = 2013
/
update ECARD set IS_TRANSLATABLE = 1  where card_id = 2014
/
update ECARD set IS_TRANSLATABLE = 1  where card_id = 2015
/
update ECARD set IS_TRANSLATABLE = 1  where card_id = 2016
/
update ECARD set IS_TRANSLATABLE = 1  where card_id = 2017
/

--cleanup current ecard_locale data, since those values are invalid
delete from ecard_locale where card_id in (2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016)
/

Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'de_DE', 'Above & Beyond Fish_de_DE')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'en_GB', 'Above & Beyond Fish_en_GB')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'es_ES', 'Above & Beyond Fish_es_ES')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'es_MX', 'Above & Beyond Fish_es_MX')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'fr_CA', 'Above & Beyond Fish_fr_CA')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'fr_FR', 'Above & Beyond Fish_fr_FR')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'it_IT', 'Above & Beyond Fish_it_IT')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'ja_JP', 'Above & Beyond Fish_ja_JP')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'nl_NL', 'Above & Beyond Fish_nl_NL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'pl_PL', 'Above & Beyond Fish_pl_PL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2009, 'zh_CN', 'Above & Beyond Fish_zh_CN')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'de_DE', 'Extra Effort Pencils_de_DE')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'en_GB', 'Extra Effort Pencils_en_GB')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'es_ES', 'Extra Effort Pencils_es_ES')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'es_MX', 'Extra Effort Pencils_es_MX')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'fr_CA', 'Extra Effort Pencils_fr_CA')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'fr_FR', 'Extra Effort Pencils_fr_FR')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'it_IT', 'Extra Effort Pencils_it_IT')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'ja_JP', 'Extra Effort Pencils_ja_JP')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'nl_NL', 'Extra Effort Pencils_nl_NL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'pl_PL', 'Extra Effort Pencils_pl_PL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2010, 'zh_CN', 'Extra Effort Pencils_zh_CN')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'de_DE', 'Birthday Fireworks_de_DE')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'en_GB', 'Birthday Fireworks_en_GB')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'es_ES', 'Birthday Fireworks_es_ES')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'es_MX', 'Birthday Fireworks_es_MX')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'fr_CA', 'Birthday Fireworks_fr_CA')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'fr_FR', 'Birthday Fireworks_fr_FR')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'it_IT', 'Birthday Fireworks_it_IT')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'ja_JP', 'Birthday Fireworks_ja_JP')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'nl_NL', 'Birthday Fireworks_nl_NL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'pl_PL', 'Birthday Fireworks_pl_PL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2011, 'zh_CN', 'Birthday Fireworks_zh_CN')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'de_DE', 'Innovation Chalkboard_de_DE')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'en_GB', 'Innovation Chalkboard_en_GB')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'es_ES', 'Innovation Chalkboard_es_ES')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'es_MX', 'Innovation Chalkboard_es_MX')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'fr_CA', 'Innovation Chalkboard_fr_CA')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'fr_FR', 'Innovation Chalkboard_fr_FR')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'it_IT', 'Innovation Chalkboard_it_IT')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'ja_JP', 'Innovation Chalkboard_ja_JP')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'nl_NL', 'Innovation Chalkboard_nl_NL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'pl_PL', 'Innovation Chalkboard_pl_PL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2012, 'zh_CN', 'Innovation Chalkboard_zh_CN')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'de_DE', 'Innovation Big Ideas_de_DE')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'en_GB', 'Innovation Big Ideas_en_GB')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'es_ES', 'Innovation Big Ideas_es_ES')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'es_MX', 'Innovation Big Ideas_es_MX')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'fr_CA', 'Innovation Big Ideas_fr_CA')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'fr_FR', 'Innovation Big Ideas_fr_FR')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'it_IT', 'Innovation Big Ideas_it_IT')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'ja_JP', 'Innovation Big Ideas_ja_JP')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'nl_NL', 'Innovation Big Ideas_nl_NL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'pl_PL', 'Innovation Big Ideas_pl_PL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2013, 'zh_CN', 'Innovation Big Ideas_zh_CN')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'de_DE', 'Leadership Balance_de_DE')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'en_GB', 'Leadership Balance_en_GB')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'es_ES', 'Leadership Balance_es_ES')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'es_MX', 'Leadership Balance_es_MX')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'fr_CA', 'Leadership Balance_fr_CA')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'fr_FR', 'Leadership Balance_fr_FR')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'it_IT', 'Leadership Balance_it_IT')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'ja_JP', 'Leadership Balance_ja_JP')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'nl_NL', 'Leadership Balance_nl_NL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'pl_PL', 'Leadership Balance_pl_PL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2014, 'zh_CN', 'Leadership Balance_zh_CN')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'de_DE', 'Teamwork chain_de_DE')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'en_GB', 'Teamwork chain_en_GB')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'es_ES', 'Teamwork chain_es_ES')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'es_MX', 'Teamwork chain_es_MX')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'fr_CA', 'Teamwork chain_fr_CA')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'fr_FR', 'Teamwork chain_fr_FR')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'it_IT', 'Teamwork chain_it_IT')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'ja_JP', 'Teamwork chain_ja_JP')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'nl_NL', 'Teamwork chain_nl_NL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'pl_PL', 'Teamwork chain_pl_PL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2015, 'zh_CN', 'Teamwork chain_zh_CN')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'de_DE', 'Thank You Meadow_de_DE')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'en_GB', 'Thank You Meadow_en_GB')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'es_ES', 'Thank You Meadow_es_ES')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'es_MX', 'Thank You Meadow_es_MX')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'fr_CA', 'Thank You Meadow_fr_CA')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'fr_FR', 'Thank You Meadow_fr_FR')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'it_IT', 'Thank You Meadow_it_IT')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'ja_JP', 'Thank You Meadow_ja_JP')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'nl_NL', 'Thank You Meadow_nl_NL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'pl_PL', 'Thank You Meadow_pl_PL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2016, 'zh_CN', 'Thank You Meadow_zh_CN')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'de_DE', 'Give Thanks_de_DE')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'en_GB', 'Give Thanks_en_GB')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'es_ES', 'Give Thanks_es_ES')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'es_MX', 'Give Thanks_es_MX')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'fr_CA', 'Give Thanks_fr_CA')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'fr_FR', 'Give Thanks_fr_FR')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'it_IT', 'Give Thanks_it_IT')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'ja_JP', 'Give Thanks_ja_JP')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'nl_NL', 'Give Thanks_nl_NL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'pl_PL', 'Give Thanks_pl_PL')
/
Insert into ECARD_LOCALE(card_id, locale, display_name ) values(2017, 'zh_CN', 'Give Thanks_zh_CN')
/
