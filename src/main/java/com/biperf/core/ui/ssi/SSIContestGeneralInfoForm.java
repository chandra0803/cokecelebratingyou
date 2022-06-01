
package com.biperf.core.ui.ssi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SSIApproverType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestBillCode;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.ssi.view.SSIContestBillCodeView;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ssi.SSIContestContentValueBean;
import com.biperf.core.value.ssi.SSIContestDescriptionValueBean;
import com.biperf.core.value.ssi.SSIContestDocumentValueBean;
import com.biperf.core.value.ssi.SSIContestLanguageValueBean;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;
import com.biperf.core.value.ssi.SSIContestNameValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestGeneralInfoForm.
 * 
 * @author kandhi
 * @since Nov 7, 2014
 * @version 1.0
 */
public class SSIContestGeneralInfoForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;
  private String method;

  private String contestId;

  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private String tileStartDate = DateUtils.displayDateFormatMask;
  private String tileEndDate = DateUtils.displayDateFormatMask;
  private String contestName;
  private String contestType;
  private boolean includeMessage;
  private FormFile documentFile;
  private Long[] approversLevel1;
  private Long[] approversLevel2;
  // defaulting the current step to 1
  private int currentStep = 1;
  private boolean billCodeRequired;

  private String importFileId;

  public boolean isBillCodeRequired()
  {
    return billCodeRequired;
  }

  public void setBillCodeRequired( boolean billCodeRequired )
  {
    this.billCodeRequired = billCodeRequired;
  }

  private ArrayList<SSIContestNameValueBean> names = new ArrayList<SSIContestNameValueBean>();
  private ArrayList<SSIContestMessageValueBean> messages = new ArrayList<SSIContestMessageValueBean>();
  private ArrayList<SSIContestLanguageValueBean> languages = new ArrayList<SSIContestLanguageValueBean>();
  private ArrayList<SSIContestDescriptionValueBean> descriptions = new ArrayList<SSIContestDescriptionValueBean>();
  private ArrayList<SSIContestDocumentValueBean> documents = new ArrayList<SSIContestDocumentValueBean>();

  private List<SSIContestBillCodeView> billCodes;

  public void setBillCodes( List<SSIContestBillCodeView> billCodes )
  {
    this.billCodes = billCodes;
  }

  public SSIContestDocumentValueBean getDocuments( int index )
  {
    while ( index >= documents.size() )
    {
      documents.add( new SSIContestDocumentValueBean() );
    }
    return documents.get( index );
  }

  public SSIContestDescriptionValueBean getDescriptions( int index )
  {
    while ( index >= descriptions.size() )
    {
      descriptions.add( new SSIContestDescriptionValueBean() );
    }
    return descriptions.get( index );
  }

  public SSIContestLanguageValueBean getLanguages( int index )
  {
    while ( index >= languages.size() )
    {
      languages.add( new SSIContestLanguageValueBean() );
    }
    return languages.get( index );
  }

  public SSIContestMessageValueBean getMessages( int index )
  {
    while ( index >= messages.size() )
    {
      messages.add( new SSIContestMessageValueBean() );
    }
    return messages.get( index );
  }

  public SSIContestNameValueBean getNames( int index )
  {
    while ( index >= names.size() )
    {
      names.add( new SSIContestNameValueBean() );
    }
    return names.get( index );
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getContestId()
  {
    return contestId;
  }

  public void setContestId( String contestId )
  {
    this.contestId = contestId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getTileStartDate()
  {
    return tileStartDate;
  }

  public void setTileStartDate( String tileStartDate )
  {
    this.tileStartDate = tileStartDate;
  }

  public String getTileEndDate()
  {
    return tileEndDate;
  }

  public void setTileEndDate( String tileEndDate )
  {
    this.tileEndDate = tileEndDate;
  }

  public String getContestName()
  {
    return contestName;
  }

  public void setContestName( String contestName )
  {
    this.contestName = contestName;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public boolean isIncludeMessage()
  {
    return includeMessage;
  }

  public void setIncludeMessage( boolean includeMessage )
  {
    this.includeMessage = includeMessage;
  }

  public ArrayList<SSIContestNameValueBean> getNamesAsList()
  {
    return names;
  }

  public void setNamesAsList( SSIContestNameValueBean name )
  {
    names.add( name );
  }

  public ArrayList<SSIContestMessageValueBean> getMessagesAsList()
  {
    return messages;
  }

  public void setMessagesAsList( SSIContestMessageValueBean message )
  {
    messages.add( message );
  }

  public ArrayList<SSIContestLanguageValueBean> getLanguagesAsList()
  {
    return languages;
  }

  public void setLanguagesAsList( SSIContestLanguageValueBean language )
  {
    languages.add( language );
  }

  public ArrayList<SSIContestDescriptionValueBean> getDescriptionsAsList()
  {
    return descriptions;
  }

  public void setDescriptionsAsList( SSIContestDescriptionValueBean description )
  {
    descriptions.add( description );
  }

  public ArrayList<SSIContestDocumentValueBean> getDocumentsAsList()
  {
    return documents;
  }

  public void setDocumentsAsList( SSIContestDocumentValueBean document )
  {
    documents.add( document );
  }

  public FormFile getDocumentFile()
  {
    return documentFile;
  }

  public void setDocumentFile( FormFile documentFile )
  {
    this.documentFile = documentFile;
  }

  public Long[] getApproversLevel1()
  {
    return approversLevel1;
  }

  public void setApproversLevel1( Long[] approversLevel1 )
  {
    this.approversLevel1 = approversLevel1;
  }

  public Long[] getApproversLevel2()
  {
    return approversLevel2;
  }

  public void setApproversLevel2( Long[] approversLevel2 )
  {
    this.approversLevel2 = approversLevel2;
  }

  public int getCurrentStep()
  {
    return currentStep;
  }

  public void setCurrentStep( int currentStep )
  {
    this.currentStep = currentStep;
  }

  public String getImportFileId()
  {
    return importFileId;
  }

  public void setImportFileId( String importFileId )
  {
    this.importFileId = importFileId;
  }

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();
    if ( StringUtil.isNullOrEmpty( contestType ) || !SSIContest.CONTEST_TYPE_AWARD_THEM_NOW.equals( contestType ) )
    {
      validateContestName( actionErrors );
      if ( "save".equals( method ) )
      {
        validateDateFields( actionErrors );
        validateDescription( actionErrors, true );
        validateDocuments( actionErrors );
        validatePersonalMessage( actionErrors, true );
      }
      else
      {
        compareStartAndEndDates( actionErrors );
        validateTileStartDate( actionErrors );
        validateDescription( actionErrors, false );
        validateDocuments( actionErrors );
        validatePersonalMessage( actionErrors, false );
      }
    }
    else
    {
      if ( "save".equals( method ) )
      {
        compareStartAndEndDates( actionErrors );
        validateDescription( actionErrors, true );
        validateDocuments( actionErrors );
        validatePersonalMessage( actionErrors, true );
      }
    }
    validateApprovers( actionErrors );
    return actionErrors;
  }

  protected void validateDateFields( ActionErrors actionErrors )
  {
    validateStartAndEndDate( actionErrors );
    if ( tileStartDate == null )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_DISPLAY_DATE" ) ) );
    }
    else if ( startDate != null )
    {
      validateTileStartDate( actionErrors );
    }
  }

  protected void validateStartAndEndDate( ActionErrors actionErrors )
  {
    if ( startDate == null )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_START_DATE" ) ) );
    }
    else if ( endDate == null )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_END_DATE" ) ) );
    }
    else
    {
      compareStartAndEndDates( actionErrors );
    }
  }

  protected void validateApprovers( ActionErrors actionErrors )
  {
    if ( this.approversLevel1 != null && this.approversLevel1.length == 1 && this.approversLevel2 != null && this.approversLevel2.length == 1 )
    {
      if ( this.approversLevel1[0].equals( this.approversLevel2[0] ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.generalInfo.SAME_APPROVERS" ) );
      }
    }
  }

  protected void validateDocuments( ActionErrors actionErrors )
  {
    if ( this.documents != null && this.documents.size() > 0 )
    {
      for ( SSIContestDocumentValueBean vbBean : documents )
      {
        if ( vbBean.getName().length() > 50 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.generalInfo.DOCUMENT_EXCEEDS_LENGTH" ) );
        }
      }
    }
  }

  protected void validateDescription( ActionErrors actionErrors, boolean isRequired )
  {
    if ( this.descriptions == null || this.descriptions.size() == 0 )
    {
      if ( isRequired )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_DESCRIPTION" ) ) );
      }
    }
    else
    {
      for ( SSIContestDescriptionValueBean vbBean : descriptions )
      {
        if ( StringUtil.isNullOrEmpty( vbBean.getText() ) )
        {
          if ( isRequired )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_DESCRIPTION" ) ) );
          }
        }
        else if ( vbBean.getText().length() > 1000 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.generalInfo.DESCRIPTION_EXCEEDS_LENGTH" ) );
        }
      }
    }
  }

  private void validatePersonalMessage( ActionErrors actionErrors, boolean isRequired )
  {
    if ( includeMessage )
    {
      if ( this.messages == null || this.descriptions.size() == 0 )
      {
        if ( isRequired )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.MESSAGE" ) ) );
        }
      }
      else
      {
        for ( SSIContestMessageValueBean vbBean : messages )
        {
          if ( StringUtil.isNullOrEmpty( vbBean.getText() ) )
          {
            if ( isRequired )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.MESSAGE" ) ) );
            }
          }
          else
          {
            String withoutHtml = (String)StringUtil.skipHTML( vbBean.getText() );
            if ( withoutHtml.length() > 140 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.generalInfo.MESSAGE_EXCEEDS_LENGTH" ) );
            }
          }
        }
      }
    }
  }

  protected void validateContestName( ActionErrors actionErrors )
  {
    if ( this.names == null || this.getNamesAsList().size() == 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_NAME" ) ) );
    }
    else
    {
      for ( SSIContestNameValueBean vbBean : names )
      {
        if ( StringUtil.isNullOrEmpty( vbBean.getText() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_NAME" ) ) );
        }
        else if ( vbBean.getText().length() > 50 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.generalInfo.CONTEST_NAME_EXCEEDS_LENGTH" ) );
        }
      }
    }
  }

  private void compareStartAndEndDates( ActionErrors actionErrors )
  {
    if ( endDate != null && DateUtils.toDate( endDate ).before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_TO_DATE ) );
    }
    else if ( startDate != null && endDate != null && DateUtils.toDate( endDate ).before( DateUtils.toDate( startDate ) ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_START_DATE ) );
    }
  }

  private void validateTileStartDate( ActionErrors actionErrors )
  {
    if ( startDate != null && tileStartDate != null && DateUtils.toDate( tileStartDate ).after( DateUtils.toDate( startDate ) ) )
    {
      // tile start date is after contest startDate date
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.generalInfo.TILE_START_DATE_ERROR" ) );
    }
  }

  /**
   * Populate the contest with the request
   * @param clientStateMap
   * @return
   */
  public SSIContest toDomain( Map<String, Object> clientStateMap )
  {
    Long contestId = (Long)clientStateMap.get( "contestId" );
    Long promotionId = (Long)clientStateMap.get( "promotionId" );
    String cmAssetCode = (String)clientStateMap.get( "cmAssetCode" );
    Long contestVersion = (Long)clientStateMap.get( "contestVersion" );
    Long createdBy = (Long)clientStateMap.get( "createdBy" );
    Timestamp createdOn = (Timestamp)clientStateMap.get( "createdOn" );
    this.contestType = (String)clientStateMap.get( "contestType" );

    SSIContest ssiContest = new SSIContest();
    SSIPromotion ssiPromotion = new SSIPromotion();
    ssiPromotion.setId( promotionId );
    ssiPromotion.setPromotionType( PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) );
    ssiPromotion.setVersion( 1L );
    ssiContest.setPromotion( ssiPromotion );
    ssiContest.setContestOwnerId( UserManager.getUserId() );

    ssiContest.setId( contestId );
    if ( contestId != null )
    {
      ssiContest.setCmAssetCode( cmAssetCode );
      ssiContest.setVersion( contestVersion );
      ssiContest.getAuditCreateInfo().setCreatedBy( createdBy );
      ssiContest.getAuditCreateInfo().setDateCreated( createdOn );
    }

    if ( ssiContest.getId() == null || ssiContest.getId().equals( 0L ) )
    {
      ssiContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.DRAFT ) );
    }

    ssiContest.setContestType( SSIContest.getContestTypeFromName( contestType ) );
    if ( startDate != null )
    {
      ssiContest.setStartDate( DateUtils.toDate( startDate ) );
    }
    if ( endDate != null )
    {
      ssiContest.setEndDate( DateUtils.toDate( endDate ) );
    }
    if ( tileStartDate != null )
    {
      ssiContest.setDisplayStartDate( DateUtils.toDate( tileStartDate ) );
    }

    if ( includeMessage )
    {
      ssiContest.setIncludePersonalMessage( Boolean.TRUE );
    }
    else
    {
      ssiContest.setIncludePersonalMessage( Boolean.FALSE );
    }

    if ( this.approversLevel1 != null )
    {
      SSIContestApprover ssiContestApprover = null;
      for ( Long selectedApprover : this.approversLevel1 )
      {
        Participant approver = getParticipantService().getParticipantById( selectedApprover );
        ssiContestApprover = new SSIContestApprover();
        ssiContestApprover.setApprover( approver );
        ssiContestApprover.setApproverType( SSIApproverType.lookup( SSIApproverType.CONTEST_LEVEL1_APPROVER ) );
        ssiContest.addContestLevel1Approver( ssiContestApprover );
      }
    }

    if ( this.approversLevel2 != null )
    {
      SSIContestApprover ssiContestApprover = null;
      for ( Long selectedApprover : this.approversLevel2 )
      {
        Participant approver = getParticipantService().getParticipantById( selectedApprover );
        ssiContestApprover = new SSIContestApprover();
        ssiContestApprover.setApprover( approver );
        ssiContestApprover.setApproverType( SSIApproverType.lookup( SSIApproverType.CONTEST_LEVEL2_APPROVER ) );
        ssiContest.addContestLevel2Approver( ssiContestApprover );
      }
    }
    populateBillCodes( ssiContest );
    setBillCodeRequired( CollectionUtils.isNotEmpty( ssiContest.getContestBillCodes() ) );
    return ssiContest;
  }

  private void populateBillCodes( SSIContest ssiContest )
  {
    SSIPromotion ssiPromotion = getSSIPromotionService().getLiveSSIPromotion();
    if ( Objects.isNull( billCodes ) )
    {
      ssiContest.setContestBillCodes( new ArrayList<SSIContestBillCode>() );
      return;
    }

    SSIContestBillCode billCodeDomain;
    List<SSIContestBillCode> billCodesList = new ArrayList<SSIContestBillCode>();
    for ( SSIContestBillCodeView view : this.billCodes )
    {
      if ( Objects.isNull( view.getIndexAsLong() ) )
      {
        continue;
      }

      billCodeDomain = new SSIContestBillCode();
      billCodeDomain.setBillCode( view.getBillCodeDomainName( ssiPromotion.getId() ) );
      billCodeDomain.setCustomValue( view.getCustomValue() );
      billCodeDomain.setSortOrder( view.getIndexAsLong() );
      billCodeDomain.setTrackBillCodeBy( view.getTrackBy() );
      billCodeDomain.setSsiContest( ssiContest );
      billCodesList.add( billCodeDomain );

    }
    ssiContest.setContestBillCodes( billCodesList );
  }

  public SSIContestContentValueBean getContentValueBean()
  {
    SSIContestContentValueBean valueBean = new SSIContestContentValueBean();
    valueBean.setDescriptions( this.getDescriptionsAsList() );
    valueBean.setDocuments( this.getDocumentsAsList() );
    valueBean.setMessages( this.getMessagesAsList() );
    valueBean.setNames( this.getNamesAsList() );
    return valueBean;
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  private SSIPromotionService getSSIPromotionService()
  {
    return (SSIPromotionService)BeanLocator.getBean( SSIPromotionService.BEAN_NAME );
  }

  @SuppressWarnings( "unchecked" )
  public List<SSIContestBillCodeView> getBillCodes()
  {
    if ( billCodes == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new SSIContestBillCodeView( null, null, null, null );
        }
      };
      billCodes = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return billCodes;
  }

}
