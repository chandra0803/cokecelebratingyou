/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/proxy/ProxyDetailForm.java,v $
 */

package com.biperf.core.ui.proxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyModule;
import com.biperf.core.domain.proxy.ProxyModulePromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.UserProxyUtils;
import com.biperf.core.value.PickListValueBean;
import com.biperf.util.StringUtils;

/**
 * Proxy Detail ActionForm transfer object.
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
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class ProxyDetailForm extends BaseActionForm
{
  private String proxyId;
  private String mainUserId;
  private Long mainUserNode;
  private String proxyUserId;
  private String proxyFormattedUserString;
  private String proxyVersion;
  private String method;
  private String proxyName;
  private String prodClaimModuleId;
  private String recModuleId;
  private String nomModuleId;
  private String ssiModuleId;

  public String getSsiModuleId()
  {
    return ssiModuleId;
  }

  public void setSsiModuleId( String ssiModuleId )
  {
    this.ssiModuleId = ssiModuleId;
  }

  private String prodClaimVersion;
  private String recVersion;
  private String nomVersion;
  private String ssiVersion;

  public String getSsiVersion()
  {
    return ssiVersion;
  }

  public void setSsiVersion( String ssiVersion )
  {
    this.ssiVersion = ssiVersion;
  }

  private boolean allModules;
  private String allRecPromos = "none";
  private String allProdClaimPromos = "none";
  private String allNomPromos = "none";
  private String allSsiPromos = "none";

  public String getAllSsiPromos()
  {
    return allSsiPromos;
  }

  public void setAllSsiPromos( String allSsiPromos )
  {
    this.allSsiPromos = allSsiPromos;
  }

  private boolean showCancel;

  private List recognitionProxyList;
  private List prodClaimProxyList;
  private List nominationProxyList;

  private String[] coreAccess;
  private boolean showCoreAccess = true;
  private boolean allowLeaderboard;

  private String id;
  private String firstName;
  private String lastName;
  private String orgName;
  private String departmentName;
  private String jobName;
  private String countryName;
  private String countryCode;
  private String avatarUrl;

  /**
   * Reset all properties to their default values.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // ProxyFormBeans. If this is not done, the form wont initialize
    // properly.
    recognitionProxyList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "recognitionProxyListCount" ) );
    prodClaimProxyList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "prodClaimProxyListCount" ) );
    nominationProxyList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "nominationProxyListCount" ) );
  }

  /**
   * resets the List with empty ProxyForm beans
   *
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty ProxyFormBean
      ProxyFormBean proxyBean = new ProxyFormBean();
      valueList.add( proxyBean );
    }

    return valueList;
  }

  public void load( Proxy proxy, Participant participant, List recPromoList, List prodClaimPromoList, List nomPromoList )
  {
    if ( recPromoList != null && recPromoList.size() > 0 )
    {
      this.recognitionProxyList = createProxyList( recPromoList, PromotionType.RECOGNITION );
    }
    if ( prodClaimPromoList != null && prodClaimPromoList.size() > 0 )
    {
      this.prodClaimProxyList = createProxyList( prodClaimPromoList, PromotionType.PRODUCT_CLAIM );
    }
    if ( nomPromoList != null && nomPromoList.size() > 0 )
    {
      this.nominationProxyList = createProxyList( nomPromoList, PromotionType.NOMINATION );
    }

    if ( participant != null && participant.getUserNodes() != null )
    {
      Iterator userNodeIter = participant.getUserNodes().iterator();
      if ( userNodeIter.hasNext() )
      {
        this.mainUserNode = ( (UserNode)userNodeIter.next() ).getNode().getId();
      }
    }

    if ( proxy != null )
    {
      this.proxyId = String.valueOf( proxy.getId() );
      this.mainUserId = String.valueOf( proxy.getUser().getId() );

      this.proxyUserId = String.valueOf( proxy.getProxyUser().getId() );
      this.proxyFormattedUserString = proxy.getProxyUser().getLastName() + ", " + proxy.getProxyUser().getFirstName();
      if ( proxy.getProxyUser().isParticipant() )
      {
        Participant proxyPax = (Participant)proxy.getProxyUser();
        PickListValueBean pickListDeptValueBean = getUserService().getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                               proxyPax.getLanguageType() == null
                                                                                                   ? UserManager.getDefaultLocale().toString()
                                                                                                   : proxyPax.getLanguageType().getCode(),
                                                                                                   proxyPax.getDepartmentType() );
        PickListValueBean pickListPositionValueBean = getUserService().getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                                                                                   proxyPax.getLanguageType() == null
                                                                                                       ? UserManager.getDefaultLocale().toString()
                                                                                                       : proxyPax.getLanguageType().getCode(),
                                                                                                       proxyPax.getPositionType() );

        if ( proxyPax.getPositionType() != null )
        {
          this.proxyFormattedUserString += " - " + pickListPositionValueBean.getName();
        }
        if ( proxyPax.getDepartmentType() != null )
        {
          this.proxyFormattedUserString += " - " + pickListDeptValueBean.getName();
        }
      }
      this.proxyVersion = String.valueOf( proxy.getVersion() );
      this.allModules = proxy.isAllModules();
      this.coreAccess = UserProxyUtils.toCoreAccessList( proxy.getCoreAccess() );
      this.allowLeaderboard = proxy.isAllowLeaderboard();
      if ( proxy.getProxyModules() != null && proxy.getProxyModules().size() > 0 )
      {
        Iterator proxyModuleIter = proxy.getProxyModules().iterator();
        while ( proxyModuleIter.hasNext() )
        {
          ProxyModule proxyModule = (ProxyModule)proxyModuleIter.next();
          if ( proxyModule.getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM ) )
          {
            if ( proxyModule.isAllPromotions() )
            {
              this.allProdClaimPromos = "all";
            }
            else
            {
              if ( proxyModule.getProxyModulePromotions() != null && proxyModule.getProxyModulePromotions().size() > 0 )
              {
                this.allProdClaimPromos = "specific";
                selectPromotions( prodClaimProxyList, proxyModule.getProxyModulePromotions() );
              }
            }
          }
          if ( proxyModule.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) )
          {
            if ( proxyModule.isAllPromotions() )
            {
              this.allRecPromos = "all";
            }
            else
            {
              if ( proxyModule.getProxyModulePromotions() != null && proxyModule.getProxyModulePromotions().size() > 0 )
              {
                this.allRecPromos = "specific";
                selectPromotions( recognitionProxyList, proxyModule.getProxyModulePromotions() );
              }
            }
          }
          if ( proxyModule.getPromotionType().getCode().equals( PromotionType.NOMINATION ) )
          {
            if ( proxyModule.isAllPromotions() )
            {
              this.allNomPromos = "all";
            }
            else
            {
              if ( proxyModule.getProxyModulePromotions() != null && proxyModule.getProxyModulePromotions().size() > 0 )
              {
                this.allNomPromos = "specific";
                selectPromotions( nominationProxyList, proxyModule.getProxyModulePromotions() );
              }
            }
          }
          if ( proxyModule.getPromotionType().getCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
          {
            if ( proxyModule.isAllPromotions() )
            {
              this.allSsiPromos = "all";
            }
            else
            {
              if ( proxyModule.getProxyModulePromotions() != null && proxyModule.getProxyModulePromotions().size() > 0 )
              {
                this.allSsiPromos = "specific";
                selectPromotions( nominationProxyList, proxyModule.getProxyModulePromotions() );
              }
            }
          }
        }
      }
    }
  }

  private void selectPromotions( List fullList, Set selectedList )
  {
    Iterator selectedListIter = selectedList.iterator();
    while ( selectedListIter.hasNext() )
    {
      ProxyModulePromotion proxyPromo = (ProxyModulePromotion)selectedListIter.next();
      Iterator fullListIter = fullList.iterator();
      while ( fullListIter.hasNext() )
      {
        ProxyFormBean proxyBean = (ProxyFormBean)fullListIter.next();
        if ( proxyBean.getPromotionId().equals( String.valueOf( proxyPromo.getPromotion().getId() ) ) )
        {
          proxyBean.setProxyModulePromoId( String.valueOf( proxyPromo.getId() ) );
          proxyBean.setSelected( true );
          break;
        }
      }
    }
  }

  private List createProxyList( List proxyPromoList, String promotionType )
  {
    List returnList = new ArrayList();

    Iterator proxyPromoListIter = proxyPromoList.iterator();
    while ( proxyPromoListIter.hasNext() )
    {
      Promotion promotion = (Promotion)proxyPromoListIter.next();
      // Do not add File Load Recognition promotions to the list
      if ( ! ( PromotionType.RECOGNITION.equals( promotionType ) && promotion.isFileLoadEntry() ) )
      {
        ProxyFormBean proxyFormBean = new ProxyFormBean();
        proxyFormBean.setPromotionId( String.valueOf( promotion.getId() ) );
        proxyFormBean.setPromotionName( promotion.getName() );
        returnList.add( proxyFormBean );
      }
    }

    return returnList;
  }

  public Proxy toDomainObject()
  {
    // Create a new User with the user Id, this will be looked up in the update association
    Participant user = new Participant();
    user.setId( new Long( mainUserId ) );

    // Create a new User with the proxy user Id, this will be looked up in the update association
    Participant proxyUser = new Participant();
    proxyUser.setId( new Long( proxyUserId ) );

    Proxy proxy = new Proxy();
    if ( !StringUtils.isEmpty( proxyId ) )
    {
      proxy.setId( new Long( proxyId ) );
    }
    proxy.setUser( user );
    proxy.setProxyUser( proxyUser );
    proxy.setAllModules( allModules );
    proxy.setCoreAccess( UserProxyUtils.toCoreAccess( coreAccess ) );
    proxy.setAllowLeaderboard( allowLeaderboard );

    if ( !StringUtils.isEmpty( proxyVersion ) )
    {
      proxy.setVersion( new Long( proxyVersion ) );
    }
    if ( !allModules )
    {
      if ( !allProdClaimPromos.equals( "none" ) )
      {
        ProxyModule prodClaimProxyModule = new ProxyModule();
        if ( !StringUtils.isEmpty( prodClaimModuleId ) )
        {
          prodClaimProxyModule.setId( new Long( prodClaimModuleId ) );
        }
        if ( !StringUtils.isEmpty( prodClaimVersion ) )
        {
          prodClaimProxyModule.setVersion( new Long( prodClaimVersion ) );
        }
        if ( allProdClaimPromos.equals( "all" ) )
        {
          prodClaimProxyModule.setAllPromotions( true );
        }
        else
        {
          prodClaimProxyModule.setAllPromotions( false );
        }
        prodClaimProxyModule.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
        if ( allProdClaimPromos.equals( "specific" ) )
        {
          if ( prodClaimProxyList != null && prodClaimProxyList.size() > 0 )
          {
            addProxyModulePromotions( prodClaimProxyModule, prodClaimProxyList, PromotionType.PRODUCT_CLAIM );
          }
        }
        proxy.addProxyModule( prodClaimProxyModule );
      }
      if ( !allRecPromos.equals( "none" ) )
      {
        ProxyModule recProxyModule = new ProxyModule();
        if ( !StringUtils.isEmpty( recModuleId ) )
        {
          recProxyModule.setId( new Long( recModuleId ) );
        }
        if ( !StringUtils.isEmpty( recVersion ) )
        {
          recProxyModule.setVersion( new Long( recVersion ) );
        }
        if ( allRecPromos.equals( "all" ) )
        {
          recProxyModule.setAllPromotions( true );
        }
        else
        {
          recProxyModule.setAllPromotions( false );
        }
        recProxyModule.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
        if ( allRecPromos.equals( "specific" ) )
        {
          if ( recognitionProxyList != null && recognitionProxyList.size() > 0 )
          {
            addProxyModulePromotions( recProxyModule, recognitionProxyList, PromotionType.RECOGNITION );
          }
        }
        proxy.addProxyModule( recProxyModule );
      }
      if ( !allNomPromos.equals( "none" ) )
      {
        ProxyModule nomProxyModule = new ProxyModule();
        if ( !StringUtils.isEmpty( nomModuleId ) )
        {
          nomProxyModule.setId( new Long( nomModuleId ) );
        }
        if ( !StringUtils.isEmpty( nomVersion ) )
        {
          nomProxyModule.setVersion( new Long( nomVersion ) );
        }
        if ( allNomPromos.equals( "all" ) )
        {
          nomProxyModule.setAllPromotions( true );
        }
        else
        {
          nomProxyModule.setAllPromotions( false );
        }
        nomProxyModule.setPromotionType( PromotionType.lookup( PromotionType.NOMINATION ) );
        if ( allNomPromos.equals( "specific" ) )
        {
          if ( nominationProxyList != null && nominationProxyList.size() > 0 )
          {
            addProxyModulePromotions( nomProxyModule, nominationProxyList, PromotionType.NOMINATION );
          }
        }
        proxy.addProxyModule( nomProxyModule );
      }

      // ***************************** change ssi here*************************//
      // *****************SSI START****************
      /*
       * if ( !allSsiPromos.equals( "none" ) ) { ProxyModule ssiProxyModule = new ProxyModule(); if
       * ( !StringUtils.isEmpty( ssiModuleId ) ) { ssiProxyModule.setId( new Long( ssiModuleId ) );
       * } if ( !StringUtils.isEmpty( ssiVersion ) ) { ssiProxyModule.setVersion( new Long(
       * ssiVersion ) ); } if ( allSsiPromos.equals( "all" ) ) { ssiProxyModule.setAllPromotions(
       * true ); } else { ssiProxyModule.setAllPromotions( false ); }
       * ssiProxyModule.setPromotionType( PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES )
       * ); proxy.addProxyModule( ssiProxyModule ); } //******************SSI END*****************
       */
    }
    return proxy;
  }

  private void addProxyModulePromotions( ProxyModule proxyModule, List promotionList, String promoType )
  {
    Iterator promoListIter = promotionList.iterator();
    while ( promoListIter.hasNext() )
    {
      ProxyFormBean proxyFormBean = (ProxyFormBean)promoListIter.next();
      Promotion promo = null;
      ProxyModulePromotion proxyModulePromotion = new ProxyModulePromotion();

      if ( promoType.equals( PromotionType.PRODUCT_CLAIM ) )
      {
        promo = new ProductClaimPromotion();
      }
      if ( promoType.equals( PromotionType.RECOGNITION ) )
      {
        promo = new RecognitionPromotion();
      }
      if ( promoType.equals( PromotionType.NOMINATION ) )
      {
        promo = new NominationPromotion();
      }
      if ( promoType.equals( PromotionType.SELF_SERV_INCENTIVES ) )
      {
        promo = new SSIPromotion();
      }
      if ( proxyFormBean.isSelected() )
      {
        promo.setId( new Long( proxyFormBean.getPromotionId() ) );
        promo.setName( proxyFormBean.getPromotionName() );
        proxyModulePromotion.setPromotion( promo );
        proxyModule.addProxyModulePromotion( proxyModulePromotion );
      }
    }
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   *
   * @param mapping the mapping used to select this instance.
   * @param request the servlet request we are processing.
   * @return <code>ActionErrors</code> object that encapsulates any validation errors.
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {

    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( StringUtils.isEmpty( proxyUserId ) )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "proxy.detail.errors.NO_PROXY_USER" ) );
    }

    if ( showCoreAccess )
    {
      if ( allProdClaimPromos.equals( "none" ) && allRecPromos.equals( "none" ) && allNomPromos.equals( "none" ) && allSsiPromos.equals( "none" ) && !allModules
          && ( null == coreAccess || coreAccess.length <= 0 ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "proxy.detail.errors.NO_MODULE_CORE_ACCESS" ) );
      }
    }
    else
    {
      if ( allProdClaimPromos.equals( "none" ) && allRecPromos.equals( "none" ) && allNomPromos.equals( "none" ) && !allModules )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "proxy.detail.errors.NO_MODULE" ) );
      }
    }

    if ( allProdClaimPromos.equals( "specific" ) )
    {
      boolean promotionSelected = false;
      Iterator prodClaimPromoIter = prodClaimProxyList.iterator();
      while ( prodClaimPromoIter.hasNext() )
      {
        ProxyFormBean proxyBean = (ProxyFormBean)prodClaimPromoIter.next();
        if ( proxyBean.isSelected() )
        {
          promotionSelected = true;
          break;
        }
      }

      if ( !promotionSelected )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "proxy.detail.errors.NO_PROD_CLAIM_PROMOS" ) );
      }
    }

    if ( allRecPromos.equals( "specific" ) )
    {
      boolean promotionSelected = false;
      Iterator recPromoIter = recognitionProxyList.iterator();
      while ( recPromoIter.hasNext() )
      {
        ProxyFormBean proxyBean = (ProxyFormBean)recPromoIter.next();
        if ( proxyBean.isSelected() )
        {
          promotionSelected = true;
          break;
        }
      }

      if ( !promotionSelected )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "proxy.detail.errors.NO_REC_PROMOS" ) );
      }
    }

    if ( allNomPromos.equals( "specific" ) )
    {
      boolean promotionSelected = false;
      Iterator nomPromoIter = nominationProxyList.iterator();
      while ( nomPromoIter.hasNext() )
      {
        ProxyFormBean proxyBean = (ProxyFormBean)nomPromoIter.next();
        if ( proxyBean.isSelected() )
        {
          promotionSelected = true;
          break;
        }
      }

      if ( !promotionSelected )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "proxy.detail.errors.NO_NOM_PROMOS" ) );
      }
    }

    return errors;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMainUserId()
  {
    return mainUserId;
  }

  public void setMainUserId( String userId )
  {
    this.mainUserId = userId;
  }

  public String getProxyId()
  {
    return proxyId;
  }

  public void setProxyId( String proxyId )
  {
    this.proxyId = proxyId;
  }

  public boolean isAllModules()
  {
    return allModules;
  }

  public void setAllModules( boolean allModules )
  {
    this.allModules = allModules;
  }

  public String getProxyName()
  {
    return proxyName;
  }

  public void setProxyName( String proxyName )
  {
    this.proxyName = proxyName;
  }

  public String getProxyUserId()
  {
    return proxyUserId;
  }

  public void setProxyUserId( String proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  public List getRecognitionProxyAsList()
  {
    return recognitionProxyList;
  }

  public void setRecognitionProxyAsList( List recognitionProxyList )
  {
    this.recognitionProxyList = recognitionProxyList;
  }

  /**
   * Accessor for the number of PromotionNotificationFormBean objects in the list.
   *
   * @return int
   */
  public int getRecognitionProxyListCount()
  {
    if ( recognitionProxyList == null )
    {
      return 0;
    }

    return recognitionProxyList.size();
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of ProxyFormBean from the value list
   */
  public ProxyFormBean getRecognitionProxyList( int index )
  {
    try
    {
      return (ProxyFormBean)recognitionProxyList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public List getProdClaimProxyAsList()
  {
    return prodClaimProxyList;
  }

  public void setProdClaimProxyAsList( List prodClaimProxyList )
  {
    this.prodClaimProxyList = prodClaimProxyList;
  }

  /**
   * Accessor for the number of PromotionNotificationFormBean objects in the list.
   *
   * @return int
   */
  public int getProdClaimProxyListCount()
  {
    if ( prodClaimProxyList == null )
    {
      return 0;
    }

    return prodClaimProxyList.size();
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of ProxyFormBean from the value list
   */
  public ProxyFormBean getProdClaimProxyList( int index )
  {
    try
    {
      return (ProxyFormBean)prodClaimProxyList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getAllProdClaimPromos()
  {
    return allProdClaimPromos;
  }

  public void setAllProdClaimPromos( String allProdClaimPromos )
  {
    this.allProdClaimPromos = allProdClaimPromos;
  }

  public String getAllRecPromos()
  {
    return allRecPromos;
  }

  public void setAllRecPromos( String allRecPromos )
  {
    this.allRecPromos = allRecPromos;
  }

  public String getProdClaimModuleId()
  {
    return prodClaimModuleId;
  }

  public void setProdClaimModuleId( String prodClaimModuleId )
  {
    this.prodClaimModuleId = prodClaimModuleId;
  }

  public String getRecModuleId()
  {
    return recModuleId;
  }

  public void setRecModuleId( String recModuleId )
  {
    this.recModuleId = recModuleId;
  }

  public String getProdClaimVersion()
  {
    return prodClaimVersion;
  }

  public void setProdClaimVersion( String prodClaimVersion )
  {
    this.prodClaimVersion = prodClaimVersion;
  }

  public String getRecVersion()
  {
    return recVersion;
  }

  public void setRecVersion( String recVersion )
  {
    this.recVersion = recVersion;
  }

  public String getProxyVersion()
  {
    return proxyVersion;
  }

  public void setProxyVersion( String proxyVersion )
  {
    this.proxyVersion = proxyVersion;
  }

  public String getProxyFormattedUserString()
  {
    return proxyFormattedUserString;
  }

  public void setProxyFormattedUserString( String proxyFormattedUserString )
  {
    this.proxyFormattedUserString = proxyFormattedUserString;
  }

  public Long getMainUserNode()
  {
    return mainUserNode;
  }

  public void setMainUserNode( Long mainUserNode )
  {
    this.mainUserNode = mainUserNode;
  }

  public boolean isShowCancel()
  {
    return showCancel;
  }

  public void setShowCancel( boolean showCancel )
  {
    this.showCancel = showCancel;
  }

  public String getAllNomPromos()
  {
    return allNomPromos;
  }

  public void setAllNomPromos( String allNomPromos )
  {
    this.allNomPromos = allNomPromos;
  }

  public String getNomModuleId()
  {
    return nomModuleId;
  }

  public void setNomModuleId( String nomModuleId )
  {
    this.nomModuleId = nomModuleId;
  }

  public List getNominationProxyAsList()
  {
    return nominationProxyList;
  }

  public void setNominationProxyAsList( List nominationProxyList )
  {
    this.nominationProxyList = nominationProxyList;
  }

  /**
   * Accessor for the number of PromotionNotificationFormBean objects in the list.
   *
   * @return int
   */
  public int getNominationProxyListCount()
  {
    if ( nominationProxyList == null )
    {
      return 0;
    }

    return nominationProxyList.size();
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of ProxyFormBean from the value list
   */
  public ProxyFormBean getNominationProxyList( int index )
  {
    try
    {
      return (ProxyFormBean)nominationProxyList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getNomVersion()
  {
    return nomVersion;
  }

  public void setNomVersion( String nomVersion )
  {
    this.nomVersion = nomVersion;
  }

  public String[] getCoreAccess()
  {
    return coreAccess;
  }

  public void setCoreAccess( String[] coreAccess )
  {
    this.coreAccess = coreAccess;
  }

  public boolean isShowCoreAccess()
  {
    return showCoreAccess;
  }

  public void setShowCoreAccess( boolean showCoreAccess )
  {
    this.showCoreAccess = showCoreAccess;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public boolean isAllowLeaderboard()
  {
    return allowLeaderboard;
  }

  public void setAllowLeaderboard( boolean allowLeaderboard )
  {
    this.allowLeaderboard = allowLeaderboard;
  }
  
  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

}
