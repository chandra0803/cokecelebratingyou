
package com.biperf.core.service.home.impl;

import org.jmock.Mock;

import com.biperf.core.dao.homepage.ModuleAppDAO;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.home.FilterAppSetupService;

public class FilterAppSetupServiceTest extends BaseServiceTest
{

  private Mock moduleAppDAO;

  private FilterAppSetupService filterAppSetupService = new FilterAppSetupServiceImpl();

  private FilterAppSetupServiceImpl classUnderTest = new FilterAppSetupServiceImpl();

  protected void setUp() throws Exception
  {
    super.setUp();

    moduleAppDAO = new Mock( ModuleAppDAO.class );
    ( (FilterAppSetupServiceImpl)filterAppSetupService ).setModuleAppDAO( (ModuleAppDAO)moduleAppDAO.proxy() );
    classUnderTest.setModuleAppDAO( (ModuleAppDAO)moduleAppDAO.proxy() );

  }

  public void testUpdateSalesMakerFilterPageSetup()
  {
    moduleAppDAO.expects( once() ).method( "updateSalesMakerFilterPageSetup" ).isVoid();
    classUnderTest.updateSalesMakerFilterPageSetup( new Boolean( true ) );
    moduleAppDAO.expects( once() ).method( "updateSalesMakerFilterPageSetup" ).isVoid();
    classUnderTest.updateSalesMakerFilterPageSetup( new Boolean( false ) );

  }
}