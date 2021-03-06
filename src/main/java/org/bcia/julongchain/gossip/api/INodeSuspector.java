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
package org.bcia.julongchain.gossip.api;

/**
 * 类描述
 *
 * @author wanliangbing
 * @date 2018/08/20
 * @company Dingxuan
 */
public interface INodeSuspector {

    /**
     * NodeSuspector 返回节点不管该节点是否有嫌疑或被取消证书资格
     * @param nodeIdentity
     * @return
     */
    public Boolean execute(byte[] nodeIdentity);

}
