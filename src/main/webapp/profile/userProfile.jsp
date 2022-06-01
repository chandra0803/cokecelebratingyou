<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="profilePasswordChange" >
  <html:hidden property="method" value=""/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${profileForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="user.profile"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="user.profile"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
        <br/><br/>
		<span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="user.profile"/>
        </span>
        <cms:errors/>

        <table>
          <tr>
            <%-- column 1 --%>
            <td valign="top">
              <table>
                <tr class="form-row-spacer">
                  <td class="subheadline" colspan="3">
                    <cms:contentText key="PERSONAL_INFORMATION" code="user.profile"/>
                  </td>
                </tr>

                <%-- user name --%>
                <tr class="form-row-spacer">
                  <td class="content-field-label">
                    <cms:contentText key="NAME_LABEL" code="user.profile"/>
                  </td>
                  <td class="content-field-review">
                    <c:out value="${user.firstName}"/>&nbsp;<c:out value="${user.middleName}"/>&nbsp;<c:out value="${user.lastName}"/>
                  </td>
                </tr>

                <tr class="form-blank-row"><td colspan="2"></td></tr>
                
                <%-- street addresses --%>
                <c:forEach items="${user.userAddresses}" var="address">
                  <tr class="form-row-spacer">
                    <td class="content-field-label">
                      <c:out value="${address.addressType.name}"/>&nbsp;
                    </td>
                    <td class="content-field-review">
                      <c:out value="${address.address.addr1}"/>
                    </td>
                  </tr>

                  <c:if test='${address.address.addr2 != null}'>
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-review">
                        <c:out value="${address.address.addr2}"/>
                      </td>
                    </tr>
                  </c:if>

                  <c:if test='${address.address.addr3 != null}'>
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-review">
                        <c:out value="${address.address.addr3}"/>
                      </td>
                    </tr>
                  </c:if>
				
				
                  <c:if test='${address.address.city != null}'>
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-review">
                        <c:out value="${address.address.city}"/>,&nbsp;
                        <c:out value="${address.address.stateType.name}"/>&nbsp;
                        <c:out value="${address.address.postalCode}"/>
                      </td>
                    </tr>
                  </c:if>

                  <c:if test='${address.address.country.countryName != null}'>
                    <tr class="form-row-spacer">
                      <td></td>
                      <td class="content-field-review">
                        <c:out value="${address.address.country.countryName}"/>
                      </td>
                    </tr>
                  </c:if>

                  <tr class="form-blank-row"><td colspan="2"></td></tr>
                </c:forEach>

                <tr class="form-blank-row"><td colspan="2"></td></tr>

                <%-- email addresses --%>
                <c:forEach items="${user.userEmailAddresses}" var="emailAddress">
                  <tr class="form-row-spacer">
                    <td class="content-field-label">
                      <c:out value="${emailAddress.emailType.name}"/>&nbsp;
                      <cms:contentText key="EMAIL_ADDRESS_LABEL" code="user.profile"/>
                    </td>
                    <td class="content-field-review">
                      <c:out value="${emailAddress.emailAddr}"/>
                    </td>
                  </tr>
                </c:forEach>

                <tr class="form-blank-row"><td>&nbsp;</td></tr>

                <%-- telephone numbers --%>
                <c:forEach items="${user.userPhones}" var="phone">
                  <tr class="form-row-spacer">
                    <td class="content-field-label">
                      <c:out value="${phone.phoneType.name}"/>&nbsp;
                      <cms:contentText key="PHONE_LABEL" code="user.profile"/>
                    </td>
                    <td class="content-field-review">
                      <c:out value="${phone.phoneNbr}"/>
                    </td>
                  </tr>
                </c:forEach>
              </table>
            </td>

            <%-- column 2 --%>
            <td width="30">&nbsp;</td>
            <td valign="top">
              <table>


                <%-- login information --%>
                <tr class="form-row-spacer">
                  <td class="subheadline" colspan="2">
                    <cms:contentText key="WEB_SITE_LOGIN_INFO" code="user.profile"/>
                  </td>
                </tr>

                <tr class="form-row-spacer">
                  <td class="content-field-label">
                    <cms:contentText key="USER_NAME_LABEL" code="user.profile"/>
                  </td>
                  <td class="content-field-review">
                    <c:out value="${user.userName}"/>
                  </td>
                </tr>

                <tr class="form-row-spacer">
                  <td class="content-field-label">
                    <cms:contentText key="PASSWORD_LABEL" code="user.profile"/>
                  </td>
                  <td class="content-field">
                    <html:password property="newPassword" size="30" maxlength="40" styleClass="content-field"/>
                  </td>
                </tr>

                <tr class="form-row-spacer">
                  <td class="content-field-label">
                    <cms:contentText key="CONFIRM_PASSWORD_LABEL" code="user.profile"/>
                  </td>
                  <td class="content-field">
                    <html:password property="confirmNewPassword" size="30" maxlength="40" styleClass="content-field"/>
                  </td>
                </tr>
                
                  <tr class="form-blank-row">
		            <td></td>
		          </tr>	
				
			  	<tr class="form-row-spacer">				  
		            <td class="content-field-label">
		            <table>
		            <tr>
				        <beacon:label property="secretQuestion" required="true">
		              		<cms:contentText key="SECRET_QUESTION" code="user.user"/>
		            	</beacon:label>
		            </tr>
		              </table>
		            </td>
		            <td class="content-field">
						<html:select styleId="secretQuestion" property="secretQuestion" styleClass="content-field">
							<html:options collection="secretQuestionList" property="code" labelProperty="name"  />
			       		</html:select>
					</td>
				</tr>
				
				  <tr class="form-blank-row">
		            <td></td>
		          </tr>	
		
		        <tr class="form-row-spacer">				  
		            <td class="content-field-label">
		              <table>
		            	<tr>
					        <beacon:label property="secretAnswer" required="true">
			              		<cms:contentText key="SECRET_ANSWER" code="user.user"/>
			            	</beacon:label>
		            	</tr>
		              </table>
		            </td>	
		            <td class="content-field" nowrap>
						<html:text property="secretAnswer" size="30" styleClass="content-field"/>
					</td>
				  </tr>
		
                <%-- node assignments --%>
                <c:if test='${hasNodes == "true"}'>
                  <tr>
                    <td class="subheadline" colspan="2">
                      <cms:contentText key="NODE_ASSIGNMENTS" code="user.profile"/>
                    </td>
                  </tr>

                  <c:forEach items="${user.userNodes}" var="userNode">
                    <tr>
                      <td class="content-field-label">
                        <cms:contentText key="NODE_NAME_LABEL" code="user.profile"/>
                      </td>
                      <td class="content-field-review">
                        <c:out value="${userNode.node.name}"/>&nbsp;
                        <c:if test="${userNode.node.hierarchy.primary}">
                          <cms:contentText key="PRIMARY_INDICATOR" code="user.profile"/>
                        </c:if>
                      </td>
                    </tr>

                    <tr>
                      <td class="content-field-label">
                        <cms:contentText key="NODE_ROLE_LABEL" code="user.profile"/>
                      </td>
                      <td class="content-field-review">
                        <c:out value="${userNode.hierarchyRoleType.name}"/>
                      </td>
                    </tr>

                    <tr>
                      <td class="content-field-label">
                        <cms:contentText key="NODE_OWNER_LABEL" code="user.profile"/>
                      </td>
                      <td class="content-field-review">
                        <c:out value="${nodeOwners[userNode.node.name]}"/>
                      </td>
                    </tr>

                    <c:forEach items="${nodeManagers[userNode.node.name]}" var="manager">
                      <tr>
                        <td class="content-field-label">
                          <cms:contentText key="NODE_MANAGER_LABEL" code="user.profile"/>
                        </td>
                        <td class="content-field-review">
                          <c:out value="${manager}"/>
                        </td>
                      </tr>
                    </c:forEach>

                    <tr class="form-blank-row"><td colspan="2"></td></tr>
                  </c:forEach>

                  <tr class="form-blank-row"><td colspan="2"></td></tr>
                </c:if>
		          <tr class="form-blank-row">
		            <td></td>
		          </tr>
                
                
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>

    <c:if test='${passwordChanged == "true"}'>
      <tr>
        <td  align="center" class="error" colspan="4">
          <cms:contentText key="PASSWORD_CHANGED" code="system.general"/>
        </td>
      </tr>
    </c:if>

    <tr class="form-buttonrow">
      <td align="center" colspan="4">
        <beacon:authorize ifNotGranted="LOGIN_AS">
        <html:submit styleClass="content-buttonstyle" onclick="setDispatch('changePassword')">
          <cms:contentText key="SUBMIT" code="system.button"/>
        </html:submit>
        </beacon:authorize>

        <html:cancel styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('homePage.do','unspecified')">
          <cms:contentText key="CANCEL" code="system.button"/>
        </html:cancel>
      </td>
    </tr>
  </table>
</html:form>