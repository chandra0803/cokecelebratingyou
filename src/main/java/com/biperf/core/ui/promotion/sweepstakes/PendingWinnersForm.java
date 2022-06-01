/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/sweepstakes/PendingWinnersForm.java,v $
 */

package com.biperf.core.ui.promotion.sweepstakes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionSweepstakeService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;

/**
 * PendingWinnersForm.
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
 * <td>jenniget</td>
 * <td>Nov 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PendingWinnersForm extends BaseForm
{
  public static final String FORM_NAME = "pendingWinnersForm";

  private String promotionId;
  private String promotionName;
  private String promotionType;
  private String sweepstakesStartDate;
  private String sweepstakesEndDate;
  private String method;
  private List winnerBeans;

  public Set load( Promotion promo )
  {
    PromotionSweepstake sweepstake = null;
    /*
     * Bug # 34020 start for ( Iterator iter = promo.getPromotionSweepstakes().iterator();
     * iter.hasNext(); ) { PromotionSweepstake sweep = (PromotionSweepstake)iter.next();
     * //PROMO_SWEEPSTAKE_DRAWING if ( !sweep.isProcessed() ) { sweepstake = sweep; break; } }
     */
    List sweepstakeDrawingList = getPromotionSweepstakeService().getPromotionSweepstakesByPromotionIdNotProcessed( promo.getId() );
    sweepstake = (PromotionSweepstake)sweepstakeDrawingList.get( 0 );
    /* Bug # 34020 end */

    this.setPromotionName( promo.getName() );
    this.setPromotionId( promo.getId().toString() );
    this.setPromotionType( promo.getPromotionType().getCode() );
    this.setSweepstakesEndDate( DateUtils.toDisplayString( sweepstake.getEndDate() ) );
    this.setSweepstakesStartDate( DateUtils.toDisplayString( sweepstake.getStartDate() ) );

    /* Bug # 34020 start */
    List sweepstakeWinners = getPromotionSweepstakeService().getAllPromotionSweepstakeWinnersByDrawingId( sweepstake.getId() );
    // List formBeans = loadWinnersIntoFormBeans( sweepstake.getWinners() );
    List formBeans = loadWinnersIntoFormBeans( sweepstakeWinners );
    /* Bug # 34020 end */

    Collections.sort( formBeans, new Comparator()
    {
      public int compare( Object object, Object object1 )
      {
        PendingWinnerFormBean bean1 = (PendingWinnerFormBean)object;
        PendingWinnerFormBean bean2 = (PendingWinnerFormBean)object1;

        return bean1.getWinnerType().compareTo( bean2.getWinnerType() );
      }
    } );
    Set invalidCountries = getInvalidCountriesForWinners( sweepstake.getWinners(), formBeans );

    this.setWinnerBeans( formBeans );
    return invalidCountries;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();
    Set invalidCountries = new HashSet();

    if ( getWinnerBeansCount() > 0 )
    {
      // make sure no winner is both remove and replace
      for ( Iterator iter = getWinnerBeans().iterator(); iter.hasNext(); )
      {
        PendingWinnerFormBean bean = (PendingWinnerFormBean)iter.next();
        if ( bean.isRemove() && bean.isReplace() )
        {
          actionErrors.add( "winnerBean", new ActionMessage( "promotion.sweepstakes.pending.winners.BOTH_REMOVE_AND_REPLACE" ) );
          break;
        }
        else if ( method.equals( "process" ) && ( bean.isRemove() || bean.isReplace() ) )
        {
          // display message about processing award with winners checked
          actionErrors.add( "winnerBean", new ActionMessage( "promotion.sweepstakes.pending.winners.PROCESS_REMOVE_OR_REPLACE_ERROR" ) );
          break;
        }
        if ( !bean.isRemove() && !bean.isReplace() && !bean.isValidCountry() )
        {
          invalidCountries.add( bean.getCountryDisplayValue() );
        }
      }
    }
    for ( Iterator countryIter = invalidCountries.iterator(); countryIter.hasNext(); )
    {
      String countryName = (String)countryIter.next();
      actionErrors.add( "winnerBean", new ActionMessage( "promotion.sweepstakes.pending.winners.INVALID_COUNTRY", countryName ) );

    }

    return actionErrors;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    winnerBeans = new ArrayList();

    int count = RequestUtils.getOptionalParamInt( request, "winnerBeansCount" );

    for ( int i = 0; i < count; i++ )
    {
      winnerBeans.add( new PendingWinnerFormBean() );
    }
  } // end reset

  public List getWinnerBeans()
  {
    return winnerBeans;
  }

  public void setWinnerBeans( List winnerBeans )
  {
    this.winnerBeans = winnerBeans;
  }

  public int getWinnerBeansCount()
  {
    if ( winnerBeans != null )
    {
      return winnerBeans.size();
    }
    return 0;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PendingWinnerFormBean from the value list
   */
  public PendingWinnerFormBean getWinnerBean( int index )
  {
    try
    {
      return (PendingWinnerFormBean)winnerBeans.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getSweepstakesEndDate()
  {
    return sweepstakesEndDate;
  }

  public void setSweepstakesEndDate( String sweepstakesEndDate )
  {
    this.sweepstakesEndDate = sweepstakesEndDate;
  }

  public String getSweepstakesStartDate()
  {
    return sweepstakesStartDate;
  }

  public void setSweepstakesStartDate( String sweepstakesStartDate )
  {
    this.sweepstakesStartDate = sweepstakesStartDate;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  /**
   * @param winners
   * @return List of FormBeans
   */
  private List loadWinnersIntoFormBeans( List winners ) /* Bug # 34020 changed from Set to List */
  {
    List formBeans = new ArrayList();
    for ( Iterator iter = winners.iterator(); iter.hasNext(); )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
      if ( !winner.isRemoved() )
      { // skip winners that have been removed
        Participant pax = winner.getParticipant();
        String paxDesc = "";
        // Bug # 35081
        if ( pax != null )
        {
          paxDesc = pax.getLastName() + ", " + pax.getFirstName();
          if ( pax.getMiddleName() != null )
          {
            paxDesc = paxDesc + " " + pax.getMiddleName().substring( 0, 1 );
          }
          if ( pax.getPositionType() != null )
          {
            paxDesc = paxDesc + " - " + pax.getPositionType();
          }
          if ( pax.getDepartmentType() != null )
          {
            paxDesc = paxDesc + " - " + pax.getDepartmentType();
          }
        }
        PendingWinnerFormBean formBean = new PendingWinnerFormBean();
        formBean.setId( winner.getId().toString() );
        formBean.setDescription( paxDesc );
        formBean.setWinnerType( winner.getWinnerType() );
        formBean.setRemove( false );
        formBean.setReplace( false );
        UserAddress paxPrimaryAddress = null;
        if ( pax != null )
        {
          paxPrimaryAddress = getUserService().getPrimaryUserAddress( pax.getId() );
        }
        if ( pax != null && paxPrimaryAddress != null && paxPrimaryAddress.getAddress() != null && paxPrimaryAddress.getAddress().getCountry() != null )
        {
          formBean.setCountryCode( paxPrimaryAddress.getAddress().getCountry().getCountryCode() );
          formBean.setCountryDisplayValue( paxPrimaryAddress.getAddress().getCountry().getI18nCountryName() );
        }
        formBeans.add( formBean );
      }
    }
    return formBeans;
  }

  private Set getInvalidCountriesForWinners( Set winners, List winnerFormBeans )
  {
    Set invalidCountries = new HashSet();
    if ( winners != null && !winners.isEmpty() )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)winners.iterator().next();
      Promotion promotion = winner.getPromotionSweepstake().getPromotion();
      if ( !promotion.isRecognitionPromotion() || ! ( (RecognitionPromotion)promotion ).isAwardActive() || ! ( (RecognitionPromotion)promotion ).isAwardStructureLevel() )
      {
        return invalidCountries;
      }

      for ( Iterator iter = winners.iterator(); iter.hasNext(); )
      {
        winner = (PromotionSweepstakeWinner)iter.next();
        RecognitionPromotion recognitionPromotion = (RecognitionPromotion)winner.getPromotionSweepstake().getPromotion();
        Participant pax = winner.getParticipant();
        User participant = null;
        if ( pax != null )
        {
          AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
          associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
          associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
          associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );
          participant = getUserService().getUserByIdWithAssociations( pax.getId(), associationRequestCollection );
        }
        if ( participant != null )
        {
          if ( participant.getPrimaryAddress() != null && participant.getPrimaryAddress().getAddress() != null && participant.getPrimaryAddress().getAddress().getCountry() != null )
          {
            Country paxCountry = participant.getPrimaryAddress().getAddress().getCountry();
            if ( recognitionPromotion.getPromoMerchCountryForCountryCode( paxCountry.getCountryCode() ) == null )
            {
              if ( !winner.isRemoved() )
              {
                invalidCountries.add( paxCountry );
              }
              for ( Iterator winnerFormBeanIter = winnerFormBeans.iterator(); winnerFormBeanIter.hasNext(); )
              {
                PendingWinnerFormBean pendingWinnerFormBean = (PendingWinnerFormBean)winnerFormBeanIter.next();
                if ( pendingWinnerFormBean.getIdAsLong().equals( winner.getId() ) )
                {
                  pendingWinnerFormBean.setValidCountry( false );

                }
              }
            }
          }
        }
      }
    }
    return invalidCountries;
  }

  /* ***** Bug # 34020 start */
  /**
   * Get the PromotionSweepstakeService from the beanLocator.
   * 
   * @return PromotionSweepstakeService
   */
  private PromotionSweepstakeService getPromotionSweepstakeService()
  {
    return (PromotionSweepstakeService)BeanLocator.getBean( PromotionSweepstakeService.BEAN_NAME );
  }
  /* ***** Bug # 34020 end */

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

}
