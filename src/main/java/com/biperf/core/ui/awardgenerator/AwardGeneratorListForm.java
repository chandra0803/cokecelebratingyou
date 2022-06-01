
package com.biperf.core.ui.awardgenerator;

import com.biperf.core.ui.BaseForm;

/**
 * AwardGeneratorListForm transfer object.
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
 * <td>Jul 08, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class AwardGeneratorListForm extends BaseForm
{
  private String method;
  private String awardGeneratorId;
  private String[] deleteAwardGeneratorIds;

  public String[] getDeleteAwardGeneratorIds()
  {
    return deleteAwardGeneratorIds;
  }

  public void setDeleteAwardGeneratorIds( String[] deleteAwardGeneratorIds )
  {
    this.deleteAwardGeneratorIds = deleteAwardGeneratorIds;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getAwardGeneratorId()
  {
    return awardGeneratorId;
  }

  public void setAwardGeneratorId( String awardGeneratorId )
  {
    this.awardGeneratorId = awardGeneratorId;
  }

}
