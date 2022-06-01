/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/employer/EmployerService.java,v $
 */

package com.biperf.core.service.employer;

import java.util.List;

import com.biperf.core.domain.employer.Employer;
import com.biperf.core.service.SAO;

/**
 * EmployerService.
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
 *
 */
public interface EmployerService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "employerService";

  /**
   * Get all employers from the database through the service.
   * 
   * @return List
   */
  public abstract List getAll();

  /**
   * Search for the employer with the given param.
   * 
   * @param name
   * @return Set
   */
  public abstract List searchEmployer( String name );

  /**
   * Save or update the employer to the database.
   * 
   * @param employer
   * @return Employer
   */
  public abstract Employer saveEmployer( Employer employer );

  /**
   * Gets the employer from the database by the id param.
   * 
   * @param id
   * @return Employer
   */
  public abstract Employer getEmployerById( Long id );

  public abstract List<Employer> getActiveEmployers();

}
