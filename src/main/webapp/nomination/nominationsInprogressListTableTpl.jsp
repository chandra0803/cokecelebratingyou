<%@ include file="/include/taglib.jspf" %>
<table id="nominationsListPageTable" class="table table-striped">
	{{#if tabularData.meta.columns}}
	<thead>
		<tr>
			{{#each tabularData.meta.columns}}
			<th class="{{name}} {{#if sortable}}sortable{{/if}} {{#eq ../sortedOn name}}sorted {{../sortedBy}}{{else}}asc{{/eq}}" data-sort-on="{{name}}" data-sort-by="{{#eq ../sortedOn name}}{{#eq ../sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}" data-column-id="{{id}}">
				{{#if sortable}}
					<a href="#">
						{{displayName}}
						 <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
					</a>
				{{else}}
					{{displayName}}
				{{/if}}
			</th>
			{{/each}}
		</tr>
	</thead>
	{{/if}}

	{{#if tabularData.results}}
	<tbody>
		{{#each tabularData.results}}
		<tr data-nomineeId="{{id}}" data-indexId="{{index}}" data-clientState="{{clientState}}" data-removeParams="{{removeParams}}">
			<td>{{dateStarted}}</td>

			<td>{{nominee}}</td>

			<td>{{nominationPromotionName}}</td>

			<td class="editColumn"><a href="{{editUrl}}"><i class="icon-pencil2"></i></a></td>
			
			<td class="remove">
				<beacon:authorize ifNotGranted="LOGIN_AS">
					<a class="removeNominationPromotion" title='<cms:contentText key="REMOVE" code="default.label"/>'><i class="icon-trash"></i></a>
				</beacon:authorize>
			</td>
			
		</tr>
		{{/each}}
	</tbody>
	{{/if}}
</table>
