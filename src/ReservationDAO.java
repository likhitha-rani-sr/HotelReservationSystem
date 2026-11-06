import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // ADD RESERVATION
    public void addReservation(Reservation reservation) {

        String overlapQuery =
                "SELECT COUNT(*) FROM Reservation " +
                "WHERE room_id = ? " +
                "AND check_in_date < ? " +
                "AND check_out_date > ?";

        String insertQuery =
                "INSERT INTO Reservation " +
                "(room_id, guest_id, check_in_date, check_out_date) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement checkStmt = con.prepareStatement(overlapQuery)) {

            checkStmt.setInt(1, reservation.getRoomId());
            checkStmt.setDate(2, new java.sql.Date(
                    reservation.getCheckOutDate().getTime()));
            checkStmt.setDate(3, new java.sql.Date(
                    reservation.getCheckInDate().getTime()));

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new RuntimeException(
                        "Room is already booked for the selected dates.");
            }

            try (PreparedStatement stmt =
                         con.prepareStatement(insertQuery)) {

                stmt.setInt(1, reservation.getRoomId());
                stmt.setInt(2, reservation.getGuestId());
                stmt.setDate(3, new java.sql.Date(
                        reservation.getCheckInDate().getTime()));
                stmt.setDate(4, new java.sql.Date(
                        reservation.getCheckOutDate().getTime()));

                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error adding reservation: " + e.getMessage(), e);
        }
    }

    // GET RESERVATION BY ID
    public Reservation getReservationById(int id) {

        String query =
                "SELECT reservation_id, room_id, guest_id, " +
                "check_in_date, check_out_date " +
                "FROM Reservation WHERE reservation_id = ?";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("room_id"),
                        rs.getInt("guest_id"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error retrieving reservation: " + e.getMessage(), e);
        }

        return null;
    }

    // GET ALL RESERVATIONS
    public List<Reservation> getAllReservations() {

        List<Reservation> reservations = new ArrayList<>();

        String query =
                "SELECT reservation_id, room_id, guest_id, " +
                "check_in_date, check_out_date " +
                "FROM Reservation " +
                "ORDER BY reservation_id";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                reservations.add(
                        new Reservation(
                                rs.getInt("reservation_id"),
                                rs.getInt("room_id"),
                                rs.getInt("guest_id"),
                                rs.getDate("check_in_date"),
                                rs.getDate("check_out_date")
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error retrieving reservations: "
                            + e.getMessage(), e);
        }

        return reservations;
    }

    // GET RESERVATIONS WITH DETAILS
    public List<Object[]> getAllReservationsWithDetails() {

        List<Object[]> list = new ArrayList<>();

        String query =
                "SELECT rsv.reservation_id, " +
                "g.name AS guest_name, " +
                "r.room_number, " +
                "h.name AS hotel_name, " +
                "rsv.check_in_date, " +
                "rsv.check_out_date " +
                "FROM Reservation rsv " +
                "INNER JOIN Guest g " +
                "ON rsv.guest_id = g.guest_id " +
                "INNER JOIN Room r " +
                "ON rsv.room_id = r.room_id " +
                "INNER JOIN Hotel h " +
                "ON r.hotel_id = h.hotel_id " +
                "ORDER BY rsv.reservation_id";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                list.add(new Object[]{
                        rs.getInt("reservation_id"),
                        rs.getString("guest_name"),
                        rs.getInt("room_number"),
                        rs.getString("hotel_name"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date")
                });
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error retrieving reservation details: "
                            + e.getMessage(), e);
        }

        return list;
    }

    // UPDATE RESERVATION
    public void updateReservation(Reservation reservation) {

        String overlapQuery =
                "SELECT COUNT(*) FROM Reservation " +
                "WHERE room_id = ? " +
                "AND reservation_id <> ? " +
                "AND check_in_date < ? " +
                "AND check_out_date > ?";

        String updateQuery =
                "UPDATE Reservation SET " +
                "room_id = ?, " +
                "guest_id = ?, " +
                "check_in_date = ?, " +
                "check_out_date = ? " +
                "WHERE reservation_id = ?";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement checkStmt =
                     con.prepareStatement(overlapQuery)) {

            checkStmt.setInt(1, reservation.getRoomId());
            checkStmt.setInt(2, reservation.getId());
            checkStmt.setDate(3, new java.sql.Date(
                    reservation.getCheckOutDate().getTime()));
            checkStmt.setDate(4, new java.sql.Date(
                    reservation.getCheckInDate().getTime()));

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new RuntimeException(
                        "Room is already booked for the selected dates.");
            }

            try (PreparedStatement stmt =
                         con.prepareStatement(updateQuery)) {

                stmt.setInt(1, reservation.getRoomId());
                stmt.setInt(2, reservation.getGuestId());
                stmt.setDate(3, new java.sql.Date(
                        reservation.getCheckInDate().getTime()));
                stmt.setDate(4, new java.sql.Date(
                        reservation.getCheckOutDate().getTime()));
                stmt.setInt(5, reservation.getId());

                int rows = stmt.executeUpdate();

                if (rows == 0) {
                    throw new RuntimeException(
                            "Reservation ID "
                                    + reservation.getId()
                                    + " was not found.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating reservation: "
                            + e.getMessage(), e);
        }
    }

    // DELETE / CANCEL RESERVATION
    public void deleteReservation(int reservationId) {

        String query =
                "DELETE FROM Reservation WHERE reservation_id = ?";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, reservationId);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException(
                        "Reservation ID "
                                + reservationId
                                + " was not found.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error deleting reservation: "
                            + e.getMessage(), e);
        }
    }

    // BILLING DETAILS
    public Object[] getReservationBillingDetails(int reservationId) {

        String query =
                "SELECT rsv.reservation_id, " +
                "g.name AS guest_name, " +
                "g.email, " +
                "g.phone, " +
                "r.room_id, " +
                "r.room_number, " +
                "r.type, " +
                "r.price, " +
                "h.name AS hotel_name, " +
                "rsv.check_in_date, " +
                "rsv.check_out_date " +
                "FROM Reservation rsv " +
                "INNER JOIN Guest g " +
                "ON rsv.guest_id = g.guest_id " +
                "INNER JOIN Room r " +
                "ON rsv.room_id = r.room_id " +
                "INNER JOIN Hotel h " +
                "ON r.hotel_id = h.hotel_id " +
                "WHERE rsv.reservation_id = ?";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, reservationId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Object[]{
                        rs.getInt("reservation_id"),
                        rs.getString("guest_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("room_id"),
                        rs.getInt("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getString("hotel_name"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date")
                };
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error retrieving billing details: "
                            + e.getMessage(), e);
        }

        return null;
    }
}