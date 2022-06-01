
package com.biperf.core.ui.nomination;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.nomination.NominationDAO;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

public class NominationsWinnersModuleController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( "approverUserId", UserManager.getUserId() );

    Map<String, Object> prcResult = getNominationDao().nominationsWinnersModule( parameters );

    int pastWinners = ( (BigDecimal)prcResult.get( "p_out_all_elig_winner_flg" ) ).intValue();
    int myWinners = ( (BigDecimal)prcResult.get( "p_out_my_elig_winner_flg" ) ).intValue();

    request.setAttribute( "pastWinners", pastWinners );
    request.setAttribute( "myWinners", myWinners );

  }

  private NominationDAO getNominationDao()
  {
    return (NominationDAO)BeanLocator.getBean( NominationDAO.BEAN_NAME );

  }

}
