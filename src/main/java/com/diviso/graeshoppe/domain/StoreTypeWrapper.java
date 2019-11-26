package com.diviso.graeshoppe.domain;

import java.util.List;

public class StoreTypeWrapper {
	private List<String> storeTypeNames;

	public List<String> getStoreTypeNames() {
		return storeTypeNames;
	}

	public void setStoreTypeNames(List<String> storeTypeNames) {
		this.storeTypeNames = storeTypeNames;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((storeTypeNames == null) ? 0 : storeTypeNames.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoreTypeWrapper other = (StoreTypeWrapper) obj;
		if (storeTypeNames == null) {
			if (other.storeTypeNames != null)
				return false;
		} else if (!storeTypeNames.equals(other.storeTypeNames))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StoreTypeWrapper [storeTypeNames=" + storeTypeNames + "]";
	}
}
