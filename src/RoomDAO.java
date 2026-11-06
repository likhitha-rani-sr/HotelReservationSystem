import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private Connection connection;

    public RoomDAO() {
        try {
            connection = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String addRoom(Room room) {

        String query =
                "INSERT INTO Room (hotel_id, room_number, type, price, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, room.getHotelId());
            statement.setInt(2, room.getRoomNumber());
            statement.setString(3, room.getType());
            statement.setDouble(4, room.getPrice());
            statement.setString(5, room.getStatus());

            statement.executeUpdate();

            return "Room added successfully.";

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                return "Room number " + room.getRoomNumber()
                        + " already exists in this hotel.";
            }

            if (e.getErrorCode() == 1452) {
                return "Selected hotel does not exist.";
            }

            return "Unable to add room: " + e.getMessage();
        }
    }

    public Room getRoomById(int id) {

        String query =
                "SELECT * FROM Room WHERE room_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, id);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return new Room(
                        resultSet.getInt("room_id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getInt("room_number"),
                        resultSet.getString("type"),
                        resultSet.getDouble("price"),
                        resultSet.getString("status")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Room> getAllRooms() {

        List<Room> rooms = new ArrayList<>();

        String query =
                "SELECT * FROM Room ORDER BY room_id";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                rooms.add(
                        new Room(
                                resultSet.getInt("room_id"),
                                resultSet.getInt("hotel_id"),
                                resultSet.getInt("room_number"),
                                resultSet.getString("type"),
                                resultSet.getDouble("price"),
                                resultSet.getString("status")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    // Rooms + Hotel Name
    public List<Object[]> getAllRoomsWithHotel() {

        List<Object[]> rooms = new ArrayList<>();

        String query =
                "SELECT r.room_id, r.hotel_id, h.name AS hotel_name, " +
                "r.room_number, r.type, r.price, r.status " +
                "FROM Room r " +
                "INNER JOIN Hotel h ON r.hotel_id = h.hotel_id " +
                "ORDER BY r.room_id";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                rooms.add(new Object[]{
                        resultSet.getInt("room_id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getString("hotel_name"),
                        resultSet.getInt("room_number"),
                        resultSet.getString("type"),
                        resultSet.getDouble("price"),
                        resultSet.getString("status")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public void updateRoom(Room room) {

        String query =
                "UPDATE Room SET hotel_id = ?, room_number = ?, " +
                "type = ?, price = ?, status = ? " +
                "WHERE room_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, room.getHotelId());
            statement.setInt(2, room.getRoomNumber());
            statement.setString(3, room.getType());
            statement.setDouble(4, room.getPrice());
            statement.setString(5, room.getStatus());
            statement.setInt(6, room.getId());

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Room updated successfully.");
            } else {
                System.out.println("Room not found.");
            }

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                System.out.println(
                        "Room number already exists in this hotel."
                );
            } else {
                e.printStackTrace();
            }
        }
    }

    public void deleteRoom(int id) {

        String query =
                "DELETE FROM Room WHERE room_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, id);

            int rowsDeleted =
                    statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Room deleted successfully.");
            } else {
                System.out.println("Room not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}