
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<style>
table.metrics-table td
{
  white-space:nowrap;
  <!-- width:237px; -->
  vertical-align:top;
}
</style>
<p>&nbsp; </p>
<p>&nbsp;</p>
<table style="height: 344px; margin-left: auto; margin-right: auto;" width="550">
	<tbody>
		<tr>
			<td colspan="3">
				<label id="messageLabel">If you're not familiar with the internal functions of ElasticSearch, you should <i>not</i> use these links/buttons without developer assistance.</label>
			</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="3">
				<b>Status for index <i>${ status.name }</i>:</b>
				<table cellspadding="4" class="metrics-table">
					<tr>
						<td><b>End Point:</b></td>
						<td>${ status.endPoint }</td>
					</tr>
					<tr>
						<td><b>Index: </b></td>
						<td>${ status.exists }</td>
					</tr>
					<%--<tr>
						<td><b>Health:</b></td>
						<td>${ status.health }</td>
					</tr> 
					<tr>
						<td><b>Status:</b></td>
						<td>${ status.status }</td>
					</tr>--%>
					<tr>
						<td><b>Document Count:</b></td>
						<td><fmt:formatNumber type="number" value="${ status.documentCount }"/></td>
					</tr>  
					<tr>
						<td><b>Document Deleted Count:</b>&nbsp;&nbsp;&nbsp;</td>
						<td><fmt:formatNumber type="number" value="${ status.documentsDeleted }"/></td>
					</tr>
					<tr>
						<td><b>Search Count:</b></td>
						<td><fmt:formatNumber type="number" value="${ status.searchCount }"/></td>
					</tr>
					<tr>
						<td><b>Query Time in Millis:</b></td>
						<td><fmt:formatNumber type="number" value="${ status.queryTimeInMillis }"/></td>
					</tr>
					<tr>
						<td><b>Fetch Time in Millis:</b></td>
						<td><fmt:formatNumber type="number" value="${ status.fetchTimeInMillis }"/></td>
					</tr> 
					<tr>
						<td valign="top"><b>Store Size:</b></td>
						<td>
							Bytes: <fmt:formatNumber type="number" value="${ status.storeSize }" maxFractionDigits="2" /><br/>
							Kb: <fmt:formatNumber type="number" value="${ status.storeSizeKb }" maxFractionDigits="2" /><br/>
							Mb: <fmt:formatNumber type="number" value="${ status.storeSizeMb }" maxFractionDigits="2" />
						</td>
					</tr>
				</table>
				<b>Are the ES Username/Password Correct?</b> ${ status.securityCorrect }
			</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td> 
				Create a new index.  This should be done on startup of the application but this provides another method.
			</td>
			<td>&nbsp;</td>
			<td>
				<button id="createIndex"  style="width: 215px;" ><cms:contentText key="CREATE_INDEX" code="admin.indexing" /> </button>
			</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td>
				Delete the existing index.  The can be used in conjunction with the <i>create</i> to delete and re-create the existing index.  
				If you delete your index, the elasticsearch/participatns search will fail until it's been recreated and re-loaded.
			</td>
			<td>&nbsp;</td>
			<td>
				<button id="deleteIndex"  style="width: 215px;" > <cms:contentText key="DELETE_INDEX" code="admin.indexing" /> </button>
			</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td>
				Closing the index allow you to manually modify the underlying index.  Close the index, apply your mappings/changes, and open it.  The search is not functional while the index is closed.
			</td>
			<td>&nbsp;</td>
			<td>
				<button id="closeIndex"  style="width: 215px;" >  <cms:contentText key="CLOSE_INDEX" code="admin.indexing" /> </button>
			</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td>
				Open the index.  If the index is closed, this button will open it.  Used primarily after manually closing and altering the index mappings.
			</td>
			<td>&nbsp;</td>
			<td>
				<button id="openIndex"  style="width: 215px;" > <cms:contentText key="OPEN_INDEX" code="admin.indexing" /> </button>
			</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td>
				This button re-initialyzes the ES Client.  The System caches the Connection Factory to ES for performance.  If you change the end-point, credentials, etc in the system variables for Elastic -
				the updates will only take effect after a restart or by clicking this button.
			</td>
			<td>&nbsp;</td>
			<td>
				<button id="resetClient"  style="width: 215px;" > <cms:contentText key="RESET_CLIENT_FACTORY" code="admin.indexing" /> </button>
			</td>
		</tr>
	</tbody>
</table>


<script type="text/javascript">

  var  esAdmin =  new ElasticAdmin();
	
  var  ELASTIC_ADMIN_URL =  "${pageContext.request.contextPath}/index/";

				function ElasticAdmin(){
					 
					this.init = function(){						
						console.log('inside init');						 
						 $("#createIndex").click (this.createIndex);
						 $("#deleteIndex").click(this.deleteIndex);
						 $("#closeIndex").click(this.closeIndex);
						 $("#openIndex").click(this.openIndex);
						 $("#resetClient").click(this.resetClient);
						 console.debug('ElasticAdmin  init  Done');	 
					 };
					 
					 
					 this.deleteIndex = function(){						 
						 esAdmin.triggerjqXHRCall("delete");
					 };					 
					 
					 this.createIndex = function(){						 
						 esAdmin.triggerjqXHRCall('create');
					 };
					 
					 
					 this.openIndex = function(){
						 esAdmin.triggerjqXHRCall('open');
					 };
					 
					 this.closeIndex = function(){
						 esAdmin.triggerjqXHRCall('close');
					 };
					 
					 this.resetClient = function(){
						 esAdmin.triggerjqXHRCall('reset');
					 };
					 
					 this.displayMessage = function(message){
					     console.log(message)	
					     $("#messageLabel").text (message);
					 };
					 
					 this.triggerjqXHRCall = function(path){
						 console.log('inside ajax call');
						 $("#messageLabel").text('');
						  $.ajax({
				             dataType: 'g5json',
				             type: "POST",
				             url: ELASTIC_ADMIN_URL +  path + '.action',
				             success: function(serverResp) {
									var message = serverResp;
									console.log(message);
									esAdmin.displayMessage(message);
				             },
				             error: function(jqXHR, textStatus, errorThrown) {
				                 console.log("[ERROR] Elastic  index Admin: ", jqXHR, textStatus, errorThrown);
				                 }
				         });
					 };
					 
				}
				
				$(document).ready(function() {
					  console.log('before ElasticAdmin init');
					        esAdmin.init();
					 console.log('after ElasticAdmin init')
				});
				

</script>