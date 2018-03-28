/**
 * Copyright Dingxuan. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bcia.javachain.core.ledger;

import org.bcia.javachain.protos.common.Common;

import java.util.Map;

/**
 * 类描述
 *
 * @author wanliangbing
 * @date 2018/3/27
 * @company Dingxuan
 */
public class BlockAndPvtData {

    private Common.Block block;
    private Map<Integer, TxPvtData> blockPvtData;
    private MissingPrivateData missing;

    public Common.Block getBlock() {
        return block;
    }

    public void setBlock(Common.Block block) {
        this.block = block;
    }

    public Map<Integer, TxPvtData> getBlockPvtData() {
        return blockPvtData;
    }

    public void setBlockPvtData(Map<Integer, TxPvtData> blockPvtData) {
        this.blockPvtData = blockPvtData;
    }

    public MissingPrivateData getMissing() {
        return missing;
    }

    public void setMissing(MissingPrivateData missing) {
        this.missing = missing;
    }
}