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
package org.bcia.javachain.core.ledger.kvledger.history.historydb;

import org.bcia.javachain.common.exception.LedgerException;
import org.bcia.javachain.common.ledger.util.DBProvider;
import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;

/**
 * HistoryDB操作类
 *
 * @author sunzongyu
 * @date 2018/04/04
 * @company Dingxuan
 */
public class HistoryLevelDBProvider implements IHistoryDBProvider {
    private static final JavaChainLog logger = JavaChainLogFactory.getLog(HistoryLevelDBProvider.class);

    private DBProvider provider = null;

    @Override
    public IHistoryDB getDBHandle(String dbName) throws LedgerException {
        return HistoryLevelDB.newHistroyDB(provider, dbName);
    }

    @Override
    public void close() throws LedgerException {
        provider.close();
    }

    public static JavaChainLog getLogger() {
        return logger;
    }

    public DBProvider getProvider() {
        return provider;
    }

    public void setProvider(DBProvider provider) {
        this.provider = provider;
    }
}
