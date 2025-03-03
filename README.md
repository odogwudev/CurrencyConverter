# Currency Converter

A Jetpack Compose Android app that fetches exchange rates from the [Fixer.io](https://fixer.io/) API and converts between currencies offline (using Room) or via real-time lookups. Built with **Hilt**, **Retrofit**, **Room**, **Coroutines**, and **Flow** for a clean, reactive architecture.

<p align="center">
  <img src="https://github.com/odogwudev/CurrencyConverter/blob/master/Screenshot_20250302_180739.png" width="250" />
  <img src="https://github.com/odogwudev/CurrencyConverter/blob/master/Screenshot_20250302_180752.png" width="250" />
</p>

---

## Features

- **Local Conversion** using a fallback database (Room) with rates from **Fixer.io** (free plan, base=EUR).
- **Offline Mode**: once rates are cached, conversions work without network.
- **Jetpack Compose** UI with a modern design pattern.
- **ViewModel + Hilt** for clean dependency injection.
- **Coroutines + Flow**: asynchronous updates, real-time DB changes.

---

## Demo

**Video Walkthrough**  
[![Video Demo](https://img.shields.io/badge/View%20video-Click%20here-blue.svg)](https://github.com/odogwudev/CurrencyConverter/blob/master/Screen_recording_20250302_180838.mp4)

[![Video Demo](https://img.shields.io/badge/View%20video-Click%20here-blue.svg)](https://github.com/odogwudev/CurrencyConverter/blob/master/Screen_recording_20250303_095800.webm)


[![Video Demo](https://img.shields.io/badge/View%20video-Click%20here-blue.svg)](https://github.com/odogwudev/CurrencyConverter/blob/master/Screen_recording_20250303_095831.webm)


*(If the video doesn’t autoplay, right-click and select "Open Link in New Tab," or download to view.)*

---

## Architecture Overview

1. **Remote Data**: Fetched from Fixer.io via **Retrofit**.  
2. **Local Cache**: Stored in **Room** as `ExchangeRateEntity` rows, all relative to `EUR`.  
3. **Repository**: Handles network fetch + DB storage.  
4. **ViewModel**: Exposes `Flow<Resource<...>>` states for the UI.  
5. **UI Layer**: Jetpack Compose screens collecting flows, displaying conversion results or errors.

---

## How It Works

1. On app launch, you can **load** the latest rates (EUR-based).
2. When the user selects a “from” and “to” currency + amount, the app:
   - Checks if the currencies exist locally.
   - If missing, fetches them from the Fixer API, stores in Room.
   - Computes `(amount / fromRate) * toRate` to produce the final conversion result.
3. Displays the result or an error message if something fails.

---

## Screenshots

| Home Screen | Conversion | Select Country | 
|-------------|-----------|-----------|
| <img src="https://github.com/odogwudev/CurrencyConverter/blob/master/Screenshot_20250302_180739.png" width="250"/> | <img src="https://github.com/odogwudev/CurrencyConverter/blob/master/Screenshot_20250302_180752.png" width="250"/> | <img src="https://github.com/odogwudev/CurrencyConverter/blob/master/Screenshot_20250303_095734.png" width="250"/> | 

---

## Getting Started

1. **Clone** this repo:
   ```bash
   git clone https://github.com/odogwudev/CurrencyConverter.git
