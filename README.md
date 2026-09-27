# Hotel Booking System

A desktop hotel booking and reservation manager built with Java Swing. Guests can be booked into rooms, and bookings are saved to disk so they persist between runs — no database required.

## Features

- **Book a room** — enter guest name, check-in/check-out dates, and room type; nights and total price are calculated automatically
- **View all bookings** — see every saved reservation at a glance
- **Search bookings** — find a reservation by guest name
- **Update a booking** — edit a guest's name on an existing reservation
- **Delete all bookings** — clear saved data with a confirmation prompt
- **Room preview image** — shows a preview image for the selected room type
- **Persistent storage** — bookings are serialized to `bookings.dat`, so data survives restarts
- **Dark themed UI** — custom dark color scheme applied across all Swing components

## Room types & pricing

| Room Type | Price / night (BDT) |
|-----------|----------------------|
| Single    | 2000                 |
| Double    | 3500                 |
| Deluxe    | 5000                 |
| Suite     | 8000                 |

## Tech stack

- **Java** (Swing / AWT for the GUI)
- **Java Serialization** (`ObjectOutputStream` / `ObjectInputStream`) for saving bookings to disk
- **java.time** (`LocalDate`, `ChronoUnit`) for date handling

## Design

The project uses basic OOP principles:
- `Person` — an abstract class defining a common contract (`getDetails()`)
- `Booking` — extends `Person`, encapsulates a single reservation's data
- `HotelBookingSystem` — the main `JFrame` GUI, handles layout and all button/event logic

## Getting started

### Prerequisites
- JDK 17 or later (uses Java's `switch` expressions and `java.time`)

### Run it
```bash
javac HotelBookingSystem.java
java hotelsystem.HotelBookingSystem
```

> Room preview images are expected at `images/<roomtype>.jpg` (e.g. `images/single.jpg`) relative to the working directory. Add your own images to that folder, or the app will show a "not found" message for the preview.

## Project structure

```
.
├── HotelBookingSystem.java   # Entry point, GUI, and all classes
└── images/                   # Room preview images (single.jpg, double.jpg, deluxe.jpg, suite.jpg)
```

## Possible improvements

- Replace name-only "update" with full booking edits (dates, room type)
- Move room pricing/config to an external file
- Add input validation for overlapping bookings
- Migrate storage from Java serialization to a lightweight database (e.g. SQLite)
