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
package org.bcia.javachain.common.resourceconfig;

import com.google.protobuf.InvalidProtocolBufferException;
import org.bcia.javachain.common.configtx.IValidator;
import org.bcia.javachain.common.configtx.Validator;
import org.bcia.javachain.common.exception.PolicyException;
import org.bcia.javachain.common.exception.ValidateException;
import org.bcia.javachain.common.groupconfig.IGroupConfigBundle;
import org.bcia.javachain.common.policies.IPolicyManager;
import org.bcia.javachain.common.policies.IPolicyProvider;
import org.bcia.javachain.common.policies.PolicyManager;
import org.bcia.javachain.common.policies.PolicyRouter;
import org.bcia.javachain.common.resourceconfig.config.IResourcesConfig;
import org.bcia.javachain.common.resourceconfig.config.ResourcesConfig;
import org.bcia.javachain.protos.common.Configtx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象
 *
 * @author zhouhui
 * @date 2018/4/25
 * @company Dingxuan
 */
public class ResourcesConfigBundle {
    interface Callback {
        void call(IResourcesConfig resourcesConfig);
    }

    private String groupId;
    private Configtx.Config config;
    private IGroupConfigBundle groupConfigBundle;
    private IResourcesConfig resourcesConfig;
    private IValidator validator;
    private PolicyRouter policyRouter;

    private List<Callback> callbackList;

    public ResourcesConfigBundle(String groupId, Configtx.Config config, IGroupConfigBundle groupConfigBundle,
                                 List<Callback> callbackList) throws InvalidProtocolBufferException,
            ValidateException, PolicyException {
        this.groupId = groupId;
        this.config = config;
        this.groupConfigBundle = groupConfigBundle;
        this.callbackList = callbackList;

        this.resourcesConfig = new ResourcesConfig(config.getGroupTree());

        //TODO:需要赋值
        Map<Integer, IPolicyProvider> policyProviderMap = new HashMap<Integer, IPolicyProvider>();
//        policyProviderMap.put(Policies.Policy.PolicyType.SIGNATURE_VALUE, new PolicyProvider(configResources.getPolicyManager()));

        IPolicyManager resourcesPolicyManager = new PolicyManager(ResourceConfigConstant.RESOURCES,
                policyProviderMap, config.getGroupTree());
        IPolicyManager groupPolicyManager = groupConfigBundle.getPolicyManager();

        this.policyRouter = new PolicyRouter(groupPolicyManager, resourcesPolicyManager);

        new Validator(groupId, config, ResourceConfigConstant.RESOURCES, policyRouter);

    }
}
