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

import org.failearly.dataz.DataSet
import org.failearly.dataz.DataSetup
import org.failearly.dataz.NamedDataStore
import org.failearly.dataz.test.datastore.AdhocDataStore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import spock.lang.Subject
import spock.lang.Unroll

/**
 * DatazResourceNamingSpec shows the resolving strategy for the resources (and the names).
 */
@Subject(DataSetDriver)
class DatazResourceNamingSpec extends DatazSpecBase {
    @Unroll
    def "How to handle if the resources are not (fully) declared? - #testMethod in #testClass.simpleName"() {
        when: "Executing a dataZ test"
        runTestMethod(testClass, testMethod)

        then: "the applied data resources has expected names"
        appliedResources() == expectedDataResourcesInPathOf(testClass, expectedResources)

        and: "every resource is within package of #testClass.simpleName"
        appliedResources().every { it.startsWith(resolveResourcePath(testClass)) }

        where:
        testClass    | testMethod        || expectedResources
        UsingDataSet | "onlyOnTestClass" || ['UsingDataSet.setup', 'UsingDataSet.cleanup']
        // UsingDataSet | "noPath"          || ['UsingDataSet.setup', 'any-resource.setup', 'any-resource.cleanup', 'UsingDataSet.cleanup']
        UsingDataSet | "noResources"     || ['UsingDataSet.setup', 'UsingDataSet-noResources.setup', 'UsingDataSet-noResources.cleanup', 'UsingDataSet.cleanup']
    }

    private static List<String> appliedResources() {
        TestDataStore.getDataResourceList()
    }

    private static List<String> expectedDataResourcesInPathOf(Class testClass, resources) {
        final resourcePath = resolveResourcePath(testClass)
        resources.collect { resourcePath + it }
    }

    private static String resolveResourcePath(Class testClass) {
        "/" + testClass.getPackage().getName().replace(".", "/") + "/"
    }

    @DataSet(datastores = TheDataStore.class)
    public static class UsingDataSet {
        @Rule
        public final TestRule dataSetDriver = DataSetDriver.createDataSetDriver(this);

        @Test
        public void onlyOnTestClass() {
        }

        @Test
        @DataSet(datastores = TheDataStore.class)
        public void noResources() {
        }

        @Test
        @DataSet(datastores = TheDataStore.class, setup = "any-resource.setup", cleanup = "any-resource.cleanup")
        public void noPath() {
        }
    }

    @DataSetup(datastores = TheDataStore.class)
    public static class UsingDataSetup {
        @Rule
        public final TestRule dataSetDriver = DataSetDriver.createDataSetDriver(this);

        @Test
        public void onlyOnTestClass() {
        }

        @Test
        @DataSetup(datastores = TheDataStore.class)
        public void noResources() {
        }

        @Test
        @DataSetup(datastores = TheDataStore.class, value = "any-resource.setup")
        public void noPath() {
        }
    }

    @AdhocDataStore(name = "1st", implementation = TestDataStore.class)
    private static class TheDataStore extends NamedDataStore {
    }
}