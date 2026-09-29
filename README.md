# EyeAgeApp – Visual Age Measurement Application

## Overview

**EyeAgeApp** is an Android mobile application designed to measure the user's **visual age** through a combination of user calibration, facial measurements, and visual testing.

The application is built using **Modern Android Architecture**, with **MVVM/MVI** patterns and **Jetpack Compose**.

The application consists of three main stages:

1. **Calibration** – determines the user's interpupillary distance.
    
2. **Visual Age Test** – performs the visual test and calculates the user's visual age range.
    
3. **Data Review** – allows previously collected calibration and test results to be reviewed.
    

---

## Features

- User calibration using three iris photographs
    
- Calculation of interpupillary distance (IPD)
    
- Facial measurements using the device camera
    
- Visual age testing using digital optotypes
    
- Visual age range calculation using a standard similar to **LEA NUMBERS**
    
- Three measurements for each calibration and test
    
- Ability to reject individual measurements and final results
    
- Ability to display calibration and test instructions again
    
- Local storage of calibration and test data
    
- Selection of a specific calibration for subsequent tests
    
- Review of previous calibration and test results
    
- Error information displayed to the user
    

---

## Tech Stack

### Language & Platform

- **Kotlin** – primary programming language
    
- **Android SDK** – mobile application platform
    
- **Jetpack Compose** – declarative UI framework
    
- **Java 11** – source and target compatibility
    

### Architecture & Navigation

- **MVVM/MVI** – application architecture
    
- **Jetpack Navigation Compose** – navigation between application screens
    
- **AndroidX Lifecycle** – lifecycle-aware application components
    

### Camera & Computer Vision

- **CameraX** – camera access, image capture, and camera lifecycle management
    
- **Google ML Kit Face Detection** – on-device face detection
    
- **MediaPipe Tasks Vision** – face landmark detection and facial feature analysis
    

### Data Storage

- **Room** – local database for storing calibration and test data
    
- **KSP (Kotlin Symbol Processing)** – annotation processing for Room
    

### UI

- **Material 3** – application UI components
    
- **Jetpack Compose UI** – application interface and UI rendering
    

### Serialization

- **Kotlin Serialization** – serialization and deserialization of objects
    

---

## How It Works

### 1. User Calibration

The calibration process is used to determine the distance between the centers of the user's pupils.

During calibration:

1. The application displays instructions explaining the calibration procedure.
    
2. The user takes three photographs of their irises.
    
3. The application calculates the measurements based on the collected images.
    
4. The average iris measurement is used to determine the distance between the centers of the pupils.
    
5. The resulting calibration is stored on the device.
    
6. The calibration ID is automatically remembered for subsequent use.
    

The user can reject a measurement or the final calibration result and can display the calibration instructions again.

### 2. Visual Age Test

After calibration, the user can perform the visual age test.

During the test:

1. The application displays instructions explaining the test procedure.
    
2. The user maintains a constant distance between their head and the phone.
    
3. The user determines the smallest clearly visible font size.
    
4. The user takes three photographs of their face.
    
5. The application uses the collected measurements to estimate the user's distance from the screen.
    
6. The measured viewing distance and character size are used to calculate a visual age range.
    

The user can reject a measurement or the final result and can display the test instructions again.

### 3. Data Review

The application stores calibration and test data locally on the device.

Users can:

- Browse previously created calibrations.
    
- Select a specific calibration for a visual age test.
    
- Browse test results associated with a selected calibration.
    
- Review previously collected measurements.
    

---

## Visual Age Calculation

The visual age calculation is performed in several stages.

### Interpupillary Distance

During calibration, the application uses the average iris measurement to determine the distance between the centers of the user's pupils.

### Viewing Distance

The application uses the calculated interpupillary distance together with a photograph of the user's face to estimate the user's distance from the screen.

### Visual Age Range

The measured viewing distance and character size are used with digital optotypes based on a standard similar to **LEA NUMBERS** to determine the user's visual age range.

---

## Application Architecture

EyeAgeApp follows a modern Android architecture based on **MVVM/MVI** and **Jetpack Compose**.

The application uses dedicated components for:

- User interface and UI state
    
- Application navigation
    
- Camera and image capture
    
- Face detection and facial landmark processing
    
- Calibration and visual age calculations
    
- Local data persistence
    

---

## Data Storage

Calibration and test data are stored locally using **Room**.

After completing a calibration, the application automatically stores its calibration ID. This allows the user to select the appropriate calibration when performing a visual age test.

Previously recorded calibrations and test results remain available on the device and can be reviewed through the application's data review functionality.

---

## Requirements

- **Android 7.0 (API 24)** or newer
    
- **Device camera access**
    

---

## Installation

### Requirements

Before running the application, make sure that the target device meets the requirements listed above.

### Build and Install

To build and install the debug version of the application on a connected Android device, run the following command from the project root:

```
./gradlew installDebug
```

This command builds the debug version of the application and installs it on a connected Android device.

---

## Project Configuration

The current project configuration includes:

- **Minimum SDK:** Android 7.0 (API 24)
    
- **Target SDK:** API 36
    
- **Compile SDK:** API 37
    
- **Java compatibility:** Java 11
    
- **UI framework:** Jetpack Compose
    
- **Database:** Room
    
- **Camera:** CameraX
    

---

## Summary

**EyeAgeApp** is an Android application designed to measure visual age through user calibration, facial measurements, and visual testing.

The application combines camera-based computer vision, local data storage, and digital optotypes to provide a complete workflow for calibration, visual age testing, and reviewing previously collected results.
