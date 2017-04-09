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
import org.failearly.dataz.DataSet;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.NoDataSet;
import org.failearly.dataz.SuppressCleanup;
import org.failearly.dataz.test.datastore.AdhocDataStore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Result;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * DataSetDriverClassAndMethodTest contains tests for DataSetDriver
 */
@Subject(DataSetDriver.class)
public class DataSetDriverClassAndMethodTest extends DataSetDriverTestBase {

    @Test
    public void what_should_happen_in_case_of_not_failing_test() throws Exception {
        final Result testResult = runTestMethod(UseDataSetOnClassAndMethod.class, "notFailingTest");

        assertTrue("Test should be successful", testResult.wasSuccessful());
        assertDataResourcesOnDataStore(
            "/datastore.setup",
            "/class.setup",
            "/method.setup",
            "/method.cleanup",
            "/class.cleanup",
            "/datastore.cleanup"
        );
    }

    @Test
    public void what_should_happen_in_case_of_failing_test() throws Exception {
        final Result testResult = runTestMethod(UseDataSetOnClassAndMethod.class, "failingTest");

        assertFalse("Test should fail", testResult.wasSuccessful());
        assertDataResourcesOnDataStore(
            "/datastore.setup",
            "/class.setup",
            "/method.setup",
            "/method.cleanup",
            "/class.cleanup",
            "/datastore.cleanup"
        );
    }

    @Test
    public void what_should_happen_in_case_of_dataset_on_class_only() throws Exception {
        final Result testResult = runTestMethod(UseDataSetOnClassAndMethod.class, "classOnly");

        assertTrue("Test should be successful", testResult.wasSuccessful());
        assertDataResourcesOnDataStore(
            "/datastore.setup",
            "/class.setup",
            "/class.cleanup",
            "/datastore.cleanup"
        );
    }

    @Test
    public void what_should_happen_in_case_of_SuppressCleanup() throws Exception {
        final Result testResult = runTestMethod(UseDataSetOnClassAndMethod.class, "suppressCleanup");

        assertTrue("Test should be successful", testResult.wasSuccessful());
        assertDataResourcesOnDataStore(
            "/datastore.setup",
            "/class.setup",
            "/method.setup"
        );
    }

    @Test
    public void what_should_happen_in_case_of_NoDataSet() throws Exception {
        final Result testResult = runTestMethod(UseDataSetOnClassAndMethod.class, "noDataSet");

        assertTrue("Test should be successful", testResult.wasSuccessful());
        assertThat("No usage of any DataStore instance.", TestDataStore.hasBeenApplied(), is(false));
    }

    @DataSet(datastores = TheDataStore.class, setup = "/class.setup", cleanup = "/class.cleanup")
    public static class UseDataSetOnClassAndMethod {
        @Rule
        public final TestRule dataSetDriver = createInnerRule(DataSetDriver.createDataSetDriver(this));

        @Test
        @DataSet(datastores = TheDataStore.class, setup = "/method.setup", cleanup = "/method.cleanup")
        public void notFailingTest() {
        }

        @Test
        @DataSet(datastores = TheDataStore.class, setup = "/method.setup", cleanup = "/method.cleanup")
        public void failingTest() {
            fail("Always fail");
        }

        @Test
        public void classOnly() {
        }

        @Test
        @DataSet(datastores = TheDataStore.class, setup = "/method.setup", cleanup = "/method.cleanup")
        @SuppressCleanup
        public void suppressCleanup() {
        }

        @Test
        @NoDataSet
        public void noDataSet() {
        }
    }

    @AdhocDataStore(name = "1st", implementation = TestDataStore.class)
    @DataSet(setup = "/datastore.setup", cleanup = "/datastore.cleanup")
    private static class TheDataStore extends NamedDataStore {
    }
}
