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

import org.failearly.common.test.annotations.Subject;
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
