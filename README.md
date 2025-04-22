# Movie Finder

![Android CI](https://github.com/gdomingues/android-app/actions/workflows/android.yml/badge.svg)

## What

Sample app designed to showcase a clean and modern Android architecture.

Make use of The Movie Database's API to display a list of trending movies and allows the user to
add and remove movies to/from a Watchlist.

The Movie Detail screen will be displayed upon clicking on a movie card on the Trending Movies and
Watchlist screens.

## How

- Built with `Android Studio Meerkat | 2024.3.1 Patch 1`, using the
  built-in [JetBrains Runtime](https://developer.android.com/build/jdks).
- Use Github Actions to run a CI on every commit and Pull Request
- Code coverage using Jacoco for both Unit and UI (Instrumented) tests

## Future improvements

- Add a toggle button to the movie card, so the user can add/remove it from the Watchlist without
  having to enter the Movie Detail screen
- Handle localization gracefully. My main goal was to create an app with a nice and clean
  architecture, including tests and a basic CI. Ideally in a real-world product the localization and
  string copy support would be better than the current state of this project
- Implement some logging/observability functionality, which couldn't be done within the available
  time.
- Add analytics support
- Even though some basic error handling was added (such as including a retry functionality when the
  trending movies' list can't be fetched), it's just displaying the error message as is. It could be
  integrated with the localization system.

## Important

- Remember to set the `ACCESS_TOKEN` field on `AuthInterceptor` with a token created
  on [The Movie Database](https://developer.themoviedb.org/docs/getting-started)
