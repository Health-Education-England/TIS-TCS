[![Build Status](https://build.tis.nhs.uk/jenkins/buildStatus/icon?job=HEE%2FTIS-TCS%2Fmaster)](https://build.tis.nhs.uk/jenkins/buildStatus/icon?job=HEE%2FTIS-TCS%2Fmaster)
# TIS-TCS
TIS Core Services

## Running the unit tests locally
To run the failed unit tests locally add `CLOUD_BLOB_ACCOUNT_NAME` and `CLOUD_BLOB_ACCOUNT_KEY` value to /tcs-service/src/test/resources/config/application.yml file.
The values can be found from developer's azure account in the blob called `tisdevstor`.
