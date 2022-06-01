/*exported CheersRecognitionEzView*/
/*global
Modernizr,
TemplateManager,
CheersRecognitionEzView:true
*/
CheersRecognitionEzView = Backbone.View.extend({

    //override super-class initialize function
    events: {
        'keyup   .recognition-comment': 'updateCharacterCount',
        'click   .lang': 'setSpellCheckLang',
        'click   #ezCheersSendBtn': 'postRecognition',
        'click   #ezRecChangeRecipLink': 'closeModule',
        'click   #ezCheersCancelBtn, #ezRecModalCloseBtn': 'closeModule',
        'blur    input': 'forceIOSredraw'
    },

    initialize: function(opts) {
        /*
         *
         * Takes the passed options and stores them in the view for later use.
         *
         */

        // console.log('initialized", opts);

        if (!Modernizr.input.placeholder) {
            this.$el.find('input, textarea').placeholder();
        }

        this.close = opts.close;
        this.recipient = opts.recipient;
        this.recipientInfo = opts.recipientInfo;
        this.promotionId = opts.promotionId;

        this.ismodal = this.$el.hasClass('modal');
        this.loadCheersInfo();
    },

    loadCheersInfo: function() {
        /*
         *
         * Takes the selected recipient and passes it's information to
         * the server, waits for a response, and passes that data on
         * to be templated.
         *
         * [1] Passed information.
         *
         * [2] Ajax request for recipent recognition form JSON.
         *
         */

        var self = this,
            activeRecipient = this.recipient, // [1]
            requestedData = null;


        requestedData = $.ajax({ // [2]
            dataType: 'g5json',
            type: 'POST',
            url: G5.props.URL_JSON_CHEERS_INFO,
            data: {
                recipientId: activeRecipient,
                promotionId: self.promotionId
            },
            beforeSend: function() {
                // add a dataLoading class to the modal while we wait
                self.$el.addClass('dataLoading');
                G5.util.showSpin(self.$el);
            },
            success: function(serverResp) {
                // remove the dataLoading class from the modal
                self.$el.removeClass('dataLoading');
                console.log('[INFO] CheersRecognitionEzView: submitRecipient ajax call successfully returned this JSON object: ', serverResp);
                self.renderCheersData(serverResp.data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log('[ERROR] requestPromotionList: ', jqXHR, textStatus, errorThrown);
                self.resetModule();
            },
            complete: function(jqXHR, textStatus) {
                console.log('[INFO] CheersRecognitionEzView: submitRecipient ajax call completed');
                G5.util.hideSpin(self.$el);
                self.trigger('loadComplete', jqXHR, textStatus); // broadcast status
            }
        });

        requestedData.fail(function(jqXHR, textStatus) {
            console.log('[INFO] Cheers Recogntion Error While Pulling out Data: ' + textStatus);
        });
    },

    renderCheersData: function(info) {
        var self = this,
            tplName = 'cheersRecognitionModel',
            tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_BASE_ROOT + 'tpl/';

        TemplateManager.get(tplName, function(tpl) {
            self.$el.html(tpl(info));
            self.$el.find('.ezRecLiner').hide();

            self.trigger('templateReady'); // broadcast status

        }, tplUrl);

    },

    forceIOSredraw: function() {
        $(window).trigger('scroll');
    },
    updateCharacterCount: function(event) {
        /*
         *
         * Updates Remaining Character counter in view.
         *
         */

        var $textarea = $(event.currentTarget),
            $label = this.$el.find('.remCharsLabel'),
            $counter = this.$el.find('#ezRecharCount'),
            charCount = $textarea.val().length,
            maxChars = $textarea.data('max-chars'),
            remaining = maxChars - charCount;

        $label.show();
        $counter.show().html(remaining);
    },
    postRecognition: function(event) {
        /*
         *
         * "Cheers" form submission. Posts form information via ajax.
         *
         * [1] function to return the proper orgUnit
         *
         * [2] prevent multiple form submissions
         *
         */

        event.preventDefault();

        if (!this.validateForm()) {
            return false;
        }

        console.log('validation success');

        var self = this,
            $btn = $(event.currentTarget),
            dataSent = null,
            formParams = $(event.target).closest('form').serializeArray(),
            params = {
                recipientId: this.recipient,
                comments: this.$el.find('#ezCheersCommentBox textarea').val(),
                promotionId: this.promotionId,
                points: this.$el.find('#cheersPoints').val(),
                responseType: 'html'
            };

        // convert the serialized array [{name:foo,value:bar},{name:fee,value:baz}] to two arrays of keys and values [foo,fee],[bar,baz] for the _.object() function and then convert to an object {foo:bar, fee:baz}
        formParams = _.object(_.pluck(formParams, 'name'), _.pluck(formParams, 'value'));
        params = $.extend({}, params, formParams);

        dataSent = $.ajax({
            type: 'POST',
            url: G5.props.URL_JSON_CHEERS_SEND_RECOGNITION,
            data: params,
            dataType: 'g5html',
            beforeSend: function() {
                $btn.button('loading'); // [2]
            },
            success: function(serverResp) {
                // console.log('[INFO] EZRecognitionCollection: postEzRecognition ajax post successfully posted this JSON object: ", serverResp);
                console.log('[INFO] EZRecognitionCollection: server returned html');

                self.trigger('cheersRecognitionSent');
                self.closeModule();

                var $serverResp = $('<div />', {
                    'class': 'modal fade autoModal recognitionResponseModal',
                    'html': serverResp
                });

                $serverResp.modal('show');
                $serverResp.on('shown.bs.modal', function() {
                    $('.cofettiPop').show();
                    $('body').addClass('cofettiPopBody');
                });
                $serverResp.on('hide', function() {
                    $('.cofettiPop').hide();
                    $('body').removeClass('cofettiPopBody');
                });
            },
            error: function(jqXHR, status) {
                alert('Server error: ' + status);
            },
            complete: function() {
                $btn.button('reset');
            }
        });

        dataSent.fail(function(jqXHR, textStatus) {
            console.log('[INFO] CheersRecognition: postCheersRecognition ajax post failed: ' + textStatus, jqXHR);
        });
    },


    validateForm: function() {

        var $validate = this.$el.find('.ezCheersModal .validateme'),
            promotionId = this.promotionId;

        var qtipOpts = {
            position: {
                my: this.$el.closest('.modal').length ? 'top center' : 'bottom center',
                at: this.$el.closest('.modal').length ? 'bottom center' : 'top center'
            }
        };
        if (!G5.util.formValidate($validate, null, { qtipOpts: qtipOpts })) {
            return false;
        }

        return true;
    },


    setSpellCheckLang: function(event) {

        var self = this,
            lang = $(event.currentTarget).data('lang'),
            localization = $.extend({}, G5.props.spellCheckerLocalization.en, G5.props.spellCheckerLocalization[lang]),
            $commentBox = this.$el.find('#ezCheersCommentBox textarea');

        if (!self.$el.find('#ezCheersCommentBox .badwordsContainer').length) {
            self.$el.find('#ezCheersCommentBox').append('<div class="badwordsContainer"><div class="badwordsWrapper"><div class="badwordsContent" /></div></div>');
        }

        $commentBox
            .spellchecker({
                url: G5.props.spellcheckerUrl,
                lang: lang,
                localization: localization,
                engine: 'jazzy',
                suggestBoxPosition: 'above',
                innerDocument: false,
                wordlist: {
                    action: 'html',
                    element: self.$el.find('#ezCheersCommentBox .badwordsContent')
                }
            })
            // and call the spellchecker
            .spellchecker('check', {
                callback: function(result) {
                    if (result === true) {
                        self.$el.find('#ezCheersCommentBox .spellcheck-badwords').remove();
                        alert(localization.noMisspellings);
                    } else {
                        self.$el.find('#ezCheersCommentBox .spellcheck-badwords')
                            .prepend('<strong>' + localization.menu + ':</strong>')
                            .append('<a class="close"><i class="icon-close" /></a>');
                    }
                },
                localization: lang
            });

        // add a click handler for the badwords box close
        this.$el.find('#ezCheersCommentBox').on('click', '.spellcheck-badwords .close', function() {
            self.$el.find('#ezCheersCommentBox .spellcheck-badwords').remove();
        });
    },

    destroy: function() {
        // do anything here we need to destroy
        this.remove();
        this.unbind();

    },

    closeModule: function() {
        this.resetModule();
        this.$el.find('.ezCheersModal').hide();
        this.trigger('closed', this);
        this.close();
    },

    resetModule: function() {
        console.log('we are resettting');

    }
});