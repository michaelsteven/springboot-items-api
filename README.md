# Spring Boot Items-API Archetype Microservice

This project is intended to serve as a CRUD archetype using Spring Boot and Spring Data in an imperative (non-reactive) way.  It is also intended to demonstrate the use of Aspect Oriented Programming (AOP) for trace logging, database exception conversion, and annotation based event processing.

## Deploying to Openshift
Explore running in OpenShift with a Developer Sandbox (free) account available in RedHat: https://developers.redhat.com/developer-sandbox.

Below is an step-by-step of a way to build and deploy this microservice using the CLI. They could also be used part of a CI/CD pipeline.

1. clone down this git repository
2. build the code with maven (mvn clean install)
3. install the OC command
4  log in to openshift using the CLI
5. change to the right project (if needed)
6. create a ConfigMap with your application settings:
``oc create configmap springboot-items-api --from-file=application.yaml``
7. create a secret with your database credentials:
``oc create secret generic mysql-credentials --from-literal=username=root --from-literal=password=password``
8. create a build configuration for the application using a template:
``oc new-app --file=./manifests/openshift/build.yaml --param=APP=springboot-items-api``
9. initiate a build of the build configuration, uploading the current directory for use in the Dockerfile:
``oc start-build springboot-items-api --from-dir .``
This will build the Dockerfile and publish it into openshift's integrated container registry.
10. Use the deployment template to deploy the application:
``oc process -f manifests/openshift/deployment.yaml --param=REGISTRY=image-registry.openshift-image-registry.svc:5000/michael-hepfer-dev | oc apply -f -``



