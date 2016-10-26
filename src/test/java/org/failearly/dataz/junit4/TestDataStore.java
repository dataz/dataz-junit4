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
