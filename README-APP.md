# OBD2 Injector Coder — Mobile App

This package is prepared as an installable Progressive Web App (PWA). It keeps the existing Next.js/PostgreSQL backend and adds:

- Android/iPhone home-screen installation support
- Standalone app display mode
- App icon and splash/theme metadata
- Service-worker fallback for the main shell
- Mobile-friendly portrait orientation

## Run it

```bash
npm install
npm run dev
```

Open the site on the phone using the computer/server URL, then use the browser menu and choose **Add to Home screen / Install app**.

## Important

The injector coding and diagnostic API routes are server-side Next.js routes and still require the Next.js server plus PostgreSQL. The PWA wrapper does not make those APIs offline.

For a true Android APK, deploy this app to a HTTPS URL first. Then it can be wrapped with Capacitor as a native Android shell while continuing to use the existing backend.
