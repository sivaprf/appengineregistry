# CI-CD Build

## Redis - Using Redis Labs
GCP Demo Scripts

+ Access
  - Valentine key - gclouddemo-redis-spinnaker
```
redis-16050.c1.us-central1-2.gce.cloud.redislabs.com:16050
```

## cicd - image
* project
  + cicd-174318
* image
  gclouddemo-cicd-default-image-v1

## Ansible Control Machine

gcloud compute ssh builder@build-ansible-master-v1 --project cicd-174318 --zone us-central1-f

sudo pip install ansible

ansible-galaxy install  AMeng.spinnaker --roles-path /home/builder/g/gclouddemo/cicd/build/roles
