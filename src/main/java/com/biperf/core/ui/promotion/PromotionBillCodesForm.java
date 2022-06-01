
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBillCode;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PromotionBillCodesForm extends BaseForm
{
  public static final String SESSION_KEY = "promotionBillCodesForm";
  public static final String DEPT_NAME = "department";
  public static final String ORG_UNIT_NAME = "orgUnitName";
  public static final String COUNTRY_CODE = "countryCode";
  public static final String LOGIN_ID = "userName";

  private String promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;

  private boolean billCodesActive;

  private String billCode1;
  private String customValue1;
  private String billCode2;
  private String customValue2;
  private String billCode3;
  private String customValue3;
  private String billCode4;
  private String customValue4;
  private String billCode5;
  private String customValue5;
  private String billCode6;
  private String customValue6;
  private String billCode7;
  private String customValue7;
  private String billCode8;
  private String customValue8;
  private String billCode9;
  private String customValue9;
  private String billCode10;
  private String customValue10;
  private String trackBillCodeBy1;
  private String trackBillCodeBy2;
  private String trackBillCodeBy3;
  private String trackBillCodeBy4;
  private String trackBillCodeBy5;
  private String trackBillCodeBy6;
  private String trackBillCodeBy7;
  private String trackBillCodeBy8;
  private String trackBillCodeBy9;
  private String trackBillCodeBy10;

  private String method;

  private boolean awardsActive = true;
  private boolean plateauAward = false;

  public static final String CUSTOM_VALUE = "customValue";
  public static final String USER_NAME = "userName";

  public void load( Promotion promotion )
  {
    this.promotionId = promotion.getId().toString();
    this.promotionName = promotion.getName();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    if ( promotion.getAwardType() != null )
    {
      this.plateauAward = promotion.getAwardType().isMerchandiseAwardType();
    }
    this.billCodesActive = promotion.isBillCodesActive();

    List<PromotionBillCode> promoBillCodes = null;

    if ( this.billCodesActive )
    {
      promoBillCodes = new ArrayList<PromotionBillCode>();

      for ( PromotionBillCode promoBillCode : promotion.getPromotionBillCodes() )
      {
        promoBillCodes.add( promoBillCode );
      }
      loadPromotionBillCodes( promoBillCodes );
    }

    if ( promotion.isRecognitionPromotion() )
    {
      if ( ( (RecognitionPromotion)promotion ).getAwardType().isPointsAwardType() && ! ( (RecognitionPromotion)promotion ).isAwardActive() )
      {
        this.awardsActive = false;
      }
      if ( ( (RecognitionPromotion)promotion ).getAwardType().isMerchandiseAwardType() )
      {
        this.awardsActive = false;
      }

      if ( !awardsActive )
      {
        if ( ( (RecognitionPromotion)promotion ).isAllowPublicRecognitionPoints()
            || ( promotion.isSweepstakesActive() && ! ( (RecognitionPromotion)promotion ).getAwardType().isMerchandiseAwardType() )
            || ( ( (RecognitionPromotion)promotion ).getAwardType().isMerchandiseAwardType() ) ) // WIP#
                                                                                                 // 25127
        {
          this.awardsActive = true;
        }
      }

      if ( ( (RecognitionPromotion)promotion ).getAwardType().isMerchandiseAwardType() )
      {
        this.plateauAward = true;
      }
    }
    else if ( promotion.isNominationPromotion() && ! ( (NominationPromotion)promotion ).isAwardActive() && !promotion.isSweepstakesActive() )
    {
      this.awardsActive = false;
    }
    // Disable bill codes for SSI if points is not allowed in awards.
    else if ( promotion.isSSIPromotion() && ! ( (SSIPromotion)promotion ).getAllowAwardPoints() )
    {
      this.awardsActive = false;
    }
    else if ( promotion.isQuizPromotion() && ! ( (QuizPromotion)promotion ).isAwardActive() && !promotion.isSweepstakesActive() )
    {
      this.awardsActive = false;
    }
    else if ( promotion.isGoalQuestPromotion() )
    {
      if ( ( (GoalQuestPromotion)promotion ).getAwardType().isMerchandiseAwardType() && ( (GoalQuestPromotion)promotion ).getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE ) )
      {
        this.awardsActive = false;
      }

      if ( ( (GoalQuestPromotion)promotion ).getAwardType().isMerchandiseAwardType() )
      {
        this.plateauAward = true;
      }
    }
    else if ( promotion.isChallengePointPromotion() )
    {
      if ( ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().isMerchTravel()
          && ( (ChallengePointPromotion)promotion ).getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE ) )
      {
        this.awardsActive = false;
      }

      if ( ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().isMerchTravel() )
      {
        this.plateauAward = true;
      }
    }
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    //
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {

    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( !this.billCodesActive )
    {
      return errors;
    }

    List<String> billCodes = Arrays.asList( this.billCode1,
                                            this.billCode2,
                                            this.billCode3,
                                            this.billCode4,
                                            this.billCode5,
                                            this.billCode6,
                                            this.billCode7,
                                            this.billCode8,
                                            this.billCode9,
                                            this.billCode10 );

    // Filters the stream of bill codes for null/empty values. If the count
    // is 0, that means no bill
    // codes selected and validation error added.
    if ( billCodes.stream().filter( billCode -> !StringUtils.isEmpty( billCode ) ).count() == 0 )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.bill.code.NO_BILL_CODES" ) );
    }
    else
    {
      // Filters the stream of bill codes for userName. If the count is 0,
      // that means no bill
      // codes selected has userName (Login ID) and validation error
      // added.
      // Checks if the promotion is recognition/goalquest and if award
      // type is plateau
      if ( ( promotionTypeCode.equals( PromotionType.RECOGNITION ) || promotionTypeCode.equals( PromotionType.GOALQUEST ) ) && this.isPlateauAward()
          && billCodes.stream().filter( billCode -> !StringUtils.isEmpty( billCode ) && billCode.equalsIgnoreCase( USER_NAME ) ).count() == 0 )
      {
        errors.add( "promotionBillCodes", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.bill.code.LOGINID_REQ" ) ) );
      }

      // Checks if custom bill code is selected but value is empty
      if ( checkCustomBillCodeMissing( this.billCode1, this.customValue1 ) || checkCustomBillCodeMissing( this.billCode2, this.customValue2 )
          || checkCustomBillCodeMissing( this.billCode3, this.customValue3 ) || checkCustomBillCodeMissing( this.billCode4, this.customValue4 )
          || checkCustomBillCodeMissing( this.billCode5, this.customValue5 ) || checkCustomBillCodeMissing( this.billCode6, this.customValue6 )
          || checkCustomBillCodeMissing( this.billCode7, this.customValue7 ) || checkCustomBillCodeMissing( this.billCode8, this.customValue8 )
          || checkCustomBillCodeMissing( this.billCode9, this.customValue9 ) || checkCustomBillCodeMissing( this.billCode10, this.customValue10 ) )
      {
        errors.add( "promotionBillCodes", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.bill.code.CUSTOM_BILL_CODE" ) ) );
      }

      // For Nomination, Recognition and SSI, track bills by is required
      // for the bill codes selected
      if ( ( promotionTypeCode.equals( PromotionType.NOMINATION ) || promotionTypeCode.equals( PromotionType.RECOGNITION ) || promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES ) )
          && ( checkTrackBillsByMissing( this.billCode1, this.trackBillCodeBy1 ) || checkTrackBillsByMissing( this.billCode2, this.trackBillCodeBy2 )
              || checkTrackBillsByMissing( this.billCode3, this.trackBillCodeBy3 ) || checkTrackBillsByMissing( this.billCode4, this.trackBillCodeBy4 )
              || checkTrackBillsByMissing( this.billCode5, this.trackBillCodeBy5 ) || checkTrackBillsByMissing( this.billCode6, this.trackBillCodeBy6 )
              || checkTrackBillsByMissing( this.billCode7, this.trackBillCodeBy7 ) || checkTrackBillsByMissing( this.billCode8, this.trackBillCodeBy8 )
              || checkTrackBillsByMissing( this.billCode9, this.trackBillCodeBy9 ) || checkTrackBillsByMissing( this.billCode10, this.trackBillCodeBy10 ) ) )
      {
        errors.add( "promotionBillCodes", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.bill.code.TRACKBILLCODE_REQ" ) ) );
      }
    }

    // Validate max length of each custom input field
    validateCustomValue( errors, customValue1, "promotion.bill.code.BILL_CODE_1" );
    validateCustomValue( errors, customValue2, "promotion.bill.code.BILL_CODE_2" );
    validateCustomValue( errors, customValue3, "promotion.bill.code.BILL_CODE_3" );
    validateCustomValue( errors, customValue4, "promotion.bill.code.BILL_CODE_4" );
    validateCustomValue( errors, customValue5, "promotion.bill.code.BILL_CODE_5" );
    validateCustomValue( errors, customValue6, "promotion.bill.code.BILL_CODE_6" );
    validateCustomValue( errors, customValue7, "promotion.bill.code.BILL_CODE_7" );
    validateCustomValue( errors, customValue8, "promotion.bill.code.BILL_CODE_8" );
    validateCustomValue( errors, customValue9, "promotion.bill.code.BILL_CODE_9" );
    validateCustomValue( errors, customValue10, "promotion.bill.code.BILL_CODE_10" );

    return errors;
  }

  private boolean checkCustomBillCodeMissing( String billCode, String customValue )
  {
    return !StringUtils.isEmpty( billCode ) && billCode.equalsIgnoreCase( CUSTOM_VALUE ) && StringUtils.isEmpty( customValue );
  }

  private boolean checkTrackBillsByMissing( String billCode, String trackBillsBy )
  {
    return !StringUtils.isEmpty( billCode ) && StringUtils.isEmpty( trackBillsBy );
  }

  /**
   * Validate a custom value for maximum length. Adds error message if longer
   * than maximum length.
   * 
   * @param errors
   * @param value
   *            customValue variable
   * @param billCodeKey
   *            CMS key for associated bill code
   */
  private void validateCustomValue( ActionErrors errors, String value, String billCodeKey )
  {
    int CUSTOM_INPUT_LENGTH = 25;

    if ( value != null && value.length() > CUSTOM_INPUT_LENGTH )
    {
      errors.add( "promotionBillCodeCustomValue",
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_MAXLENGTH,
                                     CmsResourceBundle.getCmsBundle().getString( billCodeKey ) + ": " + CmsResourceBundle.getCmsBundle().getString( "promotion.bill.code.CUSTOM_VALUE" ),
                                     String.valueOf( CUSTOM_INPUT_LENGTH ) ) );
    }
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be
   * synchronized with a looked up promotion object in the service
   * 
   * @return Promotion
   */
  public Promotion toDomainObject( Promotion promotion )
  {
    promotion.setBillCodesActive( this.billCodesActive );
    if ( this.billCodesActive )
    {
      promotion.setPromotionBillCodes( getPromoBillCodeList( promotion ) );
    }

    return promotion;
  }

  private List<PromotionBillCode> getPromoBillCodeList( Promotion promotion )
  {
    List<PromotionBillCode> promoBillCodes = new ArrayList<PromotionBillCode>();
    if ( this.billCode1 != null && !this.billCode1.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 0 ), this.billCode1, this.customValue1, this.trackBillCodeBy1 ) );
    }
    if ( this.billCode2 != null && !this.billCode2.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 1 ), this.billCode2, this.customValue2, this.trackBillCodeBy2 ) );
    }
    if ( this.billCode3 != null && !this.billCode3.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 2 ), this.billCode3, this.customValue3, this.trackBillCodeBy3 ) );
    }
    if ( this.billCode4 != null && !this.billCode4.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 3 ), this.billCode4, this.customValue4, this.trackBillCodeBy4 ) );
    }
    if ( this.billCode5 != null && !this.billCode5.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 4 ), this.billCode5, this.customValue5, this.trackBillCodeBy5 ) );
    }
    if ( this.billCode6 != null && !this.billCode6.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 5 ), this.billCode6, this.customValue6, this.trackBillCodeBy6 ) );
    }
    if ( this.billCode7 != null && !this.billCode7.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 6 ), this.billCode7, this.customValue7, this.trackBillCodeBy7 ) );
    }
    if ( this.billCode8 != null && !this.billCode8.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 7 ), this.billCode8, this.customValue8, this.trackBillCodeBy8 ) );
    }
    if ( this.billCode9 != null && !this.billCode9.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 8 ), this.billCode9, this.customValue9, this.trackBillCodeBy9 ) );
    }
    if ( this.billCode10 != null && !this.billCode10.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 9 ), this.billCode10, this.customValue10, this.trackBillCodeBy10 ) );
    }
    return promoBillCodes;
  }

  private void loadPromotionBillCodes( List<PromotionBillCode> promotionBillCodes )
  {
    if ( promotionBillCodes != null && promotionBillCodes.size() > 0 )
    {
      Iterator promotionBillCodesList = promotionBillCodes.iterator();
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode1 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode1 != null )
        {
          this.billCode1 = promotionBillCode1.getBillCode();
          this.customValue1 = promotionBillCode1.getCustomValue();
          this.trackBillCodeBy1 = promotionBillCode1.getTrackBillCodeBy();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode2 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode2 != null )
        {
          this.billCode2 = promotionBillCode2.getBillCode();
          this.customValue2 = promotionBillCode2.getCustomValue();
          this.trackBillCodeBy2 = promotionBillCode2.getTrackBillCodeBy();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode3 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode3 != null )
        {
          this.billCode3 = promotionBillCode3.getBillCode();
          this.customValue3 = promotionBillCode3.getCustomValue();
          this.trackBillCodeBy3 = promotionBillCode3.getTrackBillCodeBy();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode4 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode4 != null )
        {
          this.billCode4 = promotionBillCode4.getBillCode();
          this.customValue4 = promotionBillCode4.getCustomValue();
          this.trackBillCodeBy4 = promotionBillCode4.getTrackBillCodeBy();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode5 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode5 != null )
        {
          this.billCode5 = promotionBillCode5.getBillCode();
          this.customValue5 = promotionBillCode5.getCustomValue();
          this.trackBillCodeBy5 = promotionBillCode5.getTrackBillCodeBy();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode6 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode6 != null )
        {
          this.billCode6 = promotionBillCode6.getBillCode();
          this.customValue6 = promotionBillCode6.getCustomValue();
          this.trackBillCodeBy6 = promotionBillCode6.getTrackBillCodeBy();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode7 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode7 != null )
        {
          this.billCode7 = promotionBillCode7.getBillCode();
          this.customValue7 = promotionBillCode7.getCustomValue();
          this.trackBillCodeBy7 = promotionBillCode7.getTrackBillCodeBy();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode8 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode8 != null )
        {
          this.billCode8 = promotionBillCode8.getBillCode();
          this.customValue8 = promotionBillCode8.getCustomValue();
          this.trackBillCodeBy8 = promotionBillCode8.getTrackBillCodeBy();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode9 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode9 != null )
        {
          this.billCode9 = promotionBillCode9.getBillCode();
          this.customValue9 = promotionBillCode9.getCustomValue();
          this.trackBillCodeBy9 = promotionBillCode9.getTrackBillCodeBy();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode10 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode10 != null )
        {
          this.billCode10 = promotionBillCode10.getBillCode();
          this.customValue10 = promotionBillCode10.getCustomValue();
          this.trackBillCodeBy10 = promotionBillCode10.getTrackBillCodeBy();
        }
      }
    }
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public boolean isBillCodesActive()
  {
    return billCodesActive;
  }

  public void setBillCodesActive( boolean billCodesActive )
  {
    this.billCodesActive = billCodesActive;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isAwardsActive()
  {
    return awardsActive;
  }

  public void setAwardsActive( boolean awardsActive )
  {
    this.awardsActive = awardsActive;
  }

  public boolean isPlateauAward()
  {
    return plateauAward;
  }

  public void setPlateauAward( boolean plateauAward )
  {
    this.plateauAward = plateauAward;
  }

  public String getBillCode1()
  {
    return billCode1;
  }

  public void setBillCode1( String billCode1 )
  {
    this.billCode1 = billCode1;
  }

  public String getCustomValue1()
  {
    return customValue1;
  }

  public void setCustomValue1( String customValue1 )
  {
    this.customValue1 = customValue1;
  }

  public String getBillCode2()
  {
    return billCode2;
  }

  public void setBillCode2( String billCode2 )
  {
    this.billCode2 = billCode2;
  }

  public String getCustomValue2()
  {
    return customValue2;
  }

  public void setCustomValue2( String customValue2 )
  {
    this.customValue2 = customValue2;
  }

  public String getBillCode3()
  {
    return billCode3;
  }

  public void setBillCode3( String billCode3 )
  {
    this.billCode3 = billCode3;
  }

  public String getCustomValue3()
  {
    return customValue3;
  }

  public void setCustomValue3( String customValue3 )
  {
    this.customValue3 = customValue3;
  }

  public String getBillCode4()
  {
    return billCode4;
  }

  public void setBillCode4( String billCode4 )
  {
    this.billCode4 = billCode4;
  }

  public String getCustomValue4()
  {
    return customValue4;
  }

  public void setCustomValue4( String customValue4 )
  {
    this.customValue4 = customValue4;
  }

  public String getBillCode5()
  {
    return billCode5;
  }

  public void setBillCode5( String billCode5 )
  {
    this.billCode5 = billCode5;
  }

  public String getCustomValue5()
  {
    return customValue5;
  }

  public void setCustomValue5( String customValue5 )
  {
    this.customValue5 = customValue5;
  }

  public String getBillCode6()
  {
    return billCode6;
  }

  public void setBillCode6( String billCode6 )
  {
    this.billCode6 = billCode6;
  }

  public String getCustomValue6()
  {
    return customValue6;
  }

  public void setCustomValue6( String customValue6 )
  {
    this.customValue6 = customValue6;
  }

  public String getBillCode7()
  {
    return billCode7;
  }

  public void setBillCode7( String billCode7 )
  {
    this.billCode7 = billCode7;
  }

  public String getCustomValue7()
  {
    return customValue7;
  }

  public void setCustomValue7( String customValue7 )
  {
    this.customValue7 = customValue7;
  }

  public String getBillCode8()
  {
    return billCode8;
  }

  public void setBillCode8( String billCode8 )
  {
    this.billCode8 = billCode8;
  }

  public String getCustomValue8()
  {
    return customValue8;
  }

  public void setCustomValue8( String customValue8 )
  {
    this.customValue8 = customValue8;
  }

  public String getBillCode9()
  {
    return billCode9;
  }

  public void setBillCode9( String billCode9 )
  {
    this.billCode9 = billCode9;
  }

  public String getCustomValue9()
  {
    return customValue9;
  }

  public void setCustomValue9( String customValue9 )
  {
    this.customValue9 = customValue9;
  }

  public String getBillCode10()
  {
    return billCode10;
  }

  public void setBillCode10( String billCode10 )
  {
    this.billCode10 = billCode10;
  }

  public String getCustomValue10()
  {
    return customValue10;
  }

  public void setCustomValue10( String customValue10 )
  {
    this.customValue10 = customValue10;
  }

  public String getTrackBillCodeBy1()
  {
    return trackBillCodeBy1;
  }

  public void setTrackBillCodeBy1( String trackBillCodeBy1 )
  {
    this.trackBillCodeBy1 = trackBillCodeBy1;
  }

  public String getTrackBillCodeBy2()
  {
    return trackBillCodeBy2;
  }

  public void setTrackBillCodeBy2( String trackBillCodeBy2 )
  {
    this.trackBillCodeBy2 = trackBillCodeBy2;
  }

  public String getTrackBillCodeBy3()
  {
    return trackBillCodeBy3;
  }

  public void setTrackBillCodeBy3( String trackBillCodeBy3 )
  {
    this.trackBillCodeBy3 = trackBillCodeBy3;
  }

  public String getTrackBillCodeBy4()
  {
    return trackBillCodeBy4;
  }

  public void setTrackBillCodeBy4( String trackBillCodeBy4 )
  {
    this.trackBillCodeBy4 = trackBillCodeBy4;
  }

  public String getTrackBillCodeBy5()
  {
    return trackBillCodeBy5;
  }

  public void setTrackBillCodeBy5( String trackBillCodeBy5 )
  {
    this.trackBillCodeBy5 = trackBillCodeBy5;
  }

  public String getTrackBillCodeBy6()
  {
    return trackBillCodeBy6;
  }

  public void setTrackBillCodeBy6( String trackBillCodeBy6 )
  {
    this.trackBillCodeBy6 = trackBillCodeBy6;
  }

  public String getTrackBillCodeBy7()
  {
    return trackBillCodeBy7;
  }

  public void setTrackBillCodeBy7( String trackBillCodeBy7 )
  {
    this.trackBillCodeBy7 = trackBillCodeBy7;
  }

  public String getTrackBillCodeBy8()
  {
    return trackBillCodeBy8;
  }

  public void setTrackBillCodeBy8( String trackBillCodeBy8 )
  {
    this.trackBillCodeBy8 = trackBillCodeBy8;
  }

  public String getTrackBillCodeBy9()
  {
    return trackBillCodeBy9;
  }

  public void setTrackBillCodeBy9( String trackBillCodeBy9 )
  {
    this.trackBillCodeBy9 = trackBillCodeBy9;
  }

  public String getTrackBillCodeBy10()
  {
    return trackBillCodeBy10;
  }

  public void setTrackBillCodeBy10( String trackBillCodeBy10 )
  {
    this.trackBillCodeBy10 = trackBillCodeBy10;
  }
}
