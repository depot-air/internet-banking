package com.dwidasa.ib.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.dao.EmptyResultDataAccessException;

import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.ib.common.EvenOdd;

public class BaseCustomerRegisterList {
	private List<CustomerRegister> list;

	public void setList(List<CustomerRegister> list) {
		this.list = list;
	}

	public List<CustomerRegister> getList() {
		return this.list;
	}

	public boolean isListNotEmpty() {
		return list != null && list.size() > 0;
	}

	@SuppressWarnings("unused")
	@Property
	private boolean selectAll;

	private HashSet<Long> selectedSet;

	public HashSet<Long> getSelectedSet() {
		return selectedSet;
	}

	public void setSelectedSet(HashSet<Long> selectedSet) {
		this.selectedSet = selectedSet;
	}

	@SuppressWarnings("unused")
	@InjectComponent
	@Property
	private Checkbox selectAllCheckbox;

	private CustomerRegister record;

	public CustomerRegister getRecord() {
		return record;
	}

	public void setRecord(CustomerRegister record) {
		this.record = record;
	}

	private List<String> restrictions;
	private List<String> orders;

	public void setRestrictions(List<String> restrictions) {
		this.restrictions = restrictions;
	}

	public void setOrders(List<String> orders) {
		this.orders = orders;
	}

	@Property
	@SuppressWarnings("unused")
	private EvenOdd evenOdd;

	@Inject
	private CustomerRegisterService customerRegisterService;

	@Persist
	private Map<String, Long> idMap;

	void setupRender() {
		list = customerRegisterService.getRegisterList(restrictions, orders);
		idMap = new HashMap<String, Long>();
		int i = 0;
		for (CustomerRegister cr : list) {
			idMap.put(String.valueOf(i), cr.getId());
			i++;
		}
		evenOdd = new EvenOdd();
	}

	@SuppressWarnings("unused")
	@Property
	private IdMapEncoder encoder;

	void onPrepare() {
		encoder = new IdMapEncoder();
	}

	public boolean getCurrentSelected() {
		if (selectedSet == null) {
			selectedSet = new HashSet<Long>();
		}
		return selectedSet.contains(record.getId());
	}

	public void setCurrentSelected(boolean value) {
		if (selectedSet == null) {
			selectedSet = new HashSet<Long>();
		}
		if (value) {
			selectedSet.add(record.getId());
		} else {
			selectedSet.remove(record.getId());
		}
	}

	private class IdMapEncoder implements ValueEncoder<CustomerRegister> {
		public String toClient(CustomerRegister value) {
			if (idMap == null)
				return "-1";
			for (String key : idMap.keySet()) {
				if (idMap.get(key).equals(value.getId())) {
					return key;
				}
			}
			return "-1";
		}

		public CustomerRegister toValue(String keyAsString) {
			try {
				CustomerRegister record = new CustomerRegister();
				record.setId(idMap.get(keyAsString));
				return record;
			} catch (EmptyResultDataAccessException e) {
				e.printStackTrace();
				return record;
			}
		}
	};
}
