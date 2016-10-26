/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
        public final TestRule dataSetDriver = DataSetDriver.createDataSetDriver(this);


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
