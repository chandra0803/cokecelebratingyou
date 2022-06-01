/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionECardAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.http.ResponseEntity;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditUpdateInfo;
import com.biperf.core.domain.company.Company;
import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.multimedia.EcardFormBean;
import com.biperf.core.domain.multimedia.EcardLocale;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.company.CompanyService;
import com.biperf.core.service.ecard.EcardService;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionCertificateUpdateAssociation;
import com.biperf.core.service.promotion.PromotionECardUpdateAssociation;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.ecard.ECardRequest;
import com.biperf.core.value.ecard.ECardResponse;

/**
 * PromotionECardAction.
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
 * <td>sedey</td>
 * <td>Oct 05, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionECardAction extends PromotionBaseDispatchAction
{

  /** Log */
  private static final Log logger = LogFactory.getLog( PromotionECardAction.class );

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

    PromotionECardForm promoECardForm = (PromotionECardForm)form;

    AbstractRecognitionPromotion promotion = null;
    // RecognitionPromotion promotion = new RecognitionPromotion();

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ) != null && request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = (AbstractRecognitionPromotion)getWizardPromotion( request );

      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ECARDS ) );
      if ( promotion.isRecognitionPromotion() || promotion.isNominationPromotion() )
      {
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
      }

      AbstractRecognitionPromotion attachedPromotion = (AbstractRecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );

      promotion.setPromotionECard( attachedPromotion.getPromotionECard() );
      if ( promotion.isRecognitionPromotion() )
      {
        ( (RecognitionPromotion)promotion ).setPromotionCertificates( ( (RecognitionPromotion)attachedPromotion ).getPromotionCertificates() );
      }
      else if ( promotion.isNominationPromotion() )
      {
        ( (NominationPromotion)promotion ).setPromotionCertificates( ( (NominationPromotion)attachedPromotion ).getPromotionCertificates() );
      }

    }
    else
    {
      // Get the promotionId from the request and get the promotion
      Long promotionId;

      if ( RequestUtils.containsAttribute( request, "promotionId" ) )
      {
        promotionId = RequestUtils.getRequiredAttributeLong( request, "promotionId" );
      }
      else
      {
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
          promotionId = (Long)clientStateMap.get( "promotionId" );
          if ( promotionId == null )
          {
            ActionMessages errors = new ActionMessages();
            errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "promotionId as part of clientState" ) );
            saveErrors( request, errors );
            return mapping.findForward( forwardTo );
          }
        }
        catch( InvalidClientStateException e )
        {
          throw new IllegalArgumentException( "request parameter clientState was missing" );
        }
      }
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ECARDS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );

      promotion = (AbstractRecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
    }

    Company company = getCompanyService().getCompanyDetail();
    ECardRequest eCardRequest = new ECardRequest();
    eCardRequest.setCompanyId( company.getCompanyId().toString() );
    eCardRequest.setVisibility( "BOTH" );
    ResponseEntity<ECardResponse[]> eCardResponse = null;
    try
    {
      eCardResponse = getEcardService().getECards( eCardRequest );

    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
    List<ECardResponse> respList = Arrays.asList( eCardResponse.getBody() );
    List certificateList = null;
    if ( promotion.isRecognitionPromotion() )
    {
      certificateList = PromotionCertificate.getList( PromotionType.RECOGNITION );
    }
    else if ( promotion.isNominationPromotion() )
    {
      certificateList = PromotionCertificate.getList( PromotionType.NOMINATION );
    }
    String cmsLocaleAvailabeinUrl = null;
    if ( request.getParameter( "cmsLocaleCode" ) != null && !request.getParameter( "cmsLocaleCode" ).equals( "" ) )
    {
      cmsLocaleAvailabeinUrl = request.getParameter( "cmsLocaleCode" ).toString();
    }

    promoECardForm.load( promotion, respList, certificateList, cmsLocaleAvailabeinUrl, RequestUtils.getBaseURI( request ) );

    return mapping.findForward( forwardTo );
  }

  public ActionForward displayClientUser( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    PromotionECardForm promoECardForm = (PromotionECardForm)form;
    promoECardForm.setClientUser( true );
    return display( mapping, form, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionECardForm promoECardForm = (PromotionECardForm)form;
    List<PromotionECardFormBean> cardFormeanList = promoECardForm.getCardAsList();
    for ( PromotionECardFormBean cardBean : cardFormeanList )
    {
      if ( cardBean.isSelected() )
      {
        ECard card = getMultimediaService().getECardById( cardBean.getCardId() );
        if ( card != null )
        {
          AuditUpdateInfo auditUpdateInfo = new AuditUpdateInfo();
          auditUpdateInfo.setModifiedBy( Long.valueOf( 5662 ) );
          auditUpdateInfo.setDateModified( new Timestamp( System.currentTimeMillis() ) );
          card.setId( cardBean.getCardId() );
          card.setName( cardBean.getName() );
          card.setSmallImageName( cardBean.getImageName() );
          card.setLargeImageName( cardBean.getPreviewImageName() );
          card.setFlashName( cardBean.getFlashName() );
          card.setTranslatable( cardBean.isTranslatable() );
          card.setActive( cardBean.isActive() );
          card.setAuditUpdateInfo( auditUpdateInfo );
          Set<EcardLocale> localesFromDB = new HashSet<EcardLocale>( card.getEcardLocales() );
          boolean noData = card.getEcardLocales().isEmpty();
          for ( EcardFormBean cardBeanLocale : cardBean.getLocaleListAsList() )
          {
            if ( StringUtils.isNotEmpty( cardBeanLocale.getLocale() ) && cardBeanLocale.getCardId() != null )
            {
              EcardLocale newLocale = new EcardLocale();
              boolean isNewLocale = true;
              if ( !noData )
              {
                for ( EcardLocale locale : card.getEcardLocales() )
                {
                  if ( Objects.nonNull( locale.getCardId() ) && StringUtils.isNotEmpty( locale.getLocale() ) )
                  {
                    if ( locale.getCardId().equals( cardBeanLocale.getCardId() ) && StringUtils.equalsIgnoreCase( locale.getLocale(), cardBeanLocale.getLocale() ) )
                    {
                      isNewLocale = false;
                      localesFromDB.remove( locale );
                      locale.setCardId( cardBeanLocale.getCardId() );
                      locale.setDisplayName( cardBeanLocale.getDisplayName() );
                      locale.setLocale( cardBeanLocale.getLocale() );
                      break;
                    }
                  }
                }
                if ( isNewLocale )
                {
                  newLocale.setCardId( cardBean.getCardId() );
                  newLocale.setDisplayName( cardBeanLocale.getDisplayName() );
                  newLocale.setLocale( cardBeanLocale.getLocale() );
                  card.getEcardLocales().add( newLocale );
                }
              }
            }
          }
          for ( EcardLocale locale : localesFromDB )
          {
            card.getEcardLocales().remove( locale );
          }
          getMultimediaService().saveCard( card );
          getMultimediaService().saveLocale( card.getEcardLocales() );
        }
        else
        {
          Set<EcardLocale> localeSet = new HashSet<>();
          card = new ECard();
          AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
          auditCreateInfo.setCreatedBy( Long.valueOf( 5662 ) );
          auditCreateInfo.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
          card.setId( cardBean.getCardId() );
          card.setName( cardBean.getName() );
          card.setSmallImageName( cardBean.getImageName() );
          card.setLargeImageName( cardBean.getPreviewImageName() );
          card.setFlashName( cardBean.getFlashName() );
          card.setTranslatable( cardBean.isTranslatable() );
          card.setActive( cardBean.isActive() );
          card.setAuditCreateInfo( auditCreateInfo );
          if ( cardBean.getLocaleListAsList() != null && cardBean.getLocaleListAsList().size() > 0 )
          {
            Iterator iter = cardBean.getLocaleListAsList().iterator();
            while ( iter.hasNext() )
            {
              EcardFormBean localeBean = (EcardFormBean)iter.next();
              EcardLocale locale = new EcardLocale();
              if ( cardBean.getCardId().equals( localeBean.getCardId() ) )
              {
                locale.setCardId( cardBean.getCardId() );
                locale.setDisplayName( localeBean.getDisplayName() );
                locale.setLocale( localeBean.getLocale() );
                localeSet.add( locale );
              }
            }
            card.addEcardLocales( localeSet );
          }
          getEcardService().saveECard( card );
        }
      }
    }
    // new
    Promotion promotion = null;

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

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ) != null && request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      promotion = getPromotionService().getPromotionById( promoECardForm.getPromotionId() );

    }

    try
    {
      // new
      // promotion = (AbstractRecognitionPromotion)getWizardPromotion( request );
      promotion = promoECardForm.toDomainObject( promoECardForm.isRecognitionPromotion() );
      PromotionECardUpdateAssociation promoECardUpdateAssociation = new PromotionECardUpdateAssociation( promotion );

      List updateAssociations = new ArrayList();
      updateAssociations.add( promoECardUpdateAssociation );
      if ( promoECardForm.isRecognitionPromotion() || promoECardForm.isNominationPromotion() )
      {
        PromotionCertificateUpdateAssociation promoCertificateUpdateAssociation = new PromotionCertificateUpdateAssociation( promotion );
        updateAssociations.add( promoCertificateUpdateAssociation );
      }
      promotion = (AbstractRecognitionPromotion)getPromotionService().savePromotion( promoECardForm.getPromotionId(), updateAssociations );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      // Client admin role removed - selector won't work
      // // if the send ECardSelector is true and the email hasn't been sent yet,
      // // send the email to the client email address provided
      // if ( promoECardForm.isSendECardSelector() && !promoECardForm.isClientEmailSent() )
      // {
      // getPromotionService().sendCardSelectionEmail( promoECardForm.getCardClientEmailAddress(),
      // promoECardForm.getPromotionId() );
      // }

      if ( isWizardMode( request ) )
      {
        setPromotionInWizardManager( request, promotion );

        forward = getWizardNextPage( mapping, request, promotion );
      }
      else
      {
        if ( promoECardForm.isClientUser() )
        {
          forward = mapping.findForward( "home" );
        }
        else
        {
          forward = saveAndExit( mapping, request, promotion );
        }
      }
    }

    return forward;
  }

  /**
   * display card preview
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayPreviewPopup( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String cardType = RequestUtils.getRequiredParamString( request, "cardType" );
    String imageName = RequestUtils.getRequiredParamString( request, "imageName" );
    String nominationPromotion = RequestUtils.getRequiredParamString( request, "isNomination" );

    request.setAttribute( "cardType", cardType );
    request.setAttribute( "imageName", imageName );
    request.setAttribute( "nominationPromotion", nominationPromotion );
    return mapping.findForward( "previewPopup" );
  }

  /**
   * Continue or exit without saving
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionFoward
   */
  public ActionForward continueOrExit( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    // ActionForward forward = mapping.findForward(ActionConstants.SUCCESS_FORWARD);
    Promotion promotion = getWizardPromotion( request );
    setPromotionInWizardManager( request, promotion );

    ActionForward forward = getWizardNextPage( mapping, request, promotion );

    return forward;
  }

  /**
   * Sort the given list of cards naturally. I.e. the names 1st, 10th, and 3rd become sorted to 1st, 3rd, 10th
   */
  private void naturalSortCardList( List<ECard> cardList )
  {
    // Make it safe to assume we have something to work with from here on out. Up to 2 elements,
    // nothing to sort, anyways.
    if ( cardList == null || cardList.size() <= 1 )
    {
      return;
    }

    // First grab just a sublist view. We know the list is already lexical sorted, only need to sort
    // until not numbered names start
    Pattern firstDigitsPattern = Pattern.compile( "^(\\d+)" );
    int lastNumericalCardIndex = 0;
    for ( int i = 0; i < cardList.size(); ++i )
    {
      Matcher firstDigitsMatcher = firstDigitsPattern.matcher( cardList.get( i ).getName() );
      if ( firstDigitsMatcher.find() )
      {
        // Now know the name starts with numbers. Assign those to a transient field we'll sort on
        int numericalName = Integer.parseInt( firstDigitsMatcher.group() );
        cardList.get( i ).setNumericalName( numericalName );
      }
      else
      {
        // Name does not start with numbers. No need to look further.
        lastNumericalCardIndex = i;
        break;
      }
    }
    List<ECard> numericalSublist = cardList.subList( 0, lastNumericalCardIndex );

    // Sort the sublist based on the first digits. It'll modify the entire list in turn.
    Collections.sort( numericalSublist, new Comparator<ECard>()
    {
      public int compare( ECard left, ECard right )
      {
        return left.getNumericalName() == right.getNumericalName() ? 0 : left.getNumericalName() < right.getNumericalName() ? -1 : 1;
      }
    } );
  }

  private MultimediaService getMultimediaService()
  {
    return (MultimediaService)getService( MultimediaService.BEAN_NAME );
  }

  private EcardService getEcardService()
  {
    return (EcardService)getService( EcardService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private static CompanyService getCompanyService()
  {
    return (CompanyService)BeanLocator.getBean( CompanyService.BEAN_NAME );
  }

}
