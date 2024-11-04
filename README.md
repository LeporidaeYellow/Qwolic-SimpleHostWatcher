# Qwolic SimpleHostWatcher

An application (Java Spring Boot) for periodically polling the URLs for a correct response and checking ssl certificates for validity. And, of course, notification of trouble events in the telegram.

The Mongodb database is used for data storage.

Document fields (data model):
- String `type` is used to determine the type of check: 
    `status200` - polling the URLs for a correct response; 
    `common` - checking ssl certificates for validity.
- String `url` is used to define URL or domain;
- Boolen "approved"  is used to turn on or off the verification process;


### Tasks in the scheduler periodically launch services (`local.leporidaeyellow.infrastructure.simplehostwatcher.job`):
- The `UrlsObserverJob` task periodically polls the list of URLs for a 200 code response. If the response is negative, it sends a message in a telegram. The list of events is monitored using the `Eventservice`;
- The `UpdateUrlsListJob` task periodically updates the list of URLs by requesting from the MongoDB database;
- The `CertsX509Observer` task periodically polls the list of `domains` for the validity of the remaining days of the certificate.



### The controller functionality is implemented in this section (`local.leporidaeyellow.infrastructure.simplehostwatcher.controller`):
- `(/forward/text)` receiving json at endpoint and sending it to the telegram channel `alarm` (or another one in the bot configuration);
- `(/events/all)` viewing current events with `status200` (polling URLs in the `local.leporidaeyellow.infrastructure.simplehostwatcher.job.UrlsObserverJob` task);
- `(/urls-list/status200)` viewing the current list of URLs;
- `(/certs/check/<DOMAIN>)` checking the <DOMAIN> certificate for validity of  remaining days;
- `(/certs/domain-list)` getting a list of domains to check for validity period of remaining days.



### The application needs environment variables:
- `ENV_HOST_CLUSTER` - name of host on which application is running;
- `ENV_CHAT_ID` - telegram chat ID;
- `ENV_TG_TOKEN` - telegram bot token;
- `ENV_DB_USER` - MongoDB user;
- `ENV_DB_PASS` - MongoDB password;
- `ENV_DB_NAME` - name of MongoDB database;
- `ENV_DB_DOCUMENT_NAME` - name of MongoDB document.