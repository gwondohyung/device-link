Main Author: Cloud Tsai

Copyright 2017, Dell, Inc.

Virtual Device Service 2- The Virtual Device Service 2 can simulate different kinds of devices to generate Events and Readings to Core Data Micro Service.  Furthermore, users can send commands and get responses through Command and Control Micro Service.  It’s useful when executing functional or performance tests without any real devices.
This version of Virtual Device Service is implemented based on Device Service SDK.



## Architecture

<img src="https://www.edgexfoundry.org/wp-content/uploads/sites/25/2017/04/EXF_Platform-Architecture_101917-1024x602.png"></img>



# to Launch device-link

### mongo

Many of the EdgeX Foundry microservices use a MongoDB instance to persist data or metadata. The MongoDB database for EdgeX Foundry must be initialized before it can be used by the system.

1. <code>git clone</code> https://github.com/edgexfoundry/developer-scripts/
2. install mongo
3. open terminal, type <code>mkdir /data/db</code> and then type <code>mongod</code>. This is initialization operation. so, you can exit terminal.
4. open terminal again, type <code>/usr/local/bin/mongod --dbpath "/data/db/"</code>
   - never add <code>—auth</code> option.



Once the database has been started, initialize the database with EdgeX Foundry access and schema elements.  The initialization code is located in [init_mongo.js](https://github.com/edgexfoundry/developer-scripts/blob/master/init_mongo.js) file.

1. while still running, move to developer-scripts repository which contain <code>init_mongo.js</code> file.
2. type <code>/usr/local/bin/mongo ./init_mongo.js</code> (you can stop it.)



Once complete, the database can now be stopped.  In the next step, you restart the database with authentication turned on.  You do not need to re-initialize the database again.

1. <code>/usr/local/bin/mongod --dbpath "/data/db/" --auth</code>
   - never stop it.



### dependencies

1. <code>git clone</code> https://github.com/edgexfoundry/core-command.git
2. <code>git clone</code> https://github.com/edgexfoundry/core-metadata.git
3. <code>git clone</code> https://github.com/edgexfoundry/core-data.git



### device-link

on Eclipse or another IDE,

need to 'maven install' in order. (See dependencies.)

and then need to 'java application' in order. (See dependencies.)

open browser, type <code>localhost:port/api/vi/hello</code>





### Reference

- https://wiki.edgexfoundry.org/display/FA/Get+EdgeX+Foundry+-+Developers
- https://github.com/edgexfoundry
- https://www.edgexfoundry.org/about/