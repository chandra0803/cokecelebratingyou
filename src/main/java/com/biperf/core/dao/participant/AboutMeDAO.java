/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/AboutMeDAO.java,v $
 */

package com.biperf.core.dao.participant;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.participant.AboutMe;

/**
 * AboutMeDAO
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
 * <td>tennant</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface AboutMeDAO extends DAO
{
  public static final String BEAN_NAME = "aboutMeDAO";

  public AboutMe getAboutMeById( Long id );

  public AboutMe saveAboutMe( AboutMe aboutMe );

  public void deleteAboutMe( AboutMe aboutMe );

  public List<AboutMe> getAllAboutMeByUserId( Long userId );

  public AboutMe getAboutMeByUserIdAndCode( Long userId, String code );
}
