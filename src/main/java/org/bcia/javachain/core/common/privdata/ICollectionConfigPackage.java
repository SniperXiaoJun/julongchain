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
package org.bcia.javachain.core.common.privdata;

import java.util.List;

/**
 * ICollection defines a common interface for collections
 *
 * @author sunianle
 * @date 4/27/18
 * @company Dingxuan
 */
public interface ICollectionConfigPackage {
    /**
     * returns this collection's ID
     * @return
     */
    String getCollectionID();

    /**
     * MemberOrgs returns the collection's members as MSP IDs. This serves as
     * a human-readable way of quickly identifying who is part of a collection.
     * @return
     */
    List<String> getMemberOrgs();
}
