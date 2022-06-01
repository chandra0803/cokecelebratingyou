<table id="nominationsApprovalPromoPageTable" class="table table-striped">
	{{#if tabularData.meta.columns}}
	<thead>
		<tr>
			{{#each tabularData.meta.columns}}
			<th class="{{name}} {{#if sortable}}sortable{{/if}} {{#eq ../sortedOn name}}sorted {{../sortedBy}}{{else}}asc{{/eq}}" data-sort-on="{{name}}" data-sort-by="{{#eq ../sortedOn name}}{{#eq ../sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"  data-column-id="{{id}}">
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
		<tr data-nomineeId="{{id}}" data-indexId="{{index}}">
			<td>
				{{nominationPromotionName}}

			</td>
			<td>
			{{#if levelName}}
                {{levelName}}
            {{else}}
                {{#if levelNumber}}
                	<cms:contentText key="LEVEL" code="nomination.approvals.module"/> {{levelNumber}}
                {{/if}}
            {{/if}}
			</td>
			<td>{{status}}</td>
			<td><a href="{{url}}&amp;nominationId={{id}}">{{tasks}}</a></td>
		</tr>
		{{/each}}
	</tbody>
	{{/if}}
</table>
