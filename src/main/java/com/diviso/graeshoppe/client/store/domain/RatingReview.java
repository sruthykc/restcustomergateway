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
package com.diviso.graeshoppe.client.store.domain;

import com.diviso.graeshoppe.client.store.model.ReviewDTO;
import com.diviso.graeshoppe.client.store.model.UserRatingDTO;

/**
 * TODO Provide a detailed description here 
 * @author MayaSanjeev
 * mayabytatech, maya.k.k@lxisoft.com
 */
public class RatingReview {

	UserRatingDTO rating;
	ReviewDTO review;
	
	public UserRatingDTO getRating() {
		return rating;
	}
	public void setRating(UserRatingDTO rating) {
		this.rating = rating;
	}
	public ReviewDTO getReview() {
		return review;
	}
	public void setReview(ReviewDTO review) {
		this.review = review;
	}
	@Override
	public String toString() {
		return "RatingReview [rating=" + rating + ", review=" + review + ", getRating()=" + getRating()
				+ ", getReview()=" + getReview() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
	
}
