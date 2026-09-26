## 🔥 Fire TV Remote Ultra Pro Max — v1.0.0

The first public release of **Fire TV Remote Ultra Pro Max** — a no-ADB remote control for Amazon Fire TV with a built-in Gemini AI assistant.

### ✨ What's in v1.0.0

#### Remote control
- 📡 **Auto-discovery** — Fire TVs on your Wi-Fi appear automatically via SSDP/UPnP
- 🔐 **PIN pairing** — secure 4-digit pairing via the TV's own local control server, with a per-device client token
- 🧭 **Full d-pad** — Up / Down / Left / Right / Select, plus Home, Back, Menu
- ⏯️ **Media transport** — Play / Pause / Rewind / Fast-forward
- 🔊 **Volume** — Up / Down / Mute
- ⌨️ **Keyboard input** — Type on your phone, text appears on the TV (one char at a time via the TV's text endpoint)
- 🖐️ **Touchpad** — Swipe to navigate, tap to select
- 📱 **Quick Actions** — Power, sleep, app shortcuts

#### Apps
- One-tap launch for 16 popular apps with branded logos (Netflix, Prime Video, Disney+, YouTube, Spotify, Hulu, Apple TV, BBC iPlayer, Plex, Twitch, Peacock, Pandora, Spotify, ITV, Channel 4, Sky News, Amazon Music)
- Add your own custom apps by package name
- Per-user library, syncs across signed-in devices

#### 🤖 Gemini AI assistant
- Hold the mic button — say *"Open Netflix"*, *"Pause"*, *"Turn off my TV"*
- Server-side processing — your voice never touches Google's Gemini directly; the Supabase Edge Function proxies the request
- Supported commands: open app by name, fire remote button, turn off TV
- Refuses anything outside scope with a helpful list of what it *can* do
- **30 requests / signed-in account / day**, reset at midnight UTC

#### ☁️ Cloud sync (signed-in)
- Sign in with Google
- Paired Fire TVs and custom apps follow you across phones
- **30-day inactivity cleanup** — devices/apps unused for a month are auto-removed
- Per-account isolation — share your phone without sharing your TVs

#### 🎨 Design
- Custom multi-accent palette — teal, ember, violet (not the stock Material default)
- Dark-first, built for a dark room next to a TV
- Light & dark themes with proper contrast
- Material 3 + Jetpack Compose, smooth gradients

---

### 📥 Download

> **Filename:** `FireTvRemoteUltraProMax-v1.0.0.apk`

**Free download — only from Uptodown.** This is the canonical source for the APK.

👉 **[Download on Uptodown](https://en.uptodown.com)** (search *"Fire TV Remote Ultra Pro Max"*)

🌐 **Official site (advertising only, no APK hosting):** [firetvremoteproultra.vercel.app](https://firetvremoteproultra.vercel.app) — links straight to the Uptodown page.

> ⚠️ The official website does **not** host the APK file. It advertises the app and shares the Uptodown download link only. Do not install APKs from any other source.

---

### 🛠️ Few changes required before the next release

A short list of items still on the punch-list — these are honest gaps, not bugs you'd want to hide:

- [ ] **Real installed-apps sync** — currently the Apps tab uses a curated catalog of 16 popular apps. There's no documented Fire TV endpoint for a live list of installed apps, so the live sync is **not** implemented and shouldn't be claimed.
- [ ] **Hardware verification of volume / keyboard / state endpoint bodies** — the JSON payloads for volume, d-pad state, and keyboard text input were built from community write-ups of the undocumented Fire TV control API. The HTTP layer is isolated in one file behind a small interface, so a packet capture against real hardware + a one-line fix is all that's needed if any of them don't match your TV.
- [ ] **App icon / brand art** — the launcher icon is a placeholder vector mark. Commission proper brand art before promoting the app publicly.
- [ ] **Google Sign-In button glyph** — currently uses a placeholder. Google's branding guidelines require the official multi-color "G" asset before shipping.
- [ ] **Replace any leftover first-draft screenshots** on the official site with real device captures.
- [ ] **Confirm Uptodown listing** — once the v1.0.0 APK is uploaded to Uptodown, paste the direct store URL into both the official site and the README download section (currently just points to uptodown.com search).

---

### 🐛 Known limitations

- **Volume control** — depends on your TV model. Some models route volume through HDMI-CEC passthrough rather than the local control API. If volume doesn't work, that's the cause — not a bug.
- **Live app list** — see above. Static catalog only.
- **Alexa button** — the originally-spec'd Alexa button (forwarding voice into Amazon's Alexa service) is **not possible** for a third-party app and was replaced by the Gemini assistant button.

---

### 📋 Compatibility

| | |
|---|---|
| **Android** | 8.0 (API 26) and above |
| **Fire TV** | All current Fire TV models with the local control server enabled |
| **Network** | Phone and Fire TV on the same Wi-Fi network |
| **Account** | Optional — guests can pair without signing in (per-session only) |

---

### 🙏 Credits

Built by **V-Dev-arch** · Powered by [Firebase](https://firebase.google.com/), [Supabase](https://supabase.com/), and [Google Gemini](https://ai.google.dev/)

---

**Full Changelog**: https://github.com/V-Dev-arch/Fire-Tv-Remote-Ultra-Pro-Max/commits/v1.0.0
