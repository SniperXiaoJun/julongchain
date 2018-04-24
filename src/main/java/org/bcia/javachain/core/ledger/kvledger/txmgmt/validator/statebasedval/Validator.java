/*
 * Copyright Dingxuan. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package org.bcia.javachain.core.ledger.kvledger.txmgmt.validator.statebasedval;


import org.bcia.javachain.common.exception.LedgerException;
import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.privacyenabledstate.DB;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.privacyenabledstate.HashedCompositeKey;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.privacyenabledstate.HashedUpdateBatch;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.privacyenabledstate.PubUpdateBatch;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.rwsetutil.CollHashedRwSet;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.rwsetutil.NsRwSet;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.rwsetutil.RwSetUtil;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.rwsetutil.TxRwSet;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.statedb.CompositeKey;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.validator.valinternal.Block;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.validator.valinternal.InternalValidator;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.validator.valinternal.PubAndHashUpdates;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.validator.valinternal.Transaction;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.version.Height;
import org.bcia.javachain.protos.ledger.rwset.kvrwset.KvRwset;
import org.bcia.javachain.protos.node.TransactionPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述
 *
 * @author sunzongyu
 * @date 2018/04/19
 * @company Dingxuan
 */
public class Validator implements InternalValidator {
    private static final JavaChainLog logger = JavaChainLogFactory.getLog(Validator.class);

    private DB db;

    public static Validator newValidator(DB db){
        Validator validator = new Validator();
        validator.setDb(db);
        return validator;
    }

    public void preLoadCommittedVersionOfRSet(Block block){
        List<CompositeKey> pubKeys = new ArrayList<>();
        List<HashedCompositeKey> hashedKeys = new ArrayList<>();
        Map<CompositeKey, Object> pubKeysMap = new HashMap<>();
        Map<HashedCompositeKey, Object> hashedKeyMap = new HashMap<>();
        for(Transaction tx : block.getTxs()){
            for(NsRwSet nsRwSet : tx.getRwSet().getNsRwSets()){
                for(KvRwset.KVRead kvRead : nsRwSet.getKvRwSet().getReadsList()){
                    CompositeKey compositeKey = new CompositeKey();
                    compositeKey.setNamespace(nsRwSet.getNameSpace());
                    compositeKey.setKey(kvRead.getKey());
                    if(!pubKeysMap.containsKey(compositeKey)){
                        pubKeysMap.put(compositeKey, null);
                        pubKeys.add(compositeKey);
                    }
                }
                for(CollHashedRwSet col : nsRwSet.getCollHashedRwSets()){
                    for(KvRwset.KVWriteHash kvHashedRead : col.getHashedRwSet().getHashedWritesList()){
                        HashedCompositeKey hashedCompositeKey = new HashedCompositeKey();
                        hashedCompositeKey.setNamespace(nsRwSet.getNameSpace());
                        hashedCompositeKey.setCollectionName(col.getCollectionName());
                        hashedCompositeKey.setKeyHash(new String(kvHashedRead.getKeyHash().toByteArray()));
                        if(!hashedKeyMap.containsKey(hashedCompositeKey)){
                            hashedKeyMap.put(hashedCompositeKey, null);
                            hashedKeys.add(hashedCompositeKey);
                        }
                    }
                }
            }
        }

        if(pubKeys.size() > 0 || hashedKeys.size() > 0){
            db.loadCommittedVersionsOfPubAndHashedKeys(pubKeys, hashedKeys);
        }
    }

    @Override
    public PubAndHashUpdates validateAndPrepareBatch(Block block, boolean doMVCCValidation) throws LedgerException {
        preLoadCommittedVersionOfRSet(block);
        PubAndHashUpdates updates = PubAndHashUpdates.newPubAndHashUpdates();
        for(Transaction tx : block.getTxs()){
            TransactionPackage.TxValidationCode validationCode = validateEndorserTX(tx.getRwSet(), doMVCCValidation, updates);

        }
        return null;
    }

    private TransactionPackage.TxValidationCode validateEndorserTX(TxRwSet txRwSet, boolean doMVCCValidation, PubAndHashUpdates updates) throws LedgerException{
        TransactionPackage.TxValidationCode txValidationCode = TransactionPackage.TxValidationCode.VALID;
        if(doMVCCValidation){
            txValidationCode = validateTx(txRwSet, updates);
        }
        return txValidationCode;
    }

    private TransactionPackage.TxValidationCode validateTx(TxRwSet txRwSet, PubAndHashUpdates updates) throws LedgerException {
        for(NsRwSet nsRwSet : txRwSet.getNsRwSets()){
            String ns = nsRwSet.getNameSpace();
            if(!validateReadSet(ns, nsRwSet.getKvRwSet().getReadsList(), updates.getPubUpdates())){
                return TransactionPackage.TxValidationCode.MVCC_READ_CONFLICT;
            }
            if(!validateRangeQueries(ns, nsRwSet.getKvRwSet().getRangeQueriesInfoList(), updates.getPubUpdates())){
                return TransactionPackage.TxValidationCode.MVCC_READ_CONFLICT;
            }
            if(!validateNsHashedReadSets(ns, nsRwSet.getCollHashedRwSets(), updates.getHashedUpdates())){
                return TransactionPackage.TxValidationCode.MVCC_READ_CONFLICT;
            }
        }
        return TransactionPackage.TxValidationCode.VALID;
    }

    private boolean validateReadSet(String ns, List<KvRwset.KVRead> kvReads, PubUpdateBatch updates) throws LedgerException {
        for(KvRwset.KVRead kvRead : kvReads){
            if(!validateKVRead(ns, kvRead, updates)){
                return false;
            }
        }
        return true;
    }

    private boolean validateKVRead(String ns, KvRwset.KVRead kvRead, PubUpdateBatch updates) throws LedgerException {
        if(updates.getBatch().exists(ns, kvRead.getKey())){
            return false;
        }
        Height committedVersion = db.getVersion(ns, kvRead.getKey());
        logger.debug("Comparing versions for keys " + kvRead.getKey());
        if(Height.areSame(committedVersion, RwSetUtil.newVersion(kvRead.getVersion()))){
            logger.debug(String.format("Version mismatch for key [%s:%s]", ns, kvRead.getKey()));
            return false;
        }
        return true;
    }

    private boolean validateRangeQueries(String ns, List<KvRwset.RangeQueryInfo> rangeQueryInfo, PubUpdateBatch updates) throws LedgerException {
        for(KvRwset.RangeQueryInfo rqi : rangeQueryInfo){
            if(!validateRangeQuery(ns, rqi, updates)){
                return false;
            }
        }
        return true;
    }

    private boolean validateRangeQuery(String ns, KvRwset.RangeQueryInfo rqi, PubUpdateBatch updates) throws LedgerException {
        logger.debug(String.format("validateRangeQueryL ns = %s, rangQueryInfo = %s", ns, rqi));
        boolean includeEndKey = !rqi.getItrExhausted();

        CombinedIterator combinedItr = CombinedIterator.newCombinedIterator(db, updates.getBatch(), ns, rqi.getStartKey(), rqi.getEndKey(), includeEndKey);
        IRangeQueryValidator validator;
        if(null != rqi.getReadsMerkleHashes()){
            logger.debug("Hashing results are present in the range query info hence, initiating hashing based validation");
            validator = new IRangeQueryHashValidator();
        } else {
            logger.debug("Hashing results are not present in the range query info hence, initiating hashing based validation");
            validator = new IRangeQueryResultsValidator();
        }
        validator.init(rqi, combinedItr);
        combinedItr.close();
        return validator.validate();
    }

    private boolean validateNsHashedReadSets(String ns, List<CollHashedRwSet> collHashedRwSets, HashedUpdateBatch updates) throws LedgerException {
        for(CollHashedRwSet col : collHashedRwSets){
            if(!validateCollHashedReadSet(ns, col.getCollectionName(), col.getHashedRwSet().getHashedReadsList(), updates)){
                return false;
            }
        }
        return true;
    }

    public boolean validateCollHashedReadSet(String ns, String collectionName, List<KvRwset.KVReadHash> kvReadHashes, HashedUpdateBatch updates) throws LedgerException {
        for(KvRwset.KVReadHash kvReadHash : kvReadHashes){
            if(!validateKVReadHash(ns, collectionName, kvReadHash, updates)){
                return false;
            }
        }
        return true;
    }

    public boolean validateKVReadHash(String ns, String collectionName, KvRwset.KVReadHash kvReadHash, HashedUpdateBatch updates) throws LedgerException{
        if(updates.contains(ns, collectionName, kvReadHash.toByteArray())){
            return false;
        }
        Height committedVersion = db.getKeyHashVersion(ns, collectionName, kvReadHash.getKeyHash().toByteArray());
        if(!Height.areSame(committedVersion, RwSetUtil.newVersion(kvReadHash.getVersion()))){
            logger.debug(String.format("Version mismatch for key[%s:%s]", ns, collectionName));
            return false;
        }
        return true;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }
}