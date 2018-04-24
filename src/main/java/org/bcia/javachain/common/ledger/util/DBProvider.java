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
package org.bcia.javachain.common.ledger.util;

import org.bcia.javachain.common.exception.LedgerException;
import org.bcia.javachain.common.ledger.util.leveldbhelper.UpdateBatch;
import org.iq80.leveldb.DB;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 操作数据库辅助类接口
 *
 * @author sunzongyu
 * @date 2018/04/24
 * @company Dingxuan
 */
public interface DBProvider {
    /**
     * 关闭
     */
    void close() throws LedgerException;

    /**
     * 根据key获取value
     */
    byte[] get(byte[] key) throws LedgerException;

    /**
     * 插入kv
     */
    void put(byte[] key, byte[] value, boolean sync) throws LedgerException;

    /**
     * 删除key
     */
    void delete(byte[] key, boolean sync) throws LedgerException;

    /**
     * 批量写入
     */
    void writeBatch(UpdateBatch batch, boolean sync) throws LedgerException;

    /**
     * 迭代
     */
    Iterator<Map.Entry<byte[], byte[]>> getIterator(byte[] startKey, byte[] endKey) throws LedgerException;

    /**
     * 获取DB路径
     */
    String getDbPath() throws LedgerException;

    /**
     * 获取DB
     */
    DBHandle getDb();
}
