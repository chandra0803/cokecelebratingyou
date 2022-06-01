package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.BillCodeSSIType;
import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.domain.ssi.SSIContestPaxClaimField;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.currency.CurrencyService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAwardThemNowService;
import com.biperf.core.service.ssi.SSIContestParticipantService;
import com.biperf.core.service.ssi.SSIContestPaxClaimService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.ssi.view.SSIContestBillCodeView;
import com.biperf.core.ui.ssi.view.SSIContestClaimDetailView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ssi.SSIContestBillCodeBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author dudam
 * @since Dec 22, 2014
 * @version 1.0
 */
public class SSIContestBaseAction extends BaseDispatchAction
{
  protected static final int DECIMAL_PRECISION_ZERO = 0;
  protected static final int DECIMAL_PRECISION_ONE = 1;
  protected static final int DECIMAL_PRECISION_TWO = 2;
  protected static final int DECIMAL_PRECISION_THREE = 3;
  protected static final int DECIMAL_PRECISION_FOUR = 4;

  protected SSIContestValueBean getContestValueBean( SSIContest ssiContest, int participantsCount )
  {
    SSIContestValueBean valueBean = new SSIContestValueBean();
    if ( ssiContest.getActivityMeasureType() != null && ssiContest.getActivityMeasureType().getCode().equals( SSIActivityMeasureType.CURRENCY_CODE ) )
    {
      valueBean.setActivityMeasureCurrency( getCurrencyService().getCurrencyByCode( ssiContest.getActivityMeasureCurrencyCode() ) );
    }
    else
    {
      valueBean.setActivityMeasureCurrency( null );
    }
    valueBean.setAttachmentTitle( getCMAssetService().getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_DOCUMENT_DISPLAY_NAME, UserManager.getLocale(), true ) );
    valueBean.setAttachmentUrl( getCMAssetService().getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_DOCUMENT_URL, UserManager.getLocale(), true ) );
    valueBean.setAttachmentOriginalName( getCMAssetService().getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_DOCUMENT_ORIGINAL_NAME, UserManager.getLocale(), true ) );
    String docType = valueBean.getAttachmentOriginalName().substring( valueBean.getAttachmentOriginalName().lastIndexOf( "." ) + 1 );
    if ( "doc".equals( docType ) || "docx".equals( docType ) )
    {
      valueBean.setAttachmentType( "word" );
    }
    else if ( "pdf".equals( docType ) )
    {
      valueBean.setAttachmentType( "pdf" );
    }
    else
    {
      valueBean.setAttachmentType( null );
    }
    valueBean.setPayoutType( ssiContest.getPayoutType() );
    valueBean.setActivityMeasureType( ssiContest.getActivityMeasureType() );
    if ( ssiContest.getAuditCreateInfo() != null && ssiContest.getAuditCreateInfo().getCreatedBy() != null )
    {
      valueBean.setContestCreatedBy( getFullName( ssiContest.getCreatorId() ) );
    }
    valueBean.setContestName( ssiContest.getContestNameFromCM() );
    valueBean.setDescription( getCMAssetService().getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_DESCRIPTION, UserManager.getLocale(), true ) );
    valueBean.setMessage( getCMAssetService().getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_MESSAGE, UserManager.getLocale(), true ) );
    if ( ssiContest.getPromotion() != null && ssiContest.getPromotion().getContestApprovalLevels() > 0 )
    {
      if ( ssiContest.getDateApprovedLevel1() != null && ssiContest.getApprovedByLevel1() != null )
      {
        valueBean.setLevel1Approver( getFullName( ssiContest.getApprovedByLevel1() ) );
      }
      if ( ssiContest.getDateApprovedLevel2() != null && ssiContest.getApprovedByLevel2() != null )
      {
        valueBean.setLevel2Approver( getFullName( ssiContest.getApprovedByLevel2() ) );
      }
    }
    valueBean.setParticipantCount( participantsCount );
    if ( ssiContest.getPayoutType() != null && ssiContest.getPayoutType().isOther() )
    {
      if ( ssiContest.getPayoutOtherCurrencyCode() != null )
      {
        valueBean.setPayoutOtherCurrency( getCurrencyService().getCurrencyByCode( ssiContest.getPayoutOtherCurrencyCode() ) );
      }
    }
    else
    {
      valueBean.setPayoutOtherCurrency( null );
    }
    if ( ssiContest.getContestType() != null && ssiContest.getContestType().getCode().equals( SSIContestType.DO_THIS_GET_THAT ) )
    {
      valueBean.setPayoutSumDtgt( getSSIContestService().calculatePayoutDoThisGetThatTotals( ssiContest.getId() ) );
    }
    return valueBean;
  }

  protected String getSysUrl()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  protected SSIPromotionService getSSIPromotionService()
  {
    return (SSIPromotionService)getService( SSIPromotionService.BEAN_NAME );
  }

  protected SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  protected SSIContestAwardThemNowService getSSIContestAwardThemNowService()
  {
    return (SSIContestAwardThemNowService)getService( SSIContestAwardThemNowService.BEAN_NAME );
  }

  protected SSIContestParticipantService getSSIContestParticipantService()
  {
    return (SSIContestParticipantService)getService( SSIContestParticipantService.BEAN_NAME );
  }

  protected CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  protected CurrencyService getCurrencyService()
  {
    return (CurrencyService)getService( CurrencyService.BEAN_NAME );
  }

  protected ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * @deprecated For not to use it in future
   * @see ClientStateUtils clientState methods
   * */
  @Deprecated
  protected Map<String, Object> getSSIContestClientStateMap( String clientState )
  {
    try
    {
      String password = ClientStatePasswordManager.getPassword();
      return ClientStateSerializer.deserialize( clientState, password );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "Invalid client state" );
    }
  }

  /**
   * @deprecated For not to use it in future
   * @see ClientStateUtils clientState methods
   * */
  @Deprecated
  protected Map<String, Object> getSSIContestClientStateMap( String clientState, String password )
  {
    try
    {
      return ClientStateSerializer.deserialize( clientState, password );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "Invalid client state" );
    }
  }

  /**
   * @deprecated For not to use it in future
   * @see ClientStateUtils clientState methods
   * */
  @Deprecated
  protected Long getContestIdFromClientState( String clientState, String cryptoPass )
  {
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    try
    {
      Map<String, Object> clientStateMap = getSSIContestClientStateMap( clientState, password );
      return Long.parseLong( clientStateMap.get( SSIContestUtil.CONTEST_ID ).toString() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( "Unable Decode Client State: " + e );
    }
  }

  protected ProcessService getProcessService()
  {
    return (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  protected ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)BeanLocator.getBean( ClaimFormDefinitionService.BEAN_NAME );
  }

  protected SSIContestPaxClaimService getSSIContestPaxClaimService()
  {
    return (SSIContestPaxClaimService)BeanLocator.getBean( SSIContestPaxClaimService.BEAN_NAME );
  }

  protected WebErrorMessage addServiceException( ServiceErrorException see )
  {
    WebErrorMessage message = new WebErrorMessage();
    List serviceErrors = see.getServiceErrors();
    for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
    {
      ServiceError error = (ServiceError)iter.next();
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
      message.setSuccess( false );
      message.setText( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
      message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
      message.setName( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.preview.ERROR" ) );
    }
    return message;
  }

  protected void moveToRequest( HttpServletRequest request )
  {
    if ( request.getSession().getAttribute( SSIContestUtil.SHOW_MODAL ) != null )
    {
      request.setAttribute( SSIContestUtil.SHOW_MODAL, request.getSession().getAttribute( SSIContestUtil.SHOW_MODAL ) );
      request.setAttribute( SSIContestUtil.MODAL_MESSAGE, request.getSession().getAttribute( SSIContestUtil.MODAL_MESSAGE ) );
      request.getSession().removeAttribute( SSIContestUtil.SHOW_MODAL );
      request.getSession().removeAttribute( SSIContestUtil.MODAL_MESSAGE );
    }
  }

  protected Long getClientStateParameterValueAsLong( HttpServletRequest request, String parameter ) throws InvalidClientStateException
  {
    Object paramValue = getClientStateParameterValue( request, parameter );
    Long parameterValue = null;
    try
    {
      parameterValue = (Long)paramValue;
    }
    catch( ClassCastException cce )
    {
      parameterValue = new Long( (String)paramValue );
    }
    return parameterValue;
  }

  private Object getClientStateParameterValue( HttpServletRequest request, String parameter ) throws InvalidClientStateException
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    return clientStateMap.get( parameter );
  }

  protected SSIContestClaimDetailView popluateClaimDetailValueBean( SSIContestPaxClaim paxClaim, String clientState )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ACTIVITIES ) );
    SSIContest contest = getSSIContestService().getContestByIdWithAssociations( paxClaim.getContestId(), associationRequestCollection );

    String submitterName = getParticipantService().getLNameFNameByPaxId( paxClaim.getSubmitterId() );
    String amountsQuantities = null;
    String activityPrefix = getActivityPrefix( contest );
    SSIContestClaimDetailView ssiContestClaimDetailView = new SSIContestClaimDetailView();
    ssiContestClaimDetailView.setClientState( clientState );
    ssiContestClaimDetailView.setId( paxClaim.getClaimNumber() );
    ssiContestClaimDetailView.setName( contest.getContestNameFromCM() );
    ssiContestClaimDetailView.setStatus( paxClaim.getStatus().getCode() );
    ssiContestClaimDetailView.setDateApproved( DateUtils.toDisplayString( paxClaim.getApproveDenyDate() ) );
    ssiContestClaimDetailView.setApprovedBy( getFullName( paxClaim.getApproverId() ) );
    ssiContestClaimDetailView.setDateDenied( DateUtils.toDisplayString( paxClaim.getApproveDenyDate() ) );
    ssiContestClaimDetailView.setDeniedBy( getFullName( paxClaim.getApproverId() ) );
    ssiContestClaimDetailView.setDeniedReason( paxClaim.getDeniedReason() );
    ssiContestClaimDetailView.setDateSubmitted( DateUtils.toDisplayString( paxClaim.getSubmissionDate() ) );
    ssiContestClaimDetailView.setSubmittedBy( submitterName );
    ssiContestClaimDetailView.setMeasureType( contest.getActivityMeasureType().getCode() );
    List<SSIContestPaxClaimField> paxClaimFields = getSortedPaxClaimFields( paxClaim );
    for ( SSIContestPaxClaimField paxClaimField : paxClaimFields )
    {
      if ( SSIContestUtil.CLAIM_FIELD_QUANTITY.equalsIgnoreCase( paxClaimField.getClaimField().getFormElement().getI18nLabel() )
          || SSIContestUtil.CLAIM_FIELD_AMOUNT.equalsIgnoreCase( paxClaimField.getClaimField().getFormElement().getI18nLabel() ) )
      {
        amountsQuantities = paxClaimField.getFieldValue();
      }
      else
      {
        SSIContestClaimDetailView.SSIContestClaimFormField fields = new SSIContestClaimDetailView().new SSIContestClaimFormField();
        fields.setName( paxClaimField.getClaimField().getFormElement().getI18nLabel() );
        fields.setName( paxClaimField.getClaimField().getFormElement().getI18nLabel() );
        if ( paxClaimField.getClaimField().getFormElement().getClaimFormElementType().isFile() )
        {
          String[] tokenisedString = SSIContestUtil.fileUrlStringTokenizer( paxClaimField.getFieldValue() );
          if ( tokenisedString != null && tokenisedString.length > 1 )
          {
            fields.setDescription( tokenisedString[0] );
            fields.setDocURL( SSIContestUtil.getCm3damBaseUrl() + tokenisedString[1] );
          }
        }
        else
        {
          fields.setDescription( paxClaimField.getFieldValue() );
        }
        ssiContestClaimDetailView.getFields().add( fields );
      }
    }
    if ( contest.getContestType().isDoThisGetThat() && amountsQuantities != null )
    {
      String[] amountQuantityArray = amountsQuantities.split( ",", -1 );
      int i = 0;
      for ( SSIContestActivity activity : contest.getContestActivities() )
      {
        String amountQuantity = amountQuantityArray[i];
        SSIContestClaimDetailView.SSIContestClaimActivity ssiContestClaimActivity = new SSIContestClaimDetailView().new SSIContestClaimActivity();
        ssiContestClaimActivity.setActivityDescription( activity.getDescription() );
        if ( !StringUtil.isNullOrEmpty( amountQuantity ) )
        {
          String amountFormatted = activityPrefix + SSIContestUtil.getFormattedValue( new Double( amountQuantity ), SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() ) );
          ssiContestClaimActivity.setActivityAmount( amountFormatted );
        }
        ssiContestClaimDetailView.getActivities().add( ssiContestClaimActivity );
        i++;
      }
    }
    else
    {
      SSIContestClaimDetailView.SSIContestClaimActivity activity = new SSIContestClaimDetailView().new SSIContestClaimActivity();
      activity.setActivityDescription( getActivityDescription( contest, paxClaim ) );
      if ( amountsQuantities != null )
      {
        String amountFormatted = activityPrefix + SSIContestUtil.getFormattedValue( new Double( amountsQuantities ), SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() ) );
        activity.setActivityAmount( amountFormatted );
      }
      ssiContestClaimDetailView.getActivities().add( activity );
    }

    ssiContestClaimDetailView.setContestType( contest.getContestType().getCode() );
    return ssiContestClaimDetailView;
  }

  private List<SSIContestPaxClaimField> getSortedPaxClaimFields( SSIContestPaxClaim paxClaim )
  {
    List<SSIContestPaxClaimField> paxClaimFields = new ArrayList<SSIContestPaxClaimField>();
    for ( SSIContestPaxClaimField paxClaimField : paxClaim.getPaxClaimFields() )
    {
      paxClaimFields.add( paxClaimField );
    }
    Collections.sort( paxClaimFields, SEQUENCE_COMPARATOR );
    return paxClaimFields;
  }

  private static Comparator<SSIContestPaxClaimField> SEQUENCE_COMPARATOR = new Comparator<SSIContestPaxClaimField>()
  {
    public int compare( SSIContestPaxClaimField paxClaimField1, SSIContestPaxClaimField paxClaimField2 )
    {
      return paxClaimField1.getClaimField().getSequenceNumber() - paxClaimField2.getClaimField().getSequenceNumber();
    }
  };

  private String getActivityDescription( SSIContest contest, SSIContestPaxClaim ssiContestPaxClaim )
  {
    String retrivedActivitiydescription = null;
    if ( contest.getContestType().isObjectives() )
    {
      if ( contest.getSameObjectiveDescription() )
      {
        retrivedActivitiydescription = contest.getActivityDescription();
      }
      else
      {
        SSIContestParticipant ssiContestParticipant = getSSIContestParticipantService().getSSIContestParticipantByPaxId( contest.getId(), ssiContestPaxClaim.getSubmitterId() );
        if ( ssiContestParticipant != null )
        {
          retrivedActivitiydescription = ssiContestParticipant.getActivityDescription();
        }
      }
    }
    else
    {
      retrivedActivitiydescription = contest.getActivityDescription();
    }
    return retrivedActivitiydescription;

  }

  protected String getFullName( Long userId )
  {
    if ( userId != null )
    {
      return getParticipantService().getLNameFNameByPaxId( userId );
    }
    else
    {
      return null;
    }

  }

  protected String getActivityPrefix( SSIContest contest )
  {
    if ( contest != null && contest.getActivityMeasureType() != null && SSIActivityMeasureType.CURRENCY_CODE.equals( contest.getActivityMeasureType().getCode() ) )
    {
      String activityPrefix = getCurrencyService().getCurrencyByCode( contest.getActivityMeasureCurrencyCode() ).getCurrencySymbol();
      return activityPrefix;
    }
    else
    {
      return "";
    }
  }

  protected String getPayoutPrefix( SSIContest ssiContest )
  {
    if ( SSIPayoutType.OTHER_CODE.equals( ssiContest.getPayoutType().getCode() ) )
    {
      String payoutOtherCurrencyCode = ssiContest.getPayoutOtherCurrencyCode();
      if ( payoutOtherCurrencyCode != null )
      {
        String payoutPrefix = getCurrencyService().getCurrencyByCode( payoutOtherCurrencyCode ).getCurrencySymbol();
        return payoutPrefix;
      }
      else
      {
        return "";
      }
    }
    else
    {
      return "";
    }
  }

  protected String getPayoutSuffix( SSIContest ssiContest )
  {
    if ( SSIPayoutType.POINTS_CODE.equals( ssiContest.getPayoutType().getCode() ) )
    {
      return " " + ssiContest.getPayoutType().getName();
    }
    else
    {
      return "";
    }
  }

  // To see any payouts when the contest is ended.
  protected boolean hasPayouts( SSIContest contest ) throws ServiceErrorException
  {
    boolean hasPayouts = false;
    List<SSIContestProgressValueBean> contestProgress = getSSIContestParticipantService().getContestProgress( contest.getId(), null );
    if ( contest.getContestType().isObjectives() )
    {
      if ( contestProgress.get( 0 ).getPotentialPayout() != null && contestProgress.get( 0 ).getPotentialPayout().longValue() > 0 )
      {
        hasPayouts = true;
      }
    }
    else if ( contest.getContestType().isDoThisGetThat() )
    {
      for ( SSIContestProgressValueBean valueBean : contestProgress )
      {
        if ( valueBean.getPotentialPayout() != null && valueBean.getPotentialPayout().longValue() > 0 )
        {
          hasPayouts = true;
          break;
        }
      }
    }
    else if ( contest.getContestType().isStepItUp() )
    {
      if ( contestProgress.get( 0 ).getTotalPayout() != null && contestProgress.get( 0 ).getTotalPayout().longValue() > 0 )
      {
        hasPayouts = true;
      }
    }
    else if ( contest.getContestType().isStackRank() )
    {
      if ( contestProgress.get( 0 ).getPotentialPayout() != null && contestProgress.get( 0 ).getPotentialPayout().longValue() > 0 )
      {
        hasPayouts = true;
      }
    }
    return hasPayouts;
  }

  public List<SSIContestBillCodeView> getBillCodeView( List<SSIContestBillCodeBean> billCodes )
  {
    if ( billCodes == null )
    {
      return new ArrayList<SSIContestBillCodeView>();
    }

    List<SSIContestBillCodeView> billCodeView = new ArrayList<SSIContestBillCodeView>();

    for ( SSIContestBillCodeBean bean : billCodes )
    {
      SSIContestBillCodeView view = new SSIContestBillCodeView();
      String billCode = null;
      if ( bean.getBillCode().equalsIgnoreCase( "customValue" ) )
      {
        BillCodeSSIType trackBy = BillCodeSSIType.lookup( bean.getTrackBillCodeBy() );
        view.setBillCodeName( trackBy.getName() + " - " + bean.getCustomValue() );
        billCode = trackBy.getName() + "_" + bean.getCustomValue();
      }
      else
      {
        view.setBillCodeName( bean.getDisplayName() );
        billCode = bean.getTrackBillCodeBy() + "_" + bean.getBillCode();
      }

      view.setBillCode( billCode );
      view.setIndex( bean.getSortOrder() != null ? String.valueOf( bean.getSortOrder() ) : null );
      view.setCustomValue( bean.getCustomValue() );
      view.setPromoBillCodeOrder( bean.getPromoBillCodeOrder() != null ? String.valueOf( bean.getPromoBillCodeOrder() ) : null );
      billCodeView.add( view );
    }
    return billCodeView;
  }

  private List<SSIContestBillCodeView> getBillCodeViewByContestId( Long contestId )
  {
    return getBillCodeView( getSSIContestService().getContestBillCodesByContestId( contestId ) );
  }

  public List<SSIContestBillCodeView> getBillCodeViewByPromoId( Long promoId )
  {
    List<SSIContestBillCodeBean> promoBillCodes = getSSIContestService().getBillCodesByPromoId( promoId );
    promoBillCodes.stream().forEach( b ->
    {
      b.setPromoBillCodeOrder( b.getSortOrder() );
      b.setSortOrder( null );
    } );
    return getBillCodeView( promoBillCodes );
  }

  public List<SSIContestBillCodeView> getBillCodeViewByContest( SSIContest contest )
  {
    return getBillCodeViewByContestId( contest.getId() );
  }

}
