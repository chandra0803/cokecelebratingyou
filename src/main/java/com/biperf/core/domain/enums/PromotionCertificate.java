
package com.biperf.core.domain.enums;

import java.util.List;

import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * PromotionCertificate.
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
 * <td>Kaiden Vo</td>
 * <td>Aug 23, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class PromotionCertificate
{
  /**
   * @param promotionCode
   * @return List
   */
  public static List<Content> getList( String promotionCode )
  {
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List certificates = null;

    if ( promotionCode.equals( PromotionType.NOMINATION ) )
    {
      Object contentObject = contentReader.getContent( "report.certificate.nomination" );
      if ( contentObject != null && contentObject instanceof List )
      {
        certificates = (List)contentObject;
      }
    }
    else if ( promotionCode.equals( PromotionType.RECOGNITION ) )
    {
      Object contentObject = contentReader.getContent( "report.certificate.recognition" );
      if ( contentObject != null && contentObject instanceof List )
      {
        certificates = (List)contentObject;
      }
    }
    else if ( promotionCode.equals( PromotionType.QUIZ ) || promotionCode.equals( PromotionType.DIY_QUIZ ) )
    {
      Object contentObject = contentReader.getContent( "report.certificate.quiz" );
      if ( contentObject != null && contentObject instanceof List )
      {
        certificates = (List)contentObject;
      }
    }

    return certificates;
  }

  /**
   * @param promotionType
   * @return List
   */
  public static List getList( PromotionType promotionType )
  {
    return getList( promotionType.getCode() );
  }

}
