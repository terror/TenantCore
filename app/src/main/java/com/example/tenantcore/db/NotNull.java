/*
 * Copyright (c) 2020 Ian Clement. All rights reserved.
 */

package com.example.tenantcore.sqlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate non-nullable fields in the resulting sqlite database.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NotNull {
}
