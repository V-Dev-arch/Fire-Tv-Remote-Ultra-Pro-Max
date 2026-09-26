# Privacy

A plain-language summary of what Fire TV Remote Ultra Pro Max does and doesn't
do with your data.

## TL;DR

- The app **never collects personal data** beyond what you explicitly sign in
  with (Google, if you choose to sign in).
- The AI assistant processes your voice **server-side** — your audio is sent
  to a Supabase Edge Function, which forwards the *transcribed text* (not
  raw audio) to Google Gemini.
- Paired TVs and custom apps are stored in **your** Supabase account, gated
  by your Firebase ID token — no one else can read them.
- Anything unused for **30 days** is automatically cleaned up.
- No ads. No analytics SDKs. No third-party trackers.

## What's stored where

### On your phone

| What | Where | Lifetime |
|---|---|---|
| Auth tokens (Google, Firebase) | Android Keystore + Firebase SDK | Until you sign out |
| Last-known paired Fire TV (guest users) | Local cache only | Until app close |
| Crash trace (only if app crashes) | App's external files dir | Until you delete it |
| None of the above is uploaded | — | — |

### In your cloud account (only if signed in)

| What | Where | Lifetime |
|---|---|---|
| Paired Fire TVs (IP, name, client token) | Supabase `paired_devices` table | Until you remove the device OR 30 days of inactivity |
| Custom apps you added | Supabase `custom_apps` table | Until you remove the app OR 30 days since last open |
| AI daily-limit counter | Supabase `gemini_daily_usage` table | Reset at midnight UTC; row pruned after 7 days |

All reads/writes go through Supabase Edge Functions that verify your
Firebase ID token — there's no admin key path, no service-role bypass.

### Voice / AI

- **Audio is captured locally**, encoded, and sent to the
  `gemini-assistant` Edge Function over HTTPS.
- The Edge Function forwards the prompt to Google Gemini and returns the
  structured reply (e.g. *"launch_app: netflix"*).
- **Audio bytes are not persisted** — neither on the phone, nor on Supabase,
  nor on Gemini's side beyond Google's standard request handling.
- **Daily limit:** 30 requests / signed-in account / day. Reset at midnight
  UTC. The count is server-side, so reinstalling the app doesn't reset it.

## What we DON'T do

- ❌ No ad networks, analytics, or third-party trackers
- ❌ No ADB, no developer mode toggles, no rooting required
- ❌ No background location access (the `ACCESS_NETWORK_STATE` /
  `NEARBY_WIFI_DEVICES` permissions are for LAN discovery, not location)
- ❌ No clipboard access, no contact list access, no storage access beyond
  the app's own external-files directory (used only for crash-trace backup)
- ❌ No calls to any server other than Firebase, Supabase, and (via
  Supabase) Gemini
- ❌ No phone-home telemetry — we have no backend to phone home *to*

## Your controls

You can clear everything at any time:

| Action | Where |
|---|---|
| Remove a paired Fire TV | **Settings → Saved Fire TVs → Forget** |
| Remove a custom app | **Apps → ⋮ → Remove** |
| Delete all cloud data | **Settings → Account → Delete account data** |
| Revoke Google sign-in | **Settings → Sign out** — also revoke from your [Google account permissions page](https://myaccount.google.com/permissions) |
| Delete the app | Uninstall — clears everything on-device |

## Permissions explained

| Permission | Why |
|---|---|
| `INTERNET` | Talk to Firebase / Supabase / the Fire TV |
| `ACCESS_NETWORK_STATE` | Check if Wi-Fi is connected before LAN discovery |
| `ACCESS_WIFI_STATE` | Read the current SSID for LAN discovery |
| `CHANGE_WIFI_MULTICAST_STATE` | Receive SSDP multicast replies |
| `NEARBY_WIFI_DEVICES` | Android 13+ requirement for the same LAN discovery flow |
| `RECORD_AUDIO` | Capture your voice for the Gemini assistant |

No location permission is requested. `NEARBY_WIFI_DEVICES` is declared with
`usesPermissionFlags="neverForLocation"`.

## Inactivity cleanup (30-day rule)

To keep your saved-devices list from becoming a graveyard of TVs you no
longer own:

- Each paired TV has a `last_seen_at` timestamp, updated on pairing and
  every time the TV responds (at most once per hour).
- Each custom app has a `last_opened_at` timestamp, updated when opened from
  the Apps tab or by Gemini. Adding an app starts the clock.
- A daily `pg_cron` job at **03:00 UTC** (`purge-inactive-account-data`)
  deletes rows older than 30 days for everyone.
- Every list call also purges the *caller's* expired rows first, so the
  list you see is always current.
- You're told about this in **Settings → Storage & inactivity** and on the
  Apps tab.

## Contact

Questions or concerns about privacy?

- Open an issue on this repo
- Or reach out via the contact form on the [official site](https://firetvremoteproultra.vercel.app)

---

*Last updated: 2026*
