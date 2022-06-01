
package com.biperf.core.ui.help;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

public class HelpController extends BaseController
{

  /**
   * Tiles controller for the ContactUs page
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) Overridden from
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    // ContactUs controller code starts here
    ContactUsForm form = (ContactUsForm)request.getAttribute( "contactUsForm" );
    if ( form == null )
    {
      form = new ContactUsForm();
      request.setAttribute( "contactUsForm", form );
    }
    if ( UserManager.isUserLoggedIn() )
    {
      Long userId = UserManager.getUserId();

      AssociationRequestCollection requestCollection = new AssociationRequestCollection();
      requestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
      requestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
      User user = getUserService().getUserByIdWithAssociations( userId, requestCollection );

      UserEmailAddress userEmailAddress = user.getPrimaryEmailAddress();
      if ( form.getEmailAddress() != null && form.getEmailAddress().equals( "" ) && userEmailAddress != null )
      {
        form.setEmailAddress( userEmailAddress.getEmailAddr() );
      }
      UserAddress userAddress = user.getPrimaryAddress();
      if ( userAddress != null && userAddress.getAddress() != null && userAddress.getAddress().getCountry() != null )
      {
        form.setCountryId( userAddress.getAddress().getCountry().getId() );
      }
      // added below code due to Bug# 24848 START
      form.setFirstName( user.getFirstName() );
      form.setLastName( user.getLastName() );
      // End
    }
    else
    {
      request.setAttribute( "isUserLoggedIn", "no" );
    }
    request.setAttribute( "countryList", getCountryList() );
    request.setAttribute( "secretQuestionList", SecretQuestionType.getList() );
    // ContactUs controller code ends here

  }

  private static Comparator<Country> countrySort = new Comparator<Country>()
  {
    public int compare( Country country1, Country country2 )
    {
      String countryName1 = country1.getI18nCountryName();
      String countryName2 = country2.getI18nCountryName();
      return countryName1.compareTo( countryName2 );
    }
  };

  private List<Country> getCountryList()
  {
    List<Country> activeCountries = getCountryService().getAllActive();
    Collections.sort( activeCountries, countrySort );
    return activeCountries;
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}
