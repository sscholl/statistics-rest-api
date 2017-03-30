## General Description

API is supposed to calculate real-time statistics summary based on transaction data records received for the last 60 seconds. Calculation of statistic is made in constant time and memory (O(1)).


## API endpoints

#### POST /transactions

This endpoint is called every time a new transaction happened.

Request:

    {
    	"amount": 12.3,
    	"timestamp": 1478192204000
    }
where:
 - amount: double specifying the amount 
 - time: long specifying unix epoch time

Response: empty body with HTTP status 201

#### GET /statistics

This endpoint calculates and returns statistics in O(1) constant time and memory based on transaction data for the last 60 seconds.

Response:

    {
    	"sum": 1000,
    	"avg": 100,
    	"max": 200,
    	"min": 50,
    	"count": 10
    }

where:
 - sum: double specifying total sum of transaction values
 - avg: double specifying average of all transaction values
 - max: double specifying highest transaction value
 - max: double specifying lowest transaction value
 - count: long specifying total number of transactions happened



## API error codes

In case if any error occured, API returns an error response like below:

    {
       "errorCode": 1003,
       "errorMessage": "Missing amount field"
    }

Error codes:

    1000 - Validation error
    1001 - Empty request body
    1002 - Missing timestamp field
    1003 - Missing amount field
    9999 - Internal API Error
