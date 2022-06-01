
package com.biperf.core.ui.awardgenerator;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.awardgenerator.AwardGeneratorService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.value.FormattedValueBean;

/**
 * Implements the controller for the AwardGeneratorList page.
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
 * <td>chwodhur</td>
 * <td>Jul 15, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class AwardGeneratorController extends BaseController
{

  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    AwardGeneratorForm form = (AwardGeneratorForm)request.getAttribute( "awardGeneratorForm" );

    List promotionList = getPromotionService().getAwardGenEligiblePromotionList();
    request.setAttribute( "promotionList", promotionList );

    Long awardGenId = form.getAwardGeneratorId();
    List<FormattedValueBean> batchList = getAwardGenBatchList( awardGenId );
    request.setAttribute( "batchList", batchList );
    request.setAttribute( "extractBatchList", batchList );

    List<FormattedValueBean> examineList = getExamineList();
    request.setAttribute( "examineList", examineList );
  }

  private List<FormattedValueBean> getAwardGenBatchList( Long awardGenId )
  {
    return getAwardGeneratorService().getAwardGenBatches( awardGenId );
  }

  private List<FormattedValueBean> getExamineList()

  {
    return getAwardGeneratorService().getExaminerList();
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private AwardGeneratorService getAwardGeneratorService()
  {
    return (AwardGeneratorService)getService( AwardGeneratorService.BEAN_NAME );
  }

}
