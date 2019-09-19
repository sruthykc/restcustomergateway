 /*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diviso.graeshoppe.client.store.model;

import java.util.List;

/**
 * TODO Provide a detailed description here 
 * @author MayaSanjeev
 * mayabytatech, maya.k.k@lxisoft.com
 */
public class StoreBundleDTO {

	
	StoreDTO store;
	List<DeliveryInfoDTO> deliveryInfos;
    List<TypeDTO> types;
	public StoreDTO getStore() {
		return store;
	}
	public void setStore(StoreDTO store) {
		this.store = store;
	}
	public List<DeliveryInfoDTO> getDeliveryInfos() {
		return deliveryInfos;
	}
	public void setDeliveryInfos(List<DeliveryInfoDTO> deliveryInfos) {
		this.deliveryInfos = deliveryInfos;
	}
	public List<TypeDTO> getTypes() {
		return types;
	}
	public void setTypes(List<TypeDTO> types) {
		this.types = types;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deliveryInfos == null) ? 0 : deliveryInfos.hashCode());
		result = prime * result + ((store == null) ? 0 : store.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
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
		StoreBundleDTO other = (StoreBundleDTO) obj;
		if (deliveryInfos == null) {
			if (other.deliveryInfos != null)
				return false;
		} else if (!deliveryInfos.equals(other.deliveryInfos))
			return false;
		if (store == null) {
			if (other.store != null)
				return false;
		} else if (!store.equals(other.store))
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		return true;
	}
    
	
	
}
