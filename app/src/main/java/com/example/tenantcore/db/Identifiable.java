/*
 * Copyright (c) 2020 Ian Clement. All rights reserved.
 */

package com.example.tenantcore.sqlite;

/**
 * Indicated model classes that have an ID field.
 *
 * @param <I>
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public interface Identifiable<I> {
  I getId();

  Identifiable<I> setId(I id);
}
