# Architecture

This document gives a high-level overview of how Fire TV Remote Ultra Pro Max
fits together. The full source for the proprietary components lives in a
separate private repository; this page describes the system shape, not the
implementation details.

## Bird's-eye view

```
┌─────────────────────────────────────────────────────────────────┐
│                     Android app (Kotlin)                        │
│                                                                 │
│   ┌─────────┐    ┌──────────────┐    ┌──────────────────────┐   │
│   │  UI     │    │  State       │    │  Domain repositories │   │
│   │ Compose │◄──►│  (Flow +     │◄──►│                      │   │
│   │ screens │    │   StateFlow) │    │  · Auth              │   │
│   └─────────┘    └──────────────┘    │  · Discovery         │   │
│                                      │  · Fire TV client    │   │
│                                      │  · Account data      │   │
│                                      │  · Gemini assistant  │   │
│                                      └──────────┬───────────┘   │
└─────────────────────────────────────────────────┼───────────────┘
                                                  │
            ┌─────────────────────────────────────┼────────────────────────┐
            │                                     │                        │
            ▼                                     ▼                        ▼
   ┌────────────────┐                  ┌────────────────────┐   ┌────────────────────┐
   │  Firebase      │                  │   Fire TV (LAN)    │   │   Supabase         │
   │  Authentication│                  │   local control    │   │   · Postgres       │
   │  (Google)      │                  │   API (HTTPS)      │   │   · Edge Functions │
   └────────────────┘                  └────────────────────┘   │   · Auth bridge    │
                                                               └─────────┬──────────┘
                                                                         │
                                                                         ▼
                                                               ┌────────────────────┐
                                                               │   Google Gemini    │
                                                               │   (model API)      │
                                                               └────────────────────┘
```

## Layers

### 1. UI layer — Jetpack Compose

- Single-activity, Compose-only UI (`MainActivity`).
- Material 3 with a **custom palette** — dark-first, multi-accent (teal,
  ember, violet) instead of the stock Material default. Light theme included.
- Navigation via sealed `Screen` routes + bottom-tab `BottomTab` enum.
- Every screen reads theme colors through `LocalFirePalette` rather than
  hard-coded values, so light/dark switching is automatic.

### 2. State layer

- Coroutines + `StateFlow` for screen state.
- Repositories expose `Flow<T>` for reactive UI updates.
- No global mutable state — each screen owns its own view-model-shaped state
  holder.

### 3. Domain repositories

- **`AuthRepository`** — wraps Firebase Auth (Google sign-in), exposes the
  current Firebase ID token to other layers.
- **`SsdpDiscovery`** — sends SSDP `M-SEARCH` multicast on the LAN, parses
  responses to find Fire TVs.
- **`FireTvClient`** — single class behind a small interface that handles
  PIN pairing (`pin/display`, `pin/verify`) and every subsequent control
  call (dpad, media, volume, apps, keyboard). Returns a per-device client
  token after pairing that all later calls must carry.
- **`AccountDataRepository`** — talks to the `paired-devices` Supabase Edge
  Function to read/write the user's paired TVs and custom apps.
- **`GeminiAssistantRepository`** + **`VoiceCaptureController`** — captures
  mic audio, posts it to the `gemini-assistant` Edge Function, surfaces the
  command + daily-limit state to the UI.

### 4. Backend services

- **Firebase Auth** — Google sign-in, ID token issuance, Firebase Third-Party
  Auth on Supabase.
- **Supabase Postgres** — `paired_devices` and `custom_apps` tables, all
  reads/writes gated by Firebase ID token verification on the Edge Function.
- **`paired-devices` Edge Function** — list/add/remove paired TVs and custom
  apps. Performs the 30-day inactivity cleanup for the caller on every read.
- **`gemini-assistant` Edge Function** — verifies the caller's Firebase ID
  token, checks/enforces the daily 30-request limit, forwards the prompt to
  Gemini, returns the assistant's reply. The Gemini API key lives only in
  Supabase secrets — never in the app.
- **Daily `pg_cron` job** (`purge-inactive-account-data`, 03:00 UTC) —
  removes expired rows across all users.

## Data flow: a typical voice command

1. User taps the mic button on the **Remote** screen.
2. `VoiceCaptureController` starts recording via `RECORD_AUDIO`.
3. User stops → audio bytes are packaged into a request.
4. `GeminiAssistantRepository` POSTs to the `gemini-assistant` Edge
   Function, attaching the current Firebase ID token in the `Authorization`
   header.
5. Edge Function:
   - verifies the ID token against Firebase,
   - checks/increments the user's daily count for that account,
   - forwards the prompt + supported-command list to Gemini,
   - returns the assistant's structured reply.
6. The app receives the reply, parses the intent, and dispatches the
   matching `FireTvClient` call (e.g. `launchApp("netflix")`).
7. UI updates: button press feedback, daily-count badge, and (if a TV
   command) the live remote state.

## Security boundaries

| Boundary | Mechanism |
|---|---|
| App ↔ Firebase | Firebase SDK (HTTPS, no cleartext option exposed) |
| App ↔ Supabase | Supabase SDK + Firebase ID token in `Authorization` header |
| Supabase ↔ Firebase | Firebase Third-Party Auth — Supabase verifies ID tokens natively |
| Supabase Edge Fn ↔ Gemini | API key in Supabase secrets; not in app code |
| App ↔ Fire TV | LAN only. PIN-paired per device. Per-device client token, not reusable across TVs. Cleartext limited to local subnet IPs the user discovered themselves. |
| App ↔ Internet (other) | None. The app makes no other outbound calls. |

## Tech versions

| | Version |
|---|---|
| compileSdk | 35 |
| targetSdk | 35 |
| minSdk | 26 (Android 8.0 Oreo) |
| Kotlin | 2.3 |
| AGP | 8.13 |
| Gradle | 8.x |
| Jetpack Compose | via Material 3 BOM |
| Firebase BoM | latest |
| Supabase Kotlin SDK | latest |
