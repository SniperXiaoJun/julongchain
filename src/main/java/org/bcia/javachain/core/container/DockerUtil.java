/**
 * Copyright Dingxuan. All Rights Reserved.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bcia.javachain.core.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.google.common.collect.Sets;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;
import net.schmizz.sshj.SSHClient;
import org.apache.commons.lang3.StringUtils;
import org.bcia.javachain.common.exception.JavaChainException;
import org.bcia.javachain.common.exception.SmartContractException;
import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 使用docker-java client连接和运行docker命令, docker要以dockerd -H tcp://0.0.0.0:2375方式运行,
 * 参考：https://github.com/docker-java/docker-java/wiki
 *
 * @author wanliangbing
 * @date 2018/5/11
 * @company Dingxuan
 */
public class DockerUtil {

  private static JavaChainLog logger = JavaChainLogFactory.getLog(DockerUtil.class);

  /** docker host ip */
  private static final String DOCKER_HOST_IP = "localhost";

  /** docker host port */
  private static final String DOCKER_HOST_PORT = "2375";

  /** build name */
  private static final String BUILD_NAME = "javachain";

  /** source path */
  private static final String SOURCE_PATH =
      "/var/lib/jenkins/workspace/"
          + BUILD_NAME
          + "/src/main/java/org/bcia/javachain/core/smartcontract/client/";

  /** jar path */
  private static final String JAR_PATH =
      "/var/lib/jenkins/workspace/" + BUILD_NAME + "/target/javachain-jar-with-dependencies.jar";

  private static DockerClient getDockerClient() {
    DockerClient dockerClient =
        DockerClientBuilder.getInstance("tcp://" + DOCKER_HOST_IP + ":" + DOCKER_HOST_PORT).build();
    return dockerClient;
  }

  private static void closeDockerClient(DockerClient dockerClient) {
    try {
      dockerClient.close();
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * 创建Docker镜像
   *
   * @param dockerFilePath dockerFile文件路径
   * @param tag 镜像的标签
   * @return
   */
  public static String buildImage(String dockerFilePath, String tag) {
    DockerClient dockerClient = getDockerClient();

    BuildImageResultCallback callback =
        new BuildImageResultCallback() {
          @Override
          public void onNext(BuildResponseItem item) {
            logger.info(item.toString());
            super.onNext(item);
          }
        };

    String imageId =
        dockerClient
            .buildImageCmd()
            .withDockerfile(new File(dockerFilePath))
            .withTags(Sets.newHashSet(tag))
            .exec(callback)
            .awaitImageId();

    closeDockerClient(dockerClient);

    logger.info("build image success, imageId:" + imageId);

    return imageId;
  }

  /**
   * 从docker hub中查找镜像
   *
   * @param imageName 镜像名称
   * @return
   */
  public static List<String> searchImages(String imageName) {
    DockerClient dockerClient = getDockerClient();
    List<SearchItem> searchImageItemList = dockerClient.searchImagesCmd(imageName).exec();
    List<String> searchImageNameList = new ArrayList<String>();
    for (SearchItem searchItem : searchImageItemList) {
      searchImageNameList.add(searchItem.getName());
    }
    closeDockerClient(dockerClient);
    return searchImageNameList;
  }

  /**
   * list images
   *
   * @param imageName 镜像名称
   * @return
   */
  public static List<String> listImages(String imageName) {
    List<String> imageNameList = new ArrayList<String>();
    DockerClient dockerClient = getDockerClient();
    List<Image> imageList = dockerClient.listImagesCmd().exec();
    for (Image image : imageList) {
      String imageTag = image.getRepoTags()[0];
      if (StringUtils.isEmpty(imageName) || StringUtils.contains(imageTag, imageName)) {
        imageNameList.add(imageTag);
      }
    }
    closeDockerClient(dockerClient);
    return imageNameList;
  }

  /**
   * list containers
   *
   * @param name 容器名称
   * @return
   */
  public static List<String> listContainers(String name) {
    // Show only containers with the passed status (created|restarting|running|paused|exited).
    List<String> result = new ArrayList<String>();
    DockerClient dockerClient = getDockerClient();
    List<Container> containerList = dockerClient.listContainersCmd().withShowAll(true).exec();
    for (Container container : containerList) {
      if (StringUtils.isEmpty(name) || StringUtils.contains(container.getImage(), name)) {
        result.add(container.getImage());
      }
    }
    closeDockerClient(dockerClient);
    return result;
  }

  public static String createContainer(String imageId, String containerName) {
    String containerId =
        getDockerClient()
            .createContainerCmd(imageId)
            .withName("mycc")
            .withCmd("/bin/sh", "-c", "java -jar /root/javachain/target/javachain-smartcontract-java-jar-with-dependencies.jar -i mycc")
            .exec()
            .getId();
    logger.info("container ID:" + containerId);
    return containerId;
  }

  public static void startContainer(String containerId) {
    logger.info("start container, ID:" + containerId);
    getDockerClient().startContainerCmd(containerId).exec();
  }

  public static void stopContainer(String containerId) {
    getDockerClient().stopContainerCmd(containerId).exec();
  }

  /**
   * 上传文件到服务器
   *
   * @param smartContractFilePath
   */
  public static void uploadSmartContractFile(String smartContractFilePath) {
    uploadFile(smartContractFilePath, SOURCE_PATH);
  }

  public static void uploadFile(String localPath, String remotePath) {
    if (StringUtils.isEmpty(localPath) || StringUtils.isEmpty(remotePath)) {
      return;
    }
    SSHClient ssh = new SSHClient();
    try {
      ssh.loadKnownHosts();
      ssh.connect("localhost", 22);
      ssh.authPassword("jenkins", "jenkins");
      ssh.newSCPFileTransfer().upload(localPath, remotePath);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    } finally {
      try {
        ssh.close();
      } catch (IOException e) {
        logger.error(e.getMessage(), e);
      }
    }
  }

  /**
   * 执行build命令
   *
   * @throws IOException
   * @throws URISyntaxException
   * @throws JavaChainException
   */
  public static void execBuild() throws SmartContractException {

    try {
      JenkinsServer jenkinsServer =
          new JenkinsServer(new URI("http://localhost:8080"), "jenkins", "jenkins");
      JobWithDetails jenkinsJob = jenkinsServer.getJob(BUILD_NAME);
      jenkinsJob.build();

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        logger.error(e.getMessage(), e);
      }

      JobWithDetails details = jenkinsJob.details();
      Build lastBuild = details.getLastBuild();

      while (lastBuild.details().getResult() == null) {}

      String success = "SUCCESS";

      if (!StringUtils.equals(lastBuild.details().getResult().toString(), success)) {
        throw new JavaChainException("build jar error.");
      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new SmartContractException(e.getMessage(), e);
    }
  }

  /** 下载build完成后的jar包 */
  public static void downloadJar() {
    SSHClient ssh = new SSHClient();
    try {
      ssh.loadKnownHosts();
      ssh.connect("localhost", 22);
      ssh.authPassword("jenkins", "jenkins");
      ssh.newSCPFileTransfer().download(JAR_PATH, "D:\\");
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    } finally {
      try {
        ssh.close();
      } catch (IOException e) {
        logger.error(e.getMessage(), e);
      }
    }
  }

  /**
   * 上传智能合约java文件，执行build，下载jar包
   *
   * @param smartContractFilePath
   * @throws IOException
   * @throws URISyntaxException
   * @throws JavaChainException
   */
  public static synchronized void uploadAndGetJar(String smartContractFilePath)
      throws SmartContractException {
    // 上传SC
    uploadSmartContractFile(smartContractFilePath);

    // 执行jenkins build
    execBuild();

    // 下载jar包
    // downloadJar();
  }

  public static void main(String[] args) throws Exception {
    // buildImage("/root/javachain/images/scenv/Dockerfile", "javachain-baseimage1");
    // buildImage("/root/instantiate/Dockerfile", "javachain-baseimage4");
    // DockerClient dockerClient = getDockerClient();
    // CreateContainerResponse container =
    //     dockerClient
    //         .createContainerCmd("mycc-1.0")
    //         .withAttachStdin(true)
    //         .withCmd("/bin/bash")
    //         .exec();

    DockerClient dockerClient = getDockerClient();

    String containerId =
        dockerClient
            .createContainerCmd("mycc-1.0")
            .withCmd("/bin/sh", "-c", "java -jar /root/javachain/target/javachain-smartcontract-java-jar-with-dependencies.jar -i mycc")
            // .withRestartPolicy(RestartPolicy.alwaysRestart())
            .withName("mycc-1.0-" + UUID.randomUUID().toString())
            .exec()
            .getId();

    // dockerClient.startContainerCmd(containerId).exec();


    // String s = "java -jar /root/javachain/target/javachain-smartcontract-java-jar-with-dependencies.jar -i mycc\r\n";
    //
    // getDockerClient().attachContainerCmd(containerId).withStdIn(new ByteArrayInputStream(s.getBytes()));

    System.out.println(containerId);

  }

}
