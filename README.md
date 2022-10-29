# RMM API Project

The project is configured to use an in-memory H2 database that is volatile. If you wish to make it maintain data on application shut down, you can change the spring.database.jdbc-url to point at a file like `jdbc:h2:file:/{your file path here}`

## Starting the Application

- With the IDE run the `RmmApplication` class
- With command line type `./gradlew bootRun`


## Authentication
In order to perform requests, you need to authenticate. This application uses JWT tokens with Spring Security.

With a rest client like Postman or Insomnia, or curl, do a POST request to:
> localhost:8080/login

With the request body:
```json
{
  "username" : "dev",
  "password" : "dev"
}
```
Get the JWT token and use it as a **Bearer** Authorization header, like:

> Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZXYiLCJleHAiOjE2NjcwNzUyOTF9.8kYo3O86OXxvpIM773JLtnuAlQgmfY-oAJghvqkeKxrkHIwG-rlV7vNr_jxid6CvKYkRKb5lLxkcI5wLJl_klA


## Swagger UI
Go to: 
http://localhost:8080/swagger-ui/index.html

In order to use Swagger UI you need to provide the auth token. In the swagger home, click **Authorize**:
![Token](swagger1.png)

Then, type the auth token with the prefix Bearer and click Authorize:
![Token](swagger2.png)

You should be able to use the endpoints for `Devices` and `Services`.
## H2 Console 
<br>
In order to see and interact with your db, access the h2 console in your browser.
After running the application, go to:

http://localhost:8080/h2-console

Enter the information for the url, username, and password in the application.yml:

```yml
url: jdbc:h2:mem:localdb
username: sa 
password: password
```

You should be able to see a db console now that has the Sample Repository in it.

Type:

```sql
SELECT * FROM DEVICE;
````

Click `Run`, you should see two rows, for ids `1` and `2`

### Suggestions

Feel free to remove or repurpose the existing Sample Repository, Entity, Controller, and Service. 
