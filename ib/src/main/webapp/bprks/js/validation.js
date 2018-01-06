Tapestry.FieldEventManager.addMethods( {
	/**
	 * Overrides the corresponding method of Tapestry.FieldEventManager in tapestry.js.
	 */
	initialize : function(field) {
		this.field = $(field);

		// Identify the field, label and message and their containers
		var id = this.field.id;
		this.fieldContainer = this.field.parentNode;
		Element.extend(this.fieldContainer);

		var labelSelector = "label[for='" + id + "']";
		this.label = this.field.up("form").down(labelSelector);

		if (this.label) {
			this.labelContainer = this.label.parentNode;
			Element.extend(this.labelContainer);
		}

		var msgId = id + '-msg';
		this.msg = $(msgId);

		// If there's no message element then must not have supplied a CustomError component,
		// but rather than not displaying messages, we'll create a message element below the field
		if (!this.msg) {
			this.msg = new Element('b', {
				'id' : msgId,
				'class' : 'msg'
			});
			this.field.insert( {
				after : this.msg
			});
		}

		this.msgContainer = this.msg.parentNode;
		Element.extend(this.msgContainer);

		this.translator = Prototype.K;

		var fem = $(this.field.form).getFormEventManager();

		if (fem.validateOnBlur) {

			document.observe(Tapestry.FOCUS_CHANGE_EVENT, function(event) {
				/*
				 * If changing focus *within the same form* then perform
				 * validation. Note that Tapestry.currentFocusField does not
				 * change until after the FOCUS_CHANGE_EVENT notification.
				 */
				if (Tapestry.currentFocusField == this.field
						&& this.field.form == event.memo.form)
					this.validateInput();

			}.bindAsEventListener(this));
		}

		if (fem.validateOnSubmit) {
			$(this.field.form).observe(Tapestry.FORM_VALIDATE_FIELDS_EVENT,
					this.validateInput.bindAsEventListener(this));
		}
	},

	/**
	 * Show a validation message and decorate the parent element of it, the field, and the label.
	 * Overrides the corresponding method of Tapestry.FieldEventManager in tapestry.js.
	 *
	 * @param message
	 *            validation message to display
	 */
	showValidationMessage : function(message) {
		$T(this.field).validationError = true;
		$T(this.field.form).validationError = true;

		// Put the given message inside the msg element
		this.msg.update("<span></span>" + message);

		this.field.addClassName("error_field");
		this.msg.addClassName("error_msg");
	},

	/**
	 * Removes validation decorations if present.
	 * Overrides the corresponding method of Tapestry.FieldEventManager in tapestry.js.
	 */
	removeDecorations : function() {
		this.msg.update(null);
		this.field.removeClassName("error_field");
		this.msg.removeClassName("error_msg");
	}
} );

// Source: http://stackoverflow.com/questions/497790
var dates = {
    convert:function(d) {
        // Converts the date in d to a date-object. The input can be:
        //   a date object: returned without modification
        //  an array      : Interpreted as [year,month,day]. NOTE: month is 0-11.
        //   a number     : Interpreted as number of milliseconds
        //                  since 1 Jan 1970 (a timestamp)
        //   a string     : Any format supported by the javascript engine, like
        //                  "YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.
        //  an object     : Interpreted as an object with year, month and date
        //                  attributes.  **NOTE** month is 0-11.
        return (
            d.constructor === Date ? d :
            d.constructor === Array ? new Date(d[0],d[1],d[2]) :
            d.constructor === Number ? new Date(d) :
            d.constructor === String ? new Date(d) :
            typeof d === "object" ? new Date(d.year,d.month,d.date) :
            NaN
        );
    },
    compare:function(a,b) {
        // Compare two dates (could be of any type supported by the convert
        // function above) and returns:
        //  -1 : if a < b
        //   0 : if a = b
        //   1 : if a > b
        // NaN : if a or b is an illegal date
        // NOTE: The code inside isFinite does an assignment (=).
        return (
            isFinite(a=this.convert(a).valueOf()) &&
            isFinite(b=this.convert(b).valueOf()) ?
            (a>b)-(a<b) :
            NaN
        );
    },
    inRange:function(d,start,end) {
        // Checks if date in d is between dates in start and end.
        // Returns a boolean or NaN:
        //    true  : if d is between start and end (inclusive)
        //    false : if d is before start or after end
        //    NaN   : if one or more of the dates is illegal.
        // NOTE: The code inside isFinite does an assignment (=).
       return (
            isFinite(d=this.convert(d).valueOf()) &&
            isFinite(start=this.convert(start).valueOf()) &&
            isFinite(end=this.convert(end).valueOf()) ?
            start <= d && d <= end :
            NaN
        );
    }
};

/**
 * Future date validator will throw an error if the input date is less than current date,
 * but for current date itself will be treat as a valid input.
 */
Tapestry.Validator.futureDate = function (field, message, currentDate) {
    field.addValidator(function(value) {
        var adaptedValue = value.substring(3, 5) + "/" + value.substring(0, 2) + "/" + value.substring(6);
        if (dates.compare(adaptedValue, currentDate) < 0) {
            throw message;
        }
    });
};

/**
 * Required if will execute parameterized functionName, if true then the field is mandatory
 * otherwise optional. Won't work for radio or check box component.
 */
Tapestry.Validator.requiredIf = function (field, message, functionName) {
    $(field).getFieldEventManager().requiredCheck = function(value) {
        var result = eval(functionName+".call();");
        if (result && (typeof(value) == "undefined" || value == "" || value == null)) {
            $(field).showValidationMessage(message);
        }
    };
};

/**
 * Across field validator will execute registered function, and its returning result
 * will decide whether an exception will be fired or not.
 */
Tapestry.Validator.acrossField = function (field, message, functionName) {
    field.addValidator(function(value) {
        var result = eval(functionName+".call();");
        if (!result) {
            throw message;
        }
    });
};
