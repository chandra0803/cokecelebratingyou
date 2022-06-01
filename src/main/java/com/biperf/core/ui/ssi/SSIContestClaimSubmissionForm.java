
package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.domain.ssi.SSIContestPaxClaimField;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.ssi.view.SSIContestDataCollectionFieldsView;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * SSIContestClaimSubmissionForm.
 * 
 * @author dudam
 * @since May 20, 2015
 * @version 1.0
 */
public class SSIContestClaimSubmissionForm extends BaseActionForm
{

  private static final long serialVersionUID = 1L;

  private Long contestId;
  private String ssiContestClientState;
  private ArrayList<SSIContestDataCollectionFieldsView> fields = new ArrayList<SSIContestDataCollectionFieldsView>();
  private ArrayList<NameIdBean> activities = new ArrayList<NameIdBean>();
  private FormFile ssiClaimDoc;

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public String getSsiContestClientState()
  {
    return ssiContestClientState;
  }

  public void setSsiContestClientState( String ssiContestClientState )
  {
    this.ssiContestClientState = ssiContestClientState;
  }

  public SSIContestDataCollectionFieldsView getFields( int index )
  {
    while ( index >= fields.size() )
    {
      fields.add( new SSIContestDataCollectionFieldsView() );
    }
    return fields.get( index );
  }

  public ArrayList<SSIContestDataCollectionFieldsView> getFieldsAsList()
  {
    return fields;
  }

  public void setFieldsAsList( SSIContestDataCollectionFieldsView field )
  {
    this.fields.add( field );
  }

  public NameIdBean getActivities( int index )
  {
    while ( index >= activities.size() )
    {
      activities.add( new NameIdBean() );
    }
    return activities.get( index );
  }

  public ArrayList<NameIdBean> getActivitiesAsList()
  {
    return activities;
  }

  public void setActivitiesAsList( NameIdBean activity )
  {
    this.activities.add( activity );
  }

  public FormFile getSsiClaimDoc()
  {
    return ssiClaimDoc;
  }

  public void setSsiClaimDoc( FormFile ssiClaimDoc )
  {
    this.ssiClaimDoc = ssiClaimDoc;
  }

  @Override
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();
    validateActivityAmount( actionErrors );
    boolean northAmericanAddress = isNorthAmericanAddress();
    boolean countrySelected = isCountrySelected();
    for ( SSIContestDataCollectionFieldsView field : this.fields )
    {
      SSIContestClaimField contestClaimField = getSSIContestService().getContestClaimFieldById( field.getId() );
      if ( SSIContestUtil.CLAIM_FIELD_GROUP_ADDRESS.equals( field.getFieldGroup() ) )
      {
        if ( field.getIsRequired() )
        {
          if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY.equals( field.getSubType() ) )
          {
            if ( StringUtil.isNullOrEmpty( field.getValue() ) )
            {
              addRequiredError( actionErrors, contestClaimField, field );
            }
          }
          else if ( ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS1.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_CITY.equals( field.getSubType() )
              || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_POSTAL_CODE.equals( field.getSubType() ) ) && field.getIsRequired() && StringUtil.isNullOrEmpty( field.getValue() ) )
          {
            addRequiredError( actionErrors, contestClaimField, field );
          }
          else if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_STATE.equals( field.getSubType() ) && northAmericanAddress && StringUtil.isNullOrEmpty( field.getValue() ) )
          {
            addRequiredError( actionErrors, contestClaimField, field );
          }
        }
        else if ( countrySelected )
        {
          // if country is selected
          // North American Country, required fields are addr1, state, city, postalCode
          // Non North American Country, required fields are addr1, city, postalCode
          if ( northAmericanAddress )
          {
            if ( ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS1.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_CITY.equals( field.getSubType() )
                || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_STATE.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_POSTAL_CODE.equals( field.getSubType() ) )
                && StringUtil.isNullOrEmpty( field.getValue() ) )
            {
              addRequiredError( actionErrors, contestClaimField, field );
            }
          }
          else if ( ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS1.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_CITY.equals( field.getSubType() )
              || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_POSTAL_CODE.equals( field.getSubType() ) ) && StringUtil.isNullOrEmpty( field.getValue() ) )
          {
            addRequiredError( actionErrors, contestClaimField, field );
          }
        }
      }
      else if ( field.getIsRequired() && StringUtil.isNullOrEmpty( field.getValue() ) )
      {
        // non address field and is required
        addRequiredError( actionErrors, contestClaimField, field );
      }
      else
      {
        // non address field and value is entered. invalid data check
        if ( SSIContestUtil.CLAIM_FIELD_TYPE_NUMBER.equals( field.getType() ) && !StringUtil.isNullOrEmpty( field.getValue() ) && !NumberUtils.isNumber( field.getValue() ) )
        {
          addInvalidError( actionErrors, contestClaimField, field );
        }
        if ( SSIContestUtil.CLAIM_FIELD_TYPE_DATE.equals( field.getType() ) && !StringUtil.isNullOrEmpty( field.getValue() ) )
        {
          Date today = new Date();
          if ( DateUtils.toDate( field.getValue(), UserManager.getLocale() ).after( today ) )
          {
            addInvalidError( actionErrors, contestClaimField, field );
          }
        }
      }
    }
    return actionErrors;
  }

  private void validateActivityAmount( ActionErrors actionErrors )
  {
    SSIContest contest = getSSIContestService().getContestById( contestId );
    if ( contest.getContestType().isDoThisGetThat() )
    {
      boolean allActivityAmtEmpty = true;
      for ( NameIdBean activity : activities )
      {
        if ( !StringUtil.isNullOrEmpty( activity.getValue() ) )
        {
          allActivityAmtEmpty = false;
          break;
        }
      }
      if ( allActivityAmtEmpty )
      {
        String label = contest.getActivityMeasureType().isCurrency()
            ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.AMOUNT" )
            : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.QUANTITY" );
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.claims.ACTIVITY_AMT_REQ", label ) );
      }
      else
      {
        try
        {
          int count = 0;
          for ( NameIdBean activity : activities )
          {
            if ( !StringUtil.isNullOrEmpty( activity.getValue() ) )
            {
              String value = activity.getValue();
              boolean isValid = false;
              if ( contest.getActivityMeasureType().isCurrency() )
              {
                isValid = validateDecimals( true, value );
                if ( !isValid )
                {
                  count++;
                }
              }
              else
              {
                isValid = validateDecimals( false, value );
                if ( !isValid )
                {
                  count++;
                }
              }
              Double.parseDouble( activity.getValue() );
            }
          }
          if ( count > 1 )
          {
            String label = contest.getActivityMeasureType().isCurrency()
                ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.AMOUNT" )
                : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.QUANTITY" );
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.claims.ACTIVITY_AMT_INVALID", label ) );
          }
        }
        catch( NumberFormatException nfe )
        {
          String label = contest.getActivityMeasureType().isCurrency()
              ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.AMOUNT" )
              : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.QUANTITY" );
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.claims.ACTIVITY_AMT_INVALID", label ) );
        }
      }
    }
  }

  private void addRequiredError( ActionErrors actionErrors, SSIContestClaimField contestClaimField, SSIContestDataCollectionFieldsView field )
  {
    if ( SSIContestUtil.CLAIM_FIELD_GROUP_ADDRESS.equals( field.getFieldGroup() ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, getAddressFieldLabel( field ) ) );
    }
    else
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, contestClaimField.getFormElement().getI18nLabel() ) );
    }
  }

  private void addInvalidError( ActionErrors actionErrors, SSIContestClaimField contestClaimField, SSIContestDataCollectionFieldsView field )
  {
    if ( SSIContestUtil.CLAIM_FIELD_GROUP_ADDRESS.equals( field.getFieldGroup() ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, getAddressFieldLabel( field ) ) );
    }
    else
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, contestClaimField.getFormElement().getI18nLabel() ) );
    }
  }

  private String getAddressFieldLabel( SSIContestDataCollectionFieldsView field )
  {
    String label = null;
    if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY.equals( field.getSubType() ) )
    {
      label = CmsResourceBundle.getCmsBundle().getString( "participant.address.COUNRTY_LABEL" );
    }
    else if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS1.equals( field.getSubType() ) )
    {
      label = CmsResourceBundle.getCmsBundle().getString( "participant.address.ADDR1_LABEL" );
    }
    else if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS2.equals( field.getSubType() ) )
    {
      label = CmsResourceBundle.getCmsBundle().getString( "participant.address.ADDR2_LABEL" );
    }
    else if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS3.equals( field.getSubType() ) )
    {
      label = CmsResourceBundle.getCmsBundle().getString( "participant.address.ADDR3_LABEL" );
    }
    else if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_CITY.equals( field.getSubType() ) )
    {
      label = CmsResourceBundle.getCmsBundle().getString( "participant.address.CITY_LABEL" );
    }
    else if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_STATE.equals( field.getSubType() ) )
    {
      label = CmsResourceBundle.getCmsBundle().getString( "participant.address.STATE_LABEL" );
    }
    else if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_POSTAL_CODE.equals( field.getSubType() ) )
    {
      label = CmsResourceBundle.getCmsBundle().getString( "participant.address.POSTAL_CODE_LABEL" );
    }
    return label;
  }

  public SSIContestPaxClaim toDomain( SSIContest contest, Long submitterId )
  {
    // wil get activity amt only if contest is dtgt
    List<String> activityAmt = getActivityAmt( contest );
    SSIContestPaxClaim paxClaim = new SSIContestPaxClaim();
    if ( isAddressAvailable() )
    {
      List<String> address = new ArrayList<String>();
      boolean isNorthAmericanAddress = isNorthAmericanAddress();
      boolean countrySelected = isCountrySelected();
      Long fieldId = getFieldId();
      for ( SSIContestDataCollectionFieldsView field : this.fields )
      {
        if ( isNorthAmericanAddress )
        {
          if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS1.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS2.equals( field.getSubType() )
              || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_CITY.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_STATE.equals( field.getSubType() )
              || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_POSTAL_CODE.equals( field.getSubType() ) )
          {
            if ( countrySelected )
            {
              if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY.equals( field.getSubType() ) )
              {
                if ( StringUtil.isNullOrEmpty( field.getValue() ) )
                {
                  address.add( "" );
                }
                else
                {
                  Country country = getCountryService().getCountryByCode( field.getValue() );
                  address.add( country.getI18nCountryName() );
                }
              }
              else if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_STATE.equals( field.getSubType() ) )
              {
                address.add( StringUtil.isNullOrEmpty( field.getValue() ) ? "" : StateType.lookup( field.getValue() ).getName() );
              }
              else
              {
                address.add( field.getValue() );
              }
            }
          }
        }
        else
        {
          if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS1.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS2.equals( field.getSubType() )
              || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_ADDRESS3.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_CITY.equals( field.getSubType() )
              || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY.equals( field.getSubType() ) || SSIContestUtil.CLAIM_FIELD_SUB_TYPE_POSTAL_CODE.equals( field.getSubType() ) )
          {
            if ( countrySelected )
            {
              if ( SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY.equals( field.getSubType() ) )
              {
                if ( StringUtil.isNullOrEmpty( field.getValue() ) )
                {
                  address.add( "" );
                }
                else
                {
                  Country country = getCountryService().getCountryByCode( field.getValue() );
                  address.add( country.getI18nCountryName() );
                }
              }
              else
              {
                address.add( field.getValue() );
              }
            }
          }
        }
      }
      // exclude address claim field and build pax claim
      paxClaim = buildPaxClaim( paxClaim, contest, submitterId, true, activityAmt );

      // add address claim field to pax claim
      if ( address.size() > 0 )
      {
        SSIContestClaimField claimField = getSSIContestService().getContestClaimFieldById( fieldId );
        SSIContestPaxClaimField paxClaimField = new SSIContestPaxClaimField();
        paxClaimField.setClaimField( claimField );
        paxClaimField.setFieldType( ClaimFormElementType.lookup( ClaimFormElementType.ADDRESS_BLOCK ) );
        paxClaimField.setFieldValue( StringUtil.join( address, " | " ) );
        paxClaim.addPaxClaimField( paxClaimField );
      }
    }
    else
    {
      // build pax claim with all fields
      paxClaim = buildPaxClaim( paxClaim, contest, submitterId, false, activityAmt );
    }
    return paxClaim;
  }

  private List<String> getActivityAmt( SSIContest contest )
  {
    List<String> activityAmt = null;
    if ( contest.getContestType().isDoThisGetThat() )
    {
      activityAmt = new ArrayList<String>();
      for ( NameIdBean activity : activities )
      {
        activityAmt.add( activity.getValue() );
      }
    }
    return activityAmt;
  }

  // checks if address is available in claim submission form
  private boolean isAddressAvailable()
  {
    boolean addressAvailable = false;
    for ( SSIContestDataCollectionFieldsView field : this.fields )
    {
      if ( SSIContestUtil.CLAIM_FIELD_GROUP_ADDRESS.equals( field.getFieldGroup() ) )
      {
        addressAvailable = true;
        break;
      }
    }
    return addressAvailable;
  }

  // checks if country selected in claim submission form is a north american country
  private boolean isNorthAmericanAddress()
  {
    boolean northAmericanAddress = false;
    for ( SSIContestDataCollectionFieldsView field : this.fields )
    {
      if ( SSIContestUtil.CLAIM_FIELD_GROUP_ADDRESS.equals( field.getFieldGroup() ) && SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY.equals( field.getSubType() ) )
      {
        if ( Country.CANADA.equals( field.getValue() ) || Country.MEXICO.equals( field.getValue() ) || Country.UNITED_STATES.equals( field.getValue() ) )
        {
          northAmericanAddress = true;
        }
        break;
      }
    }
    return northAmericanAddress;
  }

  // checks if country is selected in claim submission form
  private boolean isCountrySelected()
  {
    boolean countrySelected = false;
    for ( SSIContestDataCollectionFieldsView field : this.fields )
    {
      if ( SSIContestUtil.CLAIM_FIELD_GROUP_ADDRESS.equals( field.getFieldGroup() ) && SSIContestUtil.CLAIM_FIELD_SUB_TYPE_COUNTRY.equals( field.getSubType() )
          && !StringUtil.isNullOrEmpty( field.getValue() ) )
      {
        countrySelected = true;
        break;
      }
    }
    return countrySelected;
  }

  // gets the address claim field id
  private Long getFieldId()
  {
    Long fieldId = 0L;
    for ( SSIContestDataCollectionFieldsView field : this.fields )
    {
      if ( SSIContestUtil.CLAIM_FIELD_GROUP_ADDRESS.equals( field.getFieldGroup() ) )
      {
        fieldId = field.getId();
        break;
      }
    }
    return fieldId;
  }

  private SSIContestPaxClaim buildPaxClaim( SSIContestPaxClaim paxClaim, SSIContest contest, Long submitterId, boolean excludeAddress, List<String> activityAmts )
  {
    paxClaim.setContestId( contest.getId() );
    paxClaim.setSubmitterId( submitterId );
    paxClaim.setSubmissionDate( new Date() );
    for ( SSIContestDataCollectionFieldsView field : this.fields )
    {
      if ( excludeAddress )
      {
        if ( !SSIContestUtil.CLAIM_FIELD_GROUP_ADDRESS.equals( field.getFieldGroup() ) )
        {
          buildPaxClaimField( paxClaim, field );
        }
      }
      else
      {
        buildPaxClaimField( paxClaim, field );
      }
    }
    // adding claim field for amount/quantity for dtgt contest type
    if ( activityAmts != null )
    {
      for ( SSIContestClaimField claimField : contest.getClaimFields() )
      {
        if ( SSIContestUtil.CLAIM_FIELD_QUANTITY.equalsIgnoreCase( claimField.getFormElement().getI18nLabel() )
            || SSIContestUtil.CLAIM_FIELD_AMOUNT.equalsIgnoreCase( claimField.getFormElement().getI18nLabel() ) )
        {
          SSIContestPaxClaimField paxClaimField = new SSIContestPaxClaimField();
          paxClaimField.setClaimField( claimField );
          paxClaimField.setFieldType( ClaimFormElementType.lookup( claimField.getFormElement().getClaimFormElementType().getCode() ) );
          paxClaimField.setFieldValue( StringUtils.join( activityAmts, "," ) );
          double dtgtActivityAmt = 0;
          for ( String activityAmt : activityAmts )
          {
            if ( !StringUtil.isNullOrEmpty( activityAmt ) )
            {
              dtgtActivityAmt += new Double( activityAmt );
            }
          }
          paxClaim.setClaimAmountQuantity( dtgtActivityAmt );
          paxClaim.setActivitiesAmountQuantity( StringUtils.join( activityAmts, "," ) );
          paxClaim.addPaxClaimField( paxClaimField );
        }
      }
    }
    return paxClaim;
  }

  private void buildPaxClaimField( SSIContestPaxClaim paxClaim, SSIContestDataCollectionFieldsView field )
  {
    SSIContestClaimField claimField = getSSIContestService().getContestClaimFieldById( field.getId() );
    if ( claimField != null )
    {
      SSIContestPaxClaimField paxClaimField = new SSIContestPaxClaimField();
      paxClaimField.setClaimField( claimField );
      if ( field.getType().equals( SSIContestUtil.CLAIM_FIELD_TYPE_SELECT ) )
      {
        paxClaimField.setFieldType( ClaimFormElementType.lookup( ClaimFormElementType.SELECTION ) );
      }
      else if ( field.getType().equals( SSIContestUtil.CLAIM_FIELD_TYPE_TEXT_AREA ) )
      {
        paxClaimField.setFieldType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_BOX_FIELD ) );
      }
      else
      {
        paxClaimField.setFieldType( ClaimFormElementType.lookup( field.getType() ) );
      }
      if ( claimField.getFormElement().getClaimFormElementType().isFile() && !StringUtil.isNullOrEmpty( field.getValue() ) )
      {
        paxClaimField.setFieldValue( field.getDescription() + "|" + field.getValue().replace( SSIContestUtil.getCm3damBaseUrl(), "" ) );
      }
      else
      {
        paxClaimField.setFieldValue( field.getValue() );
      }
      if ( claimField.getFormElement().getI18nLabel().equalsIgnoreCase( SSIContestUtil.CLAIM_FIELD_AMOUNT )
          || claimField.getFormElement().getI18nLabel().equalsIgnoreCase( SSIContestUtil.CLAIM_FIELD_QUANTITY ) )
      {
        paxClaim.setClaimAmountQuantity( new Double( field.getValue() ) );
      }
      paxClaim.addPaxClaimField( paxClaimField );
    }
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)BeanLocator.getBean( SSIContestService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }

  private boolean validateDecimals( boolean isCurrency, String value )
  {
    if ( isCurrency )
    {
      if ( value.contains( "." ) )
      {
        String[] amountArr = value.split( "\\." );
        if ( ! ( amountArr[0].length() <= 9 && amountArr[1].length() <= 2 ) )
        {
          return false;
        }
      }
      else
      {
        if ( value.length() > 9 )
        {
          return false;
        }
      }
    }
    else
    {
      if ( value.contains( "." ) )
      {
        String[] quantityArr = value.split( "\\." );
        if ( ! ( quantityArr[0].length() <= 9 && quantityArr[1].length() <= 4 ) )
        {
          return false;
        }
      }
      else
      {
        if ( value.length() > 9 )
        {
          return false;
        }
      }
    }
    return true;
  }

}
