
package com.biperf.core.ui.promotion;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.hibernate.LazyInitializationException;

import com.biperf.core.domain.enums.NominationWizardOrder;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionWizard;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PromotionWizardForm extends BaseActionForm
{
  private Long promotionId;
  private String method;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionName;
  private String awardGroupType;
  private String awardGroupOrder;
  private String nomineeOrder;
  private String behaviourOrder;
  private String eCardOrder;
  private String whyOrder;
  private String why;
  private Long claimFormId;
  private List claimFormSteps = new ArrayList();

  private boolean commentsActive;
  private boolean behaviorActive;
  private boolean eCardActive;

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    Set<String> wizardOrder = new HashSet<String>();
    List<String> duplicates = new ArrayList<String>();
    boolean orderEmpty = false;

    if ( awardGroupOrder != null )
    {
      if ( awardGroupOrder.length() != 0 )
      {
        if ( "0".equals( awardGroupOrder ) )
        {
          errors.add( "PromotionWizardForm", new ActionMessage( "promotion.wizard.INVALID_WIZARD_ORDER" ) );
        }
        else if ( !wizardOrder.add( awardGroupOrder ) )
        {
          duplicates.add( awardGroupOrder );
        }
      }
      else
      {
        orderEmpty = true;
      }
    }

    if ( nomineeOrder != null )
    {
      if ( nomineeOrder.length() != 0 )
      {
        if ( "0".equals( nomineeOrder ) )
        {
          errors.add( "PromotionWizardForm", new ActionMessage( "promotion.wizard.INVALID_WIZARD_ORDER" ) );
        }
        else if ( !wizardOrder.add( nomineeOrder ) )
        {
          duplicates.add( nomineeOrder );
        }
      }
      else
      {
        orderEmpty = true;
      }
    }

    if ( this.behaviorActive && behaviourOrder != null )
    {
      if ( behaviourOrder.length() != 0 )
      {
        if ( "0".equals( behaviourOrder ) )
        {
          errors.add( "PromotionWizardForm", new ActionMessage( "promotion.wizard.INVALID_WIZARD_ORDER" ) );
        }
        else if ( !wizardOrder.add( behaviourOrder ) )
        {
          duplicates.add( behaviourOrder );
        }
      }
      else
      {
        orderEmpty = true;
      }
    }

    if ( this.eCardActive && eCardOrder != null )
    {
      if ( eCardOrder.length() != 0 )
      {
        if ( "0".equals( eCardOrder ) )
        {
          errors.add( "PromotionWizardForm", new ActionMessage( "promotion.wizard.INVALID_WIZARD_ORDER" ) );
        }
        else if ( !wizardOrder.add( eCardOrder ) )
        {
          duplicates.add( eCardOrder );
        }
      }
      else
      {
        orderEmpty = true;
      }
    }

    if ( whyOrder != null )
    {
      if ( whyOrder.length() != 0 )
      {
        if ( "0".equals( whyOrder ) )
        {
          errors.add( "PromotionWizardForm", new ActionMessage( "promotion.wizard.INVALID_WIZARD_ORDER" ) );
        }
        else if ( !wizardOrder.add( whyOrder ) )
        {
          duplicates.add( whyOrder );
        }
      }
      else
      {
        orderEmpty = true;
      }
    }
    if ( this.commentsActive )
    {
      if ( StringUtils.isEmpty( why ) )
      {
        errors.add( "PromotionWizardForm", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.wizard.WHY_REQUIRED" ) ) );
      }
    }

    if ( orderEmpty )
    {
      errors.add( "PromotionWizardForm", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.wizard.ORDER_REQUIRED" ) ) );
    }

    if ( duplicates.size() > 0 )
    {
      String duplicateValues = StringUtils.join( duplicates, "," );
      String message = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "promotion.wizard.DUPLICATE_WIZARD_ORDER" ), new Object[] { duplicateValues } );
      errors.add( "PromotionWizardForm", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_EMPTY, message ) );
    }
    return errors;
  }

  public void load( Promotion promotion )
  {
    NominationPromotion nominationPromotion = (NominationPromotion)promotion;
    this.promotionId = promotion.getId();
    this.commentsActive = nominationPromotion.isWhyNomination();
    this.behaviorActive = nominationPromotion.isBehaviorActive();
    this.eCardActive = nominationPromotion.isCardActive() || nominationPromotion.isCertificateActive() && !nominationPromotion.isOneCertPerPromotion();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionName = promotion.getName();
    this.claimFormId = nominationPromotion.getClaimForm().getId();
    this.awardGroupType = nominationPromotion.getAwardGroupType().getName() != null ? nominationPromotion.getAwardGroupType().getName().toLowerCase() : "";
    try
    {
      Set<PromotionWizard> promotionWizards = promotion.getPromotionWizardOrder();
      if ( promotionWizards != null && promotionWizards.size() > 0 )
      {
        Iterator wizards = promotionWizards.iterator();
        Map<String, String> wizardsMap = new HashMap<String, String>();
        while ( wizards.hasNext() )
        {
          PromotionWizard wizard = (PromotionWizard)wizards.next();
          wizardsMap.put( wizard.getWizardOrderName(), wizard.getWizardOrder() );
        }

        this.behaviourOrder = wizardsMap.get( NominationWizardOrder.BEHAVIOUR );
        this.eCardOrder = wizardsMap.get( NominationWizardOrder.ECARD );
        this.whyOrder = wizardsMap.get( NominationWizardOrder.WHY );
        if ( wizardsMap.containsKey( NominationWizardOrder.WHY_AFTER ) )
        {
          this.why = NominationWizardOrder.WHY_AFTER;
        }
        else
        {
          this.why = NominationWizardOrder.WHY_BEFORE;
        }
      }
      else
      {
        this.why = NominationWizardOrder.WHY_BEFORE;
      }
    }
    catch( LazyInitializationException e )
    {
    }
  }

  public Promotion toDomainObject( Promotion promotion )
  {
    List<PromotionWizard> wizards = new ArrayList<PromotionWizard>();

    if ( this.awardGroupOrder != null && !this.awardGroupOrder.isEmpty() )
    {
      wizards.add( new PromotionWizard( NominationWizardOrder.INDIVIDUAL_OR_TEAM, this.awardGroupOrder ) );
    }
    if ( this.nomineeOrder != null && !this.nomineeOrder.isEmpty() )
    {
      wizards.add( new PromotionWizard( NominationWizardOrder.NOMINEE, this.nomineeOrder ) );
    }
    if ( this.behaviourOrder != null && !this.behaviourOrder.isEmpty() )
    {
      wizards.add( new PromotionWizard( NominationWizardOrder.BEHAVIOUR, this.behaviourOrder ) );
    }
    if ( this.eCardOrder != null && !this.eCardOrder.isEmpty() )
    {
      wizards.add( new PromotionWizard( NominationWizardOrder.ECARD, this.eCardOrder ) );
    }
    if ( this.whyOrder != null && !this.whyOrder.isEmpty() )
    {
      wizards.add( new PromotionWizard( NominationWizardOrder.WHY, this.whyOrder ) );
    }
    if ( this.why != null )
    {
      wizards.add( new PromotionWizard( NominationWizardOrder.lookup( this.why ).getCode(), null ) );
    }

    promotion.setPromotionWizardOrder( new HashSet<PromotionWizard>( wizards ) );

    return promotion;
  }

  public boolean isValidWizardOrder( Promotion promotion )
  {
    boolean valid = promotion.getPromotionWizardOrder() != null && promotion.getPromotionWizardOrder().size() > 0;

    if ( !valid )
    {
      return valid;
    }

    int wizardsSize = 3; // since team or individual, nominee and why are always enabled
    if ( ( (NominationPromotion)promotion ).isBehaviorActive() )
    {
      wizardsSize++;
    }
    if ( ( (NominationPromotion)promotion ).isCardActive() || ( (NominationPromotion)promotion ).isCertificateActive() && ! ( (NominationPromotion)promotion ).isOneCertPerPromotion() )
    {
      wizardsSize++;
    }

    Set<PromotionWizard> wizards = promotion.getPromotionWizardOrder();

    Set<String> wizardOrder = new HashSet<String>();
    List<String> duplicates = new ArrayList<String>();
    List<Integer> order = new ArrayList<Integer>();

    for ( PromotionWizard promoWizard : wizards )
    {
      String wizOrder = promoWizard.getWizardOrder();
      if ( wizOrder != null )
      {
        order.add( Integer.parseInt( wizOrder ) );
      }

      if ( !wizardOrder.add( wizOrder ) )
      {
        duplicates.add( wizOrder );
      }
    }

    Integer maxOrder = Collections.max( order );

    if ( maxOrder != wizardsSize )
    {
      valid = false;
    }

    if ( duplicates.size() > 0 )
    {
      valid = false;
    }

    return valid;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getAwardGroupOrder()
  {
    return awardGroupOrder;
  }

  public void setAwardGroupOrder( String awardGroupOrder )
  {
    this.awardGroupOrder = awardGroupOrder;
  }

  public String getNomineeOrder()
  {
    return nomineeOrder;
  }

  public void setNomineeOrder( String nomineeOrder )
  {
    this.nomineeOrder = nomineeOrder;
  }

  public String getBehaviourOrder()
  {
    return behaviourOrder;
  }

  public void setBehaviourOrder( String behaviourOrder )
  {
    this.behaviourOrder = behaviourOrder;
  }

  public String geteCardOrder()
  {
    return eCardOrder;
  }

  public void seteCardOrder( String eCardOrder )
  {
    this.eCardOrder = eCardOrder;
  }

  public String getWhyOrder()
  {
    return whyOrder;
  }

  public void setWhyOrder( String whyOrder )
  {
    this.whyOrder = whyOrder;
  }

  public String getWhy()
  {
    return why;
  }

  public void setWhy( String why )
  {
    this.why = why;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isCommentsActive()
  {
    return commentsActive;
  }

  public void setCommentsActive( boolean commentsActive )
  {
    this.commentsActive = commentsActive;
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

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public boolean isBehaviorActive()
  {
    return behaviorActive;
  }

  public void setBehaviorActive( boolean behaviorActive )
  {
    this.behaviorActive = behaviorActive;
  }

  public boolean iseCardActive()
  {
    return eCardActive;
  }

  public void seteCardActive( boolean eCardActive )
  {
    this.eCardActive = eCardActive;
  }

  public Long getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( Long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public List getClaimFormSteps()
  {
    return claimFormSteps;
  }

  public void setClaimFormSteps( List claimFormSteps )
  {
    this.claimFormSteps = claimFormSteps;
  }

  public String getAwardGroupType()
  {
    return awardGroupType;
  }

  public void setAwardGroupType( String awardGroupType )
  {
    this.awardGroupType = awardGroupType;
  }
}
