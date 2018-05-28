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
package org.bcia.javachain.core.smartcontract;

import com.google.protobuf.ByteString;
import org.bcia.javachain.common.exception.SmartContractException;
import org.bcia.javachain.core.common.smartcontractprovider.ISmartContractProvider;
import org.bcia.javachain.core.common.smartcontractprovider.SmartContractContext;
import org.bcia.javachain.core.ledger.INodeLedger;
import org.bcia.javachain.core.ledger.LedgerContext;
import org.bcia.javachain.protos.node.ProposalPackage;
import org.bcia.javachain.protos.node.Smartcontract;

import java.util.List;

/**
 * SmartContractExecuteProvider is an implementation of the ISmartContractProvider interface
 *
 * @author sunianle
 * @date 5/11/18
 * @company Dingxuan
 */
public class SmartContractExecuteProvider implements ISmartContractProvider {
    @Override
    public LedgerContext getContext(INodeLedger ledger, String txID) throws SmartContractException {
        return null;
    }

    @Override
    public SmartContractContext getSCContext(String groupId, String name, String version, String txId, boolean isSysSC,
                                             ProposalPackage.SignedProposal signedProposal, ProposalPackage.Proposal
                                                     proposal) {
        return null;
    }

    @Override
    public SmartContractExecuteResult executeSmartContract(LedgerContext ctxt, SmartContractContext scContext,
                                                           byte[][] args) throws SmartContractException {
        return null;
    }

    @Override
    public SmartContractExecuteResult execute(LedgerContext ctxt, SmartContractContext scContext, Object spec) throws SmartContractException {
        return null;
    }

    @Override
    public SmartContractExecuteWithFilterResult executeWithErrorFilter(LedgerContext ctxt, SmartContractContext scContext, Object spec) throws SmartContractException {
        return null;
    }

    @Override
    public void stop(LedgerContext ctxt, SmartContractContext scContext, Smartcontract.SmartContractDeploymentSpec spec) throws SmartContractException {

    }
}
