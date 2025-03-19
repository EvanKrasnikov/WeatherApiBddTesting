# WeatherApiBddTesting
BDD testing of Weather API

### How to run tests
Command will run tests and open generated Allure report in browser
```bash
./mvnw test allure:report allure:serve
```

### Where to find logs
Log with failing assertions `errors.log` will appear in the root project folder after test run execution.