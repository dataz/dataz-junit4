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

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreBase;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.resource.DataResource;
import org.failearly.dataz.test.datastore.AdhocDataStore;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TestDataStore is responsible for ...
 */
final class TestDataStore extends DataStoreBase {
    private static TestDataStore lastTestDataStore = null;
    private final List<DataResource> dataResourceList = new LinkedList<>();

    @SuppressWarnings("unused")
    static DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
        return new TestDataStore(namedDataStore, annotation);
    }

    private TestDataStore(Class<? extends NamedDataStore> namedDataStore, AdhocDataStore annotation) {
        super(namedDataStore, annotation);
    }

    @Override
    protected void doApplyResource(DataResource dataResource) throws DataStoreException {
        lastTestDataStore = this;
        dataResourceList.add(dataResource);
    }

    static List<String> getDataResourceList() {
        return lastTestDataStore.dataResourceList.stream().map(DataResource::getResource).collect(Collectors.toList());
    }

    static boolean hasBeenApplied() {
        return lastTestDataStore != null;
    }

    static void reset() {
        lastTestDataStore = null;
    }
}
