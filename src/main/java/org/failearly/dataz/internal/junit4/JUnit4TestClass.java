/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.internal.junit4;

import org.failearly.dataz.internal.model.AtomicTest;
import org.failearly.dataz.internal.model.TestClassBase;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * JUnit4TestClass represents a single test class. It is responsible for collecting {@link AtomicTest}s by name.
 */
public final class JUnit4TestClass extends TestClassBase {
    private static final JUnit4TestClass PROTOTYPE = new JUnit4TestClass(null);

    private JUnit4TestClass(Class<?> testClass) {
        super(testClass);
    }

    public static TestClassBase prototype() {
        return PROTOTYPE;
    }

    @Override
    protected boolean isTestMethod(Method method) {
        return method.isAnnotationPresent(Test.class) && ! method.isAnnotationPresent(Ignore.class);
    }

    @Override
    protected TestClassBase createInstanceFrom(Class<?> testClass) {
        return new JUnit4TestClass(testClass);
    }
}
