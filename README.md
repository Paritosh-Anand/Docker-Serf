# Docker-Serf

Storm topology to operate on Docker events. This project take cares the right side of the below design.

![Docker Events]
(https://d2mxuefqeaa7sj.cloudfront.net/s_5E4E5D4E7819918F4A7FFC5AD14BA5406AEE511BEF98A38F756ED666B912BEA8_1469560303675_docker+events.PNG)

Primarily this project was initiated to provide a framework from where custom tasks can be executed that operations over Docker events. This project is also in sync with [Docker-Envoy](https://github.com/Paritosh-Anand/Docker-Envoy)

## Using Docker Serf

#### Container topology

This topology listens to a kafka topic where all Docker events related to containers are being produced. Use below command to submit this topology to Storm cluster from the storm installation directory

```
./bin/storm jar /tmp/Docker-Serf-1.0-SNAPSHOT.jar com.panand.docker.serf.ContainerTopology
```

For demo sake default functionality of this topology is to capture the event in elastic search. This can be extended to create a dashboard that can used to track different state of a container. 

Task that writes the Docker event to elasticsearch is implemented using `com.panand.docker.serf.bolts.IndexEvents`

```
{
  "_index" : "docker-container-events-2016-10",
  "_type" : "container",
  "_id" : "AVe4pC-AdsFfT8wHxsBL",
  "_score" : 1.0,
  "_source" : {
    "nodeName" : "mmt5898-HP-ProBook-430-G3",
    "eventType" : "container",
    "eventTime" : 1476271547,
    "containerId" : "25dbf125f0c8a048dbdf69b4525d1e325fef14a781cec47019a725114c862f36",
    "fromImage" : "route-planner-web:0.1",
    "status" : "destroy",
    "oOMKilled" : false,
    "labels" : null,
    "hostPorts" : { },
    "hostExposedPorts" : { }
  }
}
```


Similarly we can write different Bolts to implement other custom tasks over various Docker events that are being produced via [Docker-Envoy](https://github.com/Paritosh-Anand/Docker-Envoy)

#### Properties

Storm topology requires few configuration like broker hosts, spout count, bolt count etc. such properties can be defined in `serf.properties`.

## Roadmap

Motivation to use this aproach was because of absolutely no support of pre/post hooks from Docker which can be utilized to execute any custom task on various events like before or after starting/stopping a container or creating/destroying a new network and many more such use cases.

Docker Serf aims to use Apache storm and provide a way to create custom tasks that can be executed almost instantly.

## Community
Contributions, questions, and comments are all welcomed and encouraged! Do open issues/pull requests for any concerns.

Just started on this project, so there may be bugs right now. Will be adding test cases soon.