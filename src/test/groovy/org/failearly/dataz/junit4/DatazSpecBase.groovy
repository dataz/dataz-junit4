/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.failearly.dataz.junit4

import org.failearly.dataz.common.test.junit4.JUnitTestUtilities
import org.failearly.dataz.datastore.DataStores
import org.junit.runner.Result
import spock.lang.Specification
/**
 * DatazSpecBase is  the base class for dataZ specifications.
 */
abstract class DatazSpecBase extends Specification {
    void setup() throws Exception {
        TestDataStore.reset();
        DataStores.dispose();
    }

    protected static Result runTestMethod(Class<?> testClass, String testMethod) {
        final Result testResult = JUnitTestUtilities.runTestMethod(testClass, testMethod);
        DataStores.dispose();
        return testResult;
    }
}