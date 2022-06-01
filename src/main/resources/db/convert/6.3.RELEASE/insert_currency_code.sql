--liquibase formatted sql

--changeset palaniss:1
--comment Insert currency code ALL
INSERT INTO currency 
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000015','ALL','ALL','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ALL');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000015';

--changeset palaniss:2
--comment Insert currency code DZD
INSERT INTO currency 
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000016','DZD','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'DZD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000016';

--changeset palaniss:3
--comment Insert currency code AOA
INSERT INTO currency 
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000017','AOA','Kz','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'AOA');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000017';

--changeset palaniss:4
--comment Insert currency code XCD
INSERT INTO currency 
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000018','XCD','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'XCD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000018';

--changeset palaniss:5
--comment Insert currency code ARS
INSERT INTO currency 
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000019','ARS','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ARS');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000019';

--changeset palaniss:6
--comment Insert currency code AWG
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000020','AWG','ƒ','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'AWG');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000020';

--changeset palaniss:7
--comment Insert currency code AZN
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000021','AZN','m','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'AZN');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000021';

--changeset palaniss:8
--comment Insert currency code BSD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000022','BSD','B$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BSD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000022';

--changeset palaniss:9
--comment Insert currency code BDT
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000023','BDT','৳','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BDT');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000023';

--changeset palaniss:10
--comment Insert currency code BBD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000024','BBD','Bds$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BBD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000024';

--changeset palaniss:11
--comment Insert currency code BYR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000025','BYR','BR','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BYR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000025';

--changeset palaniss:12
--comment Insert currency code BZD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000026','BZD','BZ$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BZD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000026';

--changeset palaniss:13
--comment Insert currency code BMD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000027','BMD','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BMD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000027';

--changeset palaniss:14
--comment Insert currency code BTN
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000028','BTN','Nu.','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BTN');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000028';

--changeset palaniss:15
--comment Insert currency code BOB
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000029','BOB','Bs','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BOB');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000029';

--changeset palaniss:16
--comment Insert currency code BAM
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000030','BAM','Nu.','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BAM');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000030';

--changeset palaniss:17
--comment Insert currency code BWP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000031','BWP','P','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BWP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000031';

--changeset palaniss:18
--comment Insert currency code NOK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000032','NOK','kr','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'NOK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000032';

--changeset palaniss:19
--comment Insert currency code BND
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000033','BND','B$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BND');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000033';

--changeset palaniss:20
--comment Insert currency code BGN
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000034','BGN','лв.','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BGN');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000034';

--changeset palaniss:21
--comment Insert currency code XOF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000035','XOF','CFA','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'XOF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000035';

--changeset palaniss:22
--comment Insert currency code BIF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000036','BIF','FBu','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'BIF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000036';

--changeset palaniss:23
--comment Insert currency code CVE
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000037','CVE','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'CVE');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000037';

--changeset palaniss:24
--comment Insert currency code KHR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000038','KHR','៛','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'KHR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000038';

--changeset palaniss:25
--comment Insert currency code XAF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000039','XAF','FCFA','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'XAF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000039';

--changeset palaniss:26
--comment Insert currency code KYD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000040','KYD','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'KYD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000040';

--changeset palaniss:27
--comment Insert currency code CLP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000041','CLP','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'CLP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000041';

--changeset palaniss:28
--comment Insert currency code KMF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000042','KMF','CF','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'KMF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000042';

--changeset palaniss:29
--comment Insert currency code CDF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000043','CDF','FC','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'CDF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000043';

--changeset palaniss:30
--comment Insert currency code NZD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000044','NZD','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'NZD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000044';

--changeset palaniss:31
--comment Insert currency code CRC
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000045','CRC','₡','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'CRC');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000045';

--changeset palaniss:32
--comment Insert currency code HRK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000046','HRK','₡','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'HRK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000046';

--changeset palaniss:33
--comment Insert currency code CUP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000047','CUP','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'CUP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000047';

--changeset palaniss:34
--comment Insert currency code ANG
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000048','ANG','NAƒ','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ANG');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000048';

--changeset palaniss:35
--comment Insert currency code DKK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000049','DKK','kr.','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'DKK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000049';

--changeset palaniss:36
--comment Insert currency code DJF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000050','DJF','Fdj','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'DJF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000050';

--changeset palaniss:37
--comment Insert currency code DOP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000051','DOP','RD$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'DOP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000051';

--changeset palaniss:38
--comment Insert currency code EGP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000052','EGP','E£','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'EGP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000052';

--changeset palaniss:39
--comment Insert currency code SVC
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000053','SVC','₡','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'SVC');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000053';

--changeset palaniss:40
--comment Insert currency code ERN
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000054','ERN','Nfk','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ERN');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000054';

--changeset palaniss:41
--comment Insert currency code ETB
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000055','ETB','Br','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ETB');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000055';

--changeset palaniss:42
--comment Insert currency code FKP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000056','FKP','£','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'FKP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000056';

--changeset palaniss:43
--comment Insert currency code FJD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000057','FJD','FJ$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'FJD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000057';

--changeset palaniss:44
--comment Insert currency code XPF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000058','XPF','₣','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'XPF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000058';

--changeset palaniss:46
--comment Insert currency code GMD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000060','GMD','D','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'GMD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000060';

--changeset palaniss:47
--comment Insert currency code GEL
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000061','GEL','ლ','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'GEL');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000061';

--changeset palaniss:48
--comment Insert currency code GHS
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000062','GHS','GH₵','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'GHS');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000062';

--changeset palaniss:49
--comment Insert currency code GIP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000063','GIP','£','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'GIP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000063';

--changeset palaniss:50
--comment Insert currency code GTQ
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000064','GTQ','Q','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'GTQ');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000064';

--changeset palaniss:51
--comment Insert currency code GNF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000065','GNF','Q','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'GNF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000065';

--changeset palaniss:52
--comment Insert currency code GYD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000066','GYD','Q','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'GYD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000066';

--changeset palaniss:53
--comment Insert currency code HNL
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000067','HNL','L','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'HNL');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000067';

--changeset palaniss:54
--comment Insert currency code HUF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000068','HUF','Ft','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'HUF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000068';

--changeset palaniss:55
--comment Insert currency code ISK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000069','ISK','kr','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ISK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000069';

--changeset palaniss:56
--comment Insert currency code IDR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000070','IDR','Rp','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'IDR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000070';

--changeset palaniss:57
--comment Insert currency code CZK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000071','CZK','Kč','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'CZK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000071';

--changeset palaniss:58
--comment Insert currency code ILS
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000072','ILS','₪','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ILS');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000072';

--changeset palaniss:59
--comment Insert currency code JMD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000073','JMD','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'JMD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000073';

--changeset palaniss:60
--comment Insert currency code KZT
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000074','KZT','₸','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'KZT');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000074';

--changeset palaniss:61
--comment Insert currency code KES
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000075','KES','KSh','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'KES');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000075';

--changeset palaniss:62
--comment Insert currency code KRW
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000076','KRW','₩','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'KRW');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000076';

--changeset palaniss:63
--comment Insert currency code LAK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000077','LAK','₭','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'LAK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000077';

--changeset palaniss:64
--comment Insert currency code LRD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000078','LRD','L$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'LRD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000078';

--changeset palaniss:65
--comment Insert currency code LYD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000079','LYD','LD','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'LYD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000079';

--changeset siemback:65.5
--comment udpate the column size to support more than 5 characters
alter table CURRENCY MODIFY CURRENCY_SYMBOL varchar2(10);
--rollback alter table CURRENCY MODIFY CURRENCY_SYMBOL varchar2(5);

--changeset palaniss:66
--comment Insert currency code MKD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000080','MKD','ден','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MKD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000080';

--changeset palaniss:67
--comment Insert currency code MWK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000081','MWK','MK','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MWK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000081';

--changeset palaniss:68
--comment Insert currency code MYR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000082','MYR','RM','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MYR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000082';

--changeset palaniss:69
--comment Insert currency code MVR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000083','MVR','Rf','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MVR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000083';

--changeset palaniss:70
--comment Insert currency code MRO
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000084','MRO','UM','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MRO');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000084';

--changeset palaniss:71
--comment Insert currency code MUR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000085','MUR','Rs','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MUR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000085';

--changeset palaniss:72
--comment Insert currency code MNT
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000086','MNT','₮','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MNT');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000086';

--changeset palaniss:73
--comment Insert currency code MAD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000087','MAD','MAD','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MAD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000087';

--changeset palaniss:74
--comment Insert currency code MZN
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000088','MZN','MT','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MZN');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000088';

--changeset palaniss:75
--comment Insert currency code MMK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000089','MMK','K','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MMK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000089';

--changeset palaniss:76
--comment Insert currency code NAD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000090','NAD','N$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'NAD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000090';

--changeset palaniss:77
--comment Insert currency code NPR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000091','NPR','Rs','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'NPR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000091'

--changeset palaniss:78
--comment Insert currency code NIO
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000092','NIO','C$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'NIO');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000092';

--changeset palaniss:79
--comment Insert currency code NGN
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000093','NGN','₦','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'NGN');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000093';

--changeset palaniss:80
--comment Insert currency code PKR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000094','PKR','Rs','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'PKR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000094';

--changeset palaniss:81
--comment Insert currency code PAB
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000095','PAB','B/.','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'PAB');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000095';

--changeset palaniss:82
--comment Insert currency code PGK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000096','PGK','K','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'PGK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000096';

--changeset palaniss:83
--comment Insert currency code PYG
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000097','PYG','Gs','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'PYG');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000097';

--changeset palaniss:84
--comment Insert currency code PEN
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000098','PEN','S/.','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'PEN');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000098';

--changeset palaniss:85
--comment Insert currency code PHP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000099','PHP','₱','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'PHP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000099';

--changeset palaniss:86
--comment Insert currency code PLN
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000100','PLN','zł','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'PLN');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000100';

--changeset palaniss:87
--comment Insert currency code RON
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000101','RON','lei','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'RON');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000101';

--changeset palaniss:88
--comment Insert currency code RUB
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000102','RUB','py6','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'RUB');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000102';

--changeset palaniss:89
--comment Insert currency code RWF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000103','RWF','FRw','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'RWF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000103';

--changeset palaniss:90
--comment Insert currency code RSD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000104','RSD','FRw','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'RSD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000104';

--changeset palaniss:91
--comment Insert currency code SCR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000105','SCR','Rs','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'SCR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000105';

--changeset palaniss:92
--comment Insert currency code SBD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000106','SBD','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'SBD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000106';

--changeset palaniss:93
--comment Insert currency code SOS
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000107','SOS','S','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'SOS');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000107';

--changeset palaniss:94
--comment Insert currency code ZAR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000108','ZAR','R','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ZAR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000108';

--changeset palaniss:95
--comment Insert currency code LKR
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000109','LKR','Rs','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'LKR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000109';

--changeset palaniss:96
--comment Insert currency code SRD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000110','SRD','$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'SRD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000110';

--changeset palaniss:97
--comment Insert currency code SEK
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000111','SEK','kr','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'SEK');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000111';

--changeset palaniss:98
--comment Insert currency code SYP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000112','SYP','£','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'SYP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000112';

--changeset palaniss:99
--comment Insert currency code TWD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000113','TWD','NT$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'TWD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000113';

--changeset palaniss:100
--comment Insert currency code TOP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000114','TOP','T$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'TOP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000114';

--changeset palaniss:101
--comment Insert currency code TTD
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000115','TTD','TT$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'TTD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000115';

--changeset palaniss:102
--comment Insert currency code TRY
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000116','TRY','₺','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'TRY');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000116';

--changeset palaniss:103
--comment Insert currency code TMT
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000117','TMT','T','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'TMT');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000117';

--changeset palaniss:104
--comment Insert currency code UGX
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000118','UGX','USh','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'UGX');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000118';

--changeset palaniss:105
--comment Insert currency code UAH
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000119','UAH','₴','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'UAH');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000119';

--changeset palaniss:106
--comment Insert currency code UYU
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000120','UYU','$U','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'UYU');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000120';

--changeset palaniss:107
--comment Insert currency code UZS
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000121','UZS','лв','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'UZS');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000121';

--changeset palaniss:108
--comment Insert currency code VUV
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000122','VUV','VT','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'VUV');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000122';

--changeset palaniss:109
--comment Insert currency code VEF
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000123','VEF','Bs','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'VEF');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000123';

--changeset palaniss:110
--comment Insert currency code ZMW
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
select currency_pk_sq.nextval,'currency.currencyName.10000124','ZMW','ZK','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ZMW');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000124';

--changeset palaniss:111
--comment Insert currency code ZWL
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000125','ZWL','Z$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'ZWL');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000125';
