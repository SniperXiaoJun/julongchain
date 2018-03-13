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
package org.bcia.javachain.consenter.common.deliver;

import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;

/**
 * @author zhangmingyang
 * @Date: 2018/3/12
 * @company Dingxuan
 */
public class DeliverHandler {
    private static JavaChainLog log = JavaChainLogFactory.getLog(DeliverHandler.class);
   public void handle(){
      log.info("this is deliver handler");
    }
    public void  deliverBlocks(){
       log.info("this is deliverBlocks");
    }
}
