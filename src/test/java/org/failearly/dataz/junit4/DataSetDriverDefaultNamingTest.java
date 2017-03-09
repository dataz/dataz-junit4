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
