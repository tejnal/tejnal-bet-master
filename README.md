# Information to run locally

1) It uses internally H2 database, however can be switched to any database by changing the drivers in properties file.
Username -> sa
Password -> password.1
2)We provide a default admin user for the benefit to fetch all details, whose credentials are
Username -> ADM_USR_1
Password -> password.1
3) Swagger Documentation is also enabled for firing the APIs on leisure, which can be access at [http://localhost:2022/bet-master/swagger-ui/#/]
4) For Swagger to be used, login with Sign In API once, and copy the access token in Authorize popup as Bearer <access_token_received>

### PRIMARY APIs
•	http://localhost:2022/bet-master/api/auth/signin
•	http://localhost:2022/bet-master/api/auth/signup
•	http://localhost:2022/bet-master/api/bet/initiate
•	http://localhost:2022/bet-master/api/auth/refreshtoken
•	http://localhost:2022/bet-master/api/auth/logout
•	http://localhost:2022/bet-master/api/bet/history
•	http://localhost:2022/bet-master/api/bet/check_details?include_hist=true
•	http://localhost:2022/bet-master/api/bet/admin/check_details?include_hist=false
•	http://localhost:2022/bet-master/api/bet/admin/all?include_hist=true






