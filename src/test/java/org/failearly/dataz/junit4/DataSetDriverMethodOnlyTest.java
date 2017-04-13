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

import org.failearly.dataz.DataSet;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.NoDataSet;
import org.failearly.dataz.test.datastore.AdhocDataStore;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Result;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * DataSetDriverClassAndMethodTest contains tests for DataSetDriver
 */
public class DataSetDriverMethodOnlyTest extends DataSetDriverTestBase {

    @Test
    public void what_should_happen_in_case_of_a_not_failing_test() throws Exception {
        final Result testResult = runTestMethod(DataSetOnMethodsOnly.class, "notFailingTest");

        assertTrue("Test should be successful", testResult.wasSuccessful());
        assertDataResourcesOnDataStore(
            "/datastore.setup",
            "/method.setup",
            "/method.cleanup",
            "/datastore.cleanup"
        );
    }

    @Test
    public void what_should_happen_in_case_of_a_failing_test() throws Exception {
        final Result testResult = runTestMethod(DataSetOnMethodsOnly.class, "failingTest");

        assertFalse("Test should fail", testResult.wasSuccessful());
        assertDataResourcesOnDataStore(
            "/datastore.setup",
            "/method.setup",
            "/method.cleanup",
            "/datastore.cleanup"
        );
    }

    @Test
    public void what_should_happen_in_case_of_an_ignored_test() throws Exception {
        final Result testResult = runTestMethod(DataSetOnMethodsOnly.class, "ignoredTest");

        assertThat("Test should be ignored", testResult.getIgnoreCount(), is(1));
        assertThat("No usage of any DataStore instance.", TestDataStore.hasBeenApplied(), is(false));
    }


    @Test
    public void what_should_happen_in_case_of_NoDataSet() throws Exception {
        final Result testResult = runTestMethod(DataSetOnMethodsOnly.class, "noDataSet");

        assertTrue("Test should be successful", testResult.wasSuccessful());
        assertThat("No usage of any DataStore instance.", TestDataStore.hasBeenApplied(), is(false));
    }

    public static class DataSetOnMethodsOnly {
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
        @Ignore
        @DataSet(datastores = TheDataStore.class, setup = "/method.setup", cleanup = "/method.cleanup")
        public void ignoredTest() {
        }

        @Test
        @NoDataSet
        @DataSet(datastores = TheDataStore.class, setup = "/method.setup", cleanup = "/method.cleanup")
        public void noDataSet() {
        }
    }

    @AdhocDataStore(name = "1st", implementation = TestDataStore.class)
    @DataSet(setup = "/datastore.setup", cleanup = "/datastore.cleanup")
    private static class TheDataStore extends NamedDataStore {
    }
}
