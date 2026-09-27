package hotelsystem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

// ================= ABSTRACT CLASS (ABSTRACTION) =================
abstract class Person implements Serializable {
    protected String name;
    public abstract String getDetails();
}

// ================= ENTITY CLASS (ENCAPSULATION) =================
class Booking extends Person implements Serializable {
    private String roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private long nights;
    private int total;

    public Booking(String name, String roomType, LocalDate in, LocalDate out, long nights, int total) {
        this.name = name;
        this.roomType = roomType;
        this.checkIn = in;
        this.checkOut = out;
        this.nights = nights;
        this.total = total;
    }

    public String getRoomType() { return roomType; }
    public long getNights() { return nights; }
    public int getTotal() { return total; }

    @Override
    public String getDetails() {
        return name + " | " + roomType + " | " + nights + " nights | " + total + " BDT";
    }
}

// ================= MAIN GUI CLASS =================
public class HotelBookingSystem extends JFrame implements ActionListener {

    JTextField nameField;
    JSpinner checkInSpinner, checkOutSpinner;
    JComboBox<String> roomBox;
    JLabel imageLabel, priceLabel, nightsLabel;
    JButton bookBtn, viewBtn, deleteBtn, updateBtn, searchBtn;

    ArrayList<Booking> bookings = new ArrayList<>();
    File dataFile = new File("bookings.dat");

    public HotelBookingSystem() {
        // Modern Dark Theme with Accents
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(59, 130, 246)); // Blue accent
        UIManager.put("Button.border", BorderFactory.createEmptyBorder());
        UIManager.put("Panel.background", new Color(17, 24, 39)); // Dark gray
        UIManager.put("TextField.background", new Color(31, 41, 55));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("ComboBox.background", new Color(31, 41, 55));
        UIManager.put("ComboBox.foreground", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.background", new Color(17, 24, 39));
        UIManager.put("Panel.background", new Color(17, 24, 39));

        setTitle("Hotel Booking System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title with gradient-like effect
        JLabel titleLabel = new JLabel("Hotel Booking & Reservation", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(59, 130, 246));
        titleLabel.setBorder(new EmptyBorder(20, 10, 20, 10));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(17, 24, 39));

        add(titleLabel, BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.WEST);
        add(createImagePanel(), BorderLayout.CENTER);
        add(createManagementPanel(), BorderLayout.EAST);

        loadBookings();
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        TitledBorder border = new TitledBorder("Booking Details");
        border.setTitleColor(new Color(59, 130, 246));
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.setBorder(border);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        JLabel nameLabel = new JLabel("Guest Name:");
        nameLabel.setFont(labelFont);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setFont(labelFont);
        panel.add(nameField, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        JLabel checkInLabel = new JLabel("Check In:");
        checkInLabel.setFont(labelFont);
        panel.add(checkInLabel, gbc);

        gbc.gridx = 1;
        checkInSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor inEditor = new JSpinner.DateEditor(checkInSpinner, "dd-MM-yyyy");
        checkInSpinner.setEditor(inEditor);
        checkInSpinner.setFont(labelFont);
        panel.add(checkInSpinner, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        JLabel checkOutLabel = new JLabel("Check Out:");
        checkOutLabel.setFont(labelFont);
        panel.add(checkOutLabel, gbc);

        gbc.gridx = 1;
        checkOutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor outEditor = new JSpinner.DateEditor(checkOutSpinner, "dd-MM-yyyy");
        checkOutSpinner.setEditor(outEditor);
        checkOutSpinner.setFont(labelFont);
        panel.add(checkOutSpinner, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        JLabel roomLabel = new JLabel("Room Type:");
        roomLabel.setFont(labelFont);
        panel.add(roomLabel, gbc);

        gbc.gridx = 1;
        roomBox = new JComboBox<>(new String[]{"Single", "Double", "Deluxe", "Suite"});
        roomBox.setFont(labelFont);
        roomBox.addActionListener(this);
        panel.add(roomBox, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        nightsLabel = new JLabel("Nights: 0");
        nightsLabel.setFont(labelFont);
        panel.add(nightsLabel, gbc);

        gbc.gridx = 1;
        priceLabel = new JLabel("Price: 2000 BDT");
        priceLabel.setFont(labelFont);
        panel.add(priceLabel, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        bookBtn = new JButton("Confirm Booking");
        bookBtn.setPreferredSize(new Dimension(200, 40));
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookBtn.addActionListener(this);
        panel.add(bookBtn, gbc);

        panel.setBackground(new Color(17, 24, 39));

        return panel;
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        TitledBorder border = new TitledBorder("Room Preview");
        border.setTitleColor(new Color(59, 130, 246));
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.setBorder(border);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        panel.add(imageLabel, BorderLayout.CENTER);
        updateImage("Single");
        panel.setBackground(new Color(17, 24, 39));

        return panel;
    }

    private JPanel createManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        TitledBorder border = new TitledBorder("Management");
        border.setTitleColor(new Color(59, 130, 246));
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.setBorder(border);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        viewBtn = new JButton("View Bookings");
        deleteBtn = new JButton("Delete All");
        updateBtn = new JButton("Update Booking");
        searchBtn = new JButton("Search Booking");

        viewBtn.setPreferredSize(new Dimension(160, 35));
        deleteBtn.setPreferredSize(new Dimension(160, 35));
        updateBtn.setPreferredSize(new Dimension(160, 35));
        searchBtn.setPreferredSize(new Dimension(160, 35));

        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updateBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        viewBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        searchBtn.addActionListener(this);

        gbc.gridy = 0;
        panel.add(viewBtn, gbc);

        gbc.gridy = 1;
        panel.add(searchBtn, gbc);

        gbc.gridy = 2;
        panel.add(updateBtn, gbc);

        gbc.gridy = 3;
        panel.add(deleteBtn, gbc);

        panel.setBackground(new Color(17, 24, 39));

        return panel;
    }

    private void updateImage(String room) {
        try {
            ImageIcon icon = new ImageIcon("images/" + room.toLowerCase() + ".jpg");
            Image img = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image not found for " + room + "!");
        }
    }

    private int getPrice(String room) {
        return switch (room) {
            case "Single" -> 2000;
            case "Double" -> 3500;
            case "Deluxe" -> 5000;
            case "Suite" -> 8000;
            default -> 0;
        };
    }

    private long nights() {
        LocalDate in = ((Date) checkInSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate out = ((Date) checkOutSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(in, out);
    }

    // ================= FILE I/O =================
    private void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(bookings);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Save failed!");
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBookings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            bookings = (ArrayList<Booking>) ois.readObject();
        } catch (Exception e) {
            bookings = new ArrayList<>();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String room = (String) roomBox.getSelectedItem();
        updateImage(room);
        priceLabel.setText("Price: " + getPrice(room) + " BDT per night");

        if (e.getSource() == bookBtn) {
            try {
                if (nameField.getText().trim().isEmpty()) throw new Exception();

                long n = nights();
                if (n <= 0) throw new Exception();

                int total = (int) (n * getPrice(room));

                nightsLabel.setText("Nights: " + n);
                priceLabel.setText("Total: " + total + " BDT");

                Booking b = new Booking(
                        nameField.getText().trim(),
                        room,
                        LocalDate.now(),
                        LocalDate.now().plusDays(n),
                        n,
                        total
                );

                bookings.add(b);
                saveBookings();

                JOptionPane.showMessageDialog(
                        this,
                        "Booking Saved Successfully!\n\n" + b.getDetails(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid name and dates!",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        if (e.getSource() == viewBtn) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bookings.size(); i++) {
                sb.append((i + 1) + ". " + bookings.get(i).getDetails()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.length() == 0 ? "No bookings" : sb.toString(), "All Bookings", JOptionPane.INFORMATION_MESSAGE);
        }

        if (e.getSource() == deleteBtn) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all bookings?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bookings.clear();
                saveBookings();
                JOptionPane.showMessageDialog(this, "All bookings deleted!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (e.getSource() == updateBtn) {
            if (bookings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No bookings to update!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] options = new String[bookings.size()];
            for (int i = 0; i < bookings.size(); i++) {
                options[i] = (i + 1) + ". " + bookings.get(i).getDetails();
            }

            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a booking to update:",
                    "Update Booking",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (selected != null) {
                int index = Integer.parseInt(selected.split("\\.")[0]) - 1;
                Booking b = bookings.get(index);

                String newName = JOptionPane.showInputDialog(
                        this,
                        "Enter new guest name:",
                        b.name
                );

                if (newName != null && !newName.trim().isEmpty()) {
                    b.name = newName.trim();
                    saveBookings();
                    JOptionPane.showMessageDialog(
                            this,
                            "Booking updated successfully!\n\n" + b.getDetails(),
                            "Updated",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        }

        if (e.getSource() == searchBtn) {
            String searchName = JOptionPane.showInputDialog(this, "Enter guest name to search:");
            if (searchName != null && !searchName.trim().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Booking b : bookings) {
                    if (b.name.toLowerCase().contains(searchName.toLowerCase())) {
                        sb.append(b.getDetails()).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(this, sb.length() == 0 ? "No matching bookings" : sb.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelBookingSystem());
    }
}