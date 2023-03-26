# Environment setup

## Install Hadoop & start HDFS in standalone mode

* Download binary file from [Hadoop official site](https://hadoop.apache.org/releases.html)
* Setup Hadoop environment variables:
  ```
    export HADOOP_HOME={path/to/hadoop}
    export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
  ```
* [Start HDFS locally](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html)
  ```
  sudo apt install ssh
  ```
  * Edit hadoop & hdfs config
    * `etc/hadoop/core-site.xml`
      ```
      <configuration>
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://localhost:9000</value>
        </property>
      </configuration>
      ```
    * `etc/hadoop/hdfs-site.xml`
    ```
    <configuration>
       <property>
         <name>dfs.replication</name>
         <value>1</value>
       </property>
    </configuration>
    ```
  * Setup ssh
    ```
    // generate rsa key
    ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
    chmod 0600 ~/.ssh/authorized_keys
    
    ssh localhost
    ```
  * Setup hdfs
    ```
    // start
    bin/hdfs namenode -format
    sbin/start-dfs.sh
    
    // stop
    sbin/stop-dfs.sh
    ```

## [Start local FTP server](https://www.digitalocean.com/community/tutorials/how-to-set-up-vsftpd-for-a-user-s-directory-on-ubuntu-20-04)
