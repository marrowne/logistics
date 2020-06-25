#!/bin/bash

MVN_VERSION=3.6.3

echo "Install Java JDK 8"
yum remove -y java
yum install -y java-1.8.0-openjdk

echo "Install Docker engine"
yum update -y
yum install docker -y
usermod -aG docker ec2-user
service docker start

echo "Install git"
yum install -y git

echo "Install Telegraf"
wget https://dl.influxdata.com/telegraf/releases/telegraf-1.6.0-1.x86_64.rpm -O /tmp/telegraf.rpm
yum localinstall -y /tmp/telegraf.rpm
rm /tmp/telegraf.rpm
chkconfig telegraf on
usermod -aG docker telegraf
mv /tmp/telegraf.conf /etc/telegraf/telegraf.conf
service telegraf start

echo "Install Maven"
wget http://apache.claz.org/maven/maven-3/$MVN_VERSION/binaries/apache-maven-$MVN_VERSION-bin.tar.gz

sudo mkdir /usr/local/apache-maven
tar -xzf apache-maven-$MVN_VERSION-bin.tar.gz
sudo cp -r apache-maven-$MVN_VERSION /usr/local/apache-maven

echo "export M2_HOME=/usr/local/apache-maven/apache-maven-$MVN_VERSION/" >> ~/.bash_profile
echo 'export M2=$M2_HOME/bin' >> ~/.bash_profile
echo 'export PATH=$M2:$PATH' >> ~/.bash_profile

echo "Install Terraform"
wget https://releases.hashicorp.com/terraform/0.12.26/terraform_0.12.26_linux_amd64.zip
unzip terraform_*_linux_amd64.zip
mv ./terraform /bin

echo "Install aws-iam-authenticator"
curl -o aws-iam-authenticator https://amazon-eks.s3.us-west-2.amazonaws.com/1.16.8/2020-04-16/bin/linux/amd64/aws-iam-authenticator
chmod +x ./aws-iam-authenticator
mv ./aws-iam-authenticator /bin

echo "Install kubectl"
curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl
chmod +x ./kubectl
mv ./kubectl /bin/kubectl
