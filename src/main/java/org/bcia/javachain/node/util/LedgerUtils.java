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
package org.bcia.javachain.node.util;

import org.bcia.javachain.common.exception.LedgerException;
import org.bcia.javachain.common.exception.ValidateException;
import org.bcia.javachain.common.util.ValidateUtils;
import org.bcia.javachain.common.util.proto.BlockUtils;
import org.bcia.javachain.core.ledger.INodeLedger;
import org.bcia.javachain.protos.common.Common;
import org.bcia.javachain.protos.common.Ledger;

/**
 * 对象
 *
 * @author zhouhui
 * @date 2018/4/26
 * @company Dingxuan
 */
public class LedgerUtils {
    public static Common.Block getConfigBlockFromLedger(INodeLedger nodeLedger) throws LedgerException,
            ValidateException {
        Ledger.BlockchainInfo blockchainInfo = nodeLedger.getBlockchainInfo();
        ValidateUtils.isNotNull(blockchainInfo, "blockchainInfo can not be null");

        Common.Block lastBlock = nodeLedger.getBlockByNumber(blockchainInfo.getHeight() - 1);

        BlockUtils.getLastConfigIndexFromBlock(lastBlock);
        return null;


    }
}
