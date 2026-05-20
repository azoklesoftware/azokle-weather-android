# Contributing to Azokle Weather

First off, thank you for considering contributing to Azokle Weather! It's people like you that make open source software such a great community to learn, inspire, and create.

## Where to start

* **Bugs:** If you find a bug, please check the [Issue Tracker](https://github.com/azoklesoftware/azokle-weather-android/issues) first to see if it has already been reported. If not, open a new issue with a clear description, steps to reproduce, and your device details.
* **Features:** If you have an idea for a new feature, please open a feature request issue to discuss it before starting work. This helps avoid duplicated efforts and ensures it aligns with the project's roadmap.
* **Code:** Look for issues tagged with `good first issue` or `help wanted` if you're looking for something to tackle. 

## Development Environment Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/azoklesoftware/azokle-weather-android.git
   cd azokle-weather-android
   ```

2. **Open in Android Studio:**
   Import the project into the latest stable version of Android Studio. The build system is Gradle and it will download necessary dependencies automatically.

3. **Build the App:**
   Run `./gradlew assembleDebug` or use the IDE Run button.

## Pull Request Process

1. Fork the repo and create your branch from `dev`.
2. Ensure you have tested your changes. If you've added new functionality, please try to add unit tests if applicable.
3. Keep your PRs focused on a single logical change.
4. Update the `README.md` or other documentation if your change warrants it.
5. Submit the PR targeting the `dev` branch.

## Code Style

This project follows the official Kotlin style guide. The codebase is heavily reliant on Jetpack Compose, so please adhere to Compose best practices (hoisting state, avoiding side effects in composables when not intended, etc.).

By contributing to Azokle Weather, you agree that your contributions will be licensed under the GPLv3 License.
