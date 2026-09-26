<div align="center">

<img src="app/src/main/res/drawable/ic_logo_foreground.xml" alt="Fire TV Remote Ultra Pro Max" width="120" />

# Fire TV Remote Ultra Pro Max

### A powerful, no-ADB remote for your Amazon Fire TV — with a built-in AI assistant.

[![Platform](https://img.shields.io/badge/platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](#)
[![Min SDK](https://img.shields.io/badge/min%20SDK-26%20(Android%208.0)-1f6feb?style=for-the-badge)](#)
[![Target SDK](https://img.shields.io/badge/target%20SDK-35%20(Android%2015)-1f6feb?style=for-the-badge)](#)
[![License](https://img.shields.io/badge/license-MIT-22c55e?style=for-the-badge)](#LICENSE)
[![Downloads](https://img.shields.io/badge/download-free-ff8a4c?style=for-the-badge)](#-download)
[![Official Site](https://img.shields.io/badge/website-firetvremoteproultra.vercel.app-22e7be?style=for-the-badge)](https://firetvremoteproultra.vercel.app)

[**🌐 Official Site**](https://firetvremoteproultra.vercel.app) · [**📥 Download (free)**](#-download) · [**🐛 Report a bug**](../../issues) · [**💡 Request a feature**](../../issues)

</div>

---

## ✨ What is it?

**Fire TV Remote Ultra Pro Max** is a third-party Android app that turns your phone into a fully-featured remote for any Amazon Fire TV on the same Wi-Fi network — **no ADB, no root, no developer-mode toggles**.

Pair in seconds with a 4-digit PIN, then control everything from your couch: navigation, media playback, volume, apps, keyboard input, and even a touchpad for the times the on-screen keyboard is too slow.

The killer feature? A **built-in Gemini AI assistant**. Hold the mic button and say *"Open Netflix"* or *"Turn off my TV"* — it just works.

---

## 🎯 Features

### 🎮 Full remote control
- 📡 **Auto-discovery** — your Fire TVs appear on the network automatically (SSDP/UPnP)
- 🔐 **PIN pairing** — secure 4-digit pairing via the TV's own local control server
- 🧭 **D-pad, Home, Back, Menu, Select** — every standard navigation button
- ⏯️ **Media transport** — play, pause, rewind, fast-forward
- 🔊 **Volume** — up, down, mute
- ⌨️ **Keyboard input** — type on your phone, text appears on the TV
- 🖐️ **Touchpad** — swipe to navigate, tap to select

### 📺 Apps tab
- Launch any installed app by name with one tap
- Add your own custom apps by package name
- 16 popular apps pre-loaded with branded logos (Netflix, Prime Video, Disney+, YouTube, Spotify, Plex, …)

### 🤖 Gemini AI assistant
- Voice commands like *"Open YouTube"*, *"Pause"*, *"Turn off my TV"*
- Server-side processing — no AI calls on your phone, no battery drain
- Daily limit: 30 requests/account (resets at midnight UTC)
- Strictly limited to supported commands — refuses anything outside its scope

### ☁️ Cloud-synced (signed-in users)
- Sign in with Google
- Your paired Fire TVs and custom apps follow you across devices
- Auto-cleanup: devices/apps unused for 30 days are removed
- Per-account — share your phone with family without sharing your TVs

### 🎨 Beautiful, dark-first design
- Custom multi-color palette (teal, ember, violet) — not the usual flat Material default
- Built for dark rooms next to a TV
- Smooth gradients, real depth, no clutter
- Light & dark themes with proper contrast

---

## 📥 Download

The app is **free to download** — no ads, no premium tier, no in-app purchases.

| Source | Notes |
|---|---|
| 📦 **Uptodown** *(canonical APK host)* | **[Download on Uptodown →](https://en.uptodown.com)** — the only place the official APK is hosted. Independent app store, no Google account required |
| 🌐 **[Official site](https://firetvremoteproultra.vercel.app)** | **Advertising only.** The site does **not** host the APK — it just advertises the app and links directly to the Uptodown page |
| 🏪 **GitHub Releases** | This repo's [Releases tab](../../releases) — release notes + changelog. APK is *not* attached here either; we point you to Uptodown |

> ⚠️ **Where to get the APK:** Only from **Uptodown**. The official website (`firetvremoteproultra.vercel.app`) advertises the app and shares the Uptodown link — it does **not** host the APK file. Do not install APKs from any other source.
>
> **Not affiliated with, endorsed by, or sponsored by Amazon.** "Fire TV" is a trademark of Amazon.com, Inc.

---

## 🏗️ How it's built

### High-level architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Android App (Kotlin)                     │
│                                                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────────┐  │
│  │   Auth   │  │ Discovery│  │ Fire TV  │  │   Gemini   │  │
│  │  (Firebase│  │  (SSDP)  │  │  Client  │  │ Assistant  │  │
│  │   Auth)  │  │          │  │          │  │  (voice)   │  │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └─────┬──────┘  │
│       │             │             │              │         │
│       ▼             ▼             ▼              ▼         │
│  ┌─────────────────────────────────────────────────────┐   │
│  │       Jetpack Compose UI + Material 3 theme         │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
        │              │              │              │
        ▼              ▼              ▼              ▼
   ┌────────┐    ┌──────────┐   ┌──────────┐   ┌──────────┐
   │Firebase│    │  Local   │   │ Fire TV  │   │ Supabase │
   │  Auth  │    │ Network  │   │   (LAN)  │   │  Edge Fn │
   │        │    │ (SSDP/   │   │ HTTPS    │   │  (Gemini │
   │        │    │  HTTPS)  │   │ control  │   │  proxy)  │
   └────────┘    └──────────┘   └──────────┘   └──────────┘
```

### Tech stack

| Layer | Technology |
|---|---|
| **UI** | Jetpack Compose · Material 3 · Custom theme |
| **Language** | Kotlin (Coroutines, Flow) |
| **Auth** | Firebase Authentication (Google sign-in) |
| **Backend** | Supabase (Postgres + Edge Functions) |
| **AI** | Google Gemini (server-side, proxied through Supabase Edge Function) |
| **Discovery** | SSDP/UPnP multicast over local Wi-Fi |
| **TV control** | Fire TV's own local HTTPS control API (PIN-paired, per-device client token) |
| **Build** | Gradle 8.x · AGP 8.13 · Kotlin 2.3 · compileSdk 35 |

### App structure

```
app/src/main/java/com/ultraprodev/firetvremote/
├── FireTvRemoteApp.kt          # Application class — Supabase + Firebase init
├── MainActivity.kt             # Single Compose host
├── CrashActivity.kt            # Crash-recovery screen
├── data/
│   ├── auth/                   # Firebase Auth wrapper
│   ├── discovery/              # SSDP/UPnP device discovery
│   ├── firetv/                 # Fire TV client + app catalog
│   └── gemini/                 # Voice capture + AI request routing
├── nav/
│   └── Screen.kt               # Sealed navigation routes
└── ui/
    ├── screens/                # Compose screens (Auth, Remote, Apps, …)
    └── theme/                  # Custom Material 3 color scheme + typography
```

### 🔒 Privacy & security highlights

- **No ADB.** Works with stock Fire TVs out of the box.
- **PIN pairing per device.** Each Fire TV has its own client token; revoking one doesn't affect the others.
- **AI processing is server-side.** Your voice never reaches Google's Gemini directly — the Supabase Edge Function verifies your Firebase ID token, then proxies the request.
- **30/day AI limit per signed-in account.** Counted server-side, so reinstalling the app doesn't reset it.
- **30-day inactivity cleanup.** Paired Fire TVs and custom apps you haven't touched in a month are automatically removed from your account.
- **Local data only on guest devices.** Guests (not signed in) pair every session and their added apps last only until the app closes.
- **Cleartext only on your own LAN.** Firebase, Supabase, and the Gemini endpoint are HTTPS by construction. The only cleartext traffic is to the Fire TV IP you discovered yourself. See [`app/src/main/res/xml/network_security_config.xml`](app/src/main/res/xml/network_security_config.xml).

See [**docs/PRIVACY.md**](docs/PRIVACY.md) for the full breakdown.

---

## 🛠️ Development

This repo is the **public showcase** for Fire TV Remote Ultra Pro Max. The full source (including the proprietary Fire TV control client and server-side Edge Functions) is in a separate private codebase repo.

What you'll find here:
- The public Android manifest, theme, resources, and navigation structure
- App launcher icon (vector)
- Documentation: architecture, privacy, screenshots
- A representative subset of the UI layer for reference

### Building from this repo

This repo is **not a buildable Android project on its own** — it's a public showcase. The buildable source lives in the private `Fire-Tv-Remote-Ultra-Pro-Max-Codebase` repository, which contains:

- The full Kotlin source (auth, discovery, Fire TV client, Gemini integration)
- Supabase migrations and Edge Functions
- CI/CD pipeline (GitHub Actions for APK build + Edge Function deploy)

### Tech requirements (of the full codebase)

| | |
|---|---|
| **Android Studio** | Koala or newer |
| **JDK** | 17 |
| **Android SDK** | API 26 min · API 35 target |
| **Gradle** | 8.x |
| **Kotlin** | 2.3 |
| **Backend** | Supabase project · Firebase project |

---

## 🤝 Contributing

Bug reports and feature requests are very welcome — please [**open an issue**](../../issues) on this repo.

If you'd like to contribute code, reach out via the official site first — the buildable codebase is in a separate repo, and PRs against *this* showcase repo are limited to docs/screenshots.

---

## 📜 License

The contents of **this** repository are released under the [MIT License](LICENSE).

The proprietary source code (Fire TV client implementation, Supabase Edge Functions, Gemini integration logic) remains the exclusive property of the author and is **not** part of this public repo.

---

## 🙏 Credits

- Built by **V-Dev-arch**
- Powered by [Firebase](https://firebase.google.com/), [Supabase](https://supabase.com/), and [Google Gemini](https://ai.google.dev/)
- App launcher icons for streaming services are the property of their respective owners and used here only for direct-launch identification

---

<div align="center">

**[🌐 Visit the official site →](https://firetvremoteproultra.vercel.app)**

Made with 🔥 for Fire TV owners who'd rather not dig through the couch cushions for the remote again.

</div>
