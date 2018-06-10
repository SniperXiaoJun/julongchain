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
package org.bcia.julongchain.core.common.validation;

import org.bcia.julongchain.msp.IIdentity;
import org.bcia.julongchain.msp.IIdentityDeserializer;
import org.bcia.julongchain.protos.msp.Identities;

/**
 * 类描述
 *
 * @author
 * @date 2018/3/14
 * @company Dingxuan
 */
public class MockIdentityDeserializer implements IIdentityDeserializer {
    @Override
    public IIdentity deserializeIdentity(byte[] serializedIdentity) {
        return null;
    }

    @Override
    public void isWellFormed(Identities.SerializedIdentity identity) {

    }
}