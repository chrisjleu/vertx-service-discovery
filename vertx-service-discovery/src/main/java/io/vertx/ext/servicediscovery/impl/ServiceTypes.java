/*
 * Copyright (c) 2011-2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.servicediscovery.impl;

import io.vertx.ext.servicediscovery.Record;
import io.vertx.ext.servicediscovery.spi.ServiceType;

import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Classes responsible for finding the service type implementations on the classpath.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceTypes {

  public static ServiceType get(Record record) {
    load();

    String type = record.getType();
    Objects.requireNonNull(type);

    ServiceType found = get(type);
    if (found != null) {
      return found;
    } else {
      throw new IllegalArgumentException("Unsupported service type " + type);
    }
  }

  private static void load() {
    synchronized (ServiceTypes.class) {
      if (types == null || !types.iterator().hasNext()) {
        types = ServiceLoader.load(ServiceType.class);
      }
    }
  }

  public static Iterator<ServiceType> all() {
    load();
    return types.iterator();
  }

  private static ServiceLoader<ServiceType> types;

  public static ServiceType get(String type) {
    load();
    for (ServiceType next : types) {
      if (next.name().equalsIgnoreCase(type)) {
        return next;
      }
    }
    return null;
  }
}