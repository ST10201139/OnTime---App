# OnTime Android App
The OnTime Android app is a versatile time management tool designed to help users efficiently track their tasks and goals. With its intuitive interface and robust features, OnTime simplifies the process of managing and organizing work hours, tasks, and productivity goals.
## Login Details 
1.)
- test@gmail.com 
- test123 

  
## Steps to Run the OnTime App and Navigation Overview
To run the OnTime app and understand its navigation flow, follow these steps:
#### Step 1: Install Android Studio
- If you haven't already installed Android Studio, download and install it from the official Android Studio website.

#### Step 2: Clone the Repository
- Clone the OnTime repository to your local machine using Git. Open a terminal or command prompt and run the following command:
- git clone <repository-url>

#### step 3: Open the Project in Android Studio
Launch Android Studio and open the OnTime project by selecting "Open an existing Android Studio project" from the welcome screen. Navigate to the directory where you cloned the repository and select the project folder.

#### Step 4: Set Up Emulator or Connect Physical Device
You can run the app on either an emulator or a physical Android device. Follow these instructions to set up either option:

Emulator: Create a new virtual device from the AVD Manager in Android Studio. Choose a device definition, select a system image with Google Play support, and click "Finish" to create the virtual device.
Physical Device: Connect your Android device to your computer via USB debugging mode. Ensure that USB debugging is enabled on your device.
#### Step 5: Build and Run the App
Once the project is open in Android Studio and the emulator or device is set up, click the "Run" button (green play icon) in the toolbar. Android Studio will build the project and deploy the app to the emulator or connected device.

## Navigation Overview
When you run the OnTime app, you can expect the following navigation flow:

- Login Screen: Upon launching the app, you will be presented with a login screen. Enter your credentials and click the login button to proceed.
- Home Page: After successful login, you will be directed to the home page. Here, you will see options such as view entries, add entries, view goals, and view total hours.
- Add Entry: Clicking on the "Add Entry" option will take you to a screen where you can input details such as entry name, description, start date, end date, start time, end time, and category for a new time entry.
- View Entries: Selecting the "View Entries" option will display a list of all time sheet entries previously added. Each entry will show details such as entry name, description, start date, end date, start time, end time, and category.
- View Goals: Choosing the "View Goals" option will navigate you to a screen where you can set and view your minimum and maximum hourly goals.
- View Total Hours: Clicking on the "View Total Hours" option will show a breakdown of the total hours logged for each category.step 3: Open the Project in Android Studio

## Classes Overview
#### 1. MainActivity
- Manages user authentication using Firebase Auth.
- Allows users to log in or navigate to the registration screen.
#### 2. RegisterActivity
- Handles user registration using Firebase Auth.
- Validates user input and registers new users with Firebase.
#### 3. HomeActivity
- Main activity after successful user authentication.
- Provides a bottom navigation bar for navigating to different functionalities: View Time Sheet Entries
- Add Time Entries
- View My Goals
- View Total Hours
#### 4. AddTimeEntryActivity
- Allows users to add new time entries.
- Captures entry details such as name, description, category, start/end date, and time.
- Provides options to capture/upload images for the entry.
#### 5. ViewGoalsActivity
- Enables users to view and set their goals for minimum and maximum working hours.
- Saves user goals to Firebase Realtime Database.
#### 6. ViewTimeSheetEntriesActivity
- Displays a list of time sheet entries for the current user.
- Fetches data from Firebase Realtime Database and populates the RecyclerView.
- Provides detailed information about each time entry, including start/end date, time, and category.
#### 7. ViewTotalHoursActivity
- Calculates and displays total working hours for each category.
- Retrieves time entries from Firebase Realtime Database and calculates total hours for each category.
- Displays category-wise total hours in a RecyclerView.
## Installation
To run the OnTime Android app locally, follow these steps:

- Clone the repository to your local machine.
- Open the project in Android Studio.
- Connect your Android device or use an emulator.
- Build and run the project.
Ensure that you have the necessary dependencies configured in your Android project, such as Firebase Auth and Realtime Database.

## Dependencies
The OnTime Android app relies on the following dependencies:

- Firebase Auth: Used for user authentication.
- Firebase Realtime Database: Stores user data, time entries, and goals.
- Google Material Components: Provides UI components and theming.
## Usage
Upon launching the OnTime app, users can log in or register to access various features:

- Add new time entries with details such as name, description, and category.
- Set personal goals for minimum and maximum working hours.
- View total working hours for each category and track progress towards goals.
- Navigate between different functionalities using the bottom navigation bar.

## Recommended System Requirements
- Operating System : Windows 10 version 1703 or higher :Home,Professional,Education,and Enterprise(LTSC and s are not supported) Hardware : 1.8 GHz or faster processor .Quad -core or better recommended
## Version
- Giraffe|2022.3.2 Patch 4
- This project was developed using Android Studio version 2022.3.2 Patch 4, also known as Giraffe. It's recommended to use the same version of Android Studio or a compatible version to ensure compatibility and seamless development experience. Using the recommended version helps in avoiding potential compatibility issues and ensures that all features and functionalities work as expected.
## Challenges Faced
- The first major error that I encounted was trying to save my image in a Cloud Storage firbase . It was saving correctly but I ran into difficulties when I tried displaying to my View Entries Page .
- The fix was to go the base64 and bitmap to store the image to the real time database in firebase
- Calculating the correct logic for the Category Hours method to display the right time
- At first I never use adapter classes for the Recycle Views so ran into errors 
## Frequently Asked Questions (FAQs)
### General Questions
#### 1. What is OnTime?
- OnTime is an Android application designed to help users manage their time effectively, track their schedules, and set personal goals.
#### 2. How can I download OnTime?
- OnTime is currently available as a source code repository on GitHub. You can clone or download the repository to your local machine and run it using Android Studio.
#### 3. Is OnTime available for iOS devices?
- No, OnTime is currently developed only for Android devices. There are no plans for an iOS version at the moment.
#### 4. Can I contribute to the OnTime project?
- Yes, contributions to the OnTime project are welcome! You can contribute by submitting pull requests, reporting issues, or suggesting new features.
#### 5. Can I use OnTime offline?
- OnTime requires an internet connection to authenticate users and store data in Firebase Realtime Database. However, once authenticated, some features may work offline, such as viewing previously loaded data
## Contributors
- [Liam Munsamy]
- [ST10201139@vcconnect.edu.za]
- Feel free to contribute to the project by submitting pull requests or reporting issues.

## License
This project is licensed under the MIT License.
