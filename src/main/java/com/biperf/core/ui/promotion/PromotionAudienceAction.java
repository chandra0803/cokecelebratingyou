/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionAudienceAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionJobPositionType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionCompetitorsAudience;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionPartnerAudience;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionAudienceUpdateAssociation;
import com.biperf.core.service.throwdown.ParticipantAudienceConflictResult;
import com.biperf.core.service.throwdown.ThrowdownAudienceValidationResult;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionAudienceAction.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>asondgeroth</td>
 * <td>Jul 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAudienceAction extends PromotionBaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PromotionAudienceAction.class );

  /**
   * RETURN_ACTION_URL_PARAM
   */
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  /**
   * SESSION_PROMO_AUDIENCE_FORM
   */
  public static String SESSION_PROMO_AUDIENCE_FORM = "sessionPromoAudienceForm";

  /**
   * Overridden from
   * 
   * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
   *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  public ActionForward onChangePrimary( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    updateCountries( form, request );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward onChangeSecondary( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    updateCountries( form, request );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Update the active countries for Spotlight
   * @param form
   * @param request
   */
  private void updateCountries( ActionForm form, HttpServletRequest request )
  {
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;
    boolean isRecognition = promoAudienceForm.getPromotionTypeCode().equals( PromotionType.lookup( PromotionType.RECOGNITION ).getCode() );
    // If there are no countries in list now then this is probably a non-merch promo
    if ( isRecognition && promoAudienceForm.isAwardMerchandise() )
    {
      List activeCountries = getPromoMerchCountryService().getActiveCountriesInPromoRecAudience( Long.valueOf( promoAudienceForm.getPromotionId() ),
                                                                                                 PrimaryAudienceType.lookup( promoAudienceForm.getPrimaryAudienceType() ),
                                                                                                 SecondaryAudienceType.lookup( promoAudienceForm.getSecondaryAudienceType() ),
                                                                                                 promoAudienceForm.getPrimaryAudienceSet(),
                                                                                                 promoAudienceForm.getSecondaryAudienceSet() );
      List oldRequiredCountryList = promoAudienceForm.getRequiredActiveCountryList();
      List oldNonRequiredCountryList = promoAudienceForm.getNonRequiredActiveCountryList();
      List newRequiredCountryList = new ArrayList();
      List newNonRequiredCountryList = new ArrayList();
      for ( Iterator promoMerchCountryFormBeanIter = oldRequiredCountryList.iterator(); promoMerchCountryFormBeanIter.hasNext(); )
      {
        PromoMerchCountryFormBean promoMerchCountryFormBean = (PromoMerchCountryFormBean)promoMerchCountryFormBeanIter.next();
        if ( doesPromoMerchCountryListContainCountryId( activeCountries, promoMerchCountryFormBean.getCountryId() ) )
        {
          newRequiredCountryList.add( promoMerchCountryFormBean );
        }
        else
        {
          newNonRequiredCountryList.add( promoMerchCountryFormBean );
        }
      }
      for ( Iterator promoMerchCountryFormBeanIter = oldNonRequiredCountryList.iterator(); promoMerchCountryFormBeanIter.hasNext(); )
      {
        PromoMerchCountryFormBean promoMerchCountryFormBean = (PromoMerchCountryFormBean)promoMerchCountryFormBeanIter.next();
        if ( doesPromoMerchCountryListContainCountryId( activeCountries, promoMerchCountryFormBean.getCountryId() ) )
        {
          newRequiredCountryList.add( promoMerchCountryFormBean );
        }
        else
        {
          newNonRequiredCountryList.add( promoMerchCountryFormBean );
        }
      }
      Comparator promoMerchCountryComparator = new Comparator()
      {
        public int compare( Object arg0, Object arg1 )
        {
          PromoMerchCountryFormBean country1 = (PromoMerchCountryFormBean)arg0;
          PromoMerchCountryFormBean country2 = (PromoMerchCountryFormBean)arg1;
          return country1.getCountryName().compareTo( country2.getCountryName() );
        }
      };
      Collections.sort( newRequiredCountryList, promoMerchCountryComparator );
      Collections.sort( newNonRequiredCountryList, promoMerchCountryComparator );
      promoAudienceForm.setRequiredActiveCountryList( newRequiredCountryList );
      promoAudienceForm.setNonRequiredActiveCountryList( newNonRequiredCountryList );
      request.setAttribute( "requiredActiveCountryListCount", String.valueOf( promoAudienceForm.getRequiredActiveCountryList().size() ) );
      request.setAttribute( "nonRequiredActiveCountryListCount", String.valueOf( promoAudienceForm.getNonRequiredActiveCountryList().size() ) );
    }
  }

  private boolean doesPromoMerchCountryListContainCountryId( List countryList, Long countryId )
  {
    for ( Iterator countryIter = countryList.iterator(); countryIter.hasNext(); )
    {
      PromoMerchCountry country = (PromoMerchCountry)countryIter.next();
      if ( country.getCountry().getId().equals( countryId ) )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    Promotion promotion = null;

    request.getSession().removeAttribute( "rpmAudienceUpdated" );
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;

    promoAudienceForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    String promotionId = promoAudienceForm.getPromotionId();

    if ( isWizardMode( request ) )
    {
      promotion = getWizardPromotion( request );
      promotionId = promotion.getId().toString();
    }

    if ( promotionId != null && promotionId.length() > 0 )
    {
      promotion = getPromotion( new Long( promotionId ), promoAudienceForm.getPromotionTypeCode() );
    }
    else
    {
      throw new IllegalArgumentException( "promotionId is null" );
    }

    // load the form with the promotion
    if ( promotion.getPartnerAudienceType() != null && !promotion.getPartnerAudienceType().equals( PartnerAudienceType.lookup( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE ) ) )
    {
      setPartnerAudiences( promotion );
    }
    promoAudienceForm.load( setPrimaryAndSecondaryAudiences( promotion ) );

    if ( promotion.isRecognitionPromotion() && promotion.getAwardType().isMerchandiseAwardType() )
    {
      promoAudienceForm.loadRecognitionPromoMerchCountryLists( promotion.getId() );
      request.setAttribute( "requiredActiveCountryListCount", String.valueOf( promoAudienceForm.getRequiredActiveCountryList().size() ) );
      request.setAttribute( "nonRequiredActiveCountryListCount", String.valueOf( promoAudienceForm.getNonRequiredActiveCountryList().size() ) );
    }

    return mapping.findForward( forwardTo );
  }

  public ActionForward validateAudienceDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    Promotion promotion = null;

    Map<String, Object> paramMap = new HashMap<String, Object>();

    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;

    promoAudienceForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    String promotionId = promoAudienceForm.getPromotionId();

    if ( isWizardMode( request ) )
    {
      promotion = getWizardPromotion( request );
      promotionId = promotion.getId().toString();
    }

    if ( promotionId != null && promotionId.length() > 0 )
    {
      promotion = getPromotion( new Long( promotionId ), promoAudienceForm.getPromotionTypeCode() );
    }
    else
    {
      throw new IllegalArgumentException( "promotionId is null" );
    }

    // load the form with the promotion
    if ( promotion.getPartnerAudienceType() != null && !promotion.getPartnerAudienceType().equals( PartnerAudienceType.lookup( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE ) ) )
    {
      setPartnerAudiences( promotion );
    }
    promoAudienceForm.load( setPrimaryAndSecondaryAudiences( promotion ) );
    promoAudienceForm.setValidateAudience( true );

    paramMap.put( "promotionId", promoAudienceForm.getPromotionId() );
    paramMap.put( "version", promoAudienceForm.getVersion() );
    paramMap.put( "claimFormId", promoAudienceForm.getClaimFormId() );

    String completeUrl = "/promotion/promotionOverviewComplete.do?method=markAsComplete";

    request.setAttribute( "completeUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), completeUrl, paramMap ) );

    if ( promotion.isRecognitionPromotion() && promotion.getAwardType().isMerchandiseAwardType() )
    {
      promoAudienceForm.loadRecognitionPromoMerchCountryLists( promotion.getId() );
      request.setAttribute( "requiredActiveCountryListCount", String.valueOf( promoAudienceForm.getRequiredActiveCountryList().size() ) );
      request.setAttribute( "nonRequiredActiveCountryListCount", String.valueOf( promoAudienceForm.getNonRequiredActiveCountryList().size() ) );
    }

    return mapping.findForward( forwardTo );
  }

  public ActionForward removeDivision( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;
    Long divisionId = promoAudienceForm.getDivisionId();

    if ( divisionId > 0 )
    {
      getThrowdownService().deleteDivision( divisionId );
    }

    Iterator<ThrowdownDivisionBean> divisionIter = promoAudienceForm.getDivisionList().iterator();
    while ( divisionIter.hasNext() )
    {
      ThrowdownDivisionBean divisionBean = divisionIter.next();
      if ( divisionBean.getId().longValue() == divisionId.longValue() )
      {
        divisionIter.remove();
      }
    }

    return mapping.findForward( forwardTo );
  }

  @SuppressWarnings( "unchecked" )
  private Promotion getPromotion( Long promotionId, String promotionType )
  {
    // Set the Association Request Collection
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );

    if ( PromotionType.PRODUCT_CLAIM.equals( promotionType ) )
    {
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.TEAM_POSITIONS ) );
    }
    if ( PromotionType.THROWDOWN.equals( promotionType ) )
    {
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISIONS ) );
    }

    // Get the Promotion details
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );

    return promotion;
  }

  /**
   * If an audience (primary or secondary) is a criteria-based audience, set the nbr of
   * participannts for the audience.
   * 
   * @param promotion
   * @return a promotion loaded with primary and secondary audiences
   */
  private Promotion setPrimaryAndSecondaryAudiences( Promotion promotion )
  {
    // iterate Promotion Primary audiences and set nbr of pax for criteria based audience
    if ( promotion.getPrimaryAudiences() != null )
    {
      Set promotionPrimaryAudienceSetWithPaxSize = new LinkedHashSet();

      Iterator promotionPrimaryAudienceIterator = promotion.getPrimaryAudiences().iterator();
      while ( promotionPrimaryAudienceIterator.hasNext() )
      {
        Audience audience = (Audience)promotionPrimaryAudienceIterator.next();
        audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );

        PromotionPrimaryAudience promotionPrimaryAudience = new PromotionPrimaryAudience();
        promotionPrimaryAudience.setPromotion( promotion );
        promotionPrimaryAudience.setAudience( audience );
        promotionPrimaryAudienceSetWithPaxSize.add( promotionPrimaryAudience );
      }
      promotion.setPromotionPrimaryAudiences( promotionPrimaryAudienceSetWithPaxSize );
    }

    // iterate Promotion Secondary audiences and set nbr of pax for criteria based audience
    if ( promotion.getSecondaryAudiences() != null )
    {
      Set promotionSecondaryAudienceSetWithPaxSize = new LinkedHashSet();

      Iterator promotionSecondaryAudienceIterator = promotion.getSecondaryAudiences().iterator();
      while ( promotionSecondaryAudienceIterator.hasNext() )
      {
        Audience audience = (Audience)promotionSecondaryAudienceIterator.next();
        audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );

        PromotionSecondaryAudience promotionSecondaryAudience = new PromotionSecondaryAudience();
        promotionSecondaryAudience.setPromotion( promotion );
        promotionSecondaryAudience.setAudience( audience );
        promotionSecondaryAudienceSetWithPaxSize.add( promotionSecondaryAudience );
      }
      promotion.setPromotionSecondaryAudiences( promotionSecondaryAudienceSetWithPaxSize );
    }

    if ( promotion.isThrowdownPromotion() )
    {
      ThrowdownPromotion throwdownPromotion = (ThrowdownPromotion)promotion;
      Set<Division> divisions = throwdownPromotion.getDivisions();
      for ( Division division : divisions )
      {
        Set<DivisionCompetitorsAudience> promotionDivisionAudienceSetWithPaxSize = new LinkedHashSet<DivisionCompetitorsAudience>();
        Iterator<DivisionCompetitorsAudience> promotionDivisionAudienceIterator = division.getCompetitorsAudience().iterator();
        while ( promotionDivisionAudienceIterator.hasNext() )
        {
          Audience audience = (Audience)promotionDivisionAudienceIterator.next().getAudience();
          audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );

          DivisionCompetitorsAudience divCompetitorAudience = new DivisionCompetitorsAudience();
          divCompetitorAudience.setDivision( division );
          divCompetitorAudience.setAudience( audience );
          promotionDivisionAudienceSetWithPaxSize.add( divCompetitorAudience );
        }
        division.setCompetitorsAudience( promotionDivisionAudienceSetWithPaxSize );
      }
    }

    return promotion;
  }

  private Promotion setPartnerAudiences( Promotion promotion )
  {
    // iterate Promotion Paertner audiences and set nbr of pax for criteria based audience
    if ( promotion.getPartnerAudiences() != null )
    {
      Set promotionPartnerAudienceSetWithPaxSize = new LinkedHashSet();

      Iterator promotionPartnerAudienceIterator = promotion.getPartnerAudiences().iterator();
      while ( promotionPartnerAudienceIterator.hasNext() )
      {
        Audience audience = (Audience)promotionPartnerAudienceIterator.next();
        audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );

        PromotionPartnerAudience promotionPartnerAudience = new PromotionPartnerAudience();
        promotionPartnerAudience.setPromotion( promotion );
        promotionPartnerAudience.setAudience( audience );
        promotionPartnerAudienceSetWithPaxSize.add( promotionPartnerAudience );
      }
      promotion.setPromotionPartnerAudiences( promotionPartnerAudienceSetWithPaxSize );
    }
    return promotion;
  }

  private int getNbrOfPaxsInCriteriaAudience( Audience audience )
  {
    /* Fix 24956 start */
    /*
     * original code return getPaxsInCriteriaAudience( audience ).size();
     */

    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    return getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true ).size();
  }

  private List getPaxsInCriteriaAudience( Audience audience )
  {
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    return getListBuilderService().searchParticipants( audiences, primaryHierarchyId, false, null, true );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    ActionForward forward = null;
    ActionMessages errors = new ActionMessages();
    ActionMessages warnings = new ActionMessages();

    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        forward = getCancelForward( mapping, request );
      }

      return forward;
    }
    if ( promoAudienceForm.getGqpartnersEnabled().equals( "true" ) && promoAudienceForm.getPartnerAudienceType() != null
        && promoAudienceForm.getPartnerAudienceType().equals( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE ) )
    {
      if ( promoAudienceForm.getPartnerAudienceList() != null && promoAudienceForm.getPartnerAudienceList().size() > 0 )
      {
        promoAudienceForm.setPartnerAudienceType( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE );
      }
    }
    else if ( promoAudienceForm.getGqpartnersEnabled().equals( "true" ) && promoAudienceForm.getPartnerAudienceType() != null
        && promoAudienceForm.getPartnerAudienceType().equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE ) )
    {
      promoAudienceForm.setPartnerAudienceType( PartnerAudienceType.SPECIFY_AUDIENCE_CODE );
    }

    else if ( promoAudienceForm.getGqpartnersEnabled().equals( "true" ) && promoAudienceForm.getPartnerAudienceType() != null
        && promoAudienceForm.getPartnerAudienceType().equals( PartnerAudienceType.USER_CHAR ) )
    {
      promoAudienceForm.setPartnerAudienceType( PartnerAudienceType.USER_CHAR );
    }
    else
    {
      promoAudienceForm.setPartnerAudienceType( null );
    }
    // Validate partner and primary audience shouldn't contain same pax.
    if ( promoAudienceForm.getGqpartnersEnabled().equals( "true" ) && promoAudienceForm.getPrimaryAudienceAsList().size() > 0 && promoAudienceForm.getPartnerAudienceList().size() > 0 )
    {
      List primAudList = promoAudienceForm.getPrimaryAudienceAsList();
      List partnerAudList = promoAudienceForm.getPartnerAudienceList();
      // BugFix 20214.
      for ( int i = 0; i < partnerAudList.size(); i++ )
      {
        PromotionAudienceFormBean partAudFB = (PromotionAudienceFormBean)partnerAudList.get( i );
        for ( int j = 0; j < primAudList.size(); j++ )
        {
          PromotionAudienceFormBean primAudFB = (PromotionAudienceFormBean)primAudList.get( j );
          if ( isPaxExistinBothAudinces( partAudFB.getAudienceId(), primAudFB.getAudienceId() ) )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.audience.PARTNER_AND_PRIMARY_AUDIENCE_CANNOT_SAME" ) );
            saveErrors( request, errors );
            return mapping.findForward( ActionConstants.FAIL_FORWARD );
          }
        }
      }

    }

    if ( promoAudienceForm.getPromotionTypeCode().equals( PromotionType.THROWDOWN ) )
    {
      errors = validateDivision( promoAudienceForm, errors );
      errors = validateParticipantAudiences( promoAudienceForm, errors );
      if ( !errors.isEmpty() )
      {
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }

    Promotion promotion = null;
    if ( isWizardMode( request ) )
    {
      promotion = getWizardPromotion( request );
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISIONS ) );

      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.TEAM_POSITIONS ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
      promotion = getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), associationRequestCollection );
      promotion = promoAudienceForm.toDomainObject( promotion );
    }
    else
    {
      promotion = promoAudienceForm.toDomainObject();
    }

    promoAudienceForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    try
    {
      // GQ /Challengepoint: Self Enrolling Audience
      if ( promotion.isGoalQuestOrChallengePointPromotion() && promoAudienceForm.isAllowSelfEnroll() )
      {
        saveSelfEnrollAudience( promotion, promoAudienceForm, errors );
      }
      // END GQ/Chellengepoint
      if ( errors.isEmpty() )
      {
        boolean setPromotionUnderConstruction = false;
        if ( promotion.isRecognitionPromotion() )
        {
          try
          {
            if ( !getPromoMerchCountryService().validateAndDeleteInvalidProgramLevels( promotion, false ) )
            {
              setPromotionUnderConstruction = true;
            }
            else if ( StringUtils.equalsIgnoreCase( promoAudienceForm.getPromotionStatus(), PromotionStatusType.COMPLETE )
                || StringUtils.equalsIgnoreCase( promoAudienceForm.getPromotionStatus(), PromotionStatusType.LIVE ) )
            {
              for ( Iterator promoMerchCountryIter = promotion.getPromoMerchCountries().iterator(); promoMerchCountryIter.hasNext(); )
              {
                PromoMerchCountry currentPromoMerchCountry = (PromoMerchCountry)promoMerchCountryIter.next();
                if ( currentPromoMerchCountry.getId() == null || currentPromoMerchCountry.getId().longValue() == 0 )
                {
                  setPromotionUnderConstruction = true;
                  break;
                }
              }
            }
            if ( setPromotionUnderConstruction )
            {
              String messageKey = "promotion.errors.MERCHCOUNTRY_PROGRAMID_CHANGED";
              warnings.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( messageKey ) );
              saveWarningMessages( request, warnings );
            }
          }
          catch( ServiceErrorException see )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) );
            saveErrors( request, errors );
            return mapping.findForward( ActionConstants.FAIL_FORWARD );
          }
        }

        if ( promotion.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) && promoAudienceForm.getPromotionId() != null
            && getPromotionService().isRecogPromotionInRPM( new Long( promoAudienceForm.getPromotionId() ) ) )
        {
          AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
          promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
          promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
          RecognitionPromotion recPromo = (RecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( new Long( promoAudienceForm.getPromotionId() ),
                                                                                                                        promoAssociationRequestCollection );
          // Check if the primary audience is updated in the recognition promotion
          List<Audience> dbAudienceList = new ArrayList<Audience>( recPromo.getPrimaryAudiences() );
          List<Audience> formAudienceList = new ArrayList<Audience>( promotion.getPrimaryAudiences() );
          boolean audienceUpdated = isRecognitionPromotionAudienceUpdated( dbAudienceList,
                                                                           formAudienceList,
                                                                           recPromo.getPrimaryAudienceType().getCode(),
                                                                           promotion.getPrimaryAudienceType().getCode() );

          if ( !audienceUpdated )
          {
            // If primary audiences is not updated then check if the secondary audience is updated
            // in
            // the recognition promotion
            dbAudienceList = new ArrayList<Audience>( recPromo.getSecondaryAudiences() );
            formAudienceList = new ArrayList<Audience>( promotion.getSecondaryAudiences() );
            audienceUpdated = isRecognitionPromotionAudienceUpdated( dbAudienceList, formAudienceList, recPromo.getSecondaryAudienceType().getCode(), promotion.getSecondaryAudienceType().getCode() );
          }
          request.getSession().setAttribute( "rpmAudienceUpdated", Boolean.valueOf( audienceUpdated ) );
        }

        PromotionAudienceUpdateAssociation promoAudienceUpdateAssociation = new PromotionAudienceUpdateAssociation( promotion );
        promoAudienceUpdateAssociation.setAddPrimaryAudiencesToChildPromotions( promoAudienceForm.isAddPrimaryAudiencesToChildPromotions() );
        promoAudienceUpdateAssociation.setAddSecondaryAudiencesToChildPromotions( promoAudienceForm.isAddSecondaryAudiencesToChildPromotions() );
        promoAudienceUpdateAssociation.setManagerCanSelect( promoAudienceForm.isManagerCanSelect() );
        promotion = getPromotionService().savePromotion( new Long( promoAudienceForm.getPromotionId() ), promoAudienceUpdateAssociation );
        if ( setPromotionUnderConstruction )
        {
          promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
          getPromotionService().savePromotion( promotion );
        }
      }
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }

    getAudienceService().clearPromoEligibilityCache();

    // validate any merch programs with the OM system
    List requiredCountries = promoAudienceForm.getRequiredActiveCountryList();
    List nonRequiredCountries = promoAudienceForm.getNonRequiredActiveCountryList();

    validateMerchandisePrograms( errors, requiredCountries );
    validateMerchandisePrograms( errors, nonRequiredCountries );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      if ( isWizardMode( request ) )
      {
        setPromotionInWizardManager( request, promotion );

        forward = getWizardNextPage( mapping, request, promotion );
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }

    return forward;
  }

  /**
   * This method checks whether audiences changed for a recognition promotion.
   * This is used by RPM promotion to alert the user to updated the benchmarks in Engagement promotion
   * @param dbAudienceList
   * @param formAudienceList
   * @param dbAudienceType
   * @param formAudienceType
   * @return
   */
  private boolean isRecognitionPromotionAudienceUpdated( List<Audience> dbAudienceList, List<Audience> formAudienceList, String dbAudienceType, String formAudienceType )
  {
    // If the audience type is changed
    if ( !dbAudienceType.equals( formAudienceType ) )
    {
      return true;
    }

    List<Long> dbAudienceIds = new ArrayList<Long>();
    List<Long> formAudienceIds = new ArrayList<Long>();
    boolean audienceUpdated = false;

    for ( Iterator<Audience> dbIter = dbAudienceList.iterator(); dbIter.hasNext(); )
    {
      Audience dbAudience = dbIter.next();
      dbAudienceIds.add( dbAudience.getId() );
    }

    for ( Iterator<Audience> formIter = formAudienceList.iterator(); formIter.hasNext(); )
    {
      Audience formAudience = formIter.next();
      formAudienceIds.add( formAudience.getId() );
    }

    // If the audience counts do not match
    if ( dbAudienceIds.size() != formAudienceIds.size() )
    {
      return true;
    }

    // If the audience counts match but the audiences are different
    for ( Iterator<Audience> formAudIter = formAudienceList.iterator(); formAudIter.hasNext(); )
    {
      if ( !dbAudienceIds.contains( formAudIter.next().getId() ) )
      {
        audienceUpdated = true;
      }
    }

    for ( Iterator<Audience> dbAudIter = dbAudienceList.iterator(); dbAudIter.hasNext(); )
    {
      if ( !formAudienceIds.contains( dbAudIter.next().getId() ) )
      {
        audienceUpdated = true;
      }
    }
    return audienceUpdated;
  }

  private ActionMessages validateDivision( PromotionAudienceForm promoAudienceForm, ActionMessages errors )
  {
    if ( promoAudienceForm.getDivisionList() == null || promoAudienceForm.getDivisionList().isEmpty() )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.audience.NO_DIVISIONS" ) );
      return errors;
    }
    boolean isUnique = false;
    Set<String> divsionName = new HashSet<String>();
    for ( ThrowdownDivisionBean division : promoAudienceForm.getDivisionList() )
    {
      if ( StringUtils.isEmpty( division.getDivisionName() ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.audience.MISSING_DIVISION_NAME" ) );
        return errors;
      }

      isUnique = divsionName.add( division.getDivisionName().toLowerCase() );
      if ( !isUnique )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.audience.DUPLICATE_DIVISION_NAME" ) );
        return errors;
      }
    }
    return errors;
  }

  private ActionMessages validateParticipantAudiences( PromotionAudienceForm promoAudienceForm, ActionMessages errors )
  {
    for ( ThrowdownDivisionBean division : promoAudienceForm.getDivisionList() )
    {
      if ( division.getDivisionAudiences() == null || division.getDivisionAudiences().isEmpty() )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.audience.DIVISION_MISSING_AUDIENCE" ) );
        return errors;
      }
    }
    // create some temp beans for the service
    Set<Division> divisions = new HashSet<Division>();
    for ( ThrowdownDivisionBean divisionBean : promoAudienceForm.getDivisionList() )
    {
      Division division = new Division();
      division.setId( divisionBean.getId() );
      division.setDivisionName( divisionBean.getDivisionName() );

      for ( PromotionAudienceFormBean audienceBean : divisionBean.getDivisionAudiences() )
      {
        DivisionCompetitorsAudience divisionAudience = new DivisionCompetitorsAudience();
        Audience audience = new PaxAudience();
        audience.setId( audienceBean.getAudienceId() );
        audience.setName( audienceBean.getName() );
        divisionAudience.setAudience( audience );
        divisionAudience.setDivision( division );
        division.getCompetitorsAudience().add( divisionAudience );
      }
      divisions.add( division );
    }
    ThrowdownAudienceValidationResult result = getThrowdownService().getAudienceValidationResults( divisions );
    if ( !result.isValidPromotionAudience() )
    {
      StringBuilder sb = new StringBuilder();
      for ( ParticipantAudienceConflictResult conflict : result.getConflictingAudienceMembers() )
      {
        sb.append( "<br/> " + conflict.getParticipant().getId() + " " + conflict.getParticipant().getNameFLNoComma() + ": " );
        for ( Division division : conflict.getDivisions() )
        {
          sb.append( division.getDivisionNameForCM() + "," );
        }
      }
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.audience.DUPLICATE_PAX_IN_AUDIENCES", sb.toString() ) );
    }
    return errors;
  }

  /**
   * Save Goal Quest self enroll audience when the audience is specified in the program code.
   * 
   * @param promotion
   * @param promoAudienceForm
   * @param errors
   */
  private void saveSelfEnrollAudience( Promotion promotion, PromotionAudienceForm promoAudienceForm, ActionMessages errors )
  {

    Set selfEnrollAudienceSet = promotion.getPromotionSecondaryAudiences();

    PromotionAudience promoAudience = (PromotionAudience)selfEnrollAudienceSet.iterator().next();

    PaxAudience paxAudience = (PaxAudience)promoAudience.getAudience();
    paxAudience.getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
    paxAudience.getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );

    // If insert or "update with name change", check and see if audience name is unique.
    String existingName = null;
    if ( paxAudience.getId() != null )
    {
      Audience existingAudience = getAudienceService().getAudienceById( paxAudience.getId(), null );
      existingName = existingAudience.getName();
    }
    boolean nameIsUnique = getAudienceService().isAudienceNameUnique( paxAudience.getName() );
    boolean isUpdate = promoAudience.getAudience().getId() != null;
    if ( isUpdate && !paxAudience.getName().equals( existingName ) && !nameIsUnique || !isUpdate && !nameIsUnique )
    {
      errors.add( "audienceDuplicate", new ActionMessage( ServiceErrorMessageKeys.AUDIENCE_DUPLICATE, CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.AUDIENCE_DUPLICATE ) ) );
    }
    else
    // if ( !isUpdate && errors.isEmpty() )
    {
      try
      {
        getAudienceService().save( paxAudience );
        selfEnrollAudienceSet.clear();
        promoAudience.setAudience( paxAudience );
        promoAudience.setPromotion( promotion );
        selfEnrollAudienceSet.add( promoAudience );
      }
      catch( ServiceErrorException e )
      {
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
      }
    }
  }

  /**
   * Removes submitter audiences from the given promotion.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward removeSubmitterAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    // Save a copy of the parent promotion's primary audience list.
    List primaryAudienceList = new ArrayList();
    primaryAudienceList.addAll( promotionAudienceForm.getPrimaryAudienceAsList() );

    try
    {
      // Remove audiences from the primary audience list.
      promotionAudienceForm.removeItems( PromotionAudienceForm.PRIMARY );

      // Determine whether removing audiences from the parent promotion's primary audience list
      // causes child promotions' primary audiences lists to be empty.
      Promotion detachedPromotion = promotionAudienceForm.toDomainObject();
      PromotionAudienceUpdateAssociation updateAssocationRequest = new PromotionAudienceUpdateAssociation( detachedPromotion );
      getPromotionService().validatePromotion( detachedPromotion.getId(), updateAssocationRequest );
      updateCountries( form, request );
    }
    catch( ServiceErrorExceptionWithRollback e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );

      // Restore the parent promotion's primary audience list.
      promotionAudienceForm.setPrimaryAudienceAsList( primaryAudienceList );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * removes any team audiences selected
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward removeTeamAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    // Save a copy of the parent promotion's secondary audience list.
    List secondaryAudienceList = new ArrayList();
    secondaryAudienceList.addAll( promotionAudienceForm.getSecondaryAudienceAsList() );

    try
    {
      // Remove audiences from the secondary audience list.
      promotionAudienceForm.removeItems( PromotionAudienceForm.SECONDARY );

      // Determine whether removing audiences from the parent promotion's secondary audience list
      // causes child promotions' secondary audiences lists to be empty.
      Promotion detachedPromotion = promotionAudienceForm.toDomainObject();
      PromotionAudienceUpdateAssociation updateAssocationRequest = new PromotionAudienceUpdateAssociation( detachedPromotion );
      getPromotionService().validatePromotion( detachedPromotion.getId(), updateAssocationRequest );
      updateCountries( form, request );
    }
    catch( ServiceErrorExceptionWithRollback e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );

      // Restore the parent promotion's secondary audience list.
      promotionAudienceForm.setSecondaryAudienceAsList( secondaryAudienceList );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * removes any selected team positions
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removeTeamPosition( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;

    promoAudienceForm.removeItems( PromotionAudienceForm.TEAM_POSITION );

    return forward;
  }

  /**
   * remove partner  action
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removePartnerAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    // Save a copy of the parent promotion's primary audience list.
    List partnerAudienceList = new ArrayList();
    partnerAudienceList.addAll( promotionAudienceForm.getPartnerAudienceList() );
    // Remove audiences from the partner audience list.
    promotionAudienceForm.removeItems( PromotionAudienceForm.PARTNER );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promoAudienceForm.getPromotionId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String method = "method=display";
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, method } );
  }

  /**
   * add submitter audience
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addSubmitterAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;

    // If there was no audience selected, then return an error
    if ( promoAudienceForm.getPrimaryAudienceId() == null || promoAudienceForm.getPrimaryAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    addPromotionAudience( promoAudienceForm, request, PromotionAudienceForm.PRIMARY, new Long( promoAudienceForm.getPrimaryAudienceId() ) );

    updateCountries( form, request );

    request.setAttribute( "promotionAudienceForm", promoAudienceForm );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * add team audience
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addTeamAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;

    // If there was no audience selected, then return an error
    if ( promoAudienceForm.getSecondaryAudienceId() == null || promoAudienceForm.getSecondaryAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    addPromotionAudience( promoAudienceForm, request, PromotionAudienceForm.SECONDARY, new Long( promoAudienceForm.getSecondaryAudienceId() ) );
    updateCountries( form, request );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward addNewDivision( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;
    ThrowdownDivisionBean divisionBean = new ThrowdownDivisionBean();
    // yuck...
    long tempId = 0;
    for ( ThrowdownDivisionBean currentBean : promoAudienceForm.getDivisionList() )
    {
      if ( currentBean.getId().longValue() < tempId )
      {
        tempId = currentBean.getId().longValue();
      }
    }
    divisionBean.setId( tempId - 1 );
    promoAudienceForm.getDivisionList().add( divisionBean );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward addDivisionAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;
    // bugfix 49464
    if ( request.getParameter( "divisionAudienceId_" + promoAudienceForm.getDivisionId() ) == null || request.getParameter( "divisionAudienceId_" + promoAudienceForm.getDivisionId() ).length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    Long divisionAudienceId = new Long( RequestUtils.getOptionalParamLong( request, "divisionAudienceId_" + promoAudienceForm.getDivisionId() ) );
    // bugfix 49421
    if ( divisionAudienceId != null && divisionAudienceId.longValue() > 0 )
    {
      addDivisionAudience( promoAudienceForm, request, PromotionAudienceForm.DIVISION, divisionAudienceId );
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward removeDivisionAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    // Save a copy of the promotion's Division audience list.
    List<ThrowdownDivisionBean> divisionListCopy = new ArrayList<ThrowdownDivisionBean>();
    divisionListCopy.addAll( promotionAudienceForm.getDivisionList() );

    // Remove audiences from the primary audience list.
    for ( ThrowdownDivisionBean division : promotionAudienceForm.getDivisionList() )
    {
      Iterator<PromotionAudienceFormBean> audienceFormBeanIter = division.getDivisionAudiences().iterator();
      while ( audienceFormBeanIter.hasNext() )
      {
        PromotionAudienceFormBean audienceFormBean = audienceFormBeanIter.next();
        if ( audienceFormBean.isRemoved() )
        {
          audienceFormBeanIter.remove();
        }
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * add promotion team position
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addPromotionTeamPosition( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;

    promoAudienceForm.addTeamPosition( PromotionJobPositionType.lookup( promoAudienceForm.getJobPositionId() ) );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Makes a request to the Audience builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareSubmitterAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    String promotionId = promotionAudienceForm.getPromotionId();

    ActionForward returnForward = mapping.findForward( "submitterAudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );
    Map parmMap = new HashMap();
    parmMap.put( "saveAudienceReturnUrl", returnUrl );
    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;
    queryString2 = ClientStateUtils.generateEncodedLink( "", "", parmMap );
    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PROMO_AUDIENCE_FORM, promotionAudienceForm );

    return ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );
  }

  /**
   * Makes a request to the Audience builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareTeamAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    String promotionId = promotionAudienceForm.getPromotionId();

    ActionForward returnForward = mapping.findForward( "teamAudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );

    Map parmMap = new HashMap();
    parmMap.put( "saveAudienceReturnUrl", returnUrl );
    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;
    queryString2 = ClientStateUtils.generateEncodedLink( "", "", parmMap );

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PROMO_AUDIENCE_FORM, promotionAudienceForm );

    return ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );
  }

  public ActionForward prepareDivisionAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    String promotionId = promotionAudienceForm.getPromotionId();
    Long divisionId = promotionAudienceForm.getDivisionId();

    ActionForward returnForward = mapping.findForward( "divisionAudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotionId );
    clientStateParameterMap.put( "divisionId", divisionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );

    Map parmMap = new HashMap();
    parmMap.put( "saveAudienceReturnUrl", returnUrl );
    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;
    queryString2 = ClientStateUtils.generateEncodedLink( "", "", parmMap );

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PROMO_AUDIENCE_FORM, promotionAudienceForm );

    return ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );
  }

  public ActionForward preparePartnerAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    String promotionId = promotionAudienceForm.getPromotionId();

    ActionForward returnForward = mapping.findForward( "partnerAudienceLookup" );

    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "promotionId", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );
    Map<String, Object> parmMap = new HashMap<String, Object>();
    parmMap.put( "saveAudienceReturnUrl", returnUrl );
    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;
    queryString2 = ClientStateUtils.generateEncodedLink( "", "", parmMap );
    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PROMO_AUDIENCE_FORM, promotionAudienceForm );

    return ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );
  }

  public ActionForward returnPartnerAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionAudienceForm sessionPromoAudienceForm = (PromotionAudienceForm)request.getSession().getAttribute( SESSION_PROMO_AUDIENCE_FORM );

    if ( sessionPromoAudienceForm != null )
    {
      String formAudienceType = PromotionAudienceForm.PARTNER;
      extractAndAddPromotionAudience( request, promotionAudienceForm, sessionPromoAudienceForm, formAudienceType );
    }

    request.getSession().removeAttribute( SESSION_PROMO_AUDIENCE_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Handles the return from the audience builder. This will look for the AudienceId on the request,
   * load the audience and the promotion and build a new PromotionWebRulesAudience which is set onto
   * the form in preparation to saving the webRules.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */

  public ActionForward returnSubmitterAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionAudienceForm sessionPromoAudienceForm = (PromotionAudienceForm)request.getSession().getAttribute( SESSION_PROMO_AUDIENCE_FORM );

    if ( sessionPromoAudienceForm != null )
    {
      String formAudienceType = PromotionAudienceForm.PRIMARY;
      extractAndAddPromotionAudience( request, promotionAudienceForm, sessionPromoAudienceForm, formAudienceType );
    }

    request.getSession().removeAttribute( SESSION_PROMO_AUDIENCE_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward returnDivisionAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionAudienceForm sessionPromoAudienceForm = (PromotionAudienceForm)request.getSession().getAttribute( SESSION_PROMO_AUDIENCE_FORM );

    if ( sessionPromoAudienceForm != null )
    {
      String formAudienceType = PromotionAudienceForm.DIVISION;
      extractAndAddPromotionAudience( request, promotionAudienceForm, sessionPromoAudienceForm, formAudienceType );
    }

    request.getSession().removeAttribute( SESSION_PROMO_AUDIENCE_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void extractAndAddPromotionAudience( HttpServletRequest request, PromotionAudienceForm promotionAudienceForm, PromotionAudienceForm sessionPromoAudienceForm, String formAudienceType )
  {
    try
    {
      BeanUtils.copyProperties( promotionAudienceForm, sessionPromoAudienceForm );
    }
    catch( Exception e )
    {
      logger.info( "returnCategoryLookup: Copy Properties failed." );
    }
    Long audienceId = null;
    try
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
      audienceId = (Long)clientStateMap.get( "audienceId" );
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since this is an option parameter
    }

    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addPromotionAudience( promotionAudienceForm, request, formAudienceType, audienceId );
    }
  }

  /**
   * display participant list popup
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayPaxListPopup( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    Long audienceId = null;
    try
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
      try
      {
        audienceId = new Long( (String)clientStateMap.get( "audienceId" ) );
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

      if ( audienceId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "audienceId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( "paxListPopup" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Audience audience = getAudienceService().getAudienceById( audienceId, null );
    List paxFormattedValueList = getPaxsInCriteriaAudience( audience );

    request.setAttribute( "paxFormattedValueList", paxFormattedValueList );
    request.setAttribute( "paxFormattedValueListSize", String.valueOf( paxFormattedValueList.size() ) );
    return mapping.findForward( "paxListPopup" );
  }

  /**
   * Handles the return from the audience builder. This will look for the AudienceId on the request,
   * load the audience and the promotion and build a new PromotionWebRulesAudience which is set onto
   * the form in preparation to saving the webRules.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward returnTeamAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionAudienceForm promotionAudienceForm = (PromotionAudienceForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionAudienceForm sessionPromoAudienceForm = (PromotionAudienceForm)request.getSession().getAttribute( SESSION_PROMO_AUDIENCE_FORM );

    if ( sessionPromoAudienceForm != null )
    {
      String formAudienceType = PromotionAudienceForm.SECONDARY;
      extractAndAddPromotionAudience( request, promotionAudienceForm, sessionPromoAudienceForm, formAudienceType );
      updateCountries( form, request );
    }

    request.getSession().removeAttribute( SESSION_PROMO_AUDIENCE_FORM );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void validateMerchandisePrograms( ActionMessages errors, List programs ) throws ServiceErrorException
  {
    if ( programs == null || programs.isEmpty() )
    {
      return;
    }
    boolean isError = false;
    StringBuffer errorProgramNumbers = new StringBuffer();
    for ( int i = 0; i < programs.size(); i++ )
    {
      PromoMerchCountryFormBean merchCountry = (PromoMerchCountryFormBean)programs.get( i );
      if ( merchCountry != null && merchCountry.getProgramId() != null && merchCountry.getProgramId().trim().length() > 0 )
      {
        try
        {
          getAwardBanQService().isValidGiftCodeProgram( merchCountry.getProgramId() );
        }
        catch( Exception e )
        {
          isError = true;
          // Even first program ID will have ',' in front of it, which we will
          // remove before adding it to error.
          errorProgramNumbers.append( ", " + merchCountry.getProgramId() );
        }
      }
    }
    if ( isError )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.PROGRAM_NUMBER_INVALID", errorProgramNumbers.toString().substring( 1 ) ) );
    }
  }

  private void addDivisionAudience( PromotionAudienceForm form, HttpServletRequest request, String promoAudienceType, Long audienceId )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    // add the audience to the list
    for ( ThrowdownDivisionBean divisionBean : form.getDivisionList() )
    {
      if ( divisionBean.getId().intValue() == form.getDivisionId() )
      {
        PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
        audienceFormBean.setAudienceId( audience.getId() );
        audienceFormBean.setName( audience.getName() );
        audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
        audienceFormBean.setSize( audience.getSize() );
        audienceFormBean.setVersion( audience.getVersion() );
        divisionBean.getDivisionAudiences().add( audienceFormBean );
      }
    }
  }

  private void addPromotionAudience( PromotionAudienceForm form, HttpServletRequest request, String promoAudienceType, Long audienceId )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    if ( AudienceType.SPECIFIC_PAX_TYPE.equals( form.getPrimaryAudienceType() ) || audience instanceof CriteriaAudience )
    {
      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      Long primaryHierarchyId = primaryHierarchy.getId();

      Set audiences = new LinkedHashSet();
      audiences.add( audience );
      List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );
      audience.setSize( paxFormattedValueList.size() );
    }

    // add the audience to the list
    form.addAudience( audience, promoAudienceType );
  }

  public ActionForward addPartnerAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionAudienceForm promoAudienceForm = (PromotionAudienceForm)form;

    // If there was no audience selected, then return an error
    if ( promoAudienceForm.getPartnerAudienceId() == null || promoAudienceForm.getPartnerAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    // validate if the audience selected as existing participant's audience
    /*
     * if( promoAudienceForm.getPrimaryAudienceList() != null &&
     * promoAudienceForm.getPrimaryAudienceList().size() > 0 ) { Iterator iter =
     * promoAudienceForm.getPrimaryAudienceList().iterator(); while ( iter.hasNext() ) {
     * PromotionAudienceFormBean audienceFormBean = ( PromotionAudienceFormBean )iter.next(); String
     * audid = String.valueOf( audienceFormBean.getAudienceId() ); if(
     * promoAudienceForm.getPartnerAudienceId().equals( audid ) ) { ActionMessages errors = new
     * ActionMessages(); errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage(
     * "promotion.audience.PARTNER_AND_PRIMARY_AUDIENCE_CANNOT_SAME" ) ); saveErrors( request,
     * errors ); return mapping.findForward( ActionConstants.FAIL_FORWARD ); } } }
     */
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( new Long( promoAudienceForm.getPartnerAudienceId() ), null );

    if ( AudienceType.SPECIFIC_PAX_TYPE.equals( promoAudienceForm.getPartnerAudienceType() ) || audience instanceof CriteriaAudience )
    {
      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      Long primaryHierarchyId = primaryHierarchy.getId();

      Set audiences = new LinkedHashSet();
      audiences.add( audience );
      List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );
      audience.setSize( paxFormattedValueList.size() );
    }

    // add the audience to the list
    promoAudienceForm.addAudience( audience, PromotionAudienceForm.PARTNER );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private boolean isPaxExistinBothAudinces( Long primAudId, Long partnerAudId )
  {
    boolean isExist = false;
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();
    Set primAudiences = new LinkedHashSet();
    Set partnerAudiences = new LinkedHashSet();
    primAudiences.add( getAudienceService().getAudienceById( primAudId, null ) );
    partnerAudiences.add( getAudienceService().getAudienceById( partnerAudId, null ) );
    List paxList = getListBuilderService().searchParticipants( primAudiences, primaryHierarchyId, true, null, true );
    List partnersList = getListBuilderService().searchParticipants( partnerAudiences, primaryHierarchyId, true, null, true );
    if ( paxList != null && paxList.size() > 0 && partnersList != null && partnersList.size() > 0 )
    {
      for ( int i = 0; i < paxList.size(); i++ )
      {
        Long paxId = ( (FormattedValueBean)paxList.get( i ) ).getId();
        for ( int j = 0; j < partnersList.size(); j++ )
        {
          Long partnerId = ( (FormattedValueBean)partnersList.get( j ) ).getId();
          if ( paxId.longValue() == partnerId.longValue() )
          {
            isExist = true;
            break;
          }
        }
        if ( isExist )
        {
          break;
        }
      }
    }

    return isExist;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  /**
   * Gets an AudienceService
   * 
   * @return AudienceService
   */
  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  /**
   * Gets a HierarchyService
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  /**
   * @return PromoMerchCountryService
   */
  private PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }

  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

  /**
   * @return AwardBanqService
   */
  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return factory.getAwardBanQService();
  }

}
