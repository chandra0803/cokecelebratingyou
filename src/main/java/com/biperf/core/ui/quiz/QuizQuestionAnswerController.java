/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/quiz/QuizQuestionAnswerController.java,v $
 */

package com.biperf.core.ui.quiz;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.BaseController;

/*
 * QuizQuestionAnswerController <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Oct
 * 28, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class QuizQuestionAnswerController extends BaseController
{
  /**
   * Method associated to a tile and called immediately before the tile is included. Use onExecute
   * like you would normally use execute().
   * 
   * @param tileContext Current tile context.
   * @param request Current request
   * @param response Current response
   * @param servletContext Current servlet context
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // Empty block
  }
}
