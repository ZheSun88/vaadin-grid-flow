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

package com.vaadin.flow.component.grid.i.dataview;

import com.vaadin.flow.testutil.TestPath;
import org.junit.Assert;
import org.junit.Test;

@TestPath("row-count-estimate")
public class RowCountEstimateGridIT extends AbstractRowCountGridIT {

    @Test
    public void rowCountEstimateGrid_scrollingPastEstimate_keepsScrolling() {
        int initialEstimate = 300;
        open(initialEstimate);
        verifyRows(initialEstimate);

        grid.scrollToRow(initialEstimate);

        verifyRows(initialEstimate + 200);

        grid.scrollToRow(initialEstimate + 200);

        verifyRows(initialEstimate + 400);
    }

    @Test
    public void rowCountEstimateGrid_reachesEndBeforeEstimate_sizeChanges() {
        int initialEstimate = 500;
        open(initialEstimate);
        verifyRows(initialEstimate);

        int undefinedSizeBackendSize = 333;
        setUnknownCountBackendSize(undefinedSizeBackendSize);

        grid.scrollToRow(400);

        verifyRows(undefinedSizeBackendSize);
        Assert.assertEquals("Incorrect last row visible",
                undefinedSizeBackendSize - 1, grid.getLastVisibleRowIndex());

        // check that new estimate is not applied after size is known
        setEstimate(700);

        verifyRows(undefinedSizeBackendSize);
        Assert.assertEquals("Incorrect last row visible",
                undefinedSizeBackendSize - 1, grid.getLastVisibleRowIndex());
    }

    @Test
    public void rowCountEstimateGrid_switchesToDefinedSize_sizeChanges() {
        int initialEstimate = 500;
        open(initialEstimate);
        verifyRows(initialEstimate);

        setUnknownCountBackendSize(1000);
        verifyRows(initialEstimate);

        setCountCallback();
        verifyRows(1000);
    }

    @Test
    public void rowCountEstimateGrid_estimateChanged_newEstimateApplied() {
        int initialEstimate = 1000;
        open(initialEstimate);
        verifyRows(initialEstimate);

        setEstimate(2000);
        verifyRows(2000);

        grid.scrollToRow(1999);
        verifyRows(2000 + 200);

        setEstimate(3000);
        verifyRows(3000);
    }

    @Test
    public void rowCountEstimateGrid_estimateLessThanCurrentRange_estimateNotChanged() {
        int initialEstimate = 1000;
        open(initialEstimate);

        // "too little" estimate should not be applied
        grid.scrollToRow(1500);
        setEstimate(1501);

        verifyRows(1501);

        setEstimate(1600);
        verifyRows(1600);

        grid.scrollToRow(1100);
        setEstimate(1300);
        verifyRows(1300);
    }

}
