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
package org.failearly.dataz.junit4;

import org.failearly.dataz.common.test.junit4.JUnit4TestUtilities;
import org.failearly.dataz.datastore.DataStores;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Result;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * DataSetDriverTestBase is responsible for ...
 */
public class DataSetDriverTestBase {
    @Rule
    public TestRule testRule=DoNotRunInnerTestRules.createOuterRule();

    static TestRule createInnerRule(TestRule testRule) {
        return DoNotRunInnerTestRules.createInnerRule(testRule);
    }

    protected static Result runTestMethod(Class<?> testClass, String testMethod) {
        final Result testResult = JUnit4TestUtilities.runTestMethod(testClass, testMethod);
        DataStores.dispose();
        return testResult;
    }

    protected static void assertDataResourcesOnDataStore(String... expectedDataResources) {
        assertThat(
            "Applied data resources on the data store",
            TestDataStore.getDataResourceList(),
            is(Arrays.asList(expectedDataResources))
        );
    }

    @Before
    public void setUp() throws Exception {
        TestDataStore.reset();
        DataStores.dispose();
    }
}
