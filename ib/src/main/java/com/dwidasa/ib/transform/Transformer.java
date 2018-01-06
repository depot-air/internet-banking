package com.dwidasa.ib.transform;

import java.util.List;

/**
 * Base interface for all transform implementations.
 * This interface define the common methods to do transformation
 * between view object to transaction's POJO
 *
 * @author prayugo
 */
public interface Transformer {
    /**
	 * do the transformation from entity object into view object
	 * @param entities list of entity object
	 * @return list of view object
	 */
    public List transform(List entities);
}
