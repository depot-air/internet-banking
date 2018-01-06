package com.dwidasa.admin.services;

import com.dwidasa.engine.ui.GenericSelectModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.util.List;

/**
 * Alternative factory class to create SelectModel based on GenericSelectModel
 * interface.
 *
 * @author rk
 */
public class GenericSelectModelFactory {
    private final ThreadLocale threadLocale;

    public GenericSelectModelFactory(ThreadLocale threadLocale) {
        this.threadLocale = threadLocale;
    }

    /**
     * {@inheritDoc}
     */
    public SelectModel create(List<? extends GenericSelectModel> objects) {
        final List<OptionModel> options = CollectionFactory.newList();

        if (objects != null) {
            for (GenericSelectModel object : objects) {
                options.add(new OptionModelImpl(object.getLabel(threadLocale.getLocale()), object.getValue()));
            }
        }

        return new SelectModelImpl(null, options);
    }
}
