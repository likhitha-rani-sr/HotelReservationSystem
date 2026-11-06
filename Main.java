import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private HotelDAO hotelDAO;
    private RoomDAO roomDAO;
    private GuestDAO guestDAO;
    private ReservationDAO reservationDAO;

    // Colors
    private static final Color BACKGROUND =
            new Color(245, 248, 252);

    private static final Color HEADER =
            new Color(39, 70, 100);

    private static final Color BUTTON =
            new Color(220, 232, 244);

    private static final Color BUTTON_HOVER =
            new Color(205, 220, 235);

    private static final Color TEXT =
            new Color(25, 35, 45);

    public Main() {

        hotelDAO = new HotelDAO();
        roomDAO = new RoomDAO();
        guestDAO = new GuestDAO();
        reservationDAO = new ReservationDAO();

        createGUI();
    }

    private void createGUI() {

        JFrame frame =
                new JFrame("Hotel Reservation System");

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setSize(800, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);


        // =====================================================
        // MAIN PANEL
        // =====================================================

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(BACKGROUND);


        // =====================================================
        // HEADER
        // =====================================================

        JPanel headerPanel =
                new JPanel();

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        headerPanel.setBackground(HEADER);

        headerPanel.setBorder(
                new EmptyBorder(
                        35,
                        20,
                        32,
                        20
                )
        );


        JLabel title =
                new JLabel(
                        "  HOTEL RESERVATION SYSTEM "
                );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        29
                )
        );


        JLabel subtitle =
                new JLabel(
                        "Hotel Management & Reservation"
                );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        subtitle.setForeground(
                new Color(225, 235, 245)
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        17
                )
        );


        headerPanel.add(title);

        headerPanel.add(
                Box.createVerticalStrut(8)
        );

        headerPanel.add(subtitle);


        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );


        // =====================================================
        // CENTER PANEL
        // =====================================================

        JPanel centerPanel =
                new JPanel(new BorderLayout());

        centerPanel.setBackground(BACKGROUND);


        // =====================================================
        // SEARCH PANEL
        // Search is directly below the header for quick access.
        // =====================================================

        JPanel searchPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.CENTER,
                        10,
                        10
                ));

        searchPanel.setBackground(BACKGROUND);

        JLabel searchLabel =
                new JLabel("Search:");

        searchLabel.setForeground(TEXT);
        searchLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        JComboBox<String> searchTypeCombo =
                new JComboBox<>(
                        new String[]{
                                "Hotels",
                                "Rooms",
                                "Guests",
                                "Reservations"
                        }
                );

        searchTypeCombo.setPreferredSize(
                new Dimension(125, 38)
        );

        JTextField searchField =
                new JTextField();

        searchField.setPreferredSize(
                new Dimension(300, 38)
        );

        searchField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        JButton searchButton =
                createRetrieveButton("Search");

        searchButton.setPreferredSize(
                new Dimension(105, 38)
        );

        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        centerPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );


        // =====================================================
        // BUTTON PANEL
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new GridBagLayout()
                );

        buttonPanel.setBackground(
                BACKGROUND
        );

        buttonPanel.setBorder(
                new EmptyBorder(
                        5,
                        70,
                        10,
                        70
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.BOTH;

        gbc.insets =
                new Insets(
                        15,
                        15,
                        15,
                        15
                );

        gbc.weightx = 1;
        gbc.weighty = 0;


        // =====================================================
        // BUTTONS
        // =====================================================

        JButton addHotelButton =
                createButton(
                        "🏨",
                        "Add Hotel"
                );

        JButton addRoomButton =
                createButton(
                        "🛏",
                        "Add Room"
                );

        JButton addGuestButton =
                createButton(
                        "👤",
                        "Add Guest"
                );

        JButton addReservationButton =
                createButton(
                        "📅",
                        "Add Reservation"
                );

        JButton retrieveButton =
                createButton(
                        "🔍",
                        "Manage Data"
                );

        JButton servicesButton =
                createButton(
                        "⚙",
                        "Services"
                );


        // =====================================================
        // ADD HOTEL
        // =====================================================

        addHotelButton.addActionListener(e -> {

            String name =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter hotel name:"
                    );

            if (name == null) {
                return;
            }

            String location =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter hotel location:"
                    );

            if (location == null) {
                return;
            }

            name = name.trim();
            location = location.trim();

            if (name.isEmpty() ||
                    location.isEmpty()) {

                showError(
                        frame,
                        "Hotel name and location cannot be empty."
                );

                return;
            }

            Hotel hotel =
                    new Hotel(
                            name,
                            location
                    );

            String message =
                    hotelDAO.addHotel(hotel);

            showMessage(frame, message);
        });


        // =====================================================
        // ADD ROOM
        // =====================================================

        addRoomButton.addActionListener(e -> {

            List<Hotel> hotels =
                    hotelDAO.getAllHotels();

            if (hotels.isEmpty()) {

                showError(
                        frame,
                        "No hotels found.\nPlease add a hotel first."
                );

                return;
            }


            JComboBox<Hotel> hotelComboBox =
                    new JComboBox<>(
                            hotels.toArray(
                                    new Hotel[0]
                            )
                    );

            hotelComboBox.setPreferredSize(
                    new Dimension(350, 35)
            );


            int result =
                    JOptionPane.showConfirmDialog(
                            frame,
                            hotelComboBox,
                            "Select Hotel",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }


            Hotel selectedHotel =
                    (Hotel)
                            hotelComboBox.getSelectedItem();

            if (selectedHotel == null) {
                return;
            }


            int hotelId =
                    selectedHotel.getId();


            String roomNumberText =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter room number:"
                    );

            if (roomNumberText == null) {
                return;
            }


            int roomNumber;

            try {

                roomNumber =
                        Integer.parseInt(
                                roomNumberText.trim()
                        );

                if (roomNumber <= 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException ex) {

                showError(
                        frame,
                        "Room number must be a valid positive number."
                );

                return;
            }


            String[] roomTypes = {
                    "Single",
                    "Double",
                    "Suite"
            };

            String type =
                    (String)
                            JOptionPane.showInputDialog(
                                    frame,
                                    "Select room type:",
                                    "Room Type",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    roomTypes,
                                    roomTypes[0]
                            );

            if (type == null) {
                return;
            }


            String priceText =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter room price:"
                    );

            if (priceText == null) {
                return;
            }


            double price;

            try {

                price =
                        Double.parseDouble(
                                priceText.trim()
                        );

                if (price <= 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException ex) {

                showError(
                        frame,
                        "Price must be greater than 0."
                );

                return;
            }


            String[] statuses = {
                    "Available",
                    "Occupied",
                    "Maintenance"
            };

            String status =
                    (String)
                            JOptionPane.showInputDialog(
                                    frame,
                                    "Select room status:",
                                    "Room Status",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    statuses,
                                    statuses[0]
                            );

            if (status == null) {
                return;
            }


            Room room =
                    new Room(
                            hotelId,
                            roomNumber,
                            type,
                            price,
                            status
                    );


            String message =
                    roomDAO.addRoom(room);

            showMessage(frame, message);
        });


        // =====================================================
        // ADD GUEST
        // =====================================================

        addGuestButton.addActionListener(e -> {

            String name =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter guest name:"
                    );

            if (name == null) {
                return;
            }

            name = name.trim();

            if (name.isEmpty()) {

                showError(
                        frame,
                        "Guest name cannot be empty."
                );

                return;
            }


            String email =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter email:"
                    );

            if (email == null) {
                return;
            }

            email = email.trim();


            if (!email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                showError(
                        frame,
                        "Please enter a valid email address."
                );

                return;
            }


            String phone =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter phone number:"
                    );

            if (phone == null) {
                return;
            }

            phone = phone.trim();


            if (!phone.matches("\\d{10}")) {

                showError(
                        frame,
                        "Phone number must contain exactly 10 digits."
                );

                return;
            }


            Guest guest =
                    new Guest(
                            name,
                            email,
                            phone
                    );


            String message =
                    guestDAO.addGuest(guest);

            showMessage(frame, message);
        });


        // =====================================================
        // ADD RESERVATION
        // =====================================================

        addReservationButton.addActionListener(e -> {

            List<Room> rooms =
                    roomDAO.getAllRooms();

            if (rooms.isEmpty()) {

                showError(
                        frame,
                        "No rooms found.\nPlease add a room first."
                );

                return;
            }


            JComboBox<Room> roomComboBox =
                    new JComboBox<>(
                            rooms.toArray(
                                    new Room[0]
                            )
                    );

            roomComboBox.setPreferredSize(
                    new Dimension(370, 35)
            );


            int roomResult =
                    JOptionPane.showConfirmDialog(
                            frame,
                            roomComboBox,
                            "Select Room",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (roomResult != JOptionPane.OK_OPTION) {
                return;
            }


            Room selectedRoom =
                    (Room)
                            roomComboBox.getSelectedItem();

            if (selectedRoom == null) {
                return;
            }


            int roomId =
                    selectedRoom.getId();


            List<Guest> guests =
                    guestDAO.getAllGuests();

            if (guests.isEmpty()) {

                showError(
                        frame,
                        "No guests found.\nPlease add a guest first."
                );

                return;
            }


            JComboBox<Guest> guestComboBox =
                    new JComboBox<>(
                            guests.toArray(
                                    new Guest[0]
                            )
                    );

            guestComboBox.setPreferredSize(
                    new Dimension(370, 35)
            );


            int guestResult =
                    JOptionPane.showConfirmDialog(
                            frame,
                            guestComboBox,
                            "Select Guest",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (guestResult != JOptionPane.OK_OPTION) {
                return;
            }


            Guest selectedGuest =
                    (Guest)
                            guestComboBox.getSelectedItem();

            if (selectedGuest == null) {
                return;
            }


            int guestId =
                    selectedGuest.getId();


            String checkInText =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter check-in date (yyyy-MM-dd):"
                    );

            if (checkInText == null) {
                return;
            }


            String checkOutText =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter check-out date (yyyy-MM-dd):"
                    );

            if (checkOutText == null) {
                return;
            }


            SimpleDateFormat format =
                    new SimpleDateFormat(
                            "yyyy-MM-dd"
                    );

            format.setLenient(false);


            Date checkIn;
            Date checkOut;


            try {

                checkIn =
                        format.parse(
                                checkInText.trim()
                        );

                checkOut =
                        format.parse(
                                checkOutText.trim()
                        );

            } catch (ParseException ex) {

                showError(
                        frame,
                        "Invalid date.\nUse yyyy-MM-dd format."
                );

                return;
            }


            if (!checkOut.after(checkIn)) {

                showError(
                        frame,
                        "Check-out date must be after check-in date."
                );

                return;
            }


            Reservation reservation =
                    new Reservation(
                            roomId,
                            guestId,
                            checkIn,
                            checkOut
                    );


            try {
                reservationDAO.addReservation(reservation);

                showMessage(
                        frame,
                        "Reservation added successfully."
                );

            } catch (Exception ex) {
                showError(frame,
                        "Failed to add reservation.\n\n"
                                + ex.getMessage());
            }
        });


        // =====================================================
        // SEARCH
        // =====================================================
        searchButton.addActionListener(e -> {

            String keyword =
                    searchField.getText().trim();

            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter a keyword to search.",
                        "Search",
                        JOptionPane.WARNING_MESSAGE
                );
                searchField.requestFocusInWindow();
                return;
            }

            String type =
                    (String) searchTypeCombo.getSelectedItem();

            performSearch(
                    frame,
                    type,
                    keyword
            );
        });

        searchField.addActionListener(e ->
                searchButton.doClick()
        );


        // =====================================================
        // RETRIEVE DATA
        // =====================================================
        retrieveButton.addActionListener(e ->
                showManageDataMenu(frame));

        servicesButton.addActionListener(e ->
                showServicesMenu(frame));
        

        // =====================================================
        // GRID LAYOUT
        // =====================================================

        // Add Hotel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;

        buttonPanel.add(
                addHotelButton,
                gbc
        );


        // Add Room
        gbc.gridx = 1;

        buttonPanel.add(
                addRoomButton,
                gbc
        );


        // Add Guest
        gbc.gridx = 0;
        gbc.gridy = 1;

        buttonPanel.add(
                addGuestButton,
                gbc
        );


        // Add Reservation
        gbc.gridx = 1;

        buttonPanel.add(
                addReservationButton,
                gbc
        );


        // Manage Data
        // Place Manage Data and Services vertically, one below the other.
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        retrieveButton.setPreferredSize(
                new Dimension(520, 60)
        );

        buttonPanel.add(
                retrieveButton,
                gbc
        );

        // Services
        gbc.gridy = 3;
        gbc.weighty = 0;

        servicesButton.setPreferredSize(
                new Dimension(520, 60)
        );

        buttonPanel.add(
                servicesButton,
                gbc
        );


        centerPanel.add(
                buttonPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        frame.setContentPane(
                mainPanel
        );

        frame.setVisible(true);
    }


    // =====================================================
    // SERVICES MENU
    // =====================================================

    private void showServicesMenu(JFrame frame) {

        JDialog dialog =
                new JDialog(
                        frame,
                        "Services",
                        true
                );

        dialog.setSize(430, 330);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);

        JPanel panel =
                new JPanel(
                        new GridLayout(3, 1, 12, 12)
                );

        panel.setBackground(BACKGROUND);
        panel.setBorder(
                new EmptyBorder(
                        30,
                        45,
                        30,
                        45
                )
        );

        JButton availabilityButton =
                createRetrieveButton("Room Availability");

        JButton billingButton =
                createRetrieveButton("Customer Billing");

        JButton invoiceButton =
                createRetrieveButton("Invoice Generation");

        panel.add(availabilityButton);
        panel.add(billingButton);
        panel.add(invoiceButton);

        availabilityButton.addActionListener(e -> {
            dialog.dispose();
            searchRoomAvailability(frame);
        });

        billingButton.addActionListener(e -> {
            dialog.dispose();
            showCustomerBilling(frame);
        });

        invoiceButton.addActionListener(e -> {
            dialog.dispose();
            generateInvoice(frame);
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }


    // =====================================================
    // MANAGE DATA MENU
    // =====================================================

    private void showManageDataMenu(JFrame frame) {

        JDialog dialog =
                new JDialog(
                        frame,
                        "Manage Data",
                        true
                );

        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);

        JPanel panel =
                new JPanel(
                        new GridLayout(3, 1, 10, 10)
                );

        panel.setBackground(BACKGROUND);

        panel.setBorder(
                new EmptyBorder(
                        25,
                        35,
                        25,
                        35
                )
        );

        JButton viewButton =
                createRetrieveButton("View Data");

        JButton updateButton =
                createRetrieveButton("Update Record");

        JButton deleteButton =
                createRetrieveButton("Delete Record");

        panel.add(viewButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        viewButton.addActionListener(e -> {
            dialog.dispose();
            showRetrieveMenu(frame);
        });

        updateButton.addActionListener(e -> {
            dialog.dispose();
            showUpdateMenu(frame);
        });

        deleteButton.addActionListener(e -> {
            dialog.dispose();
            showDeleteMenu(frame);
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }


    // =====================================================
    // VIEW DATA MENU
    // =====================================================

    private void showRetrieveMenu(JFrame frame) {

        JDialog dialog =
                new JDialog(
                        frame,
                        "View Data",
                        true
                );

        dialog.setSize(350, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);

        JPanel panel =
                new JPanel(
                        new GridLayout(4, 1, 10, 10)
                );

        panel.setBackground(BACKGROUND);

        panel.setBorder(
                new EmptyBorder(
                        25,
                        35,
                        25,
                        35
                )
        );

        JButton hotelsButton =
                createRetrieveButton("Hotels");

        JButton roomsButton =
                createRetrieveButton("Rooms");

        JButton guestsButton =
                createRetrieveButton("Guests");

        JButton reservationsButton =
                createRetrieveButton("Reservations");

        panel.add(hotelsButton);
        panel.add(roomsButton);
        panel.add(guestsButton);
        panel.add(reservationsButton);

        hotelsButton.addActionListener(
                e -> showHotels(frame)
        );

        roomsButton.addActionListener(
                e -> showRooms(frame)
        );

        guestsButton.addActionListener(
                e -> showGuests(frame)
        );

        reservationsButton.addActionListener(
                e -> showReservations(frame)
        );

        dialog.add(panel);
        dialog.setVisible(true);
    }


    // =====================================================
    // UPDATE DATA MENU
    // =====================================================

    private void showUpdateMenu(JFrame frame) {

        JDialog dialog =
                new JDialog(
                        frame,
                        "Update Data",
                        true
                );

        dialog.setSize(430, 360);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                5,
                                1,
                                12,
                                12
                        )
                );

        panel.setBackground(BACKGROUND);
        panel.setBorder(
                new EmptyBorder(
                        25,
                        40,
                        25,
                        40
                )
        );

        JLabel title =
                new JLabel(
                        "What do you want to update?",
                        SwingConstants.CENTER
                );

        title.setForeground(TEXT);
        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        JButton hotelButton =
                createRetrieveButton("Hotel");
        JButton roomButton =
                createRetrieveButton("Room");
        JButton guestButton =
                createRetrieveButton("Guest");
        JButton reservationButton =
                createRetrieveButton("Reservation");

        panel.add(title);
        panel.add(hotelButton);
        panel.add(roomButton);
        panel.add(guestButton);
        panel.add(reservationButton);

        hotelButton.addActionListener(
                e -> updateHotel(frame)
        );

        roomButton.addActionListener(
                e -> updateRoom(frame)
        );

        guestButton.addActionListener(
                e -> updateGuest(frame)
        );

        reservationButton.addActionListener(
                e -> updateReservation(frame)
        );

        dialog.add(panel);
        dialog.setVisible(true);
    }


    // =====================================================
    // DELETE DATA MENU
    // =====================================================

    private void showDeleteMenu(JFrame frame) {

        JDialog dialog =
                new JDialog(
                        frame,
                        "Delete Data",
                        true
                );

        dialog.setSize(430, 360);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                5,
                                1,
                                12,
                                12
                        )
                );

        panel.setBackground(BACKGROUND);
        panel.setBorder(
                new EmptyBorder(
                        25,
                        40,
                        25,
                        40
                )
        );

        JLabel title =
                new JLabel(
                        "What do you want to delete or cancel?",
                        SwingConstants.CENTER
                );

        title.setForeground(TEXT);
        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        JButton hotelButton =
                createRetrieveButton("Hotel");
        JButton roomButton =
                createRetrieveButton("Room");
        JButton guestButton =
                createRetrieveButton("Guest");
        JButton reservationButton =
                createRetrieveButton("Cancel Reservation");

        panel.add(title);
        panel.add(hotelButton);
        panel.add(roomButton);
        panel.add(guestButton);
        panel.add(reservationButton);

        hotelButton.addActionListener(
                e -> deleteHotel(frame)
        );

        roomButton.addActionListener(
                e -> deleteRoom(frame)
        );

        guestButton.addActionListener(
                e -> deleteGuest(frame)
        );

        reservationButton.addActionListener(
                e -> cancelReservation(frame)
        );

        dialog.add(panel);
        dialog.setVisible(true);
    }


    // =====================================================
    // DELETE HOTEL
    // =====================================================

    private void deleteHotel(JFrame frame) {

        String idText =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter Hotel ID to delete:"
                );

        if (idText == null) return;

        int id;

        try {
            id = Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            showError(frame, "Hotel ID must be a valid number.");
            return;
        }

        Hotel hotel = hotelDAO.getHotelById(id);

        if (hotel == null) {
            showError(frame, "Hotel not found.");
            return;
        }

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        "Delete hotel \"" + hotel.getName()
                                + " - " + hotel.getLocation()
                                + "\"?\n\n"
                                + "This cannot be deleted if rooms still belong to this hotel.",
                        "Confirm Hotel Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (result != JOptionPane.YES_OPTION) return;

        hotelDAO.deleteHotel(id);

        if (hotelDAO.getHotelById(id) == null) {
            showMessage(frame, "Hotel deleted successfully.");
        } else {
            showError(
                    frame,
                    "Hotel could not be deleted.\n"
                            + "Make sure no rooms are linked to this hotel."
            );
        }
    }


    // =====================================================
    // DELETE ROOM
    // =====================================================

    private void deleteRoom(JFrame frame) {

        String idText =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter Room ID to delete:"
                );

        if (idText == null) return;

        int id;

        try {
            id = Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            showError(frame, "Room ID must be a valid number.");
            return;
        }

        Room room = roomDAO.getRoomById(id);

        if (room == null) {
            showError(frame, "Room not found.");
            return;
        }

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        "Delete Room " + room.getRoomNumber()
                                + " (ID: " + room.getId() + ")?\n\n"
                                + "This cannot be deleted if reservations are linked to it.",
                        "Confirm Room Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (result != JOptionPane.YES_OPTION) return;

        roomDAO.deleteRoom(id);

        if (roomDAO.getRoomById(id) == null) {
            showMessage(frame, "Room deleted successfully.");
        } else {
            showError(
                    frame,
                    "Room could not be deleted.\n"
                            + "Make sure no reservations are linked to this room."
            );
        }
    }


    // =====================================================
    // DELETE GUEST
    // =====================================================

    private void deleteGuest(JFrame frame) {

        String idText =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter Guest ID to delete:"
                );

        if (idText == null) return;

        int id;

        try {
            id = Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            showError(frame, "Guest ID must be a valid number.");
            return;
        }

        Guest guest = guestDAO.getGuestById(id);

        if (guest == null) {
            showError(frame, "Guest not found.");
            return;
        }

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        "Delete guest \"" + guest.getName()
                                + "\" (ID: " + guest.getId() + ")?\n\n"
                                + "This cannot be deleted if reservations are linked to this guest.",
                        "Confirm Guest Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (result != JOptionPane.YES_OPTION) return;

        guestDAO.deleteGuest(id);

        if (guestDAO.getGuestById(id) == null) {
            showMessage(frame, "Guest deleted successfully.");
        } else {
            showError(
                    frame,
                    "Guest could not be deleted.\n"
                            + "Make sure no reservations are linked to this guest."
            );
        }
    }


    // =====================================================
    // CANCEL RESERVATION
    // =====================================================

    private void cancelReservation(JFrame frame) {

        String idText =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter Reservation ID to cancel:"
                );

        if (idText == null) return;

        int id;

        try {
            id = Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            showError(
                    frame,
                    "Reservation ID must be a valid number."
            );
            return;
        }

        Reservation reservation =
                reservationDAO.getReservationById(id);

        if (reservation == null) {
            showError(frame, "Reservation not found.");
            return;
        }

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd");

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        "Cancel Reservation ID " + id + "?\n\n"
                                + "Check-in: "
                                + dateFormat.format(
                                        reservation.getCheckInDate()
                                )
                                + "\nCheck-out: "
                                + dateFormat.format(
                                        reservation.getCheckOutDate()
                                )
                                + "\n\nThis reservation will be removed from the system.",
                        "Confirm Reservation Cancellation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (result != JOptionPane.YES_OPTION) return;

        // The current Reservation table has no status/cancelled column.
        // Therefore cancellation is implemented by deleting the reservation.
        reservationDAO.deleteReservation(id);

        if (reservationDAO.getReservationById(id) == null) {
            showMessage(
                    frame,
                    "Reservation cancelled successfully."
            );
        } else {
            showError(
                    frame,
                    "Reservation could not be cancelled."
            );
        }
    }


    // =====================================================
    // UPDATE HOTEL
    // =====================================================

    private void updateHotel(JFrame frame) {

        String idText =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter Hotel ID to update:"
                );

        if (idText == null) return;

        int id;

        try {
            id = Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            showError(frame, "Hotel ID must be a valid number.");
            return;
        }

        Hotel hotel = hotelDAO.getHotelById(id);

        if (hotel == null) {
            showError(frame, "Hotel not found.");
            return;
        }

        String name =
                JOptionPane.showInputDialog(
                        frame,
                        "Hotel name:",
                        hotel.getName()
                );

        if (name == null) return;

        String location =
                JOptionPane.showInputDialog(
                        frame,
                        "Hotel location:",
                        hotel.getLocation()
                );

        if (location == null) return;

        name = name.trim();
        location = location.trim();

        if (name.isEmpty() || location.isEmpty()) {
            showError(
                    frame,
                    "Hotel name and location cannot be empty."
            );
            return;
        }

        hotel.setName(name);
        hotel.setLocation(location);

        hotelDAO.updateHotel(hotel);

        showMessage(frame, "Hotel updated successfully.");
    }


    // =====================================================
    // UPDATE ROOM
    // =====================================================

    private void updateRoom(JFrame frame) {

        String idText =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter Room ID to update:"
                );

        if (idText == null) return;

        int id;

        try {
            id = Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            showError(frame, "Room ID must be a valid number.");
            return;
        }

        Room room = roomDAO.getRoomById(id);

        if (room == null) {
            showError(frame, "Room not found.");
            return;
        }

        List<Hotel> hotels = hotelDAO.getAllHotels();

        if (hotels.isEmpty()) {
            showError(frame, "No hotels are available.");
            return;
        }

        JComboBox<Hotel> hotelCombo =
                new JComboBox<>(hotels.toArray(new Hotel[0]));

        for (int i = 0; i < hotels.size(); i++) {
            if (hotels.get(i).getId() == room.getHotelId()) {
                hotelCombo.setSelectedIndex(i);
                break;
            }
        }

        JTextField roomNumberField =
                new JTextField(String.valueOf(room.getRoomNumber()));

        JComboBox<String> typeCombo =
                new JComboBox<>(
                        new String[]{
                                "Single",
                                "Double",
                                "Suite"
                        }
                );
        typeCombo.setSelectedItem(room.getType());

        JTextField priceField =
                new JTextField(String.valueOf(room.getPrice()));

        JComboBox<String> statusCombo =
                new JComboBox<>(
                        new String[]{
                                "Available",
                                "Occupied",
                                "Maintenance"
                        }
                );
        statusCombo.setSelectedItem(room.getStatus());

        JPanel panel =
                new JPanel(
                        new GridLayout(5, 2, 8, 8)
                );

        panel.add(new JLabel("Hotel:"));
        panel.add(hotelCombo);
        panel.add(new JLabel("Room Number:"));
        panel.add(roomNumberField);
        panel.add(new JLabel("Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        panel,
                        "Update Room",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) return;

        int roomNumber;
        double price;

        try {
            roomNumber =
                    Integer.parseInt(
                            roomNumberField.getText().trim()
                    );
            price =
                    Double.parseDouble(
                            priceField.getText().trim()
                    );
        } catch (NumberFormatException e) {
            showError(
                    frame,
                    "Room number and price must be valid numbers."
            );
            return;
        }

        if (roomNumber <= 0 || price <= 0) {
            showError(
                    frame,
                    "Room number and price must be positive."
            );
            return;
        }

        Hotel selectedHotel =
                (Hotel) hotelCombo.getSelectedItem();

        String type =
                (String) typeCombo.getSelectedItem();

        String status =
                (String) statusCombo.getSelectedItem();

        room.setHotelId(selectedHotel.getId());
        room.setRoomNumber(roomNumber);
        room.setType(type);
        room.setPrice(price);
        room.setStatus(status);

        roomDAO.updateRoom(room);

        showMessage(frame, "Room updated successfully.");
    }


    // =====================================================
    // UPDATE GUEST
    // =====================================================

    private void updateGuest(JFrame frame) {

        String idText =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter Guest ID to update:"
                );

        if (idText == null) return;

        int id;

        try {
            id = Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            showError(frame, "Guest ID must be a valid number.");
            return;
        }

        Guest guest = guestDAO.getGuestById(id);

        if (guest == null) {
            showError(frame, "Guest not found.");
            return;
        }

        JTextField nameField =
                new JTextField(guest.getName());
        JTextField emailField =
                new JTextField(guest.getEmail());
        JTextField phoneField =
                new JTextField(guest.getPhone());

        JPanel panel =
                new JPanel(
                        new GridLayout(3, 2, 8, 8)
                );

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        panel,
                        "Update Guest",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty()) {
            showError(frame, "Guest name cannot be empty.");
            return;
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {
            showError(frame, "Enter a valid email address.");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            showError(frame, "Phone number must contain exactly 10 digits.");
            return;
        }

        guest.setName(name);
        guest.setEmail(email);
        guest.setPhone(phone);

        guestDAO.updateGuest(guest);

        showMessage(frame, "Guest updated successfully.");
    }


    // =====================================================
    // UPDATE RESERVATION
    // =====================================================

    private void updateReservation(JFrame frame) {

        String idText =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter Reservation ID to update:"
                );

        if (idText == null) return;

        int id;

        try {
            id = Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            showError(
                    frame,
                    "Reservation ID must be a valid number."
            );
            return;
        }

        Reservation reservation =
                reservationDAO.getReservationById(id);

        if (reservation == null) {
            showError(frame, "Reservation not found.");
            return;
        }

        List<Room> rooms = roomDAO.getAllRooms();
        List<Guest> guests = guestDAO.getAllGuests();

        if (rooms.isEmpty()) {
            showError(frame, "No rooms are available.");
            return;
        }

        if (guests.isEmpty()) {
            showError(frame, "No guests are available.");
            return;
        }

        JComboBox<Room> roomCombo =
                new JComboBox<>(rooms.toArray(new Room[0]));

        JComboBox<Guest> guestCombo =
                new JComboBox<>(guests.toArray(new Guest[0]));

        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId() == reservation.getRoomId()) {
                roomCombo.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < guests.size(); i++) {
            if (guests.get(i).getId() == reservation.getGuestId()) {
                guestCombo.setSelectedIndex(i);
                break;
            }
        }

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        JTextField checkInField =
                new JTextField(
                        dateFormat.format(
                                reservation.getCheckInDate()
                        )
                );

        JTextField checkOutField =
                new JTextField(
                        dateFormat.format(
                                reservation.getCheckOutDate()
                        )
                );

        JPanel panel =
                new JPanel(
                        new GridLayout(4, 2, 8, 8)
                );

        panel.add(new JLabel("Room:"));
        panel.add(roomCombo);
        panel.add(new JLabel("Guest:"));
        panel.add(guestCombo);
        panel.add(new JLabel("Check-in (yyyy-MM-dd):"));
        panel.add(checkInField);
        panel.add(new JLabel("Check-out (yyyy-MM-dd):"));
        panel.add(checkOutField);

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        panel,
                        "Update Reservation",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) return;

        Date checkIn;
        Date checkOut;

        try {
            checkIn = dateFormat.parse(
                    checkInField.getText().trim()
            );
            checkOut = dateFormat.parse(
                    checkOutField.getText().trim()
            );
        } catch (ParseException e) {
            showError(
                    frame,
                    "Enter valid dates in yyyy-MM-dd format."
            );
            return;
        }

        if (!checkOut.after(checkIn)) {
            showError(
                    frame,
                    "Check-out date must be after check-in date."
            );
            return;
        }

        Room selectedRoom =
                (Room) roomCombo.getSelectedItem();

        Guest selectedGuest =
                (Guest) guestCombo.getSelectedItem();

        reservation.setRoomId(selectedRoom.getId());
        reservation.setGuestId(selectedGuest.getId());
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);

        reservationDAO.updateReservation(reservation);

        showMessage(frame, "Reservation updated successfully.");
    }


    // =====================================================
    // SEARCH DATA
    // =====================================================

    // Search is handled by the search bar in createGUI().


    private void performSearch(
            JFrame frame,
            String type,
            String keyword) {

        switch (type) {
            case "Hotels":
                searchHotels(frame, keyword);
                break;

            case "Rooms":
                searchRooms(frame, keyword);
                break;

            case "Guests":
                searchGuests(frame, keyword);
                break;

            case "Reservations":
                searchReservations(frame, keyword);
                break;
        }
    }


    private void searchHotels(
            JFrame frame,
            String keyword) {

        List<Hotel> hotels =
                hotelDAO.getAllHotels();

        String search = keyword.toLowerCase();

        List<Hotel> results =
                new java.util.ArrayList<>();

        for (Hotel hotel : hotels) {
            if (String.valueOf(hotel.getId()).contains(search) ||
                    hotel.getName().toLowerCase().contains(search) ||
                    hotel.getLocation().toLowerCase().contains(search)) {
                results.add(hotel);
            }
        }

        String[] columns = {
                "ID", "Name", "Location"
        };

        Object[][] data =
                new Object[results.size()][3];

        for (int i = 0; i < results.size(); i++) {
            Hotel hotel = results.get(i);
            data[i][0] = hotel.getId();
            data[i][1] = hotel.getName();
            data[i][2] = hotel.getLocation();
        }

        showTableDialog(
                frame,
                "Hotel Search Results",
                columns,
                data
        );
    }


    private void searchRooms(
            JFrame frame,
            String keyword) {

        List<Object[]> rooms =
                roomDAO.getAllRoomsWithHotel();

        String search = keyword.toLowerCase();

        List<Object[]> results =
                new java.util.ArrayList<>();

        for (Object[] room : rooms) {
            if (String.valueOf(room[0]).contains(search) ||
                    String.valueOf(room[1]).contains(search) ||
                    String.valueOf(room[2]).toLowerCase().contains(search) ||
                    String.valueOf(room[3]).contains(search) ||
                    String.valueOf(room[4]).toLowerCase().contains(search) ||
                    String.valueOf(room[5]).contains(search) ||
                    String.valueOf(room[6]).toLowerCase().contains(search)) {
                results.add(room);
            }
        }

        String[] columns = {
                "ID", "Hotel ID", "Hotel Name",
                "Room No.", "Type", "Price", "Status"
        };

        Object[][] data =
                new Object[results.size()][7];

        for (int i = 0; i < results.size(); i++) {
            data[i] = results.get(i);
        }

        showTableDialog(
                frame,
                "Room Search Results",
                columns,
                data
        );
    }


    private void searchGuests(
            JFrame frame,
            String keyword) {

        List<Object[]> guests =
                guestDAO.getAllGuestsWithReservations();

        String search = keyword.toLowerCase();

        List<Object[]> results =
                new java.util.ArrayList<>();

        for (Object[] guest : guests) {
            if (String.valueOf(guest[0]).contains(search) ||
                    String.valueOf(guest[1]).toLowerCase().contains(search) ||
                    String.valueOf(guest[2]).toLowerCase().contains(search) ||
                    String.valueOf(guest[3]).toLowerCase().contains(search) ||
                    String.valueOf(guest[4]).toLowerCase().contains(search) ||
                    String.valueOf(guest[5]).toLowerCase().contains(search) ||
                    String.valueOf(guest[6]).toLowerCase().contains(search)) {
                results.add(guest);
            }
        }

        String[] columns = {
                "ID", "Name", "Email", "Phone",
                "Reservation ID", "Room No.", "Hotel Name"
        };

        Object[][] data =
                new Object[results.size()][7];

        for (int i = 0; i < results.size(); i++) {
            Object[] guest = results.get(i);
            data[i][0] = guest[0];
            data[i][1] = guest[1];
            data[i][2] = guest[2];
            data[i][3] = guest[3];
            data[i][4] = guest[4] == null ? "-" : guest[4];
            data[i][5] = guest[5] == null ? "-" : guest[5];
            data[i][6] = guest[6] == null ? "-" : guest[6];
        }

        showTableDialog(
                frame,
                "Guest Search Results",
                columns,
                data
        );
    }


    private void searchReservations(
            JFrame frame,
            String keyword) {

        List<Object[]> reservations =
                reservationDAO.getAllReservationsWithDetails();

        String search = keyword.toLowerCase();

        List<Object[]> results =
                new java.util.ArrayList<>();

        for (Object[] reservation : reservations) {
            if (String.valueOf(reservation[0]).contains(search) ||
                    String.valueOf(reservation[1]).toLowerCase().contains(search) ||
                    String.valueOf(reservation[2]).toLowerCase().contains(search) ||
                    String.valueOf(reservation[3]).toLowerCase().contains(search) ||
                    String.valueOf(reservation[4]).toLowerCase().contains(search) ||
                    String.valueOf(reservation[5]).toLowerCase().contains(search)) {
                results.add(reservation);
            }
        }

        String[] columns = {
                "ID", "Guest Name", "Room No.",
                "Hotel Name", "Check-in", "Check-out"
        };

        Object[][] data =
                new Object[results.size()][6];

        for (int i = 0; i < results.size(); i++) {
            data[i] = results.get(i);
        }

        showTableDialog(
                frame,
                "Reservation Search Results",
                columns,
                data
        );
    }


    // =====================================================
    // RETRIEVE MENU BUTTON
    // =====================================================

    private JButton createRetrieveButton(String text) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        button.setForeground(TEXT);
        button.setBackground(BUTTON);
        button.setFocusPainted(false);
        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                160,
                                180,
                                200
                        ),
                        1
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e) {

                        button.setBackground(
                                BUTTON_HOVER
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e) {

                        button.setBackground(
                                BUTTON
                        );
                    }
                }
        );

        return button;
    }


    // =====================================================
    // SHOW ALL HOTELS
    // =====================================================

    private void showHotels(JFrame frame) {

        List<Hotel> hotels =
                hotelDAO.getAllHotels();

        String[] columns = {
                "ID",
                "Name",
                "Location"
        };

        Object[][] data =
                new Object[hotels.size()][3];

        for (int i = 0; i < hotels.size(); i++) {

            Hotel hotel = hotels.get(i);

            data[i][0] = hotel.getId();
            data[i][1] = hotel.getName();
            data[i][2] = hotel.getLocation();
        }

        showTableDialog(
                frame,
                "Hotels",
                columns,
                data
        );
    }


    // =====================================================
    // SHOW ALL ROOMS
    // =====================================================

    private void showRooms(JFrame frame) {

        List<Object[]> rooms =
                roomDAO.getAllRoomsWithHotel();

        String[] columns = {
                "ID",
                "Hotel ID",
                "Hotel Name",
                "Room No.",
                "Type",
                "Price",
                "Status"
        };

        Object[][] data =
                new Object[rooms.size()][7];

        for (int i = 0; i < rooms.size(); i++) {

            Object[] room = rooms.get(i);

            data[i][0] = room[0];
            data[i][1] = room[1];
            data[i][2] = room[2];
            data[i][3] = room[3];
            data[i][4] = room[4];
            data[i][5] = room[5];
            data[i][6] = room[6];
        }

        showTableDialog(
                frame,
                "Rooms",
                columns,
                data
        );
    }


    // =====================================================
    // SHOW ALL GUESTS
    // =====================================================

    private void showGuests(JFrame frame) {

        List<Object[]> guests =
                guestDAO.getAllGuestsWithReservations();

        String[] columns = {
                "ID",
                "Name",
                "Email",
                "Phone",
                "Reservation ID",
                "Room No.",
                "Hotel Name"
        };

        Object[][] data =
                new Object[guests.size()][7];

        for (int i = 0; i < guests.size(); i++) {

            Object[] guest = guests.get(i);

            data[i][0] = guest[0];
            data[i][1] = guest[1];
            data[i][2] = guest[2];
            data[i][3] = guest[3];
            data[i][4] = guest[4] == null ? "-" : guest[4];
            data[i][5] = guest[5] == null ? "-" : guest[5];
            data[i][6] = guest[6] == null ? "-" : guest[6];
        }

        showTableDialog(
                frame,
                "Guests",
                columns,
                data
        );
    }


    // =====================================================
    // SHOW ALL RESERVATIONS
    // =====================================================

    private void showReservations(JFrame frame) {

        List<Object[]> reservations =
                reservationDAO.getAllReservationsWithDetails();

        String[] columns = {
                "ID",
                "Guest Name",
                "Room No.",
                "Hotel Name",
                "Check-in",
                "Check-out"
        };

        Object[][] data =
                new Object[reservations.size()][6];

        for (int i = 0; i < reservations.size(); i++) {

            Object[] reservation =
                    reservations.get(i);

            data[i][0] = reservation[0];
            data[i][1] = reservation[1];
            data[i][2] = reservation[2];
            data[i][3] = reservation[3];
            data[i][4] = reservation[4];
            data[i][5] = reservation[5];
        }

        showTableDialog(
                frame,
                "Reservations",
                columns,
                data
        );
    }


    // =====================================================
    // TABLE WINDOW
    // =====================================================

    private void showTableDialog(
            JFrame frame,
            String title,
            String[] columns,
            Object[][] data) {

        JDialog dialog =
                new JDialog(
                        frame,
                        title,
                        true
                );

        int width = 750;

        if (columns.length >= 7) {
            width = 1050;
        } else if (columns.length == 6) {
            width = 950;
        }

        dialog.setSize(width, 450);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(true);

        if (data.length == 0) {

            JLabel message =
                    new JLabel(
                            "No records found.",
                            SwingConstants.CENTER
                    );

            message.setForeground(TEXT);

            message.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            16
                    )
            );

            JButton backButton =
                    new JButton("Back");

            backButton.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            14
                    )
            );

            backButton.setFocusPainted(false);

            backButton.addActionListener(
                    e -> dialog.dispose()
            );

            JPanel panel =
                    new JPanel(
                            new BorderLayout(
                                    10,
                                    10
                            )
                    );

            panel.setBackground(BACKGROUND);

            panel.setBorder(
                    new EmptyBorder(
                            30,
                            30,
                            30,
                            30
                    )
            );

            panel.add(
                    message,
                    BorderLayout.CENTER
            );

            JPanel bottomPanel =
                    new JPanel();

            bottomPanel.setOpaque(false);

            bottomPanel.add(backButton);

            panel.add(
                    bottomPanel,
                    BorderLayout.SOUTH
            );

            dialog.add(panel);

        } else {

            JTable table =
                    new JTable(
                            data,
                            columns
                    );

            table.setEnabled(true);
            table.setRowHeight(30);
            table.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );

            table.setGridColor(
                    new Color(
                            180,
                            195,
                            210
                    )
            );

            table.setShowGrid(true);
            table.setIntercellSpacing(
                    new Dimension(1, 1)
            );

            table.setAutoResizeMode(
                    JTable.AUTO_RESIZE_OFF
            );

            table.getTableHeader().setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            14
                    )
            );

            table.getTableHeader().setReorderingAllowed(false);

            table.getTableHeader().setPreferredSize(
                    new Dimension(
                            table.getTableHeader().getWidth(),
                            32
                    )
            );

            for (int i = 0; i < columns.length; i++) {

                int preferredWidth = 120;

                if (columns[i].equals("ID") ||
                        columns[i].equals("Hotel ID") ||
                        columns[i].equals("Reservation ID")) {
                    preferredWidth = 100;
                } else if (columns[i].equals("Name") ||
                        columns[i].equals("Guest Name") ||
                        columns[i].equals("Hotel Name")) {
                    preferredWidth = 170;
                } else if (columns[i].equals("Email")) {
                    preferredWidth = 190;
                } else if (columns[i].equals("Phone")) {
                    preferredWidth = 130;
                } else if (columns[i].equals("Room No.")) {
                    preferredWidth = 120;
                } else if (columns[i].equals("Check-in") ||
                        columns[i].equals("Check-out")) {
                    preferredWidth = 130;
                } else if (columns[i].equals("Price")) {
                    preferredWidth = 110;
                } else if (columns[i].equals("Status") ||
                        columns[i].equals("Type")) {
                    preferredWidth = 130;
                } else if (columns[i].equals("Location")) {
                    preferredWidth = 160;
                }

                table.getColumnModel()
                        .getColumn(i)
                        .setPreferredWidth(preferredWidth);
            }

            JScrollPane scrollPane =
                    new JScrollPane(table);

            JButton backButton =
                    new JButton("Back");

            backButton.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            14
                    )
            );

            backButton.setFocusPainted(false);

            backButton.addActionListener(
                    e -> dialog.dispose()
            );

            JPanel bottomPanel =
                    new JPanel();

            bottomPanel.setOpaque(false);
            bottomPanel.add(backButton);

            JPanel mainPanel =
                    new JPanel(
                            new BorderLayout(
                                    10,
                                    10
                            )
                    );

            mainPanel.setBackground(BACKGROUND);

            mainPanel.setBorder(
                    new EmptyBorder(
                            15,
                            15,
                            15,
                            15
                    )
            );

            mainPanel.add(
                    scrollPane,
                    BorderLayout.CENTER
            );

            mainPanel.add(
                    bottomPanel,
                    BorderLayout.SOUTH
            );

            dialog.add(mainPanel);
        }

        dialog.setVisible(true);
    }


    private Date parseDate(String text) throws ParseException {

        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd");

        format.setLenient(false);

        return format.parse(text);
    }


    // =====================================================
    // ROOM AVAILABILITY SEARCH
    // =====================================================

    private void searchRoomAvailability(JFrame frame) {

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField checkInField = new JTextField();
        JTextField checkOutField = new JTextField();

        JComboBox<String> typeCombo =
                new JComboBox<>(new String[]{
                        "All Types",
                        "Single",
                        "Double",
                        "Suite"
                });

        panel.add(new JLabel("Check-in (yyyy-MM-dd):"));
        panel.add(checkInField);
        panel.add(new JLabel("Check-out (yyyy-MM-dd):"));
        panel.add(checkOutField);
        panel.add(new JLabel("Room Type:"));
        panel.add(typeCombo);

        int result = JOptionPane.showConfirmDialog(
                frame,
                panel,
                "Room Availability Search",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        Date checkIn;
        Date checkOut;

        try {
            checkIn = parseDate(checkInField.getText().trim());
            checkOut = parseDate(checkOutField.getText().trim());
        } catch (ParseException ex) {
            showError(frame,
                    "Please enter dates in yyyy-MM-dd format.");
            return;
        }

        if (!checkOut.after(checkIn)) {
            showError(frame,
                    "Check-out date must be after check-in date.");
            return;
        }

        String selectedType =
                (String) typeCombo.getSelectedItem();

        List<Room> rooms = roomDAO.getAllRooms();
        List<Reservation> reservations =
                reservationDAO.getAllReservations();
        List<Hotel> hotels = hotelDAO.getAllHotels();

        String[] columns = {
                "Room ID",
                "Hotel Name",
                "Room No.",
                "Type",
                "Price",
                "Status"
        };

        javax.swing.table.DefaultTableModel model =
                new javax.swing.table.DefaultTableModel(columns, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        for (Room room : rooms) {

            if (!"Available".equalsIgnoreCase(room.getStatus())) {
                continue;
            }

            if (!"All Types".equals(selectedType) &&
                    !room.getType().equalsIgnoreCase(selectedType)) {
                continue;
            }

            boolean booked = false;

            for (Reservation reservation : reservations) {

                if (reservation.getRoomId() != room.getId()) {
                    continue;
                }

                boolean overlaps =
                        reservation.getCheckInDate().before(checkOut) &&
                        reservation.getCheckOutDate().after(checkIn);

                if (overlaps) {
                    booked = true;
                    break;
                }
            }

            if (booked) {
                continue;
            }

            String hotelName = "Unknown";

            for (Hotel hotel : hotels) {
                if (hotel.getId() == room.getHotelId()) {
                    hotelName = hotel.getName();
                    break;
                }
            }

            model.addRow(new Object[]{
                    room.getId(),
                    hotelName,
                    room.getRoomNumber(),
                    room.getType(),
                    room.getPrice(),
                    room.getStatus()
            });
        }

        if (model.getRowCount() == 0) {
            showMessage(frame,
                    "No rooms are available for the selected dates.");
            return;
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        JOptionPane.showMessageDialog(
                frame,
                scrollPane,
                "Available Rooms",
                JOptionPane.PLAIN_MESSAGE
        );
    }


    // =====================================================
    // CUSTOMER BILLING
    // =====================================================

    private void showCustomerBilling(JFrame frame) {

        String idText = JOptionPane.showInputDialog(
                frame,
                "Enter Reservation ID for billing:"
        );

        if (idText == null) {
            return;
        }

        idText = idText.trim();

        if (idText.isEmpty()) {
            showError(frame, "Please enter a Reservation ID.");
            return;
        }

        int reservationId;

        try {
            reservationId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            showError(frame, "Reservation ID must be a valid number.");
            return;
        }

        try {
            Object[] data =
                    reservationDAO.getReservationBillingDetails(reservationId);

            if (data == null) {
                showError(frame,
                        "Reservation ID " + reservationId
                                + " was not found in the database.");
                return;
            }

            Date checkIn = (Date) data[9];
            Date checkOut = (Date) data[10];

            if (checkIn == null || checkOut == null) {
                showError(frame,
                        "Check-in or check-out date is missing.");
                return;
            }

            long nights = calculateNights(checkIn, checkOut);

            if (nights <= 0) {
                showError(frame,
                        "Invalid reservation dates. Check-out must be after check-in.");
                return;
            }

            double price = ((Number) data[7]).doubleValue();
            double total = nights * price;

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd");

            String bill =
                    "==============================================\n" +
                    "              CUSTOMER BILL\n" +
                    "           HOTEL RESERVATION SYSTEM\n" +
                    "==============================================\n\n" +
                    "Reservation ID  : " + data[0] + "\n" +
                    "Customer Name   : " + data[1] + "\n" +
                    "Email           : " + data[2] + "\n" +
                    "Phone           : " + data[3] + "\n\n" +
                    "Hotel           : " + data[8] + "\n" +
                    "Room ID         : " + data[4] + "\n" +
                    "Room Number     : " + data[5] + "\n" +
                    "Room Type       : " + data[6] + "\n\n" +
                    "Check-in        : " + sdf.format(checkIn) + "\n" +
                    "Check-out       : " + sdf.format(checkOut) + "\n" +
                    "Number of Nights: " + nights + "\n\n" +
                    "Rate / Night    : Rs. " + String.format("%.2f", price) + "\n" +
                    "Room Charges    : Rs. " + String.format("%.2f", total) + "\n" +
                    "----------------------------------------------\n" +
                    "TOTAL AMOUNT    : Rs. " + String.format("%.2f", total) + "\n" +
                    "==============================================\n";

            JTextArea area = new JTextArea(bill);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 14));
            area.setBorder(new EmptyBorder(15, 15, 15, 15));

            JScrollPane scrollPane =
                    new JScrollPane(area);
            scrollPane.setPreferredSize(
                    new Dimension(620, 420)
            );

            JOptionPane.showMessageDialog(
                    frame,
                    scrollPane,
                    "Customer Billing",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            showError(frame,
                    "Billing failed:\n\n" + ex.getMessage());
        }
    }


    // =====================================================
    // INVOICE GENERATION
    // =====================================================

    private void generateInvoice(JFrame frame) {

        String idText = JOptionPane.showInputDialog(
                frame,
                "Enter Reservation ID for invoice:"
        );

        if (idText == null) {
            return;
        }

        idText = idText.trim();

        if (idText.isEmpty()) {
            showError(frame, "Please enter a Reservation ID.");
            return;
        }

        int reservationId;

        try {
            reservationId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            showError(frame, "Reservation ID must be a valid number.");
            return;
        }

        try {
            Object[] data =
                    reservationDAO.getReservationBillingDetails(reservationId);

            if (data == null) {
                showError(frame,
                        "Reservation ID " + reservationId
                                + " was not found in the database.");
                return;
            }

            Date checkIn = (Date) data[9];
            Date checkOut = (Date) data[10];

            if (checkIn == null || checkOut == null) {
                showError(frame,
                        "Check-in or check-out date is missing.");
                return;
            }

            long nights = calculateNights(checkIn, checkOut);

            if (nights <= 0) {
                showError(frame,
                        "Invalid reservation dates. Check-out must be after check-in.");
                return;
            }

            double price = ((Number) data[7]).doubleValue();
            double total = nights * price;

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd");

            String invoice =
                    "============================================================\n" +
                    "                    HOTEL INVOICE\n" +
                    "              HOTEL RESERVATION SYSTEM\n" +
                    "============================================================\n\n" +
                    "Invoice / Reservation ID : " + data[0] + "\n" +
                    "Customer                 : " + data[1] + "\n" +
                    "Email                    : " + data[2] + "\n" +
                    "Phone                    : " + data[3] + "\n\n" +
                    "Hotel                    : " + data[8] + "\n" +
                    "Room ID                  : " + data[4] + "\n" +
                    "Room Number              : " + data[5] + "\n" +
                    "Room Type                : " + data[6] + "\n" +
                    "Check-in                 : " + sdf.format(checkIn) + "\n" +
                    "Check-out                : " + sdf.format(checkOut) + "\n" +
                    "Nights                   : " + nights + "\n\n" +
                    "Rate per Night           : Rs. " + String.format("%.2f", price) + "\n" +
                    "Room Charges             : Rs. " + String.format("%.2f", total) + "\n" +
                    "------------------------------------------------------------\n" +
                    "TOTAL AMOUNT             : Rs. " + String.format("%.2f", total) + "\n" +
                    "============================================================\n\n" +
                    "Thank you for choosing our hotel.\n";

            JTextArea area = new JTextArea(invoice);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 14));
            area.setBorder(new EmptyBorder(15, 15, 15, 15));

            JScrollPane scrollPane = new JScrollPane(area);
            scrollPane.setPreferredSize(new Dimension(650, 460));

            Object[] options = {"Save Invoice", "Close"};

            int choice = JOptionPane.showOptionDialog(
                    frame,
                    scrollPane,
                    "Invoice",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                saveInvoice(frame, invoice, reservationId);
            }

        } catch (Exception ex) {
            showError(frame,
                    "Invoice generation failed:\n\n" + ex.getMessage());
        }
    }


    private void saveInvoice(
            JFrame frame,
            String invoice,
            int reservationId) {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Invoice");
        chooser.setSelectedFile(
                new File("Invoice_Reservation_" + reservationId + ".txt")
        );

        int result = chooser.showSaveDialog(frame);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(invoice);

            showMessage(
                    frame,
                    "Invoice generated successfully.\n\n" +
                    "Saved to:\n" + file.getAbsolutePath()
            );

        } catch (IOException ex) {
            showError(
                    frame,
                    "Invoice could not be saved.\n\n" +
                    ex.getMessage()
            );
        }
    }


    // =====================================================
    // BILL / INVOICE HELPERS
    // =====================================================

    private long calculateNights(Date checkIn, Date checkOut) {

        java.sql.Date sqlCheckIn = (java.sql.Date) checkIn;
        java.sql.Date sqlCheckOut = (java.sql.Date) checkOut;

        LocalDate in = sqlCheckIn.toLocalDate();
        LocalDate out = sqlCheckOut.toLocalDate();

        return ChronoUnit.DAYS.between(in, out);
    }


    // =====================================================
    // CREATE BUTTON
    // =====================================================

    private JButton createButton(
            String icon,
            String text) {

        JButton button = new JButton();

        JPanel contentPanel =
                new JPanel();

        contentPanel.setLayout(
                new BoxLayout(
                        contentPanel,
                        BoxLayout.Y_AXIS
                )
        );

        contentPanel.setOpaque(false);

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        42
                )
        );

        JLabel textLabel =
                new JLabel(text);

        textLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        textLabel.setForeground(TEXT);

        textLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        contentPanel.add(iconLabel);
        contentPanel.add(
                Box.createVerticalStrut(8)
        );
        contentPanel.add(textLabel);

        button.setLayout(
                new GridBagLayout()
        );

        button.add(contentPanel);

        button.setForeground(TEXT);
        button.setBackground(BUTTON);
        button.setFocusPainted(false);
        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                190,
                                205,
                                220
                        ),
                        1
                )
        );

        button.setPreferredSize(
                new Dimension(
                        250,
                        115
                )
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e) {

                        button.setBackground(
                                BUTTON_HOVER
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e) {

                        button.setBackground(
                                BUTTON
                        );
                    }
                }
        );

        return button;
    }

    // =====================================================
    // MESSAGE
    // =====================================================

    private void showMessage(
            JFrame frame,
            String message) {

        JOptionPane.showMessageDialog(
                frame,
                message,
                "Hotel Reservation System",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // =====================================================
    // ERROR
    // =====================================================

    private void showError(
            JFrame frame,
            String message) {

        JOptionPane.showMessageDialog(
                frame,
                message,
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
        );
    }


    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> new Main()
        );
    }
}
