appengine-ecommerce-catalogapp
============================

A simple ecommerce GCP App Engine app that uses either a Cloud SQL or Datastore
back end to access a simple Google Store-based product catalog. Still very much
a work in progress.


* Java 8
* [Maven](https://maven.apache.org/download.cgi) (at least 3.3.9)
* [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud)

Initialize the Google Cloud SDK using:

    gcloud init

This skeleton is ready to run.



    mvn appengine:run


    mvn appengine:deploy


    mvn test



    gradle appengineRun


    gradle appengineDeploy


    gradle test


As you add / modify the source code (`src/main/java/...`) it's very useful to add [unit testing](https://cloud.google.com/appengine/docs/java/tools/localunittesting)
to (`src/main/test/...`).  The following resources are quite useful:

* [Junit4](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)
* [Truth](http://google.github.io/truth/)
