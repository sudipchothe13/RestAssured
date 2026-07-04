@Company
Feature: Companies API

Scenario: Verify POST Companies API
  Given Company request payload is prepared
  When User sends "POST" request to "/companies"
  Then Response status code should be 201
  

Scenario: Verify GET Companies API
  Given Companies endpoint is available
  When User sends "GET" request to "/companies"
  Then Response status code should be 200