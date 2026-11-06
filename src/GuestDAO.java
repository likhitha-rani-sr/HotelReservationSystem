import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    private Connection connection;

    public GuestDAO() {
        try {
            connection = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String addGuest(Guest guest) {

        String query =
                "INSERT INTO Guest (name, email, phone) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setString(1, guest.getName());
            statement.setString(2, guest.getEmail());
            statement.setString(3, guest.getPhone());

            statement.executeUpdate();

            return "Guest added successfully.";

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                return "Email or phone number already exists.";
            }

            return "Unable to add guest: " + e.getMessage();
        }
    }

    public Guest getGuestById(int id) {

        String query =
                "SELECT * FROM Guest WHERE guest_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, id);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return new Guest(
                        resultSet.getInt("guest_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Guest> getAllGuests() {

        List<Guest> guests = new ArrayList<>();

        String query =
                "SELECT * FROM Guest ORDER BY guest_id";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                guests.add(
                        new Guest(
                                resultSet.getInt("guest_id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("phone")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guests;
    }

    // Guest + Reservation + Room + Hotel
    public List<Object[]> getAllGuestsWithReservations() {

        List<Object[]> guests = new ArrayList<>();

        String query =
                "SELECT g.guest_id, g.name, g.email, g.phone, " +
                "rsv.reservation_id, " +
                "r.room_number, " +
                "h.name AS hotel_name " +
                "FROM Guest g " +
                "LEFT JOIN Reservation rsv " +
                "ON g.guest_id = rsv.guest_id " +
                "LEFT JOIN Room r " +
                "ON rsv.room_id = r.room_id " +
                "LEFT JOIN Hotel h " +
                "ON r.hotel_id = h.hotel_id " +
                "ORDER BY g.guest_id, rsv.reservation_id";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Object reservationId =
                        resultSet.getObject("reservation_id");

                Object roomNumber =
                        resultSet.getObject("room_number");

                String hotelName =
                        resultSet.getString("hotel_name");

                if (reservationId == null) {
                    reservationId = "-";
                }

                if (roomNumber == null) {
                    roomNumber = "-";
                }

                if (hotelName == null) {
                    hotelName = "-";
                }

                guests.add(new Object[]{
                        resultSet.getInt("guest_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        reservationId,
                        roomNumber,
                        hotelName
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guests;
    }

    public void updateGuest(Guest guest) {

        String query =
                "UPDATE Guest SET name = ?, email = ?, phone = ? " +
                "WHERE guest_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setString(1, guest.getName());
            statement.setString(2, guest.getEmail());
            statement.setString(3, guest.getPhone());
            statement.setInt(4, guest.getId());

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Guest updated successfully.");
            } else {
                System.out.println("Guest not found.");
            }

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                System.out.println(
                        "Email or phone number already exists."
                );
            } else {
                e.printStackTrace();
            }
        }
    }

    public void deleteGuest(int id) {

        String query =
                "DELETE FROM Guest WHERE guest_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, id);

            int rowsDeleted =
                    statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Guest deleted successfully.");
            } else {
                System.out.println("Guest not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}