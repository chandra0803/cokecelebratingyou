/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/homepage/hibernate/ModuleAppDAOImpl.java,v $
 */

package com.biperf.core.dao.homepage.hibernate;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.homepage.ModuleAppDAO;
import com.biperf.core.domain.enums.ModuleAppAudienceCodeType;
import com.biperf.core.domain.enums.ModuleAppAudienceType;
import com.biperf.core.domain.enums.ReportCategoryType;
import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.homepage.ModuleApp;

public class ModuleAppDAOImpl extends BaseDAO implements ModuleAppDAO
{
  @Override
  public ModuleApp getModuleAppById( Long moduleAppId )
  {
    ModuleApp moduleApp = (ModuleApp)getSession().get( ModuleApp.class, moduleAppId );
    return moduleApp;
  }

  @Override
  public List<ModuleApp> getAllAudienceSpecificTiles()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.homepage.AllAudienceSpecificModuleApp" );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ModuleApp> getAllForHomePage()
  {
    Criteria criteria = getSession().createCriteria( ModuleApp.class, "moduleApp" );
    Criterion restriction1 = Restrictions.ne( "moduleApp.appAudienceType", ModuleAppAudienceType.lookup( ModuleAppAudienceCodeType.DISABLED ) );
    Criterion restriction2 = Restrictions.ne( "moduleApp.audienceType", ModuleAppAudienceType.lookup( ModuleAppAudienceType.NONE ) );
    criteria.add( Restrictions.or( restriction1, restriction2 ) );
    criteria.addOrder( Order.asc( "name" ) );
    return criteria.list();
  }

  public List<ModuleApp> getAllForDelegateHomePage()
  {
    Criteria criteria = getSession().createCriteria( ModuleApp.class, "moduleApp" );
    Criterion restriction1 = Restrictions.ne( "moduleApp.appAudienceType", ModuleAppAudienceType.lookup( ModuleAppAudienceCodeType.DISABLED ) );
    Criterion restriction2 = Restrictions.ne( "moduleApp.audienceType", ModuleAppAudienceType.lookup( ModuleAppAudienceType.NONE ) );
    criteria.add( Restrictions.or( restriction1, restriction2 ) );
    Object[] moduleArray = new Object[5];
    moduleArray[0] = TileMappingType.lookup( TileMappingType.MANAGER_TOOL_KIT );
    moduleArray[1] = TileMappingType.lookup( TileMappingType.SEARCH );
    moduleArray[3] = TileMappingType.lookup( TileMappingType.LEADER_BOARD );
    moduleArray[4] = TileMappingType.lookup( TileMappingType.PRODUCT_CLAIM );
    criteria.add( Restrictions.in( "moduleApp.tileMappingType", moduleArray ) );
    criteria.addOrder( Order.asc( "name" ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<CrossPromotionalModuleApp> getCrossPromotionalModuleApps()
  {
    Criteria criteria = getSession().createCriteria( CrossPromotionalModuleApp.class );
    criteria.addOrder( Order.asc( "order" ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ModuleApp> getAll()
  {
    Criteria criteria = getSession().createCriteria( ModuleApp.class );
    criteria.addOrder( Order.asc( "name" ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ModuleApp> getAllForAdminSetup()
  {
    Criteria criteria = getSession().createCriteria( ModuleApp.class );
    criteria.add( Restrictions.eq( "adminAudienceSetup", new Boolean( true ) ) );
    criteria.add( Restrictions.ne( "appAudienceType", ModuleAppAudienceCodeType.lookup( ModuleAppAudienceCodeType.DISABLED ) ) );
    criteria.addOrder( Order.asc( "name" ) );
    return criteria.list();
  }

  @Override
  public List getAudienceforModuleApp( Long moduleAppId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.homepage.AudienceforModuleApp" );
    query.setParameter( "moduleAppId", moduleAppId );
    return query.list();
  }

  @Override
  public ModuleApp save( ModuleApp moduleApp )
  {
    getSession().saveOrUpdate( moduleApp );
    return moduleApp;
  }

  @Override
  public void delete( ModuleApp moduleApp )
  {
    getSession().delete( moduleApp );

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ModuleApp> getAllAvailableModuleApps()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.homepage.AllAvailableModuleApps" );
    List<ModuleApp> moduleList = query.list();
    // WE ARE REMOVNG THE WORKHAPPIER MODULE HERE SINCE WE DONT WANT IT TO APPEAR IN THE DROP DOWN
    moduleList.removeIf( app -> ReportCategoryType.WORKHAPPIER.equals( app.getAppName().toLowerCase() ) );
    return moduleList;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public ModuleApp getModuleByAppName( String appName )
  {
    Criteria criteria = getSession().createCriteria( ModuleApp.class, "moduleApp" );
    Criterion restriction = Restrictions.eq( "moduleApp.appName", appName ).ignoreCase();
    criteria.add( restriction );
    List<ModuleApp> moduleList = criteria.list();
    if ( CollectionUtils.isNotEmpty( moduleList ) )
    {
      return moduleList.get( 0 );
    }
    return null;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ModuleApp> getModuleByPriorityOne()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.homepage.AllLargeModuleApp" );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ModuleApp> getModuleByPriorityTwo()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.homepage.AllMediumAndSmallModuleApp" );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ModuleApp> getModuleAppByTileMappingType( String tileMappingType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.homepage.GetModuleAppByTileMappingType" );
    query.setParameter( "tileMappingType", tileMappingType );
    return query.list();
  }

  @Override
  public void updateMEPlusFilterPageSetup( boolean svValue )
  {
    if ( svValue )
    {
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.enableMEPlusFilterPageSetup" ).executeUpdate();
    }
    else
    {
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.disableMEPlusFilterPageSetup" ).executeUpdate();
    }
  }

  @Override
  public void updateRecognitionOnlyFilterPageSetup( boolean svValue )
  {
    if ( !svValue )
    {
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.disableRecognitionOnlyFilterPageSetup" ).executeUpdate();
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.disableRecognitionOnlySVSetup" ).executeUpdate();
      getSession().getNamedQuery( "com.biperf.core.domain.report.disableRecognitionOnlyReportsSetup" ).executeUpdate();
    }
    else
    {
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.enableRecognitionOnlyFilterPageSetup" ).executeUpdate();
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.enableRecognitionOnlySVSetup" ).executeUpdate();
      getSession().getNamedQuery( "com.biperf.core.domain.report.enableRecognitionOnlyReportsSetup" ).executeUpdate();
    }
  }

  @Override
  public void updateSalesMakerFilterPageSetup( boolean svValue )
  {
    // Tiles Setup
    if ( !svValue )
    {
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.disableSalesMakerFilterPageSetup" ).executeUpdate();
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.disableSalesMakerSVSetup" ).executeUpdate();
      getSession().getNamedQuery( "com.biperf.core.domain.report.disableSalesMakerReportsSetup" ).executeUpdate();
    }
    else
    {
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.enableSalesMakerFilterPageSetup" ).executeUpdate();
      getSession().getNamedQuery( "com.biperf.core.domain.homepage.enableSalesMakerSVSetup" ).executeUpdate();
      getSession().getNamedQuery( "com.biperf.core.domain.report.enableSalesMakerReportsSetup" ).executeUpdate();
    }
  }

}
