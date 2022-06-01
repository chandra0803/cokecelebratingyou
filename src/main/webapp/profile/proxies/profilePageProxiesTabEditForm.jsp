<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.domain.proxy.Proxy" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>

<h3>
	<cms:contentText key="EDIT_PROXY" code="profile.proxies.tab"/>
</h3>
<html:form styleId="contentForm" styleClass="form-horizontal" action="profilePageProxiesSubmit.do?method=save">
  <html:hidden property="method" value="save"/>
  <html:hidden property="proxyVersion" />
  <html:hidden property="showCancel"/>
  <html:hidden property="prodClaimProxyListCount"/>
  <html:hidden property="recognitionProxyListCount"/>
  <html:hidden property="nominationProxyListCount"/>
  <html:hidden property="showCoreAccess"/>
  <html:hidden property="proxyUserId" styleId="proxyUserId"/>
  <html:hidden property="id" />
  <html:hidden property="proxyId" />

  <html:hidden property="firstName" />
  <html:hidden property="lastName" />
  <html:hidden property="orgName" />
  <html:hidden property="departmentName" />
  <html:hidden property="jobName" />
  <html:hidden property="countryName" />
  <html:hidden property="countryCode" />
  <html:hidden property="avatarUrl" />

  <c:choose>
    <c:when test="${empty proxyDetailForm.proxyId}">
      <beacon:client-state>
        <beacon:client-state-entry name="mainUserId" value="${proxyDetailForm.mainUserId}"/>
        <beacon:client-state-entry name="mainUserNode" value="${proxyDetailForm.mainUserNode}"/>
        <beacon:client-state-entry name="proxyId" value=""/>
      </beacon:client-state>
    </c:when>
    <c:otherwise>
      <beacon:client-state>
        <beacon:client-state-entry name="mainUserId" value="${proxyDetailForm.mainUserId}"/>
        <beacon:client-state-entry name="mainUserNode" value="${proxyDetailForm.mainUserNode}"/>
        <beacon:client-state-entry name="proxyId" value="${proxyDetailForm.proxyId}"/>
        <beacon:client-state-entry name="proxyFormattedUserString" value="${proxyDetailForm.proxyFormattedUserString}"/>
      </beacon:client-state>
    </c:otherwise>
  </c:choose>

    <div class="row-fluid">
        <div class="span12">
            <p class="participant">
            <c:if test="${proxyDetailForm.lastName != null}">
                <a class="participant-popover" href="#" data-participant-ids="[${proxyDetailForm.id}]">
                    <img alt="${proxyDetailForm.firstName}, ${proxyDetailForm.lastName}" class="profile-proxy-list-avatar avatar" src="${proxyDetailForm.avatarUrl}">
                    <c:out value="${proxyDetailForm.firstName}"/>
                    <c:out value="${proxyDetailForm.lastName}"/>
                    <img src="<%=RequestUtils.getBaseURI(request)%>/assets/img/flags/${proxyDetailForm.countryCode}.png" alt="${proxyDetailForm.countryCode}" class="countryFlag" title="${proxyDetailForm.countryName}" />
                </a>
            </c:if>
                <span class="org"><c:out value="${proxyDetailForm.orgName}"/> <c:out value="${proxyDetailForm.departmentName}"/> <c:out value="${proxyDetailForm.jobName}"/></span>
            </p>

            <c:if test="${proxyDetailForm.showCoreAccess}">
	           <div class="control-group">
	               <label class="control-label"><cms:contentText key="CORE_ACCESS" code="profile.proxies.tab"/></label>
	               	<div class="controls">
	              			<c:forEach items="${proxyCoreAccessList}" var="proxyCoreAccessType">
	              				<c:if test="${proxyDetailForm.coreAccess == null}">
	              				<label class="checkbox">
	              					<html:checkbox property="coreAccess" value="${proxyCoreAccessType.code}" styleId="${proxyCoreAccessType.code}"/><c:out value="${proxyCoreAccessType.name}"/><br/>
	              				</label>
	              				</c:if>
	              				<c:if test="${proxyDetailForm.coreAccess != null}">
	              					<c:set var="contains" value="false" />
									<c:forEach var="item" items="${proxyDetailForm.coreAccess}">
  										<c:if test="${item eq proxyCoreAccessType.code}">
    										<c:set var="contains" value="true" />
  										</c:if>
									</c:forEach>
									<c:if test="${contains}">
									<label class="checkbox">
										<input type="checkbox" name="coreAccess" value="${proxyCoreAccessType.code}" checked="checked" id="${proxyCoreAccessType.code}"/><c:out value="${proxyCoreAccessType.name}"/><br/>
									</label>
									</c:if>
									<c:if test="${!contains}">
									<label class="checkbox">
										<input type="checkbox" name="coreAccess" value="${proxyCoreAccessType.code}"  id="${proxyCoreAccessType.code}"/><c:out value="${proxyCoreAccessType.name}"/><br/>
									</label>
									</c:if>
	              				</c:if>
	              			</c:forEach>
	               </div>
	           </div>
            </c:if>

            <div class="control-group">
                <label class="control-label"><cms:contentText key="PROMOTION_RIGHTS" code="profile.proxies.tab"/></label>
                <div class="controls">
                    <label class="checkbox">
                      <c:choose>
                      	<c:when test="${proxyDetailForm.allModules}">
                      		<input type="checkbox" value="on" checked="checked" name="allModules" id="allModules" data-conditional="onunchecked" data-conditional-target="#allModulesOpts">
                    	</c:when>
                    	<c:otherwise>
                        	<input type="checkbox" value="on" name="allModules" id="allModules" data-conditional="onunchecked" data-conditional-target="#allModulesOpts">
                        </c:otherwise>
                      </c:choose>
                      <cms:contentText key="PROMO_RIGHTS_DESC" code="profile.proxies.tab"/>
                    </label>
                </div>
            </div>

            <c:if test="${showLB}">
            <div class="control-group">
                <label class="control-label"><cms:contentText key="ALLOW_LB" code="profile.proxies.tab"/></label>
                <div class="controls">
                    <label class="checkbox">
                    	<c:choose>
                      	<c:when test="${proxyDetailForm.allowLeaderboard}">
                      		<input type="checkbox" value="on"  checked="checked" name="allowLeaderboard" id="allowLeaderboard">
                    	</c:when>
                    	<c:otherwise>
                        	<input type="checkbox" value="on" name="allowLeaderboard" id="allowLeaderboard">
                        </c:otherwise>
                      </c:choose>
                       <cms:contentText key="LEADERBOARD_MESG" code="profile.proxies.tab"/>
                    </label>
                </div>
            </div>
            </c:if>

            <div id="allModulesOpts" class="show">
				<c:if test="${showPC}">
                <div class="control-group">
                	<html:hidden property="prodClaimVersion"/>
					<beacon:client-state>
						<beacon:client-state-entry name="prodClaimModuleId" value="${proxyDetailForm.prodClaimModuleId}"/>
					</beacon:client-state>
                    <label class="control-label"><cms:contentText key="PRODUCT_CLAIM_PROMOTIONS" code="profile.proxies.tab"/></label>

                    <div class="controls">
                        <label class="radio">
                        	<c:if test="${proxyDetailForm.allProdClaimPromos != 'all' }">
                            	<input type="radio" value="all" name="allProdClaimPromos" id="prodClaimAll">
                            </c:if>
                            <c:if test="${proxyDetailForm.allProdClaimPromos == 'all' }">
                            	<input type="radio" checked="checked" value="all" name="allProdClaimPromos" id="prodClaimAll">
                            </c:if>
                            <cms:contentText key="ALL_CLAIM_PROMOS" code="profile.proxies.tab"/>
                        </label>

                        <label class="radio">
                        	<c:if test="${proxyDetailForm.allProdClaimPromos != 'none' }">
                            	<input type="radio" value="none" name="allProdClaimPromos" id="prodClaimNone">
                            </c:if>
                            <c:if test="${proxyDetailForm.allProdClaimPromos == 'none' }">
                            	<input type="radio" checked="checked" value="none" name="allProdClaimPromos" id="prodClaimNone">
                            </c:if>
                            <cms:contentText key="NO_CLAIM_PROMOS" code="profile.proxies.tab"/>
                        </label>

                        <label class="radio">
                        	<c:if test="${ not empty proxyDetailForm.prodClaimProxyAsList}">
                        		<c:if test="${proxyDetailForm.allProdClaimPromos == 'specific' }">
                        			<input type="radio" checked="checked"  value="specific" name="allProdClaimPromos" id="prodClaimSpecific" data-conditional="onchecked" data-conditional-target="#allProdClaimSpecificOpts">
                        		</c:if>
                        		<c:if test="${proxyDetailForm.allProdClaimPromos != 'specific' }">
                        			<input type="radio"  value="specific" name="allProdClaimPromos" id="prodClaimSpecific" data-conditional="onchecked" data-conditional-target="#allProdClaimSpecificOpts">
                        		</c:if>
   	        				</c:if>
   	        				<c:if test="${ empty proxyDetailForm.prodClaimProxyAsList}">
 	        					<input type="radio" value="specific" disabled="disabled"  name="allProdClaimPromos" id="prodClaimSpecific" data-conditional="onchecked" data-conditional-target="#allProdClaimSpecificOpts">
 	        				</c:if>
                            <cms:contentText key="SPECIFIC_CLAIM_PROMOS" code="profile.proxies.tab"/>
                        </label>

                        <blockquote id="allProdClaimSpecificOpts" class="hide">
	            			  <nested:iterate id="prodClaimProxy" name="proxyDetailForm" property="prodClaimProxyAsList">
	            			  	<label class="checkbox">
	              		  			<nested:hidden property="promotionId"/>
	              		  			<nested:hidden property="promotionName"/>
	                  		  		<nested:checkbox property="selected"/>
	                  		  		<c:out value="${prodClaimProxy.promotionName}"/>
	                  		  	</label>
	            			  </nested:iterate>
                        </blockquote>
                    </div>
                </div>
                </c:if>

          		<c:if test="${showRec}">
          			<html:hidden property="recVersion"/>
					<beacon:client-state>
						<beacon:client-state-entry name="recModuleId" value="${proxyDetailForm.recModuleId}"/>
					</beacon:client-state>
          		<div class="control-group">
                    <label class="control-label">
                    	<cms:contentText key="RECOGNITION_PROMOTIONS" code="profile.proxies.tab"/>
                    </label>

                    <div class="controls">
                        <label class="radio">
                        	<c:if test="${proxyDetailForm.allRecPromos != 'all' }">
                            	<input type="radio" value="all" name="allRecPromos" id="allRecPromosAll">
                            </c:if>
                            <c:if test="${proxyDetailForm.allRecPromos == 'all' }">
                            	<input type="radio" checked="checked" value="all" name="allRecPromos" id="allRecPromosAll">
                            </c:if>
                            <cms:contentText key="ALL_RECOG_PROMOS" code="profile.proxies.tab"/>
                        </label>

                        <label class="radio">
                        	<c:if test="${proxyDetailForm.allRecPromos != 'none' }">
                            	<input type="radio" value="none" name="allRecPromos" id="allRecPromosNone">
                            </c:if>
                            <c:if test="${proxyDetailForm.allRecPromos == 'none' }">
                            	<input type="radio" checked="checked" value="none" name="allRecPromos" id="allRecPromosNone">
                            </c:if>
                            <cms:contentText key="NO_RECOG_PROMOS" code="profile.proxies.tab"/>
                        </label>

                        <label class="radio">
                        	<c:if test="${ not empty proxyDetailForm.recognitionProxyAsList}">
                        		<c:if test="${proxyDetailForm.allRecPromos == 'specific' }">
                        			<input type="radio" checked="checked"  value="specific" name="allRecPromos" id="allRecPromosSpecific" data-conditional="onchecked" data-conditional-target="#allRecPromosSpecificOpts">
                        		</c:if>
                        		<c:if test="${proxyDetailForm.allRecPromos != 'specific' }">
                        			<input type="radio"  value="specific" name="allRecPromos" id="allRecPromosSpecific" data-conditional="onchecked" data-conditional-target="#allRecPromosSpecificOpts">
                        		</c:if>
   	        				</c:if>

   	        				<c:if test="${ empty proxyDetailForm.recognitionProxyAsList}">
   	        					<input type="radio"  value="specific" disabled="disabled" name="allRecPromos" id="allRecPromosSpecific" data-conditional="onchecked" data-conditional-target="#allRecPromosSpecificOpts">
   	        				</c:if>
                            <cms:contentText key="SPECIFIC_RECOG_PROMOS" code="profile.proxies.tab"/>
                        </label>
                        <blockquote id="allRecPromosSpecificOpts" class="hide">
	            				<nested:iterate id="recognitionProxy" name="proxyDetailForm" property="recognitionProxyAsList">
	            					<label class="checkbox">
                                		<nested:hidden property="promotionId"/>
	              		  				<nested:hidden property="promotionName"/>
	                  		  			<nested:checkbox property="selected"/>
	                  		  			<c:out value="${recognitionProxy.promotionName}"/>
                            		</label>
	            				</nested:iterate>
                        </blockquote>
                    </div>
                </div>
          		</c:if>

				<c:if test="${showNom}">
					<html:hidden property="nomVersion"/>
					<beacon:client-state>
						<beacon:client-state-entry name="nomModuleId" value="${proxyDetailForm.nomModuleId}"/>
					</beacon:client-state>

            	    <div class="control-group">
             	       <label class="control-label">
             	       		<cms:contentText key="NOMINATION_PROMOTIONS" code="profile.proxies.tab"/>
             	       </label>

                    <div class="controls">
                        <label class="radio">
                        	<c:if test="${proxyDetailForm.allNomPromos == 'all' }">
                            	<input type="radio" value="all" checked="checked" name="allNomPromos" id="allNomPromosAll">
                            </c:if>
                            <c:if test="${proxyDetailForm.allNomPromos != 'all' }">
                            	<input type="radio" value="all"  name="allNomPromos" id="allNomPromosAll">
                            </c:if>
                            <cms:contentText key="ALL_NOM_PROMOS" code="profile.proxies.tab"/>
                        </label>

                        <label class="radio">
                        	<c:if test="${proxyDetailForm.allNomPromos == 'none' }">
                            	<input type="radio" checked="checked" value="none" name="allNomPromos" id="allNomPromosNone">
                            </c:if>
                            <c:if test="${proxyDetailForm.allNomPromos != 'none' }">
                            	<input type="radio" value="none" name="allNomPromos" id="allNomPromosNone">
                            </c:if>
                            <cms:contentText key="NO_NOM_PROMOS" code="profile.proxies.tab"/>
                        </label>

                        <label class="radio">
                        <c:if test="${ not empty proxyDetailForm.nominationProxyAsList}">
                        	<c:if test="${proxyDetailForm.allNomPromos == 'specific' }">
 	        					<input type="radio" value="specific" checked="checked" name="allNomPromos" id="allNomPromosSpecific" data-conditional="onchecked" data-conditional-target="#allNomPromosSpecificOpts">
 	        				</c:if>
 	        				<c:if test="${proxyDetailForm.allNomPromos != 'specific' }">
 	        					<input type="radio" value="specific"  name="allNomPromos" id="allNomPromosSpecific" data-conditional="onchecked" data-conditional-target="#allNomPromosSpecificOpts">
 	        				</c:if>
 	        			</c:if>
                        <c:if test="${ empty proxyDetailForm.nominationProxyAsList}">
 	        				<input type="radio" value="specific" disabled="disabled"  name="allNomPromos" id="allNomPromosSpecific" data-conditional="onchecked" data-conditional-target="#allNomPromosSpecificOpts">
 	        			</c:if>
                            <cms:contentText key="SPECIFIC_NOM_PROMOS" code="profile.proxies.tab"/>
                        </label>

                        <blockquote id="allNomPromosSpecificOpts" class="hide">
	            				<nested:iterate id="nominationProxy" name="proxyDetailForm" property="nominationProxyAsList">
	            					<label class="checkbox">
	              		  				<nested:hidden property="promotionId"/>
	              		  				<nested:hidden property="promotionName"/>
	              		  				<nested:checkbox property="selected"/>
		                  		  		<c:out value="${nominationProxy.promotionName}"/>
		                  		  	</label>
	            				</nested:iterate>
                        </blockquote>
                    </div>
                </div>
                </c:if>

<!-- removed code to remove the display og SSI Contest avialable options from https://g5test2qa.performnet.com/g5test2/participantProfilePage.do#profile/Proxies -->
                <!-- ********************************************************************************************** -->
              	<!-- <c:if test="${showSsi}">
              	<html:hidden property="ssiVersion"/>
					<beacon:client-state>
						<beacon:client-state-entry name="ssiModuleId" value="${proxyDetailForm.ssiModuleId}"/>
					</beacon:client-state>

            	    <div class="control-group">
             	       <label class="control-label">
             	       		<cms:contentText key="SSI_CONTEST" code="profile.proxies.tab"/>
             	       </label>

                    <div class="controls">
                        <label class="radio">
                        	<c:if test="${proxyDetailForm.allSsiPromos == 'all' }">
                            	<input type="radio" value="all" checked="checked" name="allSsiPromos" id="allSsiPromosAll">
                            </c:if>
                            <c:if test="${proxyDetailForm.allSsiPromos != 'all' }">
                            	<input type="radio" value="all"  name="allSsiPromos" id="allSsiPromosAll">
                            </c:if>
                            <cms:contentText key="ALL_SSI_PROMOS" code="profile.proxies.tab"/>
                        </label>

                        <label class="radio">
                        	<c:if test="${proxyDetailForm.allSsiPromos == 'none' }">
                            	<input type="radio" checked="checked" value="none" name="allSsiPromos" id="allSsiPromosNone">
                            </c:if>
                            <c:if test="${proxyDetailForm.allSsiPromos != 'none' }">
                            	<input type="radio" value="none" name="allSsiPromos" id="allSsiPromosNone">
                            </c:if>
                            <cms:contentText key="NO_SSI_PROMOS" code="profile.proxies.tab"/>
                        </label>

                    </div>
                </div>
                </c:if>


                <!-- **************************************************************** -->

            </div><!-- /#specificModulesOpts -->

            <div class="form-actions">
              <beacon:authorize ifNotGranted="LOGIN_AS">
                <button type="submit" id="profilePageProxyTabButtonSaveProxy" class="btn btn-primary"><cms:contentText key="SAVE_CHANGES" code="system.button"/></button>
              </beacon:authorize>
                <a id="profilePageProxyTabButtonCancel" href="#" class="btn"><cms:contentText key="CANCEL" code="system.button"/></a>
            </div>
        </div>
    </div>
</html:form>