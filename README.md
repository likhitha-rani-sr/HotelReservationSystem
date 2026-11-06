# Hotel Reservation System

## Project Overview

The **Hotel Reservation System** is a Java-based desktop application developed using **Java Swing, JDBC, and MySQL**.

The application provides a graphical interface for managing hotels, rooms, guests, and reservations. It also provides functionality for searching, updating, deleting, checking room availability, viewing customer billing details, and generating invoices.

The system stores application data in a MySQL relational database and uses DAO (Data Access Object) classes to separate database operations from the graphical user interface.

---

## Objectives

- To develop a hotel management application using Java.
- To create a graphical user interface using Java Swing.
- To connect Java with MySQL using JDBC.
- To manage hotel, room, guest, and reservation information.
- To maintain relationships between database tables.
- To validate user input.
- To prevent duplicate records.
- To prevent overlapping room reservations.
- To provide search functionality.
- To provide update and delete operations.
- To check room availability.
- To provide customer billing information.
- To generate reservation invoices.
- To maintain data integrity using primary keys, foreign keys, and unique constraints.

---

## Technologies Used

| Technology | Purpose |
| Java       | Application development |
| Java Swing | Graphical User Interface |
| JDBC       | Database connectivity |
| MySQL      | Database management |
| MySQL Connector/J 8.0.33 | Java-MySQL connectivity |
| VS Code    |   Development environment |
| MySQL Workbench | Database creation and management |

---

# Main Features

## 1. Hotel Management

The system allows the user to add, retrieve, update, delete, and search hotel information.

Hotel details include:

- Hotel Name
- Location

The system prevents duplicate hotels having the same name and location.

---

## 2. Room Management

The system allows the user to add, retrieve, update, delete, and search rooms.

Room details include:

- Hotel
- Room Number
- Room Type
- Price
- Room Status

Available room types:

- Single
- Double
- Suite

Available room statuses:

- Available
- Occupied
- Maintenance

The system prevents the same room number from being added twice within the same hotel.

---

## 3. Guest Management

The system allows the user to add, retrieve, update, delete, and search guest information.

Guest details include:

- Guest Name
- Email
- Phone Number

The system validates guest information and prevents duplicate email addresses and phone numbers.

---

## 4. Reservation Management

The system allows the user to create, retrieve, update, delete, and search reservations.

Reservation details include:

- Room
- Guest
- Check-in Date
- Check-out Date

The system validates reservation dates and prevents overlapping reservations for the same room.

---

## 5. Search Functionality

The application provides search functionality for:

- Hotels
- Rooms
- Guests
- Reservations

The user can search and retrieve relevant information from the database through the graphical interface.

---

## 6. Update Functionality

The system provides update operations for:

- Hotels
- Rooms
- Guests
- Reservations

Existing records can be modified through the GUI while maintaining validation and database constraints.

---

## 7. Delete Functionality

The system provides delete operations for:

- Hotels
- Rooms
- Guests
- Reservations

The application handles database-related errors and maintains referential integrity.

---

## 8. Room Availability Search

The application provides a room availability search feature.

The user can select the required dates and check rooms that are available during that period.

The system checks existing reservations and prevents rooms with conflicting reservations from being treated as available.

---

## 9. Customer Billing

The application provides customer billing information based on a reservation.

Billing information includes:

- Reservation ID
- Customer Name
- Email
- Phone
- Room Number
- Room Type
- Room Price
- Hotel
- Check-in Date
- Check-out Date
- Number of Nights
- Total Amount

The number of nights is calculated using the check-in and check-out dates.

---

## 10. Invoice Generation

The system provides invoice generation for reservations.

The generated invoice contains reservation and customer information along with room details, stay duration, and billing amount.

The invoice can be saved for future reference.

---

# Application Interface

The main application window provides access to the following operations:

- Add Hotel
- Add Room
- Add Guest
- Add Reservation
- Retrieve Data
- Search
- Update
- Delete
- Room Availability
- Customer Billing
- Invoice Generation

The application uses a simple graphical interface developed using Java Swing.

---

# Database

Database name:

```text
hotel_reservation
The database contains four main tables:

Hotel
Room
Guest
Reservation
Database Tables
Hotel Table
hotel_id → Primary Key
name + location → Unique

Fields:

hotel_id
name
location

hotel_id is automatically generated using AUTO_INCREMENT.

Room Table
room_id → Primary Key
hotel_id → Foreign Key
hotel_id + room_number → Unique

Fields:

room_id
hotel_id
room_number
type
price
status

room_id is automatically generated using AUTO_INCREMENT.

hotel_id is a foreign key referencing the Hotel table.

The combination of hotel ID and room number is unique.

Guest Table
guest_id → Primary Key
email → Unique
phone → Unique

Fields:

guest_id
name
email
phone

guest_id is automatically generated using AUTO_INCREMENT.

Reservation Table
reservation_id → Primary Key
room_id → Foreign Key
guest_id → Foreign Key

Fields:

reservation_id
room_id
guest_id
check_in_date
check_out_date

reservation_id is automatically generated using AUTO_INCREMENT.

room_id is a foreign key referencing the Room table.

guest_id is a foreign key referencing the Guest table.

Database Relationships
             Hotel
               |
               | 1 : Many
               v
              Room
               |
               | 1 : Many
               v
          Reservation
             ^     ^
             |     |
       Many : 1    |
             |     |
           Guest

The relationships are:

One hotel can have multiple rooms.
Each room belongs to one hotel.
One guest can have multiple reservations.
Each reservation belongs to one guest.
One room can have multiple reservations on different dates.
Each reservation belongs to one room.
AUTO_INCREMENT

The following IDs are automatically generated by MySQL:

hotel_id
room_id
guest_id
reservation_id

The Java application does not manually generate these IDs.

Reservation Overlap Prevention

The system checks whether a room already has a reservation for the selected dates.

For example, if a room already has:

Check-in: 2026-09-21
Check-out: 2026-09-25

The system prevents another reservation for the same room when the requested reservation overlaps with the existing reservation period.

This helps prevent double booking.

Input Validation
Hotel Validation
Hotel name cannot be empty.
Location cannot be empty.
Duplicate hotel name and location are not allowed.
Room Validation
A hotel must be selected.
Room number must be valid.
Room price must be positive.
Room type must be selected.
Room status must be selected.
Duplicate room number within the same hotel is not allowed.
Guest Validation
Guest name cannot be empty.
Email must have a valid format.
Phone number must contain 10 digits.
Duplicate email is not allowed.
Duplicate phone number is not allowed.
Reservation Validation
A room must be selected.
A guest must be selected.
Check-in date must be valid.
Check-out date must be valid.
Check-out date must be after check-in date.
Overlapping reservations are not allowed.
Project Structure
HotelReservationSystem/
│
├── src/
│   ├── Main.java
│   ├── DatabaseConnector.java
│   ├── Hotel.java
│   ├── HotelDAO.java
│   ├── Room.java
│   ├── RoomDAO.java
│   ├── Guest.java
│   ├── GuestDAO.java
│   ├── Reservation.java
│   └── ReservationDAO.java
│
├── lib/
│   └── mysql-connector-j-8.0.33.jar
│
└── README.md
Java Classes
Main.java

Responsible for:

Creating the graphical user interface.
Displaying the main application window.
Adding hotels.
Adding rooms.
Adding guests.
Creating reservations.
Retrieving data.
Searching records.
Updating records.
Deleting records.
Checking room availability.
Displaying customer billing information.
Generating invoices.
Performing input validation.
DatabaseConnector.java

Responsible for establishing the connection between the Java application and the MySQL database using JDBC.

Hotel.java

Represents hotel information such as:

Hotel ID
Hotel Name
Location
HotelDAO.java

Handles database operations related to hotels, including adding, retrieving, updating, deleting, and searching hotel records.

Room.java

Represents room information such as:

Room ID
Hotel ID
Room Number
Room Type
Price
Status
RoomDAO.java

Handles database operations related to rooms.

Guest.java

Represents guest information such as:

Guest ID
Name
Email
Phone
GuestDAO.java

Handles database operations related to guests.

Reservation.java

Represents reservation information such as:

Reservation ID
Room ID
Guest ID
Check-in Date
Check-out Date
ReservationDAO.java

Handles database operations related to reservations, including:

Adding reservations
Retrieving reservations
Updating reservations
Deleting reservations
Checking reservation conflicts
Retrieving reservation billing details
DAO Architecture
              User
                |
                v
            Main.java
                |
                v
          DAO Classes
                |
                v
       DatabaseConnector
                |
                v
        MySQL Database
DAO Classes
HotelDAO
RoomDAO
GuestDAO
ReservationDAO

The DAO architecture separates database operations from the graphical user interface.

This makes the application more organized and easier to maintain.

Application Workflow
        Add Hotel
            |
            v
        Add Room
            |
            v
        Add Guest
            |
            v
    Add Reservation
            |
            v
     Retrieve / Search
            |
            v
       Update / Delete
            |
            v
   Room Availability
            |
            v
    Customer Billing
            |
            v
    Invoice Generation
Example Workflow
Hotel
Name: Taj
Location: Mumbai
Room
Room No: 501
Type: Single
Price: 3999
Status: Available
Guest
Name: Guest Name
Email: guest@gmail.com
Phone: 9876543210
Reservation
Room: 501
Guest: Guest Name
Check-in: 2026-09-25
Check-out: 2026-09-27
Error Handling

The application handles common errors such as:

Empty fields
Invalid email
Invalid phone number
Invalid room number
Invalid price
Invalid dates
Check-out date before check-in date
Duplicate hotel
Duplicate room
Duplicate guest email
Duplicate guest phone
Overlapping room reservation
Database errors

Error messages are displayed through the graphical interface.

Advantages
Simple graphical user interface.
Easy hotel management.
Easy room management.
Easy guest management.
Easy reservation management.
Search functionality.
Update functionality.
Delete functionality.
Room availability checking.
Customer billing.
Invoice generation.
MySQL database storage.
Automatic ID generation.
Duplicate data prevention.
Reservation overlap prevention.
Input validation.
Relational database design.
DAO-based architecture.
Data integrity using database constraints.
Future Enhancements

The system can be further enhanced with:

Admin login and role-based access.
Reservation history and advanced reporting.
Payment integration.
Online booking support.
Email or SMS reservation notifications.
Improved invoice customization.
Dashboard and analytics.
Cloud database integration.
Conclusion

The Hotel Reservation System demonstrates the development of a database-driven desktop application using Java Swing, JDBC, and MySQL.

The system provides functionality for managing hotels, rooms, guests, and reservations. It also supports searching, updating, deleting records, checking room availability, customer billing, and invoice generation.

The project demonstrates the practical application of:

Java programming
Object-Oriented Programming
Java Swing
JDBC
SQL
MySQL
Relational database concepts
Primary and foreign keys
Database constraints
Input validation
DAO architecture
GUI development
Database-driven application development
Project Information
Information	Details
Project Name	Hotel Reservation System
Programming Language	Java
GUI Framework	Java Swing
Database	MySQL
Database Connectivity	JDBC
Development Environment	VS Code
Database Management Tool	MySQL Workbench
Connector	MySQL Connector/J 8.0.33
Application Type	Desktop Application
Architecture	DAO-based Architecture
Project Type	Academic Project
Setup Requirements

The following software is required:

JDK
MySQL Server
MySQL Workbench
VS Code
Java Extension Pack for VS Code
MySQL Connector/J 8.0.33
Database Setup

Open MySQL Workbench and create the database:

CREATE DATABASE hotel_reservation;

Select the database:

USE hotel_reservation;

Create the required tables and constraints for the project.

The database should contain:

Hotel
Room
Guest
Reservation
Database Connection

Open DatabaseConnector.java and configure the MySQL connection according to your MySQL installation.

Example:

String url = "jdbc:mysql://localhost:3306/hotel_reservation";
String username = "root";
String password = "your_password";

Replace:

your_password

with the password of your MySQL account.

MySQL Connector

The project uses:

mysql-connector-j-8.0.33.jar

The JAR file must be added to the Java project's classpath.

How to Run
Open the HotelReservationSystem project in VS Code.
Make sure MySQL Server is running.
Open MySQL Workbench.
Make sure the hotel_reservation database exists.
Create the required database tables.
Check the database username and password in DatabaseConnector.java.
Make sure MySQL Connector/J is added to the project.
Open Main.java.
Run Main.java.
The Hotel Reservation System GUI will open.
Project Type

This project is developed as an Academic Project to demonstrate practical knowledge of:

Java
Object-Oriented Programming
Java Swing
JDBC
MySQL
SQL
Database Management
GUI Development
DAO Architecture
CRUD Operations
Database Relationships