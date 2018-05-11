/**
 * Copyright Aisino. All Rights Reserved.
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

package org.bcia.javachain.common.policycheck.bean;

import org.bcia.javachain.msp.IIdentity;

/**
 * 类描述
 *
 * @author yuanjun
 * @date 03/05/18
 * @company Aisino
 */
public class PolicyBean {
    public boolean evalutor;
    public IIdentity deserializer;


    public boolean isEvalutor() {
        return evalutor;
    }

    public void setEvalutor(boolean evalutor) {
        this.evalutor = evalutor;
    }

    public IIdentity getDeserializer() {
        return deserializer;
    }

    public void setDeserializer(IIdentity deserializer) {
        this.deserializer = deserializer;
    }
}