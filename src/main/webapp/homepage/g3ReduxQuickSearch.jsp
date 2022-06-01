<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.enums.QuickSearchSearchForField"%>
<%@ include file="/include/taglib.jspf" %>
<c:if test="${!empty quickSearchEnabled }"> 
   <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/quicksearch.js" type="text/javascript"></script>
    <div id="quicksearch" class="content">
        <div class="module">
            <div class="topper">
                <h2>Search (<c:out value="${quickSearchClientName}"/> - <c:out value="${quickSearchEnvironment}"/>)</h2>
            </div>
            <div class="guts">
                <form name="quickSearchForm" action="<%=RequestUtils.getBaseURI(request)%>/quickSearch.do?method=search" method="post" id="quickSearchForm">
                    <fieldset class="">
                        <div class="singleline">                            
                            <label for="quickSearchSearchForFieldCode" class="select req">
                                <span class="label"><cms:contentText key="SEARCH_FOR" code="home.quicksearch"/></span>
                                <span class="select">                                   
								  <select name="quickSearchSearchForFieldCode" id="quickSearchSearchForFieldCode" onchange="forceit(this)">
					                <c:forEach var="quickSearchSearchForField" items="${quickSearchSearchForFields}">
					                  <option value="<c:out value="${quickSearchSearchForField.code}"/>" 
					                    <c:if test="${quickSearchSearchForField.code == quickSearchForm.quickSearchSearchForFieldCode}"> selected="selected"</c:if>
					                    <c:if test="${(quickSearchForm.quickSearchSearchForFieldCode == null) && (quickSearchSearchForField.code == quickSearchSearchForDefault)}"> selected="selected"</c:if>
					                  >
					                    <c:out value="${quickSearchSearchForField.name}"/>
					                  </option>
					                </c:forEach>
					              </select>    
                                </span>
                            </label>
                    
                            <label for="quickSearchSearchByFieldCode" class="select req">
                                <span class="label"><cms:contentText key="SEARCH_BY" code="home.quicksearch"/></span>
                                <span class="select">                                    
									<select name="quickSearchSearchByFieldCode" id="quickSearchSearchByFieldCode">
						                <c:forEach var="quickSearchSearchByField" items="${quickSearchSearchByFields}">
						                  <option value="<c:out value="${quickSearchSearchByField.code}"/>" 
						                    <c:if test="${quickSearchSearchByField.code == quickSearchForm.quickSearchSearchByFieldCode}"> selected="selected"</c:if>
						                    <c:if test="${(quickSearchForm.quickSearchSearchByFieldCode == null) && (quickSearchSearchByField.code == quickSearchSearchByDefault)}"> selected="selected"</c:if>
						                  >
						                    <c:out value="${quickSearchSearchByField.name}"/>
						                </c:forEach>
						            </select> 
                                </span>
                            </label>
                    
                            <label for="quickSearchValue" class="text req">
                                <span class="label"><cms:contentText key="VALUE" code="home.quicksearch"/></span>
                                <input type="text" name="quickSearchValue" value="" id="quickSearchValue" class="text" />
                            </label>
                        
                        </div><%-- /.singleline --%>
                    
                        <div class="buttons">
                            <button type="submit" class="fancy fancy-go"><span><span>Search</span></span></button>
                        </div>
                    </fieldset>
                </form>
			   <script>
			    	<%-- trigger the onchange so that the search by list is filtered--%>
			    	document.forms['quickSearchForm'].quickSearchSearchForFieldCode.onchange();

			    	function forceit(x)
				    {				    
				    	filtery(x.value + '_',x.form.quickSearchSearchByFieldCode);
				    	$("#quickSearchSearchByFieldCode").change();
				    }
			   </script>

            </div><%-- /.guts --%>
        </div><%-- /.module --%>
    </div><%-- /#quicksearch --%>
</c:if>

