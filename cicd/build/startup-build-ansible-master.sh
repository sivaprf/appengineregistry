#! /bin/bash
apt-get update
apt-get install -y git python-setuptools python-dev build-essential
easy_install pip
#pip install -r requirements
mkdir -p /opt/google
curl https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-164.0.0-linux-x86_64.tar.gz  -o /tmp/gcloudsdk.tar.gz
tar xvzf /tmp/gcloudsdk.tar.gz -C /opt/google/google-cloud-sdk
./opt/google/google-cloud-sdk/install.sh
gcloud beta compute users create builder --owner builder@gclouddemo.com
mkdir -p /home/builder/g/gclouddemo
chown -R builder /home/builder
chgrp -R builder /home/builder
git clone https://github.com/mzuo/gclouddemo.git -b master
