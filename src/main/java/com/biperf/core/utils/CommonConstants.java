
package com.biperf.core.utils;

public interface CommonConstants
{

  /**
   * Program States
   */
  String PROGRAM_STATE_ACTIVE = "ACTIVE";
  String PROGRAM_STATE_INACTIVE = "IN_ACTIVE";

  /**
   * Program status 
   */

  String PROGRAM_STATUS_LIVE = "live";
  String PROGRAM_STATUS_EXPIRED = "expired";

  /**
   * Award type 
   */

  String MERCHANDISE_AWARD_TYPE = "merchandise";
  String GIFT_CODE_AWARD_TYPE = "Gift Code";

  /**
   * Program Types
   */
  String PROGRAM_TYPE_SERVICE_ANIVERSARY = "recognition";

  /** System property name for the AWS access key ID */
  String ACCESS_KEY_SYSTEM_PROPERTY = "aws.accessKeyId";

  /** System property name for the AWS secret key */
  String SECRET_KEY_SYSTEM_PROPERTY = "aws.secretKey";

  /**
   * JWT Token Authorization request header key
   */
  String JWT_AUTH_HEADER = "Authorization";

  /*
   * Error Code
   */
  String ERROR_CODE_JWT_TOKEN_EXPIRED = "JWT_TOKEN_EXPIRED";

  String COMPANY_ID = "company-id";

  String INVALID_TOKEN = "OOPs !. Invalid Company ID !!";

}
