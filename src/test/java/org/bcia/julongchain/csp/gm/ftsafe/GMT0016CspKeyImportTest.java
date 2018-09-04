package org.bcia.julongchain.csp.gm.ftsafe;

import org.bcia.julongchain.common.exception.JulongChainException;
import org.bcia.julongchain.csp.factory.ICspFactory;
import org.bcia.julongchain.csp.factory.IFactoryOpts;
import org.bcia.julongchain.csp.gmt0016.ftsafe.GMT0016CspConstant;
import org.bcia.julongchain.csp.gmt0016.ftsafe.GMT0016CspFactory;
import org.bcia.julongchain.csp.gmt0016.ftsafe.GMT0016FactoryOpts;
import org.bcia.julongchain.csp.gmt0016.ftsafe.IGMT0016Csp;
import org.bcia.julongchain.csp.gmt0016.ftsafe.ec.ECCOpts;
import org.bcia.julongchain.csp.gmt0016.ftsafe.entity.GMT0016KeyData;
import org.bcia.julongchain.csp.gmt0016.ftsafe.entity.GMT0016Lib;
import org.bcia.julongchain.csp.gmt0016.ftsafe.rsa.RSAImportOpts;
import org.bcia.julongchain.csp.gmt0016.ftsafe.rsa.RSAOpts;
import org.bcia.julongchain.csp.intfs.ICsp;
import org.bcia.julongchain.csp.intfs.IKey;
import org.bcia.julongchain.csp.intfs.opts.IKeyGenOpts;
import org.bcia.julongchain.csp.intfs.opts.IKeyImportOpts;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
/**
 * Test methods to keyImport component GMT0016
 *
 * @author zhaoxiaobo
 * @date 2018/08/26
 * @company FEITIAN
 */
public class GMT0016CspKeyImportTest {
	private ICsp csp;

    @Before
    public void before() throws JulongChainException{
        System.out.println("==========class GMT0016Cps function keyImport test start==========");
        GMT0016Lib gmt0016Lib = new GMT0016Lib("/home/bcia/libes_3000gm.so","ePass3000GM","0955381103160217","rockey","123456","ENTERSAFE-ESPK");
        IFactoryOpts factoryOpts = new GMT0016FactoryOpts(gmt0016Lib);
        Assert.assertNotNull(factoryOpts);
        ICspFactory cspFactory = new GMT0016CspFactory();
        Assert.assertNotNull(cspFactory);
        csp = cspFactory.getCsp(factoryOpts);
        Assert.assertNotNull(csp);
    }


    @Test
    public void testKeyImportByECC() throws JulongChainException{
        System.out.println("==========test function keyImport and IKeyGenOpts instance ECCOpts.ECCKeyGenOpts==========");

        byte[] rawPub = {(byte)0x30, (byte)0x45, (byte)0x02, (byte)0x20,
                (byte)0x09, (byte)0xF9, (byte)0xDF, (byte)0x31, (byte)0x1E, (byte)0x54, (byte)0x21, (byte)0xA1,
                (byte)0x50, (byte)0xDD, (byte)0x7D, (byte)0x16, (byte)0x1E, (byte)0x4B, (byte)0xC5, (byte)0xC6,
                (byte)0x72, (byte)0x17, (byte)0x9F, (byte)0xAD, (byte)0x18, (byte)0x33, (byte)0xFC, (byte)0x07,
                (byte)0x6B, (byte)0xB0, (byte)0x8F, (byte)0xF3, (byte)0x56, (byte)0xF3, (byte)0x50, (byte)0x20,
                (byte)0x02, (byte)0x21, (byte)0x00,
                (byte)0xCC, (byte)0xEA, (byte)0x49, (byte)0x0C, (byte)0xE2, (byte)0x67, (byte)0x75, (byte)0xA5,
                (byte)0x2D, (byte)0xC6, (byte)0xEA, (byte)0x71, (byte)0x8C, (byte)0xC1, (byte)0xAA, (byte)0x60,
                (byte)0x0A, (byte)0xED, (byte)0x05, (byte)0xFB, (byte)0xF3, (byte)0x5E, (byte)0x08, (byte)0x4A,
                (byte)0x66, (byte)0x32, (byte)0xF6, (byte)0x07, (byte)0x2D, (byte)0xA9, (byte)0xAD, (byte)0x13};

        byte[] rawPri = {(byte)0x39, (byte)0x45, (byte)0x20, (byte)0x8F,
                (byte)0x7B, (byte)0x21, (byte)0x44, (byte)0xB1,
                (byte)0x3F, (byte)0x36, (byte)0xE3, (byte)0x8A,
                (byte)0xC6, (byte)0xD3, (byte)0x9F, (byte)0x95,
                (byte)0x88, (byte)0x93, (byte)0x93, (byte)0x69,
                (byte)0x28, (byte)0x60, (byte)0xB5, (byte)0x1A,
                (byte)0x42, (byte)0xFB, (byte)0x81, (byte)0xEF,
                (byte)0x4D, (byte)0xF7, (byte)0xC5, (byte)0xB8};

        ECCOpts.ECCKeyGenOpts eccKeyGenOpts = new ECCOpts.ECCKeyGenOpts(false);
        IKey eccKey = csp.keyGen(eccKeyGenOpts);
        String containerName =  getContainerName(eccKey);
        Assert.assertNotNull(containerName);
        System.out.println("==========containerName:"+containerName);

        GMT0016KeyData gmt0016KeyData = new GMT0016KeyData();
        gmt0016KeyData.setRawPri(rawPri);
        gmt0016KeyData.setRawPub(rawPub);
        gmt0016KeyData.setContainer(containerName);

        ECCOpts.ECCKeyImportOpts keyImportOpts = new ECCOpts.ECCKeyImportOpts(false);
        keyImportOpts.setAlgID(GMT0016CspConstant.SGD_SM1_ECB);
        IKey importKey =  csp.keyImport(gmt0016KeyData,keyImportOpts);
        Assert.assertNotNull(importKey);
    }

    @Test
    public void testKeyImportByRSA() throws JulongChainException{
        System.out.println("==========test function keyImport and IKeyGenOpts instance RSAOpts.RSA2048KeyGenOpts==========");

        byte[] rawPub = {
                (byte)0x30, (byte)0x81, (byte)0x9F, (byte)0x30, (byte)0x0d,
                (byte)0x06, (byte)0x09, (byte)0x2a, (byte)0x86, (byte)0x48, (byte)0x86,
                (byte)0xf7, (byte)0x0d, (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x05,
                (byte)0x00, (byte)0x03, (byte)0x81, (byte)0x8d, (byte)0x00,
                (byte)0x30,
                (byte)0x81, (byte)0x89, (byte)0x02, (byte)0x81, (byte)0x81, (byte)0x00,
                (byte)0x9b, (byte)0x19, (byte)0x40, (byte)0xbb, (byte)0x37, (byte)0xbc,
                (byte)0xc9, (byte)0xee, (byte)0x48, (byte)0x77, (byte)0x6b, (byte)0x66,
                (byte)0x6f, (byte)0x66, (byte)0x24, (byte)0x1a, (byte)0xa1, (byte)0xc8,
                (byte)0x9c, (byte)0x60, (byte)0x3a, (byte)0x57, (byte)0xec, (byte)0x31,
                (byte)0x57, (byte)0x29, (byte)0x52, (byte)0xbf, (byte)0xd0, (byte)0xb6,
                (byte)0xb2, (byte)0x08, (byte)0x3f, (byte)0xcd, (byte)0x98, (byte)0xaf,
                (byte)0x71, (byte)0x8e, (byte)0x4a, (byte)0x10, (byte)0xf9, (byte)0xdc,
                (byte)0x9b, (byte)0x05, (byte)0x90, (byte)0xec, (byte)0xe7, (byte)0xf0,
                (byte)0x3a, (byte)0x3a, (byte)0x3f, (byte)0x42, (byte)0x1f, (byte)0x17,
                (byte)0x9e, (byte)0xc9, (byte)0x33, (byte)0xb0, (byte)0x3e, (byte)0xb8,
                (byte)0xc7, (byte)0x67, (byte)0x02, (byte)0xeb, (byte)0xa8, (byte)0xaa,
                (byte)0x32, (byte)0x76, (byte)0xdf, (byte)0xba, (byte)0x07, (byte)0xd2,
                (byte)0xbd, (byte)0xb7, (byte)0x68, (byte)0x67, (byte)0xb0, (byte)0x7c,
                (byte)0x00, (byte)0x67, (byte)0x63, (byte)0x58, (byte)0xd7, (byte)0x74,
                (byte)0x91, (byte)0x31, (byte)0x2b, (byte)0x75, (byte)0xe1, (byte)0x98,
                (byte)0x1c, (byte)0x29, (byte)0x44, (byte)0xc5, (byte)0x89, (byte)0x4e,
                (byte)0x08, (byte)0x25, (byte)0x5e, (byte)0x33, (byte)0x92, (byte)0x87,
                (byte)0x09, (byte)0x34, (byte)0xfe, (byte)0xe5, (byte)0xa3, (byte)0x41,
                (byte)0xc4, (byte)0xc3, (byte)0x98, (byte)0xdd, (byte)0x50, (byte)0xcc,
                (byte)0x4e, (byte)0x28, (byte)0x7c, (byte)0xe7, (byte)0x6b, (byte)0x2b,
                (byte)0xfa, (byte)0xcc, (byte)0x95, (byte)0xbf, (byte)0x02, (byte)0xf0,
                (byte)0x9a, (byte)0x87, (byte)0x02, (byte)0x03, (byte)0x01, (byte)0x00,
                (byte)0x01};

        byte[] rawPri =  {
                (byte)0x30, (byte)0x82, (byte)0x02, (byte)0x5D, (byte)0x02,
                (byte)0x01, (byte)0x00, (byte)0x02, (byte)0x81, (byte)0x81, (byte)0x00,
                (byte)0x9B, (byte)0x19, (byte)0x40, (byte)0xBB, (byte)0x37, (byte)0xBC,
                (byte)0xC9, (byte)0xEE, (byte)0x48, (byte)0x77, (byte)0x6B, (byte)0x66,
                (byte)0x6F, (byte)0x66, (byte)0x24, (byte)0x1A, (byte)0xA1, (byte)0xC8,
                (byte)0x9C, (byte)0x60, (byte)0x3A, (byte)0x57, (byte)0xEC, (byte)0x31,
                (byte)0x57, (byte)0x29, (byte)0x52, (byte)0xBF, (byte)0xD0, (byte)0xB6,
                (byte)0xB2, (byte)0x08, (byte)0x3F, (byte)0xCD, (byte)0x98, (byte)0xAF,
                (byte)0x71, (byte)0x8E, (byte)0x4A, (byte)0x10, (byte)0xF9, (byte)0xDC,
                (byte)0x9B, (byte)0x05, (byte)0x90, (byte)0xEC, (byte)0xE7, (byte)0xF0,
                (byte)0x3A, (byte)0x3A, (byte)0x3F, (byte)0x42, (byte)0x1F, (byte)0x17,
                (byte)0x9E, (byte)0xC9, (byte)0x33, (byte)0xB0, (byte)0x3E, (byte)0xB8,
                (byte)0xC7, (byte)0x67, (byte)0x02, (byte)0xEB, (byte)0xA8, (byte)0xAA,
                (byte)0x32, (byte)0x76, (byte)0xDF, (byte)0xBA, (byte)0x07, (byte)0xD2,
                (byte)0xBD, (byte)0xB7, (byte)0x68, (byte)0x67, (byte)0xB0, (byte)0x7C,
                (byte)0x00, (byte)0x67, (byte)0x63, (byte)0x58, (byte)0xD7, (byte)0x74,
                (byte)0x91, (byte)0x31, (byte)0x2B, (byte)0x75, (byte)0xE1, (byte)0x98,
                (byte)0x1C, (byte)0x29, (byte)0x44, (byte)0xC5, (byte)0x89, (byte)0x4E,
                (byte)0x08, (byte)0x25, (byte)0x5E, (byte)0x33, (byte)0x92, (byte)0x87,
                (byte)0x09, (byte)0x34, (byte)0xFE, (byte)0xE5, (byte)0xA3, (byte)0x41,
                (byte)0xC4, (byte)0xC3, (byte)0x98, (byte)0xDD, (byte)0x50, (byte)0xCC,
                (byte)0x4E, (byte)0x28, (byte)0x7C, (byte)0xE7, (byte)0x6B, (byte)0x2B,
                (byte)0xFA, (byte)0xCC, (byte)0x95, (byte)0xBF, (byte)0x02, (byte)0xF0,
                (byte)0x9A, (byte)0x87, (byte)0x02, (byte)0x03, (byte)0x01, (byte)0x00,
                (byte)0x01, (byte)0x02, (byte)0x81, (byte)0x80, (byte)0x5A, (byte)0xCD,
                (byte)0xA9, (byte)0x11, (byte)0x32, (byte)0xEB, (byte)0xAB, (byte)0x89,
                (byte)0x7F, (byte)0x11, (byte)0xB3, (byte)0x66, (byte)0x50, (byte)0x78,
                (byte)0x0B, (byte)0x51, (byte)0x30, (byte)0x40, (byte)0xC4, (byte)0x14,
                (byte)0xAE, (byte)0x73, (byte)0xC5, (byte)0x4B, (byte)0x89, (byte)0xCD,
                (byte)0x1E, (byte)0xAE, (byte)0x40, (byte)0x62, (byte)0x85, (byte)0xCE,
                (byte)0xC0, (byte)0x93, (byte)0xFC, (byte)0xFD, (byte)0x52, (byte)0x4D,
                (byte)0x4C, (byte)0xDD, (byte)0xAD, (byte)0x7B, (byte)0x53, (byte)0xA3,
                (byte)0x29, (byte)0x9B, (byte)0x19, (byte)0x9B, (byte)0x92, (byte)0x6B,
                (byte)0x81, (byte)0x97, (byte)0x93, (byte)0x9E, (byte)0x7F, (byte)0x8F,
                (byte)0x2A, (byte)0x5C, (byte)0xD8, (byte)0x0B, (byte)0xD4, (byte)0x6A,
                (byte)0x61, (byte)0xD1, (byte)0x6D, (byte)0xDD, (byte)0x84, (byte)0x13,
                (byte)0xC5, (byte)0x3B, (byte)0xA9, (byte)0x7C, (byte)0x65, (byte)0xC5,
                (byte)0x18, (byte)0x17, (byte)0x92, (byte)0x9E, (byte)0x8C, (byte)0xFD,
                (byte)0x94, (byte)0x75, (byte)0x68, (byte)0x79, (byte)0xC9, (byte)0xBB,
                (byte)0x87, (byte)0x79, (byte)0x76, (byte)0x32, (byte)0x20, (byte)0x5B,
                (byte)0x91, (byte)0x4F, (byte)0xA3, (byte)0x0F, (byte)0x3D, (byte)0x97,
                (byte)0x37, (byte)0x39, (byte)0x14, (byte)0x27, (byte)0xDA, (byte)0xB9,
                (byte)0x0F, (byte)0xD8, (byte)0xE0, (byte)0xCE, (byte)0x4D, (byte)0x1C,
                (byte)0xDF, (byte)0x2E, (byte)0x23, (byte)0x87, (byte)0xC2, (byte)0xAE,
                (byte)0xE5, (byte)0x8E, (byte)0xC6, (byte)0x75, (byte)0xC9, (byte)0xC4,
                (byte)0x4C, (byte)0xAC, (byte)0x0D, (byte)0xD0, (byte)0xC8, (byte)0x91,
                (byte)0x02, (byte)0x41, (byte)0x00, (byte)0xDD, (byte)0x4E, (byte)0x1F,
                (byte)0x35, (byte)0x6C, (byte)0x36, (byte)0x4E, (byte)0x27, (byte)0x86,
                (byte)0x92, (byte)0x6F, (byte)0x5D, (byte)0xD5, (byte)0x64, (byte)0x76,
                (byte)0x44, (byte)0xF8, (byte)0xB5, (byte)0xB4, (byte)0x9D, (byte)0x36,
                (byte)0x94, (byte)0xA6, (byte)0xEC, (byte)0xAC, (byte)0x5C, (byte)0x94,
                (byte)0x4C, (byte)0xE3, (byte)0x7D, (byte)0xCF, (byte)0x62, (byte)0xBF,
                (byte)0x54, (byte)0x9C, (byte)0x0E, (byte)0x63, (byte)0xF0, (byte)0xE3,
                (byte)0x47, (byte)0xBA, (byte)0xEB, (byte)0x98, (byte)0x1F, (byte)0xE6,
                (byte)0x19, (byte)0x83, (byte)0x56, (byte)0xE6, (byte)0xC2, (byte)0x94,
                (byte)0x14, (byte)0x09, (byte)0xE5, (byte)0xD0, (byte)0xA2, (byte)0x84,
                (byte)0xE1, (byte)0x48, (byte)0xE0, (byte)0x80, (byte)0x84, (byte)0xBE,
                (byte)0x3F, (byte)0x02, (byte)0x41, (byte)0x00, (byte)0xB3, (byte)0x69,
                (byte)0xFE, (byte)0x56, (byte)0x3D, (byte)0x41, (byte)0x28, (byte)0xFA,
                (byte)0x70, (byte)0x0A, (byte)0x58, (byte)0x76, (byte)0xE4, (byte)0x77,
                (byte)0x21, (byte)0xF8, (byte)0x2F, (byte)0xA6, (byte)0x53, (byte)0x66,
                (byte)0x2C, (byte)0xAD, (byte)0xCA, (byte)0x4F, (byte)0xF6, (byte)0xC3,
                (byte)0xCD, (byte)0xBE, (byte)0xF8, (byte)0xC7, (byte)0xF7, (byte)0xA1,
                (byte)0x34, (byte)0xFB, (byte)0xAA, (byte)0xE7, (byte)0x8E, (byte)0x6E,
                (byte)0x56, (byte)0x39, (byte)0xF3, (byte)0x47, (byte)0x55, (byte)0xB9,
                (byte)0x83, (byte)0x43, (byte)0x45, (byte)0xC0, (byte)0x26, (byte)0x40,
                (byte)0x27, (byte)0xF3, (byte)0x6B, (byte)0xC4, (byte)0xEF, (byte)0x3F,
                (byte)0x8D, (byte)0x82, (byte)0xCC, (byte)0xB5, (byte)0xC4, (byte)0x43,
                (byte)0x21, (byte)0xB9, (byte)0x02, (byte)0x41, (byte)0x00, (byte)0xA8,
                (byte)0x8F, (byte)0x41, (byte)0x4C, (byte)0x12, (byte)0x81, (byte)0x06,
                (byte)0x64, (byte)0x2A, (byte)0xA6, (byte)0xCC, (byte)0x76, (byte)0x5E,
                (byte)0xFB, (byte)0xA6, (byte)0xED, (byte)0x7F, (byte)0xB5, (byte)0xFE,
                (byte)0xBC, (byte)0xA3, (byte)0xFE, (byte)0xF3, (byte)0x69, (byte)0x5F,
                (byte)0x1B, (byte)0x20, (byte)0xC5, (byte)0xB3, (byte)0x9C, (byte)0x76,
                (byte)0xE2, (byte)0x52, (byte)0xB2, (byte)0xE2, (byte)0x2E, (byte)0x3F,
                (byte)0xC7, (byte)0x11, (byte)0x85, (byte)0xE8, (byte)0x04, (byte)0x5C,
                (byte)0x19, (byte)0x27, (byte)0xCC, (byte)0x94, (byte)0xF2, (byte)0x12,
                (byte)0xCF, (byte)0x6F, (byte)0xB4, (byte)0x17, (byte)0x99, (byte)0xD8,
                (byte)0xA0, (byte)0x80, (byte)0xAF, (byte)0x8B, (byte)0x2F, (byte)0x9C,
                (byte)0x13, (byte)0xFF, (byte)0x47, (byte)0x02, (byte)0x41, (byte)0x00,
                (byte)0x88, (byte)0xBA, (byte)0x05, (byte)0xF1, (byte)0x82, (byte)0xCC,
                (byte)0xDD, (byte)0x85, (byte)0xBD, (byte)0x4C, (byte)0xA5, (byte)0x0E,
                (byte)0x36, (byte)0x1D, (byte)0xF3, (byte)0xED, (byte)0x47, (byte)0xA2,
                (byte)0xE1, (byte)0x7B, (byte)0xC4, (byte)0x20, (byte)0xCD, (byte)0x45,
                (byte)0x13, (byte)0x31, (byte)0x34, (byte)0xE2, (byte)0x8C, (byte)0x85,
                (byte)0x17, (byte)0xD9, (byte)0xEA, (byte)0x9E, (byte)0xC0, (byte)0x27,
                (byte)0xA7, (byte)0x0B, (byte)0xBE, (byte)0xFA, (byte)0x7A, (byte)0xC3,
                (byte)0xAA, (byte)0x38, (byte)0xAE, (byte)0x27, (byte)0xDE, (byte)0x48,
                (byte)0x08, (byte)0xDE, (byte)0x6B, (byte)0x93, (byte)0xBE, (byte)0x55,
                (byte)0x5E, (byte)0x5E, (byte)0x78, (byte)0x61, (byte)0x6D, (byte)0xD0,
                (byte)0x37, (byte)0xB9, (byte)0x51, (byte)0x41, (byte)0x02, (byte)0x40,
                (byte)0x3D, (byte)0xA2, (byte)0x60, (byte)0x01, (byte)0xA8, (byte)0xA8,
                (byte)0x75, (byte)0x4B, (byte)0x26, (byte)0x85, (byte)0x31, (byte)0x1B,
                (byte)0x98, (byte)0xA6, (byte)0x75, (byte)0xD2, (byte)0x7B, (byte)0x4C,
                (byte)0x6B, (byte)0x8E, (byte)0x78, (byte)0x1D, (byte)0x7F, (byte)0xFC,
                (byte)0x8E, (byte)0x16, (byte)0x98, (byte)0x70, (byte)0xE8, (byte)0xCE,
                (byte)0xD9, (byte)0x5F, (byte)0x8F, (byte)0xC5, (byte)0xD9, (byte)0xDF,
                (byte)0x95, (byte)0xDB, (byte)0xBC, (byte)0xDE, (byte)0x15, (byte)0x43,
                (byte)0xC9, (byte)0x29, (byte)0x37, (byte)0xCD, (byte)0x83, (byte)0x37,
                (byte)0x21, (byte)0x90, (byte)0x93, (byte)0x1C, (byte)0x67, (byte)0xE1,
                (byte)0x01, (byte)0xE0, (byte)0x90, (byte)0xD3, (byte)0x5E, (byte)0xEB,
                (byte)0xC4, (byte)0x0B, (byte)0xA8, (byte)0xA6};
        System.out.println("rawPri length = "+rawPri.length);
        IKeyGenOpts rsa2048keyGenOpts = new RSAOpts.RSA2048KeyGenOpts(false);
        IKey rsa2048Key = csp.keyGen(rsa2048keyGenOpts);
        String containerName = getContainerName(rsa2048Key);

        GMT0016KeyData gmt0016KeyData = new GMT0016KeyData();
        gmt0016KeyData.setRawPri(rawPri);
        gmt0016KeyData.setRawPub(rawPub);
        gmt0016KeyData.setContainer(containerName);
        IKeyImportOpts opts = RSAImportOpts.SM1_ECB;
        IKey importKey =  csp.keyImport(gmt0016KeyData, opts);
        Assert.assertNotNull(importKey);
    }

    public String getContainerName(IKey key) throws JulongChainException {
        String sContainerName = "";
        byte[] ski = key.ski();
        boolean bSignFlag = true;
        byte[] cipher;
        byte[] hash;
        int type = 0;
        //analyze ski
        int skiIndex = 0;
        while (skiIndex < ski.length) {
            int tag = ski[skiIndex];
            skiIndex++;
            switch (tag) {
                case GMT0016CspConstant.TAG_CONTAINER:
                    int nameLen = ski[skiIndex];
                    skiIndex++;
                    byte[] name = new byte[nameLen];
                    System.arraycopy(ski, skiIndex, name, 0, nameLen);
                    skiIndex += nameLen;
                    sContainerName = new String(name);
                    break;
                case GMT0016CspConstant.TAG_PUBLICK_KEY_SIGN_FLAG:
                    int flagLen = ski[skiIndex];
                    skiIndex++;
                    bSignFlag = (ski[skiIndex] == (byte)1); //Sign Flag
                    skiIndex += flagLen;
                    break;
                case GMT0016CspConstant.TAG_KEY_CIPHER_DATA:
                    int dataLen = ski[skiIndex];
                    skiIndex++;
                    cipher = new byte[dataLen];
                    System.arraycopy(ski, skiIndex, cipher, 0, dataLen);
                    skiIndex += dataLen;
                    break;
                case GMT0016CspConstant.TAG_PUBLICK_KEY_HASH:
                    int hashLen = ski[skiIndex];
                    skiIndex++;
                    hash = new byte[hashLen];
                    System.arraycopy(ski, skiIndex, hash, 0, hashLen);
                    skiIndex += hashLen;
                    break;
                case GMT0016CspConstant.TAG_KEY_TYPE:
                    int typelen = ski[skiIndex];
                    type = ski[++skiIndex];
                    skiIndex += typelen;
                    break;
                default:
                    break;
            }
        }
        return sContainerName;
    }

    @After
    public void after() throws JulongChainException{
        System.out.println("==========test end==========");
        ((IGMT0016Csp)csp).finalized();
    }
}
