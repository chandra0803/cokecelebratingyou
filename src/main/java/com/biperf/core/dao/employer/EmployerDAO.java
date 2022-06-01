/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/employer/EmployerDAO.java,v $
 */

package com.biperf.core.dao.employer;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.employer.Employer;

/**
 * EmployerDAO.
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
 * <td>crosenquest</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface EmployerDAO extends DAO
{

  /**
   * Save the employer to the database.
   * 
   * @param employer
   * @return Employer
   */
  public Employer saveEmployer( Employer employer );

  /**
   * Search for the employer give the param criteria.
   * 
   * @param name
   * @return List
   */
  public List searchEmployer( String name );

  /**
   * Gets the employer by the id.
   * 
   * @param id
   * @return Employer
   */
  public Employer getEmployerById( Long id );

  /**
   * Gets a list of all employers in the database.
   * 
   * @return List
   */
  public List getAll();

  public List<Employer> getActiveEmployers();

}
