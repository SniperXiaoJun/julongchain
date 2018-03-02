package org.bcia.javachain.consenter.common.server;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author zhangmingyang
 * @Date: 2018/3/1
 * @company Dingxuan
 */
public class ConsenterServerTest {
@Autowired
public ConsenterServer consenterServer;
    @Test
    public void start() throws IOException {
        consenterServer.start();
    }

    @Test
    public void blockUntilShutdown() throws InterruptedException {
        consenterServer.blockUntilShutdown();
    }
}