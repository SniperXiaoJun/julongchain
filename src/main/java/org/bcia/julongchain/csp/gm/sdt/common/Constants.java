/**
 * Copyright SDT. All Rights Reserved.
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
package org.bcia.julongchain.csp.gm.sdt.common;

/**
 * 常量定义
 *
 * @author tengxiumin
 * @date 2018/05/14
 * @company SDT
 */
public class Constants {
    public static final int SM2_PRIVATEKEY_LEN = 32;
    public static final int SM2_PUBLICKEY_LEN = 64;
    public static final int SM2_SIGN_RANDOM_LEN = 32;
    public static final int SM2_ENC_RANDOM_LEN = 32;
    public static final int SM2_SIGNATURE_LEN = 64;
    public static final int SM3_DIGEST_LEN = 32;
    public static final int SM4_KEY_LEN = 16;
    public static final int SM4_BLOCK_LEN = 16;
    public static final int SM4_IV_LEN = 16;
    public static final int SM4_PACKAGE_LEN = 512;

    public static final int MAX_RANDOM_LENGTH = 1024;
    public static final int MAX_KDF_LENGTH = 1024;
    public static final int MAX_KDF_SOURCE_KEY_LENGTH = 384;
}
