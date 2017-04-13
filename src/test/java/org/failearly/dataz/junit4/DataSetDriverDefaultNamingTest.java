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

package org.failearly.dataz.junit4;

import org.failearly.dataz.internal.common.test.annotations.Subject;
import org.failearly.dataz.DataCleanup;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.DataSetup;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.test.datastore.AdhocDataStore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Result;

import static org.junit.Assert.assertTrue;

/**
 * DataSetDriverDefaultNamingTest tests the default naming straetgy of DataZ, if the resources are not set.
 */
@Subject(DataSetDriver.class)
public class DataSetDriverDefaultNamingTest extends DataSetDriverTestBase {

    @Test
    public void what_should_happen_in_case_of_DataSet() throws Exception {
        final Result testResult = runTestMethod(AnyTestClass.class, "useDataSet");

        assertTrue("Test should be successful", testResult.wasSuccessful());
        assertDataResourcesOnDataStore(
            "/org/failearly/dataz/junit4/TheDataStore.setup",
            "/org/failearly/dataz/junit4/AnyTestClass.setup",
            "/org/failearly/dataz/junit4/AnyTestClass-useDataSet.setup",
            "/org/failearly/dataz/junit4/AnyTestClass-useDataSet.cleanup",
            "/org/failearly/dataz/junit4/AnyTestClass.cleanup",
            "/org/failearly/dataz/junit4/TheDataStore.cleanup"
        );
    }

    @Test
    public void what_should_happen_in_case_of_DataSetup() throws Exception {
        final Result testResult = runTestMethod(AnyTestClass.class, "useDataSetup");

        assertTrue("Test should be successful", testResult.wasSuccessful());
        assertDataResourcesOnDataStore(
            "/org/failearly/dataz/junit4/TheDataStore.setup",
            "/org/failearly/dataz/junit4/AnyTestClass.setup",
            "/org/failearly/dataz/junit4/AnyTestClass-useDataSetup.setup",
            "/org/failearly/dataz/junit4/AnyTestClass.cleanup",
            "/org/failearly/dataz/junit4/TheDataStore.cleanup"
        );
    }

    @Test
    public void what_should_happen_in_case_of_DataCleanup() throws Exception {
        final Result testResult = runTestMethod(AnyTestClass.class, "useDataCleanup");

        assertTrue("Test should be successful", testResult.wasSuccessful());
        assertDataResourcesOnDataStore(
            "/org/failearly/dataz/junit4/TheDataStore.setup",
            "/org/failearly/dataz/junit4/AnyTestClass.setup",
            "/org/failearly/dataz/junit4/AnyTestClass-useDataCleanup.cleanup",
            "/org/failearly/dataz/junit4/AnyTestClass.cleanup",
            "/org/failearly/dataz/junit4/TheDataStore.cleanup"
        );
    }

    @DataSet(datastores = TheDataStore.class)
    public static class AnyTestClass {
        @Rule
        public final TestRule dataSetDriver = createInnerRule(DataSetDriver.createDataSetDriver(this));

        @Test
        @DataSet(datastores = TheDataStore.class)
        public void useDataSet() {
        }

        @Test
        @DataSetup(datastores = TheDataStore.class)
        public void useDataSetup() {
        }

        @Test
        @DataCleanup(datastores = TheDataStore.class)
        public void useDataCleanup() {
        }
    }

    @AdhocDataStore(name = "1st", implementation = TestDataStore.class)
    @DataSet()
    private static class TheDataStore extends NamedDataStore {
    }
}
