package com.dwidasa.admin.transform;

import com.dwidasa.engine.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class used for determine which {@link Transformer} implementation
 * used to transform specific transaction model data into <code>Transaction</code>
 *
 * @author prayugo
 */
public final class TransformerFactory {
    private final static Map<String, Transformer> transformers;

    static {
        transformers = new HashMap<String, Transformer>();

        //-- register transformer
        transformers.put(CellularPrefix.class.getSimpleName(), new CellularPrefixTransformer());
        transformers.put(TransactionStage.class.getSimpleName(), new TransactionStageTransformer());
        transformers.put(TreasuryStage.class.getSimpleName(), new TreasuryStageTransformer());
        transformers.put(ProductDenomination.class.getSimpleName(), new ProductDenominationTransformer());
        transformers.put(ProviderDenomination.class.getSimpleName(), new ProviderDenominationTransformer());
        transformers.put(ProviderProduct.class.getSimpleName(),  new ProviderProductTransformer());
        transformers.put(User.class.getSimpleName(), new UserTransformer());
        transformers.put(CustomerSession.class.getSimpleName(), new CustomerSessionTransformer());
        transformers.put(Transaction.class.getSimpleName(), new TransactionTransformer());
        transformers.put(KioskPrinter.class.getSimpleName(), new KioskPrinterTransformer());
        transformers.put(KioskTerminal.class.getSimpleName(), new KioskTerminalTransformer());
        transformers.put(ActivityCustomer.class.getSimpleName(), new ActivityCustomerTransformer());
    }

    /**
     * Determine which {@link Transformer} meet the requested object.
     *
     * @param className simple class name
     * @return real transform for requested object
     */
    public static Transformer getTransformer(String className) {
        Transformer result = transformers.get(className);
        if (result == null) {
            throw new RuntimeException("Transformer not available for class : " + className);
        }

        return result;
    }
}
