/*exported CommunicationsEditView */
/*global
TemplateManager,
PageView,
SelectAudienceParticipantsView,
CommunicationsImageUploadView,
CommunicationsEditModel,
CommunicationsEditView:true
*/
CommunicationsEditView = PageView.extend( {
    mode: null,

    imagesTypeMap: {
        'banners': 'banner',
        'news': 'news'
    },

    tableTplMap: {
        'banners': 'bannersContentTable',
        'news': 'newsContentTable',
        'resources': 'resourceCenterContentTable',
        'tips': 'tipsContentTable'
    },

    audienceParamsMap: {
        'banners': 'communicationsBanners',
        'news': 'communicationsNews',
        'resources': 'communicationsResourceCenter',
        'tips': 'communicationsTips'
    },

    contentDocMap: {
        'banners': 'bannerDoc',
        'resources': 'resourceDoc'
    },

    addedContentIdMap: {
        'banners': 'bannerid',
        'news': 'newsid',
        'resources': 'resourceid',
        'tips': 'tipid'
    },

    initialize: function( ) {
        var that = this;

        //set the appname (getTpl() method uses this)
        this.appName = 'communications';

        //this is how we call the super-class initialize function (inherit its magic)
        PageView.prototype.initialize.apply( this, arguments );

        //inherit events from the superclass
        this.events = _.extend( {}, PageView.prototype.events, this.events );

        this.mode = this.options.mode;

        if( this.mode == 'banners' || this.mode == 'news' ) {
            // image upload view
            this.imageUploadView = new CommunicationsImageUploadView( {
                el: '#uploadImageModal',
                type: this.imagesTypeMap[ this.mode ],
                formData: {
                    sizes: [ 'default', 'mobile', 'max' ]
                }
            } );
            this.imageUploadView.on( 'saveImages', this.doSaveImages, this );
        }

        //create model
        this.communicationsEditModel = new CommunicationsEditModel( {}, {
            mode: this.mode
        } );

        this.communicationsEditModel.loadData();

        this.$el.find( '.datepickerTrigger' ).datepicker( { container: this.$el } );

        if( this.mode == 'news' ) {
            this.$el.find( '.richtext' ).htmlarea( G5.props.richTextEditor );
        }

        if( this.mode == 'tips' ) {
            this.renderSpellcheck();
            this.updateContribComment();
        }

        this.checkForServerErrors();

        //If model did not have data, it feches data from server, page then renders on 'loadStandingsDataFinished'
        this.communicationsEditModel.on( 'loadDataFinished', function() {
            that.render();
            that.renderContentInfo();
        } );

        this.on( 'contentRendered', function() {
            that.renderAudience();
            that.displayContentTable();

            if( this.mode == 'banners' || this.mode == 'news' ) {
                that.renderChooseImages();
            }
        } );

        this.on( 'imagesRendered', function() {
            that.cardCycleInit();
        } );

        this.on( 'docUploaded', function() {
            that.updateDocAttachment();
        } );

        this.communicationsEditModel.on( 'contentAdded', function( newData, isAdded ) {
            that.render( newData, isAdded );
        } );

        this.communicationsEditModel.on( 'contentRemoved', function( content ) {
            that.render( content );
        } );

        this.communicationsEditModel.on( 'imageAdded', function( newData ) {
            that.updateImageDisplay( newData );
        } );

        this.communicationsEditModel.on( 'contentUpdated', function( content ) {
            that.doUpdateContent( content );
        } );
    },

    events: {
        //adding
        'click .addLanguageContent a': 'addNewContent',
        'click .addUrl': 'doAddUrl',
        'click .attachUrlBtn': 'doAttachUrl',
        'click .addDoc': 'doAttachDoc',
        //removing
        'click .removeLink': 'doUnattachUrlDoc',
        'click .remParticipantControl': 'confirmRemoveContent',
        'click .contentRemoveDialogConfirm': 'doRemoveContent',
        //edit
        'click .editColumn a': 'doEditContent',
        'click .defaultColumn input': 'doSetDefault',
        //images
        'click #thumbnailSelect li': 'chooseImage',
        //save & validate
        'click .contribCommentWrapper .dropdown-menu li a': 'doContribCommentSpellCheck',
        'keyup .contentUrl': 'validateUrl',
        'change .contentDoc': 'validateDoc',
        'click .saveContent': 'doSaveContent',
        'click .cancelContent': 'confirmCancelSave',
        'click .contentSubmit': 'submitForm',
        'click .contentCancel': 'confirmCancelContent',
        'click #saveCancelDialogConfirm': 'doCancelSave',
        'click .contentCancelDialogConfirm': 'doCancelConcent',
        'click #saveCancelDialogCancel, .contentCancelDialogCancel, .contentRemoveDialogCancel': function( e ) {
            e.preventDefault();
            $( e.currentTarget ).closest( '.qtip' ).qtip( 'hide' );
        },
        'click #thumbnailPager a': 'cardCyclePager'
    },

    render: function( newData, isAdded ) {
        var $contentTable = this.$el.find( '.contentTable tbody' ),
            that = this,
            content = this.communicationsEditModel.getContent();

        G5.util.hideSpin( this.$el );

        //Push values to template
        TemplateManager.get( this.tableTplMap[ this.mode ], function( tpl ) {
            if ( isAdded ) {
                // check to see if this is the first one and mark it the default
                if ( content.length === 1 ) {
                    newData.isDefaultLang = true;
                }

                $contentTable.append( tpl( newData ) );
            } else {
                $contentTable.html( '' );

                _.each( content, function( item ) {
                    $contentTable.append( tpl( item ) );

                } );
            }
            that.trigger( 'contentRendered' );
        } );
    },

    renderAudience: function() {
        if ( !this.selectAudience ) {
            this.selectAudience = new SelectAudienceParticipantsView( {
                el: this.$el.find( '.selectAudienceTableWrapper' ),
                dataParams: { page: this.audienceParamsMap[ this.mode ] }
            } );
        }
    },

    renderContentInfo: function() {
        var model = this.communicationsEditModel,
            $contentTitle = this.$el.find( '.contentTitle' ),
            $contentStartDate = this.$el.find( '.contentStartDate' ),
            $contentEndDate = this.$el.find( '.contentEndDate' );

        //fill in values
        if ( model ) {
            $contentTitle.val( model.getContent( 'Title' ) );
            $contentStartDate.val( model.getContent( 'StartDate' ) );
            $contentEndDate.val( model.getContent( 'EndDate' ) );
        }
    },

    // tips only
    renderSpellcheck: function() {
        var $langs = this.$el.find( '.commentTools .spellchecker .dropdown-menu' );

        // append each language listed in the spellCheckerLocalesToUse array
        _.each( G5.props.spellCheckerLocalesToUse, function( v ) {
            var l = G5.props.spellCheckerLocalization[ v ];
            if ( l ) {
                $langs.append( '<li><a href="' + v + '">' + l.menu + '</a></li>' );
            }
        } );
    },

    displayContentTable: function() {
        var $addedContent = this.$el.find( '.contentTable' ),
            $addAnotherBtn = this.$el.find( '.addLanguageContent a' ),
            $submitBtn = this.$el.find( '.contentSubmit' ),
            $newContent = this.$el.find( '.addNewContentContainer' );

        if ( $addedContent.find( 'tbody' ).children().length < 1 ) {
            $addedContent.responsiveTable( { destroy: true } );

            $addedContent.hide();
            $addAnotherBtn.addClass( 'disabled' );
            $submitBtn.attr( 'disabled', '' );
            this.checkAvailableLangs();
            $newContent.show();
        } else {
            $addedContent.show();
            if ( this.checkAvailableLangs() !== false ) {
                $addAnotherBtn.removeClass( 'disabled' );
            }
            $submitBtn.removeAttr( 'disabled' );
            $newContent.hide();

            this.disableDefault();
            $addedContent.responsiveTable();
        }
    },

    checkAvailableLangs: function( selectedLang ) {
        var $chooseLang = this.$el.find( '#chooseLanguage' ),
            $langs = $chooseLang.find( 'option' ),
            content = this.communicationsEditModel.getContent(),
            usedLangs = _.pluck( content, 'language' );

        if ( !selectedLang && $langs.length <= usedLangs.length ) {
            return false;
        }

        $langs.each( function() {
            var $lang = $( this );

            if ( _.indexOf( usedLangs, $lang.val() ) >= 0 && $lang.val() !== selectedLang ) {
                $lang.attr( 'disabled', 'disabled' );
            } else {
                $lang.removeAttr( 'disabled' );
            }
        } );

        $chooseLang.val( selectedLang || $langs.not( ':disabled' ).first().val() );
    },

    disableDefault: function() {
        var $addedContent = this.$el.find( '.contentTable' ),
            $defaultCol = $addedContent.find( '.defaultColumn input' );

        if ( $defaultCol.hasClass( 'systemDefault' ) ) {
            _.each( $defaultCol, function() {
                $defaultCol.attr( 'disabled', '' );
            } );
        }
    },

    // banners and news only
    renderChooseImages: function() {
        var $imagesSection = this.$el.find( '.chooseImagesSection' ),
            // tplName = 'CommunicationsBannerEdit',
            // tplUrl = G5.props.URL_TPL_ROOT || G5.props.URL_APPS_ROOT + 'communications/tpl/',
            that = this;
        // console.log( tplName, tplUrl );
        $imagesSection.empty();

        TemplateManager.get( 'chooseImages', function( tpl, vars, subTpls ) {
            that.subTpls = subTpls;
            $imagesSection.append( tpl( that.communicationsEditModel.toJSON() ) );
            that.trigger( 'imagesRendered' );
        }, this.tplPath );
    },

    // banners and news only
    chooseImage: function( e ) {
        var $tar = $( e.currentTarget ),
            $li = this.$el.find( '#thumbnailSelect li' ),
            id = $tar.data( 'id' ),
            modelImgs = this.communicationsEditModel.getImages(),
            $imageCont = this.$el.find( '.imageLargeWrapper' ),
            $selectedImage = this.$el.find( '.selectedImageInput' ),
            selectedImage = _.where( modelImgs, { id: id } ),
            preloadedImages;

        if ( selectedImage.length ) {
            if( selectedImage[ 0 ].imageSizeMax ) {
                preloadedImages = {
                    images: [
                        {
                            imageUrl: selectedImage[ 0 ].imageSizeMax,
                            size: 'max'
                        },
                        {
                            imageUrl: selectedImage[ 0 ].imageSize,
                            size: 'default'
                        },
                        {
                            imageUrl: selectedImage[ 0 ].imageSizeMobile,
                            size: 'mobile'
                        }
                    ]
                };
            }else{
                preloadedImages = {
                    images: [
                        {
                            imageUrl: selectedImage[ 0 ].imageSize_max,
                            size: 'max'
                        },
                        {
                            imageUrl: selectedImage[ 0 ].imageSize,
                            size: 'default'
                        },
                        {
                            imageUrl: selectedImage[ 0 ].imageSize_mobile,
                            size: 'mobile'
                        }
                    ]
                };
            }

        }
        /*
         *  NOTE: uncomment this section only if we want to copy images from the first item when clicking upload the first time
         *  NOTE: this would probably be best done in the addNewContent method as part of a bigger data copy
         *  ANOTHER NOTE: this would need to be genericized if it's enabled
         *
        else if(this.communicationsEditModel.get('bannerTable').banners.length) {
            preloadedImages = {
                images: [
                    {
                        imageUrl : this.communicationsEditModel.get('bannerTable').banners[0].imageSize_max,
                        size : "max"
                    },
                    {
                        imageUrl : this.communicationsEditModel.get('bannerTable').banners[0].imageSize,
                        size : "default"
                    },
                    {
                        imageUrl : this.communicationsEditModel.get('bannerTable').banners[0].imageSize_mobile,
                        size : "mobile"
                    }
                ]
            };
        }
         *
         */

        if ( $tar.hasClass( 'uploadContainer' ) ) {
            this.imageUploadView.showModal( preloadedImages );
            return;
        }

        $imageCont.find( 'img' ).remove();
        $li.removeClass( 'selected' );
        $tar.addClass( 'selected' );

        $selectedImage.attr( 'checked', 'checked' ).val( 'on' );

        if ( selectedImage.length ) {
            $imageCont.html( '<img src="' + selectedImage[ 0 ].imageSize + '"/>' );
        }
    },

    addNewContent: function( e ) {
        var that = this,
            $addContentContainer = this.$el.find( '.addNewContentContainer' ),
            $addAnotherBtn = this.$el.find( '.addLanguageContent a' ),
            $largeImg = this.$el.find( '.imageLargeWrapper' ),
            $textarea = this.$el.find( '.richtext' );

        e.preventDefault();

        if ( $addAnotherBtn.hasClass( 'disabled' ) ) {
            e.preventDefault();
            return false;
        }

        this.checkAvailableLangs();

        if( this.mode == 'tips' ) {
            this.updateContribComment();
        }

        if ( $largeImg.data( 'listenerAdded' ) !== true ) {
            G5._globalEvents.on( 'windowResized', function() {
                G5.util.sizeByRatio( $largeImg, null, [ 'height', 'line-height' ] );
            } );
            G5.util.sizeByRatio( $largeImg, null, [ 'height', 'line-height' ] );
            $largeImg.data( 'listenerAdded', true );
        }

        $addContentContainer.slideDown( G5.props.ANIMATION_DURATION, function() {
            if( that.mode == 'news' ) {
                $textarea.htmlarea( G5.props.richTextEditor );
            }
            if( that.mode == 'banners' || that.mode == 'news' ) {
                // trigger the resize event on the window so the carousel inside the image picker calibrates itself properly
                $( window ).resize();
            }
        } );

        this.$el.find( '.addedContent' ).addClass( 'isEditing' );
        $addAnotherBtn.addClass( 'disabled' );
    },

    // tips only
    updateContribComment: function() {
        var $inp = this.$el.find( '.contribCommentInp' ),
            maxChars = parseInt( $inp.attr( 'maxlength' ) ),
            $remChars = this.$el.find( '.commentTools .remChars' );

        // enforce maxlength (ie only)
        if ( $.browser.msie && $inp.val().length > maxChars ) {
            $inp.val( $inp.val().slice( 0, maxChars ) );
        }

        // remaining chars
        $remChars.text( $.format.number( maxChars - $inp.val().length ) );
    },

    // tips only
    doContribCommentSpellCheck: function( e ) {
        var $tar = $( e.currentTarget ),
            lang = $tar.attr( 'href' ),
            localization = $.extend( {}, G5.props.spellCheckerLocalization.en, G5.props.spellCheckerLocalization[ lang ] ),
            $comment = this.$el.find( '.contribCommentInp' ),
            $badWords;

        e.preventDefault();

        if ( !this.$el.find( '.contribCommentWrapper .badwordsContainer' ).length ) {
            this.$el.find( '.contribCommentWrapper' ).append( '<div class="badwordsContainer"><div class="badwordsWrapper"><div class="badwordsContent" /></div></div>' );
        }
        $badWords = this.$el.find( '.contribCommentWrapper .badwordsContent' );

        $comment.spellchecker( {
            url: G5.props.spellcheckerUrl,
            lang: lang,
            localization: localization,
            engine: 'jazzy',
            suggestBoxPosition: 'above',
            innerDocument: false,
            wordlist: {
                action: 'html',
                element: $badWords
            }
        } );

        $comment.spellchecker( 'check', {
            localization: lang,
            callback: function( result ) {
                if ( result === true ) {
                    $badWords.find( '.spellcheck-badwords' ).remove();
                    //alert( localization.noMisspellings );
                }                else {
                    $badWords.find( '.spellcheck-badwords' )
                        .prepend( '<strong>' + localization.menu + ':</strong>' )
                        .append( '<a class="close"><i class="icon-close" /></a>' );
                }
            }
        } );

        // add a click handler for the badwords box close
        $badWords.on( 'click', '.close', function() {
            $badWords.find( '.spellcheck-badwords' ).remove();
        } );
    },

    doAddDoc: function( e ) {
        var $tar = $( e.currentTarget ),
            $popoverCont = this.$el.find( '.addDocPopover' );
            // $attachBtn = this.$el.find( '.attachDocBanner' ),
            // $form = this.$el.find( '.sendForm' );

        e.preventDefault();

        // $attachBtn.attr( 'disabled', '' );

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $popoverCont, this.$el );
        }
    },

    doAddUrl: function( e ) {
        var $tar = $( e.currentTarget ),
            $popoverCont = this.$el.find( '.addUrlPopover' ),
            $attachBtn = this.$el.find( '.attachUrlBtn' );
            // $form = this.$el.find( '.sendForm' );

        e.preventDefault();

        $attachBtn.attr( 'disabled', '' );

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $popoverCont, this.$el );
        }

        this.validateUrl();
    },

    attachPopover: function( $trig, cont, $container, $viewport ) {
        $trig.qtip( {
            content: { text: cont },
            position: {
                my: 'bottom center',
                at: 'top center',
                container: $container,
                viewport: $viewport || $( window ),
                adjust: {
                    method: 'shift none'
                }
            },
            show: {
                event: 'click',
                ready: true
            },
            hide: {
                event: 'unfocus',
                fixed: true,
                delay: 200
            },
            style: {
                classes: 'ui-tooltip-shadow ui-tooltip-light participantPopoverQtip',
                tip: {
                    corner: true,
                    width: 20,
                    height: 10
                }
            }
        } );
    },

    doAttachUrl: function( e ) {
        var $tar = $( e.currentTarget ),
            $inp = $tar.parents().find( '.contentUrl' ),
            $attachCont = this.$el.find( '.contentDisplayAttached .contentDisplayLink' ),
            $hiddenInp = this.$el.find( '.contentLink' ),
            $removeLink = this.$el.find( '.removeLink' ),
            $docForm = this.$el.find( '.addDocUpload' );

        $attachCont.html( $inp.val() ).attr( 'title', $inp.val() );
        $hiddenInp.val( $inp.val() );
        $removeLink.show();

        $tar.closest( '.qtip' ).qtip( 'hide' );
        $inp.val( '' );
        $docForm[ 0 ].reset();
    },

    doAttachDoc: function( e ) {
        var that = this,
            $form = this.$el.find( '.addDocUpload' ),
            $tar = $( e.currentTarget ),
            $inp = $tar.parents().find( '.contentDoc' ),
            $container = this.$el.find( '.contentDisplayAttached' );

        e.preventDefault();

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, this.$el.find( '.addDocPopover' ), this.$el );

            $form.fileupload( {
                url: G5.props.URL_JSON_COMMUNICATION_UPLOAD_DOCUMENT,
                dataType: 'g5json',
                beforeSend: function() {
                    $tar.qtip( 'hide' );
                    G5.util.showSpin( $container );
                    that.$el.find( '.saveContent' ).attr( 'disabled', 'disabled' ).addClass( 'disabled' );
                },
                done: function( e, data ) {
                    var msg = data.result.data.messages[ 0 ];

                    if ( msg.isSuccess ) {
                        // set the document data on the model
                        that.communicationsEditModel.set( that.contentDocMap[ that.mode ], msg.docURL );

                        $inp.val( '' );
                        that.trigger( 'docUploaded' );
                    } else {
                        //alert( msg.text );
                    }

                    G5.util.hideSpin( $container );
                    that.$el.find( '.saveContent' ).removeAttr( 'disabled' ).removeClass( 'disabled' );
                }
            } );
        }
    },

    updateDocAttachment: function() {
        var $attachCont = this.$el.find( '.contentDisplayAttached' ),
            $attach = $attachCont.find( '.contentDisplayLink' ),
            $hiddenInp = this.$el.find( '.contentLink' ),
            $removeLink = $attachCont.find( '.removeLink' ),
            docUrl = this.communicationsEditModel.get( this.contentDocMap[ this.mode ] ),
            $urlInp = this.$el.find( '.contentUrl' );

        G5.util.hideSpin( $attachCont );

        $attach.html( docUrl );
        $hiddenInp.val( docUrl );
        $removeLink.show();
        $urlInp.val( '' );
    },

    doUnattachUrlDoc: function() {
        var $attachCont = this.$el.find( '.contentDisplayAttached' ),
            $attach = $attachCont.find( '.contentDisplayLink' ),
            $hiddenInp = $attachCont.find( '.contentLink' ),
            $removeLink = $attachCont.find( '.removeLink' );

        $attach.html( '' );
        $hiddenInp.val( '' );
        $removeLink.hide();
    },

    doSaveImages: function( images ) {
        var imageData = {
            id: null,
            isUploaded: true,
            imageSize: _.find( images, function( img ) { return img.size === 'default'; } ).imageUrl,
            imageSizeMax: _.find( images, function( img ) { return img.size === 'max'; } ).imageUrl,
            imageSizeMobile: _.find( images, function( img ) { return img.size === 'mobile'; } ).imageUrl
        };

        this.communicationsEditModel.addImage( imageData );
    },

    updateImageDisplay: function( newData ) {
        var $uploadContainer = this.$el.find( '.uploadContainer' ),
            $largeImage = this.$el.find( '.imageLargeWrapper' ),
            $selectedImage = this.$el.find( '.selectedImageInput' ),
            imageId = newData.id;

        $uploadContainer.attr( 'data-id', imageId );
        $largeImage.html( '<img src="' + newData.imageSize + '" />' );

        if ( imageId.match( 'addedImage' ) ) {
            this.$el.find( '#thumbnailSelect li' ).filter( '.uploadContainer' )
                .data( 'id', imageId )
                .addClass( 'selected' )
                .siblings().removeClass( 'selected' )
                .end().find( 'img' ).show().attr( 'src', newData.imageSizemobile );

            $selectedImage.attr( 'checked', 'checked' ).val( 'on' );
        }
    },

    doSaveContent: function( e ) {
        var that = this,
            $tar = $( e.currentTarget ),
            $addContent = this.$el.find( '.addNewContentContainer' ),
            $addAnotherBtn = this.$el.find( '.addLanguageContent a' ),
            $languageDisp = this.$el.find( '#chooseLanguage option:selected' ).text(),
            $language = this.$el.find( '#chooseLanguage option:selected' ).val(),
            $title = this.$el.find( '.contentLinkTitle' ).val(),
            $link = this.$el.find( '.contentDisplayAttached span' ).html(),
            $imageId = this.$el.find( '#thumbnailContainer .selected' ).data( 'id' ),
            modelImages = this.mode == 'banners' || this.mode == 'news' ? this.communicationsEditModel.getImages() : [],
            modelImageId,
            modelImg,
            modelImgMax,
            modelImgMobile,
            // news only
            $story = this.$el.find( '.messageSection textarea' ).val(),
            $headline = this.$el.find( '.contentHeadline' ).val(),
            // tips only
            $content = this.$el.find( '.contribCommentInp' ).val(),
            contentId;

        e.preventDefault();

        if ( !G5.util.formValidate( $addContent.find( '.validateme' ) ) ) {
            // invalid, was generic error, qtip visible (we use this below)
            return false;
        }

        _.each( modelImages, function( image ) {
            if ( $imageId === image.id ) {
                modelImg = image.imageSize;
                modelImgMax = image.imageSizeMax;
                modelImgMobile = image.imageSizeMobile;

                modelImageId = image.id;
            }
        } );

        $addContent.slideUp( G5.props.ANIMATION_DURATION, function() {
            var $contentTableId = that.$el.find( '.addedContent .hasEdited' ).data( that.addedContentIdMap[ that.mode ] ),
                $contentIndexId = that.$el.find( '.addedContent .hasEdited' ).data( 'indexid' ),
                data;

            if ( $contentTableId ) {
                contentId = $contentTableId.toString();
            }

            //Save data
            data = {
                id: null || contentId,
                index: null || $contentIndexId,
                languageDisplay: $languageDisp,
                language: $language,
                headline: $headline,
                content: $content,
                imageId: null || modelImageId,
                imageSize: modelImg,
                imageSize_max: modelImgMax,
                imageSize_mobile: modelImgMobile,
                title: $title,
                link: $link,
                story: $story
            };

            if ( that.$el.find( '.addedContent tr' ).hasClass( 'hasEdited' ) ) {
                that.$el.find( '.addedContent tr' ).removeClass( 'hasEdited' );
                that.communicationsEditModel.updateContent( data );
            } else {
                that.communicationsEditModel.addContent( data );
            }

            if ( that.$el.find( '.addedContent' ).hasClass( 'isEditing' ) ) {
                that.$el.find( '.addedContent' ).removeClass( 'isEditing' );
            }

            if ( that.checkAvailableLangs() !== false ) {
                $addAnotherBtn.removeClass( 'disabled' );
            }
            $tar.removeAttr( 'disabled' );
            that.$el.find( '.contentSubmit' ).removeAttr( 'disabled' ).removeClass( 'disabled' );
        } );

        this.resetForm();
    },
    doEditContent: function( e ) {
        var $tar = $( e.currentTarget ),
            $row = $tar.parents( 'tr' ),
            rowId = $row.data( this.addedContentIdMap[ this.mode ] ),
            $addContent = this.$el.find( '.addNewContentContainer' ),
            $contentLanguage = $addContent.find( '#chooseLanguage' ),
            $contentTitle = $addContent.find( '.contentLinkTitle' ),
            $contentLink = $addContent.find( '.contentDisplayAttached .contentDisplayLink' ),
            $contentLinkHidden = this.$el.find( '.contentLink' ),
            $removeLink = $addContent.find( '.removeLink' ),
            $contentComment = $addContent.find( '.contribCommentInp' ),
            $contentIdHidden = this.$el.find( '.contentId' ),
            $contentBtn = this.$el.find( '.addLanguageContent a' ),
            $contentStory = this.$el.find( '.messageSection textarea' ),
            $contentHeadline = this.$el.find( '.contentHeadline' ),
            $contentThumb = this.$el.find( '#thumbnailSelect li' ),
            $contentLargeImg = this.$el.find( '.imageLargeWrapper' ),
            $selectedImage = this.$el.find( '.selectedImageInput' ),
            $editing,
            $newTitle,
            $newLink,
            $newHeadline,
            $storyColumn,
            $newComment,
            $newLanguageName,
            $imageId,
            contentObj;

        e.preventDefault();

        if ( $row.closest( '.addedContent' ).hasClass( 'isEditing' ) ) {
            return false;
        }
        $row.closest( '.addedContent' ).addClass( 'isEditing' );

        this.$el.find( '.contentSubmit' ).attr( 'disabled', 'disabled' ).addClass( 'disabled' );

        $row.addClass( 'isEditing hasEdited' );

        $addContent.slideDown( G5.props.ANIMATION_DURATION, function() {
            // trigger the resize event on the window so the carousel inside the image picker calibrates itthat properly
            $( window ).resize();
        } );

        if( this.mode == 'news' ) {
            this.$el.find( '.richtext' ).htmlarea( G5.props.richTextEditor );
        }

        $contentBtn.addClass( 'disabled' );

        if ( $row.hasClass( 'isEditing' ) ) {
            $editing = this.$el.find( 'tr.isEditing' );
            $newTitle = $editing.find( '.titleColumn' ).text();
            $newLink = $editing.find( '.linkColumn' ).text();
            $newHeadline = $editing.find( '.headlineColumn' ).text();
            $storyColumn = $editing.find( '.storyColumn' ).text();
            $newComment = $editing.find( '.contentColumn' ).text();
            $newLanguageName = $editing.find( '.languageColumn span' ).attr( 'data-language' );
            contentObj = _.where( this.communicationsEditModel.getContent(), { id: rowId.toString() } )[ 0 ];

            if( this.mode == 'banners' || this.mode == 'news' ) {
                $imageId = $editing.find( '.imageColumn img' ).data( 'id' ).toString();

                if ( $imageId.match( 'addedImage' ) ) {
                    $contentThumb.filter( '.uploadContainer' )
                        .addClass( 'selected' )
                        .data( 'id', $imageId )
                        .find( 'img' ).show().attr( 'src', contentObj.imageSizemobile );
                    this.updateImageDisplay( {
                        id: $imageId,
                        imageSize: contentObj.imageSize
                    } );
                    $selectedImage.attr( 'checked', 'checked' ).val( 'on' );
                } else {
                    $contentThumb.filter( '.uploadContainer' )
                        .data( 'id', '' )
                        .find( 'img' ).hide();
                    _.each( $contentThumb, function( thumb ) {
                        if ( $( thumb ).data( 'id' ).toString() === $imageId ) {
                            $( thumb ).addClass( 'selected' ).trigger( 'click' );
                        }
                    } );
                }

                if ( $contentLargeImg.data( 'listenerAdded' ) !== true ) {
                    G5._globalEvents.on( 'windowResized', function() {
                        G5.util.sizeByRatio( $contentLargeImg, null, [ 'height', 'line-height' ] );
                    } );
                    G5.util.sizeByRatio( $contentLargeImg, null, [ 'height', 'line-height' ] );
                    $contentLargeImg.data( 'listenerAdded', true );
                }

                $selectedImage.val( 'on' );
            }

            $contentLanguage.val( $newLanguageName );
            $contentTitle.val( $newTitle );
            $contentLink.html( $newLink );
            $contentLinkHidden.val( $newLink );
            $removeLink[ $contentLinkHidden.val() ? 'show' : 'hide' ]();
            $contentHeadline.val( $newHeadline );
            $contentStory.val( $storyColumn );
            $contentComment.val( $newComment );
            $contentIdHidden.val( rowId );

            if( this.mode == 'tips' ) {
                this.updateContribComment();
            }

            this.checkAvailableLangs( $newLanguageName );

            $row.removeClass( 'isEditing' );

            if( this.mode == 'news' ) {
                this.$el.find( '.richtext' ).htmlarea( 'updateHtmlArea', G5.props.richTextEditor );
            }
        }
    },

    doUpdateContent: function( updatedData ) {
        var that = this,
            $contentTable = this.$el.find( '.addedContent tbody' ),
            $contentTableRow = $contentTable.find( 'tr' );

        _.each( $contentTableRow, function( row ) {
            var newDataId = $( row ).data( that.addedContentIdMap[ that.mode ] ).toString();

            if ( newDataId === updatedData.id ) {
                //Push values to template
                TemplateManager.get( that.tableTplMap[ that.mode ], function( tpl ) {
                    $( row ).replaceWith( tpl( updatedData ) );
                } );
            }
        } );
    },

    doSetDefault: function( e ) {
        var $tar = $( e.target ).closest( 'input' ),
            id = $tar.closest( 'tr' ).data( this.addedContentIdMap[ this.mode ] ),
            content = this.communicationsEditModel.getContent();

        _.each( content, function( item, index ) {
            if ( id === item.id ) {
                content[ index ].isDefaultLang = true;
            } else {
                content[ index ].isDefaultLang = false;
            }
        } );

        if ( $tar.closest( '.isEditing' ).length > 0 ) {
            return false;
        }
    },

    confirmCancelSave: function( e ) {
        var $tar = $( e.currentTarget ),
            $cancelDialog = this.$el.find( '.saveCancelConfirm' );

        e.preventDefault();

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cancelDialog, this.$el );
        }
    },

    doCancelSave: function( e ) {
        var that = this;
        e.preventDefault();

        this.$el.find( '.cancelContent' ).qtip( 'hide' );

        this.$el.find( '.addNewContentContainer' ).slideUp( G5.props.ANIMATION_DURATION, function() {
            that.$el.find( '.addLanguageContent a' ).removeClass( 'disabled' );

            if ( that.$el.find( '.addedContent' ).hasClass( 'isEditing' ) ) {
                that.$el.find( '.addedContent' ).removeClass( 'isEditing' );
            }

            that.$el.find( '.contentSubmit' ).removeAttr( 'disabled' ).removeClass( 'disabled' );
        } );

        that.resetForm();
    },

    confirmCancelContent: function( e ) {
        var $tar = $( e.currentTarget ),
            $cancelDialog = this.$el.find( '.contentCancelConfirm' );

        e.preventDefault();

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $cancelDialog, this.$el );
        }
    },

    doCancelConcent: function( e ) {
        var $btn = this.$el.find( '.contentCancel' );

        if ( $btn.data( 'url' ) ) {
            e.preventDefault();
            window.location = $btn.data( 'url' );
        }
    },

    confirmRemoveContent: function( e ) {
        var $tar = $( e.currentTarget ),
            $popoverCont = this.$el.find( '.contentRemoveConfirmDialog' );
            // $form = this.$el.find( '.sendForm' );

        e.preventDefault();

        if ( $tar.closest( '.addedContent' ).hasClass( 'isEditing' ) ) {
            return false;
        }
        if ( $tar.hasClass( 'disabled' ) ) {
            return false;
        }

        $tar.parents( 'tr' ).addClass( 'isHiding' );

        if ( !$tar.data( 'qtip' ) ) {
            this.attachPopover( $tar, $popoverCont.eq( 0 ).clone( true ), this.$el );
        }
    },

    doRemoveContent: function( e ) {
        var $tar = $( e.currentTarget ),
            $row = this.$el.find( '.addedContent tr' ),
            id;

        $tar.closest( '.qtip' ).qtip( 'hide' );

        if ( $row.hasClass( 'isHiding' ) ) {
            id = $( '.isHiding' ).data( this.addedContentIdMap[ this.mode ] ).toString();
            $( '.isHiding' ).hide().removeClass( 'isHiding' );
        }

        this.communicationsEditModel.removeContent( id );
        e.preventDefault();
    },

    resetForm: function() {
        var $inp = this.$el.find( '.addNewContentContainer input[type="text"]' ),
            $link = this.$el.find( '.contentDisplayAttached span' ),
            $lang = this.$el.find( '#chooseLanguage' ),
            $textarea = this.$el.find( '.richtext, .addNewContentContainer textarea' ),
            $selectedImage = this.$el.find( '.selectedImageInput' ),
            $selectedThumb = this.$el.find( '#thumbnailSelect li' ),
            $userDefaultLang = this.$el.find( '#userDefaultLanguage' ).val(),
            $largeImage = this.$el.find( '.imageLargeWrapper' ),
            $removeLink = this.$el.find( '.removeLink' );

        $inp.val( '' );
        $link.html( '' );
        $lang.val( $userDefaultLang );
        $textarea.val( '' );
        $selectedImage.removeAttr( 'checked' );
        $selectedThumb.removeClass( 'selected' );
        $selectedThumb.filter( '.uploadContainer' )
                    .data( 'id', '' )
                    .find( 'img' ).hide().attr( 'src', '' );
        $largeImage.html( '' );
        $removeLink.hide();

        // removing validation errors
        this.$el.find( '.addNewContentContainer .validateme' ).each( function() {
            G5.util.formValidateMarkField( 'valid', $( this ) );
        } );

        if( this.mode == 'news' ) {
            $textarea.htmlarea( 'dispose', G5.props.richTextEditor );

            //darn IE9 issues with richtext editor causing inputs to not be clickable after save. Below seems to fix.
            $textarea.focus().blur();
        }
    },

    validateForm: function() {
        var $contentForm = this.$el.find( '.contentInfo' ),
        $valTips;

        if ( !G5.util.formValidate( $contentForm.find( '.validateme' ) ) ) {
            $valTips = $contentForm.find( '.validate-tooltip:visible' );
            // if val tips, then we have errors
            if ( $valTips.length ) {

                $.scrollTo( $valTips.get( 0 ), {
                    duration: G5.props.ANIMATION_DURATION * 2,
                    onAfter: function() {
                        $( $valTips.get( 0 ) ).data( 'qtip' ).elements.tooltip
                            .animate( { left: '+=20' }, 300 ).animate( { left: '-=20' }, 300 )
                            .animate( { left: '+=10' }, 200 ).animate( { left: '-=10' }, 200 );
                    },
                    offset: { top: -20 }
                } );

                return false;
            }
        } else {
            return true;
        }
    },

    validateRadios: function() {
        var $defaultCol = this.$el.find( '.defaultColumn' ),
            $defaultRadio = $defaultCol.find( 'input[type=radio]:checked' ),
            cont = $defaultCol.find( '.validateme' ).data( 'validateFailMsgs' );


        if ( $defaultRadio.size() > 0 ) {
            _.each( $defaultCol, function() {
                 $defaultCol.find( '.control-group' ).removeClass( 'validateme error' );
            } );

            return true;
        } else {
            this.$el.find( '.defaultHeader' ).qtip( {
                content: { text: cont },
                position: {
                    my: 'bottom center',
                    at: 'top center',
                    container: this.$el.find( '.addedContent' ),
                    viewport: this.$el || $( window ),
                    adjust: {
                        method: 'shift none'
                    }
                },
                show: {
                    ready: true
                },
                hide: {
                    event: false,
                    fixed: true
                },
                style: {
                    classes: 'ui-tooltip-shadow ui-tooltip-red validate-tooltip',
                    tip: {
                        corner: true,
                        width: 20,
                        height: 10
                    }
                }
            } );

            $defaultCol.find( '.validateme' ).addClass( 'error' );

            return false;
        }
    },

    submitForm: function( e ) {
        var data = this.$el.find( 'form' ).serializeArray();

        console.log( data );
        e.preventDefault();

        if ( this.validateForm() && this.validateRadios() ) {
            this.$el.find( '.addNewContentContainer' ).remove();
            this.$el.find( '.sendForm' ).submit();
        }
    },

    validateUrl: function() {
        var $inp = this.$el.find( '.contentUrl' ),
            $attachBtn = this.$el.find( '.attachUrlBtn' ),
            urlRe = /^(https?:\/\/)?[\da-z\.-]+\.[a-z\.]{2,6}.*$/;

        $attachBtn[ urlRe.test( $inp.val() ) ? 'removeAttr' : 'attr' ]( 'disabled', 'disabled' );
    },

    validateDoc: function() {
        var $attachBtn = this.$el.find( '.attachDoc' );

        $attachBtn.removeAttr( 'disabled' );
    },

    cardCycleInit: function() {
        var that = this,
            cc, calculate, bindToWindow;

        // create an object on the base view
        this.cardCycle = {
            // start with a bunch of handy-dandy references
            $parent: this.$el.find( '.thumbnailWrapper' ),
            $container: this.$el.find( '#thumbnailContainer' ),
            $pager: this.$el.find( '#thumbnailPager' ),
            $list: this.$el.find( '#thumbnailSelect' ),
            $cards: this.$el.find( '#thumbnailSelect li' )
        };

        // create a reference to the object that's easier to type and read
        cc = this.cardCycle;

        // this calculation is broken out so we can redo it every time the window resizes
        calculate = function() {
            // how many fit horizontally in the window?
            cc.numX = Math.floor( cc.$container.width() / cc.$cards.first().outerWidth( true ) );
            // how many fit vertically in the window?
            cc.numY = Math.floor( cc.$container.height() / cc.$cards.first().outerHeight() );
            // alright then, how many fit in the window altogether?
            cc.numFit = cc.numX * cc.numY;
            // divide the total number of cards by the number that fit per window
            cc.numSteps = Math.ceil( cc.$cards.length / cc.numFit );
            // use the full height of a step and the number that fit vertically to figure out how far we slide each time
            cc.stepSize = cc.numY * cc.$cards.first().outerHeight( true );
            // find the selected card to make sure we show it in the view, or default to the first one
            cc.showThumb = cc.showThumb || Math.max( cc.$cards.filter( '.selected' ).index(), 0 );
            // figure out which step holds the currently selected one
            cc.showStep = Math.floor( cc.showThumb / cc.numFit );

            // if there is one or fewer (ha) steps to show, hide the pager controls
            if ( cc.numSteps <= 1 ) {
                cc.$pager.find( 'a' ).hide();
            } else {
                cc.$pager.find( 'a' ).show();
            }

            // set an explicit height on the full list, just in case
            cc.$list.css( {
                height: cc.stepSize * cc.numSteps
            } );

            //Update Scroll Information
            that.calculateCardCyclePagerMeta();

            // scroll into position instantly
            that.cardCycleScroll( 0 );
        };
        // run that calculation
        calculate();

        // we use _'s handy once() method to make sure we only bind to the window resize one time
        bindToWindow = _.once( function() {
            // on the resize, we bind an throttled version of calculate that will only fire every half second at most
            $( window ).on( 'resize', _.throttle( calculate, 500 ) );
        } );
        // and bind it
        bindToWindow();
    },

    cardCyclePager: function( e ) {
        // first check to see if the target is the previous link
        var goToStep = $( e.target ).data( 'pager' ) === 'prev' || $( e.target ).closest( 'a' ).data( 'pager' ) === 'prev'
                    // if so, check to see if we're going below zero
                    ? this.cardCycle.showStep - 1 < 0
                        // if so, restart at the end by using the total number of steps and subtracting 1 to get to our zero-based index
                        ? this.cardCycle.numSteps - 1
                        // if not, go backwards one
                        : this.cardCycle.showStep - 1
                    // if we're not previous, we're next
                    : this.cardCycle.showStep + 1;
        e.preventDefault();
        // divide the step by the number of steps and take the remainder
        this.cardCycle.showStep = goToStep % this.cardCycle.numSteps;

        // log the top-left visible card in the card cycle as the one to keep visible if/when resizing
        this.cardCycle.showThumb = this.cardCycle.numFit * this.cardCycle.showStep + 1;

        // scroll it
        this.cardCycleScroll();

        //Update Scroll Information
        this.calculateCardCyclePagerMeta();
    },

    cardCycleScroll: function( duration ) {
        var coords = {
                // calculate the top by measuring the size of the step and multiplying by the number of step we're on
                top: this.cardCycle.stepSize * this.cardCycle.showStep,
                // we never scroll left/right
                left: 0
            };

        // scroll it
        this.cardCycle.$container.scrollTo( coords, {
            // use the default duration or the one passed to the function
            duration: duration === undefined ? G5.props.ANIMATION_DURATION : duration
        } );
    },

    calculateCardCyclePagerMeta: function() {
        var tpl = this.subTpls.eCardThumbnailPagerMeta,
            cc = this.cardCycle,
            json = {};

        cc.$pager.find( '#thumbnailPagerMeta' ).remove();

        if ( cc.$container.length ) {
            json.total = cc.$cards.length;
            json.actualStep = cc.showStep + 1;
            json.startNumber = ( ( json.actualStep * cc.numFit ) - cc.numFit ) + 1;
            json.endNumber = Math.min( json.actualStep * cc.numFit, json.total );

            cc.$pager.append( tpl( json ) );
        }        else {
            json = {};
            return;
        }
    },

    checkForServerErrors: function() {
        if ( $( '#serverReturnedErrored' ).val() === 'true' ) {
            $( '#communicationsErrorBlock' ).slideDown( 'fast' ); //show error block
        }
    }
} );
