CREATE OR REPLACE FUNCTION fnc_java_get_date_pattern (p_in_locale IN VARCHAR2)
 RETURN VARCHAR2 IS
 
/*******************************************************************************

-- Purpose: Function that returns date format in varchar for a input locale 
            (locale example: 'en_US' or 'en_GB' or 'de_DE' or 'fr_CA' etc)

-- Modification history

-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- Arun S      08/23/2011  Initial Creation
-- Avinash G   07/10/2012  Fix to bug #41245
  
*******************************************************************************/
 
 pattern varchar2(100);
 
BEGIN
 
	SELECT
		CASE p_in_locale
			WHEN 'ja_JP' THEN 'YYYY/MM/DD'
			WHEN 'es_PE' THEN 'DD/MM/YYYY'
			WHEN 'en' THEN 'MM/DD/YYYY'
			WHEN 'ja_JP_JP' THEN 'YYYY/MM/DD'
			WHEN 'es_PA' THEN 'MM/DD/YYYY'
			WHEN 'sr_BA' THEN 'YYYY-MM-DD'
			WHEN 'mk' THEN 'DD.MM.YYYY'
			WHEN 'es_GT' THEN 'DD/MM/YYYY'
			WHEN 'ar_AE' THEN 'DD/MM/YYYY'
			WHEN 'no_NO' THEN 'DD.MM.YYYY'
			WHEN 'sq_AL' THEN 'YYYY-MM-DD'
			WHEN 'bg' THEN 'YYYY-MM-DD'
			WHEN 'ar_IQ' THEN 'DD/MM/YYYY'
			WHEN 'ar_YE' THEN 'DD/MM/YYYY'
			WHEN 'hu' THEN 'YYYY.MM.DD.'
			WHEN 'pt_PT' THEN 'DD-MM-YYYY'
			WHEN 'el_CY' THEN 'DD/MM/YYYY'
			WHEN 'ar_QA' THEN 'DD/MM/YYYY'
			WHEN 'mk_MK' THEN 'DD.MM.YYYY'
			WHEN 'sv' THEN 'YYYY-MM-DD'
			WHEN 'de_CH' THEN 'DD.MM.YYYY'
			WHEN 'en_US' THEN 'MM/DD/YYYY'
			WHEN 'fi_FI' THEN 'DD.MM.YYYY'
			WHEN 'is' THEN 'DD.MM.YYYY'
			WHEN 'cs' THEN 'DD.MM.YYYY'
			WHEN 'en_MT' THEN 'DD/MM/YYYY'
			WHEN 'sl_SI' THEN 'DD.MM.YYYY'
			WHEN 'sk_SK' THEN 'DD.MM.YYYY'
			WHEN 'it' THEN 'DD/MM/YYYY'
			WHEN 'tr_TR' THEN 'DD.MM.YYYY'
			WHEN 'zh' THEN 'YYYY-MM-DD'
			WHEN 'th' THEN 'DD/MM/YYYY'
			WHEN 'ar_SA' THEN 'DD/MM/YYYY'
			WHEN 'no' THEN 'DD.MM.YYYY'
			WHEN 'en_GB' THEN 'DD/MM/YYYY'
			WHEN 'sr_CS' THEN 'DD.MM.YYYY.'
			WHEN 'lt' THEN 'YYYY.MM.DD'
			WHEN 'ro' THEN 'DD.MM.YYYY'
			WHEN 'en_NZ' THEN 'DD/MM/YYYY'
			WHEN 'no_NO_NY' THEN 'DD.MM.YYYY'
			WHEN 'lt_LT' THEN 'YYYY.MM.DD'
			WHEN 'es_NI' THEN 'MM-DD-YYYY'
			WHEN 'nl' THEN 'DD-MM-YYYY'
			WHEN 'ga_IE' THEN 'DD/MM/YYYY'
			WHEN 'fr_BE' THEN 'DD/MM/YYYY'
			WHEN 'es_ES' THEN 'DD/MM/YYYY'
			WHEN 'ar_LB' THEN 'DD/MM/YYYY'
			WHEN 'ko' THEN 'YYYY. MM. DD'
			WHEN 'fr_CA' THEN 'YYYY-MM-DD'
			WHEN 'et_EE' THEN 'DD.MM.YYYY'
			WHEN 'ar_KW' THEN 'DD/MM/YYYY'
			WHEN 'sr_RS' THEN 'DD.MM.YYYY.'
			WHEN 'es_US' THEN 'MM/DD/YYYY'
			WHEN 'es_MX' THEN 'DD/MM/YYYY'
			WHEN 'ar_SD' THEN 'DD/MM/YYYY'
			WHEN 'in_ID' THEN 'DD/MM/YYYY'
			WHEN 'ru' THEN 'DD.MM.YYYY'
			WHEN 'lv' THEN 'YYYY.DD.MM'
			WHEN 'es_UY' THEN 'DD/MM/YYYY'
			WHEN 'lv_LV' THEN 'YYYY.DD.MM'
			WHEN 'iw' THEN 'DD/MM/YYYY'
			WHEN 'pt_BR' THEN 'DD/MM/YYYY'
			WHEN 'ar_SY' THEN 'DD/MM/YYYY'
			WHEN 'hr' THEN 'YYYY.MM.DD'
			WHEN 'et' THEN 'DD.MM.YYYY'
			WHEN 'es_DO' THEN 'MM/DD/YYYY'
			WHEN 'fr_CH' THEN 'DD.MM.YYYY'
			WHEN 'hi_IN' THEN 'DD/MM/YYYY'
			WHEN 'es_VE' THEN 'DD/MM/YYYY'
			WHEN 'ar_BH' THEN 'DD/MM/YYYY'
			WHEN 'en_PH' THEN 'MM/DD/YYYY'
			WHEN 'ar_TN' THEN 'DD/MM/YYYY'
			WHEN 'fi' THEN 'DD.MM.YYYY'
			WHEN 'de_AT' THEN 'DD.MM.YYYY'
			WHEN 'es' THEN 'DD/MM/YYYY'
			WHEN 'nl_NL' THEN 'DD-MM-YYYY'
			WHEN 'es_EC' THEN 'DD/MM/YYYY'
			WHEN 'zh_TW' THEN 'YYYY/MM/DD'
			WHEN 'ar_JO' THEN 'DD/MM/YYYY'
			WHEN 'be' THEN 'DD.MM.YYYY'
			WHEN 'is_IS' THEN 'DD.MM.YYYY'
			WHEN 'es_CO' THEN 'DD/MM/YYYY'
			WHEN 'es_CR' THEN 'DD/MM/YYYY'
			WHEN 'es_CL' THEN 'DD-MM-YYYY'
			WHEN 'ar_EG' THEN 'DD/MM/YYYY'
			WHEN 'en_ZA' THEN 'YYYY/MM/DD'
			WHEN 'th_TH' THEN 'DD/MM/YYYY'
			WHEN 'el_GR' THEN 'DD/MM/YYYY'
			WHEN 'it_IT' THEN 'DD/MM/YYYY'
			WHEN 'ca' THEN 'DD/MM/YYYY'
			WHEN 'hu_HU' THEN 'YYYY.MM.DD.'
			WHEN 'fr' THEN 'DD/MM/YYYY'
			WHEN 'en_IE' THEN 'DD/MM/YYYY'
			WHEN 'uk_UA' THEN 'DD.MM.YYYY'
			WHEN 'pl_PL' THEN 'DD.MM.YYYY'
			WHEN 'fr_LU' THEN 'DD/MM/YYYY'
			WHEN 'nl_BE' THEN 'DD/MM/YYYY'
			WHEN 'en_IN' THEN 'DD/MM/YYYY'
			WHEN 'ca_ES' THEN 'DD/MM/YYYY'
			WHEN 'ar_MA' THEN 'DD/MM/YYYY'
			WHEN 'es_BO' THEN 'DD-MM-YYYY'
			ELSE
				CASE p_in_locale
					WHEN 'en_AU' THEN 'DD/MM/YYYY'
					WHEN 'sr' THEN 'DD.MM.YYYY.'
					WHEN 'zh_SG' THEN 'DD/MM/YYYY'
					WHEN 'pt' THEN 'DD-MM-YYYY'
					WHEN 'uk' THEN 'DD.MM.YYYY'
					WHEN 'es_SV' THEN 'MM-DD-YYYY'
					WHEN 'ru_RU' THEN 'DD.MM.YYYY'
					WHEN 'ko_KR' THEN 'YYYY. MM. DD'
					WHEN 'vi' THEN 'DD/MM/YYYY'
					WHEN 'ar_DZ' THEN 'DD/MM/YYYY'
					WHEN 'vi_VN' THEN 'DD/MM/YYYY'
					WHEN 'sr_ME' THEN 'DD.MM.YYYY.'
					WHEN 'sq' THEN 'YYYY-MM-DD'
					WHEN 'ar_LY' THEN 'DD/MM/YYYY'
					WHEN 'ar' THEN 'DD/MM/YYYY'
					WHEN 'zh_CN' THEN 'YYYY-MM-DD'
					WHEN 'be_BY' THEN 'DD.MM.YYYY'
					WHEN 'zh_HK' THEN 'YYYY-MM-DD'
					WHEN 'ja' THEN 'YYYY/MM/DD'
					WHEN 'iw_IL' THEN 'DD/MM/YYYY'
					WHEN 'bg_BG' THEN 'YYYY-MM-DD'
					WHEN 'in' THEN 'YYYY/MM/DD'
					WHEN 'mt_MT' THEN 'DD/MM/YYYY'
					WHEN 'es_PY' THEN 'DD/MM/YYYY'
					WHEN 'sl' THEN 'DD.MM.YYYY'
					WHEN 'fr_FR' THEN 'DD/MM/YYYY'
					WHEN 'cs_CZ' THEN 'DD.MM.YYYY'
					WHEN 'it_CH' THEN 'DD.MM.YYYY'
					WHEN 'ro_RO' THEN 'DD.MM.YYYY'
					WHEN 'es_PR' THEN 'MM-DD-YYYY'
					WHEN 'en_CA' THEN 'DD/MM/YYYY'
					WHEN 'de_DE' THEN 'DD.MM.YYYY'
					WHEN 'ga' THEN 'YYYY/MM/DD'
					WHEN 'de_LU' THEN 'DD.MM.YYYY'
					WHEN 'de' THEN 'DD.MM.YYYY'
					WHEN 'es_AR' THEN 'DD/MM/YYYY'
					WHEN 'sk' THEN 'DD.MM.YYYY'
					WHEN 'ms_MY' THEN 'DD/MM/YYYY'
					WHEN 'hr_HR' THEN 'DD.MM.YYYY.'
					WHEN 'en_SG' THEN 'MM/DD/YYYY'
					WHEN 'da' THEN 'DD-MM-YYYY'
					WHEN 'mt' THEN 'DD/MM/YYYY'
					WHEN 'pl' THEN 'YYYY-MM-DD'
					WHEN 'ar_OM' THEN 'DD/MM/YYYY'
					WHEN 'tr' THEN 'DD.MM.YYYY'
					WHEN 'th_TH_TH' THEN 'DD/MM/YYYY'
					WHEN 'el' THEN 'DD/MM/YYYY'
					WHEN 'ms' THEN 'YYYY/MM/DD'
					WHEN 'sv_SE' THEN 'YYYY-MM-DD'
					WHEN 'da_DK' THEN 'DD-MM-YYYY'
					WHEN 'es_HN' THEN 'MM-DD-YYYY'
					ELSE 'MM/DD/YYYY'
				END
		END
	INTO pattern
	FROM dual;

	RETURN pattern;

EXCEPTION 
  WHEN OTHERS THEN
    pattern := 'MM/DD/YYYY';
    RETURN pattern;  
END;
/
