<script>
function enableFields()
{
	var pointsEnabled = $("#allowAwardPoints").attr('checked');
	var othersEnabled = $("#allowAwardOther").attr('checked');

	if( !pointsEnabled && othersEnabled )
	{
		$("#taxableFalse").attr('checked',true);
		$("#taxableFalse").attr('disabled',true);
		$("#taxableTrue").attr('disabled',true);
	}
	else
	{
		$("#taxableFalse").attr('disabled',false);
		$("#taxableTrue").attr('disabled',false);
	}
}
</script>