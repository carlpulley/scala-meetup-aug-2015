Kebab Ordering Application
==========================

* REST API (to allow API usage by multiple client applications)
* Reactive application
  * message driven
  * responsive (failover quickly with defined areas of error control)
  * resilient (design in error handling)
  * elastic (aim for idempotent state changes or operations)
* Menu item data will be pulled from a persistent store
  * initially we work with a configuration store (e.g. *.conf)
  * then pull data dynamically from a database or persistent actor shards
* Scalable application
  * elastic application because we simply kill compute nodes to scale down
  * idempotent launching of compute nodes
  * support scalable number of concurrent connections
* Campaign (to which users are attached) allows those users to store a set of order choices against their identity
  * campaigns have a finite duration, liftime or TTL
  * expired campaigns are read-only
