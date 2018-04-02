/**
 * Copyright DingXuan. All Rights Reserved.
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
package org.bcia.javachain.csp.gm.sm2;

import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;
import org.bouncycastle.math.ec.ECPoint;

/**
 * @author zhangmingyang
 * @Date: 2018/3/27
 * @company Dingxuan
 */
public class SM2PublicKey extends SM2Key {
    private static JavaChainLog log = JavaChainLogFactory.getLog(SM2PublicKey.class);
    private  ECPoint ecPoint;
    public SM2PublicKey(ECPoint ecPoint) {
        this.ecPoint=ecPoint;
    }

    @Override
    public byte[] toBytes() {
        log.info("返回公钥");
        return  ecPoint.getEncoded(false);
    }

    @Override
    public byte[] ski() {
        return new byte[0];
    }

    @Override
    public boolean isSymmetric() {
        return false;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }
}
