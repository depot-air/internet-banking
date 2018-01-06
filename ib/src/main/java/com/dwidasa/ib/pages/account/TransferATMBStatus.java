package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 23/10/12
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public class TransferATMBStatus
{
    @Property
    private SelectModel billerModel;

    @Property
    @Persist
    private TransferView transferView;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @InjectPage
    private TransferATMBStatusInput transferAtmbStatusInput;

    public void buildBillerModel() {
        billerModel = genericSelectModelFactory.create(cacheManager.getBillers(
                com.dwidasa.engine.Constants.ATMB.TT_POSTING));
        if (billerModel.getOptions().size() > 0) {
            transferView.setProviderCode(com.dwidasa.engine.Constants.ATMB.PROVIDER_CODE);
        }
    }

    public void setupRender() {
        transferView = new TransferView();
        buildBillerModel();
    }

    public Object onSuccess()
    {
        Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.ATMB.TT_POSTING,
                transferView.getBillerCode());
        transferView.setBillerCode(biller.getBillerCode());
        transferView.setBillerName(biller.getBillerName());
        transferView.setProviderCode(com.dwidasa.engine.Constants.ATMB.PROVIDER_CODE);

        transferAtmbStatusInput.setTransferInputsView(transferView);
        return transferAtmbStatusInput;

    }

    public void pageReset()
    {
        transferView = null;
    }
}
