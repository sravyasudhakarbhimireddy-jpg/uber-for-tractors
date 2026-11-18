# Uber for Tractors – Equipment Rental Management System

A Java Full Stack internship project built with Java 17, Spring Boot, Spring Data JPA, MySQL, HTML, CSS and JavaScript.

## Features
- View farm equipment and hourly rental prices
- Add equipment (simple admin/demo function)
- Book equipment with customer name, 10-digit phone and rental hours
- Calculate total rental price automatically
- View all bookings
- Cancel bookings
- Sample equipment is inserted automatically on the first run

## Requirements
- JDK 17 or later (JDK 21 is also suitable)
- Eclipse IDE for Enterprise Java and Web Developers (or Eclipse with Spring Tools)
- MySQL Server 8.x and MySQL Workbench
- Maven (Eclipse can use the bundled Maven integration)

## 1. Create the database
Open MySQL Workbench and run:

```sql
CREATE DATABASE tractor_rental;
```

## 2. Set your MySQL password
Open `src/main/resources/application.properties` and replace:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

with the password for your local MySQL `root` user. If your MySQL root account has no password, leave the value empty:

```properties
spring.datasource.password=
```

If your MySQL username is not `root`, update `spring.datasource.username` too.

## 3. Import into Eclipse
1. Extract `uber-for-tractors.zip` to a folder.
2. Open Eclipse and select **File → Import**.
3. Select **Maven → Existing Maven Projects**, then click **Next**.
4. Browse to the extracted `uber-for-tractors` folder.
5. Select the `pom.xml` project and click **Finish**.
6. Wait for Maven to download dependencies. Internet access is required for the first Maven build.
7. In Project Explorer, right-click the project → **Maven → Update Project** if Eclipse shows dependency errors.
8. Confirm Eclipse uses JDK 17 or later: **Window → Preferences → Java → Installed JREs**.

## 4. Run the project
1. Ensure MySQL Server is running.
2. Open `TractorRentalApplication.java`.
3. Right-click → **Run As → Java Application** (or **Spring Boot App** if Spring Tools is installed).
4. Wait until the Console shows that the application has started on port 8080.
5. Open this address in your browser:

   http://localhost:8080

The database tables are created automatically by Hibernate (`ddl-auto=update`). Sample equipment is inserted when the equipment table is empty.

## 5. Test the application
- The home page should show five sample equipment cards.
- Click **Book Now**, enter customer details and hours, then confirm.
- Scroll to **Booking Management** to see the booking and calculated total.
- Use **Add Equipment** to add another machine.
- Use **Cancel** to cancel a booking.
- Use **Delete (Admin)** to delete equipment that has no bookings.

## API endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/equipment` | List equipment |
| POST | `/api/equipment` | Add equipment |
| DELETE | `/api/equipment/{id}` | Delete equipment if it has no bookings |
| GET | `/api/bookings` | List bookings |
| POST | `/api/bookings` | Create a booking |
| PUT | `/api/bookings/{id}/cancel` | Cancel a booking |

## Important notes
- This is a functional academic/demo project, not a production-ready rental platform.
- The admin functions are not protected by login/authentication. Do not expose this demo application to the public internet.
- Bookings are recorded, but equipment availability is not automatically blocked for a time slot. Add authentication, role-based access, date/time slot conflict checks and payment integration before production use.
- If port 8080 is already occupied, change `server.port` in `application.properties`.
- If MySQL reports `Access denied`, check the username and password in `application.properties`.
- If Eclipse reports a Java version error, set the project JRE to JDK 17 or later and update the Maven project.
