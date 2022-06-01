/*jslint browser: true, nomen: true, unparam: true*/
/*global
console,
$,
_,
G5,
TemplateManager,
PageView,
RecipientSelectionModelView:true
*/
RecipientSelectionModelView = PageView.extend({

    //override super-class initialize function
    initialize: function(opts) {
        console.log('[INFO] RecipientSelection: RecipientSelection model view initialized', this);

        var thisView = this;

        //set the appname (getTpl() method uses this)
        this.appName = "recipient";

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply(this, arguments);

        //inherit events from the superclass
        this.events = _.extend({},PageView.prototype.events,this.events);

        //is the text being translated?
        this.translatedText = opts.translatedText || false;

        this.checkForServerErrors();  


    },

    events:{
        "click #confirmButton": "validateChanges",
		"click .cash_selection": "onChanges"
    },
  
	onChanges: function(event) {
		var that = event.target	;
		$(that).closest("tbody").find('input[type=text]').removeAttr('readonly').attr('disabled', true);
		$(that).closest("tr").find('input[type=text]').removeAttr('disabled').attr('readonly', true);
		
		if($(that).val() == 'cash') {
		
			var txt1 = $(that).closest("tr").find('input[name="cash_selection.cash"]').val();
			var txt2 = $(that).closest("tr").find('.currency_val').text();
			$(".selected_award").html(txt1 +" "+ txt2);
			
		} else if ($(that).val() == 'points') {
		
			var txt1 = $(that).closest("tr").find('input[name="cash_selection.points"]').val();
			var txt2 = $(that).closest("tr").find('.currency_val').text();
			$(".selected_award").html(txt1 +" "+ txt2);
			
		} else if ($(that).val() == 'cashAndPoints') {
		
			var txt1 = $(that).closest("tr").find('input[name="cash_selection.Points"]').val();
			var txt2 = $(that).closest("tr").find('.currency_val').text();
			var txt3 = $(that).closest("tr").find('input[name="cash_selection.cash"]').val();
			$(".selected_award").html(txt1+" Points And "+txt3 +" "+ txt2);
		}
	},
  
	validateChanges: function(event) {
        event.preventDefault ? event.preventDefault() : event.returnValue = false;
        var $validateTargets = $("form.recipientformPage .validateme");

        if(G5.util.formValidate($validateTargets)){
			var $recipientConfirm = $('body').find('#recipientConfirm');          

			event ? event.preventDefault():false;

			$recipientConfirm.modal();
        }
    },

    checkForServerErrors: function() {
        if ($("#serverReturnedErrored").val() === "true"){
            $("#approvalErrorBlock").slideDown('fast'); //show error block
        }
    } //checkForServerErrors
});