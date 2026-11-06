import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    private Connection connection;

    public HotelDAO() {
        try {
            connection = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add Hotel
    public String addHotel(Hotel hotel) {

        String query =
                "INSERT INTO Hotel (name, location) VALUES (?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getLocation());

            statement.executeUpdate();

            return "Hotel added successfully.";

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                return "A hotel with the same name and location already exists.";
            }

            return "Unable to add hotel: " + e.getMessage();
        }
    }

    // Get Hotel by ID
    public Hotel getHotelById(int id) {

        String query =
                "SELECT * FROM Hotel WHERE hotel_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return new Hotel(
                        resultSet.getInt("hotel_id"),
                        resultSet.getString("name"),
                        resultSet.getString("location")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get All Hotels
    public List<Hotel> getAllHotels() {

        List<Hotel> hotels = new ArrayList<>();

        String query =
                "SELECT * FROM Hotel ORDER BY hotel_id";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                hotels.add(
                        new Hotel(
                                resultSet.getInt("hotel_id"),
                                resultSet.getString("name"),
                                resultSet.getString("location")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotels;
    }

    // Update Hotel
    public void updateHotel(Hotel hotel) {

        String query =
                "UPDATE Hotel SET name = ?, location = ? " +
                "WHERE hotel_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getLocation());
            statement.setInt(3, hotel.getId());

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Hotel updated successfully.");
            } else {
                System.out.println("Hotel not found.");
            }

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                System.out.println(
                        "A hotel with the same name and location already exists."
                );
            } else {
                e.printStackTrace();
            }
        }
    }

    // Delete Hotel
    public void deleteHotel(int id) {

        String query =
                "DELETE FROM Hotel WHERE hotel_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, id);

            int rowsDeleted =
                    statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Hotel deleted successfully.");
            } else {
                System.out.println("Hotel not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}