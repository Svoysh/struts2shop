/**
 * Global app variable.
 */
var APP = {
	contextPath: "", 
	
//	ckeditorBasePath: this.contextPath + "/lib/ckeditor/3.0rc/", 
	fckeditorBasePath: this.contextPath + "/lib/fckeditor/2.6.4.1/",
	animationSpeed: "normal"
};

/**
 * Shortcut for APP.
 */
//var a = APP;

function focusElem(elemId)
{
	$("#" + elemId).focus();
}

function focusElemIfEmpty(elemId)
{
	var $elem = $("#" + elemId);
//	if ($elem.get(0).value.length <= 0)
	if ($elem.val().length <= 0)
	{
		$elem.focus();
	}
}

/*function toggleCheckBox(checkBox)
{
	checkBox.checked = !checkBox.checked;
}*/

/**
 * @param textAreaId {String} id of tag textarea.
 */
function createHtmlEditor(textAreaId)
{
	createFckeditor(textAreaId);
}

/**
 * @param textAreaId {String} id of tag textarea.
 */
function createFckeditor(textAreaId)
{
	var oFCKeditor = new FCKeditor(textAreaId);
	// "/fckeditor/" is the default value.
	oFCKeditor.BasePath = APP.fckeditorBasePath;	
	oFCKeditor.Height = 300;
	oFCKeditor.ToolbarSet = "WithoutForms";
//	oFCKeditor.Value = '<p>This is some <strong>sample text<\/strong>. You are using <a href="http://www.fckeditor.net/">FCKeditor<\/a>.<\/p>';
//	oFCKeditor.Create();
	oFCKeditor.ReplaceTextarea();
}

/**
 * Load HTML page via AJAX.
 * 
 * From jQuery Docs: Internet Explorer caches the loaded file, 
 * so you should pass some extra random GET parameter to prevent caching 
 * if you plan to call this function more than once.
 * 
 * @param url {String} URL to load HTML page from;
 * 
 * @param receiverSelector {String} jQuery DOM selector to receive loaded HTML, 
 *     ex: "#id", ".class", etc;
 */
function nonCachingLoad(url, receiverSelector)
{
	$.ajax({
		url : url,
		cache : false,
		success : function(html) {
			$(receiverSelector).html(html);
		}
	});
}

/**
 * Set AJAX submit for from.
 * 
 * @param formSelector {String} jQuery DOM selector of from to submit via AJAX, 
 *     ex: "#id", ".class", etc;
 *     
 * @param receiverSelector {String} jQuery DOM selector to receive loaded HTML, 
 *     ex: "#id", ".class", etc;
 */
function nonCachingFormSubmit(formSelector, receiverSelector)
{
	$(formSelector).ajaxSubmit({
		cache : false,
		success : function(html) {
			$(receiverSelector).html(html);
		}
	});
	// Return 'false' to prevent normal browser submit and page navigation .
	return false; 
}

/**
 * @param elemSelector {String} jQuery DOM selector, ex: "#summaryTextArea";
 * @param checkBox {Element} if checked, then element selected 
 *     by <code>elemSelector</code> will be disabled; otherwise - enabled;
 */
APP.toggleEnabledElemByCheckBox = function(elemSelector, checkBox)
{
	var $elem = $(elemSelector);
	var disabled = checkBox.checked;
	var disabledStr = "disabled";
	
	if (disabled) 
	{
		$elem.attr(disabledStr, disabledStr);
	}
	else 
	{
		$elem.removeAttr(disabledStr);
	}
}

function FormCopier(formPrefix)
{
    // Helper variable to be used instead of 'this' in subfunctions.
    // TODO: Rename to "that".
    var object = this;

    this.formPrefix = formPrefix;
    // TODO: Rename to "this.indexPattern"?
    this.idPattern = /%id%/g;
    
    // Relates to first index of array or list, like 0 in Java, C and 1 in Pascal.
    this.firstIndex = 0;
    this.animationSpeed = APP.animationSpeed;
//    this.animationSpeed = 0;

	var formPatternHtml = "";

	var getFormPatternHtml = function()
	{
		return $("#" + object.formPrefix + "-pattern").html();
	};
	
	this.reloadPattern = function() 
	{
		formPatternHtml = getFormPatternHtml();
	};
	
    this.reloadPattern();
    
    var formBlockId = "#" + this.formPrefix + "-block";
    var $formBlock = $(formBlockId);
    var removeAllButtonId = "#" + this.formPrefix + "-removeAllButton";
    var $removeAllButton = $(removeAllButtonId);

    this.$formBlock = $formBlock;
    this.countForms = function()
    {
         return $formBlock.children().length;
    };
    
    var rowCount = this.countForms();
    var currentRow = rowCount + this.firstIndex;
    var allCollapsed = false;

    // Event handlers.
	this.onAdd = function($newForm){};
//	this.onRemove = function(n){};
//	this.onRemoveAll = function(){};
//	this.onCollapse = function(serial){};
//	this.onExpand = function(serial){};
	
    this.addOne = function()
    {
         this.add(1);
    };

    this.add = function(n)
    {
        if(n <= 0) return;

        if(n > 0 && rowCount <= 0)
        {
            this.showRemoveAllButton();
            rowCount = 0;
        }

        for(var i = 0; i < n; i++)
        {
            var newFormHtml = formPatternHtml.replace(this.idPattern, currentRow.toString());
            $formBlock.append(newFormHtml);

            var removeButtonId = "#" + this.formPrefix + "-removeButton-" + currentRow.toString();
            var $removeButton = $(removeButtonId);
            $removeButton.get(0).row = currentRow;
            $removeButton.click(function()
            {
                object.remove(this.row);
            });

            var newFormId = "#" + this.formPrefix + "-" + currentRow.toString();
            var $newForm = $(newFormId).fadeIn(this.animationSpeed);
            
            currentRow++;
            rowCount++;
            
            this.onAdd($newForm);
        }
    };

    this.remove = function(serial)
    {
    	// TODO: Refactor: Duplication: extract method: getFormId(serial)
    	var removeFormId = "#" + this.formPrefix + "-" + serial.toString();
        $(removeFormId).fadeOut(this.animationSpeed, function()
        {
            $(this).remove();
            if(rowCount > 0)
            {
                rowCount--;
            }

            /*
                TODO: Maybe count children by ".PersonRow" class for faster (need to add class to each row's div)? (yes)
                like:
                    if(personCount <= 0 && $(".PersonRow").length <= 0) {...}
             */
            if(rowCount <= 0 && object.countForms() <= 0)
            {
                object.hideRemoveAllButton();
                rowCount = 0;
            }
        });
    };

    this.removeAll = function()
    {
        $formBlock.fadeOut(object.animationSpeed, function()
        {
            $(this).empty().show();
            object.hideRemoveAllButton();
            rowCount = 0;
        });
    };

    this.hideRemoveAllButton = function()
    {
    	$removeAllButton.attr("disabled", "disabled");
    };

    this.showRemoveAllButton = function()
    {
    	$removeAllButton.removeAttr("disabled");
    };
    
    /*this.hideRemoveAllButton = function()
    {
        $removeAllButton.hide();
    };

    this.showRemoveAllButton = function()
    {
        $removeAllButton.show();
    };
    
    this.disableRemoveAllButton = function()
    {
        $removeAllButton.attr("disabled", "disabled");
    };

    this.enableRemoveAllButton = function()
    {
        $removeAllButton.removeAttr("disabled");
    };*/
    
    var toggleAllCollapsed = function()
    {
    	object.allCollapsed = !object.allCollapsed;
    };
    
    var getSerialFromJqContent = function($formContent)
    {
    	var contentId = $formContent.attr("id");
		var prefix = new RegExp(object.formPrefix + "-content-", "g");
		var idStr = contentId.replace(prefix, "");
		
		return parseInt(idStr);
    };
    
    var showSummary = function($formContent)
    {
    	var serial = getSerialFromJqContent($formContent);
    	var summaryId = "#" + object.formPrefix + "-summary-" + serial.toString();
    	var summary = object.getSummary(serial);
        $(summaryId).html(summary);
    };
    
    var hideSummary = function($formContent)
    {
    	var serial = getSerialFromJqContent($formContent);
    	var summaryId = "#" + object.formPrefix + "-summary-" + serial.toString();
        $(summaryId).html("");
    };
    
    // Must return summary HTML string representation of form values.
	// NOTE: Override in FormCopier instance.
    this.getSummary = function(serial)
    {
    	return "";
    };
    
    this.toggle = function(serial)
    {
    	var toggleContentId = "#" + this.formPrefix + "-content-" + serial.toString();
        $(toggleContentId).slideToggle(object.animationSpeed, function()
        {
        	var $formContent = $(this);
        	if ($formContent.hasClass("Collapsed"))
        	{
        		hideSummary($formContent);
        	}
        	else
        	{
        		showSummary($formContent);
        	}
        	// TODO: Const: "Collapsed".
    		$formContent.toggleClass("Collapsed");
        });
    };
    
    this.toggleAll = function()
    {
    	// Expand.
    	if (object.allCollapsed)
    	{
    		toggleAllCollapsed();
	    	var collapseSelectors = formBlockId + " .Content.Collapsed";
	        $(collapseSelectors).slideDown(object.animationSpeed, function()
	        {
	        	var $formContent = $(this);
	        	$formContent.toggleClass("Collapsed", false);
	        	hideSummary($formContent);
	        	
	            // TODO: Need? (n)
//	        	object.onExpand(serial);
	        });
    	}
    	// Collapse.
    	else
    	{
    		toggleAllCollapsed();
    		var expandSelectors = formBlockId + " .Content";
	        $(expandSelectors).slideUp(object.animationSpeed, function()
	        {
	        	var $formContent = $(this);
	        	$formContent.toggleClass("Collapsed", true);
	        	showSummary($formContent);
	        	
            	// TODO: Need? (n)
//	        	object.onCollapse(serial);
	        });
    	}
    };
};


/**
 * Anonymous function which do app initialization.
 */
(function() {
	// Code to run after this JS file will be loaded.
})();
 