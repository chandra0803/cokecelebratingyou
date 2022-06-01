
package com.biperf.core.ui.client;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.client.CareerMomentsView;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.CareerMomentsType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DivisionType;
import com.biperf.core.domain.enums.PurlCelebrationTabType;
import com.biperf.core.service.client.CokeCareerMomentsService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.client.ClientCareerMomentsDropdownValueBean;
import com.biperf.core.value.client.ClientDropdownList;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CokeCareerMomentsPageController extends BaseController
{
  private static final Log logger = LogFactory.getLog( CokeCareerMomentsPageController.class );
  protected static ObjectMapper mapper = new ObjectMapper();

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List<ClientCareerMomentsDropdownValueBean> view = new ArrayList<ClientCareerMomentsDropdownValueBean>();

    ClientCareerMomentsDropdownValueBean bean = new ClientCareerMomentsDropdownValueBean();
    bean.setNameId( PurlCelebrationTabType.lookup( PurlCelebrationTabType.RECOMMENDED_TAB ).getCode() );
    bean.setName( PurlCelebrationTabType.lookup( PurlCelebrationTabType.RECOMMENDED_TAB ).getName() );
    bean.setDefault( true );
    bean.setIsDefault( true );
    view.add( bean );

    ClientCareerMomentsDropdownValueBean beanAll = new ClientCareerMomentsDropdownValueBean();
    beanAll.setName( PurlCelebrationTabType.lookup( PurlCelebrationTabType.GLOBAL_TAB ).getName() );
    beanAll.setNameId( PurlCelebrationTabType.lookup( PurlCelebrationTabType.GLOBAL_TAB ).getCode() );
    view.add( beanAll );

    ClientCareerMomentsDropdownValueBean beanTeam = new ClientCareerMomentsDropdownValueBean();
    beanTeam.setName( PurlCelebrationTabType.lookup( PurlCelebrationTabType.TEAM_TAB ).getName() );
    beanTeam.setNameId( PurlCelebrationTabType.lookup( PurlCelebrationTabType.TEAM_TAB ).getCode() );
    view.add( beanTeam );

    ClientCareerMomentsDropdownValueBean beanFollowed = new ClientCareerMomentsDropdownValueBean();
    beanFollowed.setName( PurlCelebrationTabType.lookup( PurlCelebrationTabType.FOLLOWED_TAB ).getName() );
    beanFollowed.setNameId( PurlCelebrationTabType.lookup( PurlCelebrationTabType.FOLLOWED_TAB ).getCode() );
    view.add( beanFollowed );

    ClientCareerMomentsDropdownValueBean beanCountry = new ClientCareerMomentsDropdownValueBean();
    beanCountry.setName( PurlCelebrationTabType.lookup( PurlCelebrationTabType.COUNTRY_TAB ).getName() );
    beanCountry.setNameId( PurlCelebrationTabType.lookup( PurlCelebrationTabType.COUNTRY_TAB ).getCode() );
    List<Country> countryList = getCountryService().getAllActive();
    List<ClientDropdownList> countryDropdown = new ArrayList<ClientDropdownList>();
    for ( Country country : countryList )
    {
      ClientDropdownList countryItem = new ClientDropdownList();
      countryItem.setCountryCode( country.getCountryCode() );
      countryItem.setCountryName( country.getI18nCountryName() );
      countryItem.setCmAssetCode( country.getCmAssetCode() );
      countryItem.setBudgetMediaValue( country.getBudgetMediaValue() );
      countryItem.setAwardbanqAbbrev( country.getAwardbanqAbbrev() );
      countryDropdown.add( countryItem );
    }
    beanCountry.setList( countryDropdown );
    view.add( beanCountry );

    ClientCareerMomentsDropdownValueBean beanDepartment = new ClientCareerMomentsDropdownValueBean();
    beanDepartment.setName( PurlCelebrationTabType.lookup( PurlCelebrationTabType.DEPARTMENT_TAB ).getName() );
    beanDepartment.setNameId( PurlCelebrationTabType.lookup( PurlCelebrationTabType.DEPARTMENT_TAB ).getCode() );
    beanDepartment.setAdditionalProperty( "list", DepartmentType.getList() );
    view.add( beanDepartment );
    
    ClientCareerMomentsDropdownValueBean beanDivision = new ClientCareerMomentsDropdownValueBean();
    beanDivision.setName( PurlCelebrationTabType.lookup( PurlCelebrationTabType.DIVISION_TAB ).getName() );
    beanDivision.setNameId( PurlCelebrationTabType.lookup( PurlCelebrationTabType.DIVISION_TAB ).getCode() );
    beanDivision.setAdditionalProperty( "list", DivisionType.getList() );
    view.add( beanDivision );

    List optionSelectionList = new ArrayList();
    optionSelectionList.add( PurlCelebrationTabType.GLOBAL_TAB );

    request.setAttribute( "dropdownJson", toJson( view ) );
    request.setAttribute( "optionSelection", PurlCelebrationTabType.getList() );

    //List<CareerMomentsView> dataView = getCokeCareerMomentsService().getCareerMomentsDataForDetail( PurlCelebrationTabType.lookup( PurlCelebrationTabType.GLOBAL_TAB )
    //    .getCode(), 0, CareerMomentsType.lookup( CareerMomentsType.NEW_HIRE ).getCode(), null, UserManager.getUserLocale() );
    //request.setAttribute( "dataView", dataView );
  }

  private CokeCareerMomentsService getCokeCareerMomentsService()
  {
    return (CokeCareerMomentsService)getService( CokeCareerMomentsService.BEAN_NAME );
  }

  protected String toJson( Object bean )
  {
    ObjectMapper mapper = getObjectMapper();
    Writer writer = new StringWriter();

    try
    {
      mapper.writeValue( writer, bean );
    }
    catch( Throwable t )
    {
      logger.error( "\n\n\nERROR!!!\n\n\n" + t.getMessage() );
    }

    return writer.toString();
  }

  protected ObjectMapper getObjectMapper()
  {
    return mapper;
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

}
