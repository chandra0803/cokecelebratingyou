<div class="modal hide fade autoModal recognitionResponseModal">
	<div class="modal-header">
		<button class="close" data-dismiss="modal">
			<i class="icon-close"></i>
		</button>
		{{#if name}}
		<h1>{{name}}</h1>
		{{/if}}
		<p>
			<b>
			{{#if name}} {{else}}
        		{{/if}}
        	{{{text}}}
			</b>
		</p>
	</div>
</div>

