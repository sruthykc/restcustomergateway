package com.diviso.graeshoppe.client.order.model;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Notification.class)
public abstract class Notification_ {

	public static volatile SingularAttribute<Notification, Instant> date;
	public static volatile SingularAttribute<Notification, byte[]> image;
	public static volatile SingularAttribute<Notification, String> receiverId;
	public static volatile SingularAttribute<Notification, String> targetId;
	public static volatile SingularAttribute<Notification, String> imageContentType;
	public static volatile SingularAttribute<Notification, Long> id;
	public static volatile SingularAttribute<Notification, String> title;
	public static volatile SingularAttribute<Notification, String> message;
	public static volatile SingularAttribute<Notification, String> type;
	public static volatile SingularAttribute<Notification, String> status;

}

