# OBD2 Injector Coder — deploy from an Android phone

This project is prepared for a cloud deployment. You do not need Android Studio or a computer.

## 1. Create a GitHub repository

On your Android phone, create a new GitHub repository and upload the contents of this folder.

## 2. Create a PostgreSQL database

Use a managed PostgreSQL provider. Copy its PostgreSQL connection string.

The application expects one environment variable:

`DATABASE_URL`

## 3. Deploy the web app

Import the GitHub repository into a Next.js-compatible cloud host such as Vercel.

Set the environment variable `DATABASE_URL` to your database connection string and deploy.

## 4. Create the database tables

After deployment, run the Drizzle schema push from a cloud shell or provider console:

`npx drizzle-kit push`

If the provider does not provide a shell, use its supported PostgreSQL SQL console and/or a temporary cloud development environment.

## 5. Android APK

Once the site has an HTTPS URL, use that URL as the Android app's web target. The Android wrapper project can then be built by a cloud CI service (GitHub Actions) without a local computer.

## Important safety note

The current OBD session implementation is a simulator. Do not use it to write injector/ECU data to a vehicle until a real Bluetooth/OBD transport and the correct vehicle-specific ECU protocol have been implemented and tested.
