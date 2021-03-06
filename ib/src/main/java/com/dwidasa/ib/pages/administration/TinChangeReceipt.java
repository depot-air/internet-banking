package com.dwidasa.ib.pages.administration;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;

import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.ResultView;

/**
 * Created by IntelliJ IDEA.
 * User: wks-001
 * Date: 8/3/11
 * Time: 10:18 PM
 */

public class TinChangeReceipt {
	@Persist
	private ResultView resultView;


    @DiscardAfter
    public Object onSuccessFromForm() {
        return TinChangeInput.class;
    }

    public Object onActivate() {
        if (resultView == null) {
            return TinChangeInput.class;
        }
        return null;
    }

    public ResultView getResultView() {
        return resultView;
    }

    public void setResultView(ResultView resultView) {
        this.resultView = resultView;
    }
}
