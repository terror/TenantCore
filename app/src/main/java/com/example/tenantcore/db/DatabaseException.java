/*
 * Copyright (c) 2020 Ian Clement. All rights reserved.
 */

package com.example.tenantcore.db;

/**
 * Represents a database error.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class DatabaseException extends Exception {
  public DatabaseException(String s) {
    super(s);
  }

  public DatabaseException(Exception e) {
    super(e);
  }
}
