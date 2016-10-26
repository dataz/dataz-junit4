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

import org.failearly.common.test.junit4.JUnitTestUtilities;
import org.failearly.dataz.datastore.DataStores;
import org.junit.Before;
import org.junit.runner.Result;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * DataSetDriverTestBase is responsible for ...
 */
public class DataSetDriverTestBase {
    protected static Result runTestMethod(Class<?> testClass, String testMethod) {
        final Result testResult = JUnitTestUtilities.runTestMethod(testClass, testMethod);
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
