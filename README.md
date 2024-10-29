# SlurpStats

SlurpStats is an Android app for calculating blood alcohol content based on the Widmark formula. Developed in Java as part of the "Mobile Applications" study module at DHBW Karlsruhe in the Business Informatics - Software Engineering program.

## Features

- Blood alcohol calculation based on weight, gender, and consumed drinks
- Selection of predefined drinks or adding custom drinks
- Result management: save, view, and delete calculations
- Help page with information on the formula and app usage
- User-friendly interface with support for both portrait and landscape modes
- Navigation drawer for easy access to all app areas

## Installation

### Requirements
- Android Studio (version 4.0 or higher)
- Android device or emulator with API level 21 (Lollipop) or higher

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/YourUsername/SlurpStats.git


2. **Start Android Studio**:
   - Open Android Studio.

3. **Open Project**:
   - Select "Open an existing Android Studio project" and navigate to the cloned repository.

4. **Synchronize Dependencies**:
   - Android Studio should automatically synchronize the Gradle dependencies.
   - If not, click on "Sync Now."

## Run the App

1. **Select Device**:
   - Choose a connected Android device or an emulator.

2. **Start the App**:
   - Click on "Run" (green arrow) to start the app.

## Usage

### Home Screen

- **Enter Body Weight**:
  - Enter your body weight.
- **Select Gender**:
  - Choose your gender.

### Add Drinks

1. Click on the plus symbol.
2. Select a drink or add a new one.
3. Enter the consumed amount.

### Perform Calculation

- Click on "Calculate" to determine your blood alcohol content.

### Manage Results

- Save the result or return to the home screen.
- Use the Navigation Drawer to view

### Use Help Page

- In the Navigation Drawer, you can find "Help" with more information.

## Technologies

- **Programming Language**: Java
- **IDE**: Android Studio
- **Database**: SQLite
- **User Interface**: Android UI Components, ConstraintLayout
- **Libraries**: AndroidX, Material Design Components

## Architecture

The app uses the MVC pattern:

- **Model**: Data classes like `Drink`, `Result`.
- **View**: XML layout files for the user interface.
- **Controller**: Activities and Fragments like `MainActivity`, `ResultActivity`.
