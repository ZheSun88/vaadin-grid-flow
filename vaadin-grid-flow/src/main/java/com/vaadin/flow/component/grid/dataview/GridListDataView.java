/*
 * Copyright 2000-2020 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.grid.dataview;

import java.util.stream.Stream;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.AbstractListDataView;
import com.vaadin.flow.data.provider.DataCommunicator;
import com.vaadin.flow.data.provider.IdentifierProvider;

/**
 * GridListDataView for in-memory list data. Provides information on the data
 * and allows operations on it.
 *
 * @param <T>
 *            data type
 * @since
 */
public class GridListDataView<T> extends AbstractListDataView<T>
        implements GridDataView<T> {
    private DataCommunicator<T> dataCommunicator;
    private Grid<T> grid;

    public GridListDataView(DataCommunicator<T> dataCommunicator,
            Grid<T> grid) {
        super(dataCommunicator::getDataProvider, grid);
        this.dataCommunicator = dataCommunicator;
        this.grid = grid;
    }

    @Override
    public T getItemOnRow(int rowIndex) {
        validateItemIndex(rowIndex);
        return getItems().skip(rowIndex).findFirst().orElse(null);
    }

    @Override
    public Stream<T> getItems() {
        return getDataProvider()
                .fetch(dataCommunicator.buildQuery(0, Integer.MAX_VALUE));
    }

    /**
     * Gets the size of the data source with filters applied if any are set.
     * <p>
     * <em>NOTE:</em> calling this method might trigger a count call to the
     * backend when lazy data source is used.
     * 
     * @return the filtered data size
     * @see #addSizeChangeListener(ComponentEventListener)
     */
    @Override
    public int getSize() {
        return dataCommunicator.getDataSize();
    }

    @Override
    public void setIdentifierProvider(
            IdentifierProvider<T> identifierProvider) {
        super.setIdentifierProvider(identifierProvider);
        dataCommunicator.getKeyMapper().setIdentifierGetter(identifierProvider);
    }
}
