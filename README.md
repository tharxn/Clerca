# Clerca

<p align="center">
  <em>A calm, focused space for everything you need to stay on top of your day.</em>
</p>

<p align="center">
  <a href="https://clerca.vercel.app">🌐 Live App</a> &nbsp;·&nbsp;
  <a href="https://github.com/tharxn/Clerca-frontend">Frontend Repo</a> &nbsp;·&nbsp;
  <a href="https://github.com/tharxn/Clerca-backend">Backend Repo</a>
</p>

---

## What is Clerca?

Clerca is a personal productivity dashboard that brings your notes, tasks, calendar, and clock together in one beautifully minimal interface — no clutter, no distractions, just the tools you actually use every day.

Whether you're capturing a quick thought, planning your week, or just checking the time and weather, Clerca keeps everything within reach in a single, focused workspace.

> 💡 **No account? No problem.** You can explore the full dashboard as a guest without signing up. Create an account when you're ready to save your data.

---

## ✨ Features

### 📝 Notes
- Write and organise your thoughts with a clean, distraction-free editor
- Notes are auto-titled with the current date if you leave the title blank
- Browse all your notes at a glance with content previews and timestamps
- Edit or delete any note directly from the list or the full note view

### ✅ Tasks
- Add tasks with a title, optional description, **priority level** (Low / Medium / High), and a **due date**
- Switch between three smart views — **Active**, **Completed**, and **Missed** — so nothing slips through
- Satisfying animated strikethrough when you complete a task, with a 4-second **Undo** option in case you change your mind
- Edit any task in full detail at any time

### 📅 Calendar
- A clean month-view calendar where you can add a personal note or event to any date
- Choose from **7 colours** to tag your events (violet, rose, amber, teal, sky, emerald, pink)
- All your events for the month are listed below the calendar, sorted by date
- Navigate months with a smooth animated picker — jump to any month and year instantly

### 🕐 Clock & Weather
- A sleek **digital clock** with smooth animated digit transitions, or switch to a classic **analog** face
- Built-in **stopwatch** with lap tracking for when you need to time something
- Real-time **local weather** — temperature, wind speed, and conditions — fetched automatically based on your location
- **Search any city** to check its current weather and time
- Your location preference is remembered so you never have to search twice

---

## 🎨 Designed for comfort

- 🌙 **Dark mode**, ☀️ **Light mode**, and 🖥️ **System mode** — switch with an animated toggle that actually looks good
- Fully **responsive** — works beautifully on desktop and adapts gracefully to mobile
- Custom scrollbars, smooth transitions, and thoughtful micro-interactions throughout
- Your theme preference is always remembered across sessions

---

## 🔐 Your data, your account

- Sign in with **email and password**, or jump straight in with **Google**
- Your notes, tasks, and calendar events are private to your account — nobody else can see them
- Secure JWT-based authentication with automatic session refresh so you stay logged in seamlessly
- Want to try it first? **Explore as a guest** — no signup required

---

## 🛠️ Built with

| Layer | Technology |
|---|---|
| Frontend | Next.js 16, TypeScript, Tailwind CSS |
| Backend | Spring Boot 3, Java 21, Spring Security |
| Database | PostgreSQL (Neon) |
| Auth | JWT + Google OAuth2 |
| Hosting | Vercel (frontend) · Render (backend) |
| Weather | Open-Meteo API |
| Maps | OpenStreetMap / Nominatim |

---

## ⚡ A note on loading speed

Clerca's backend is hosted on Render's free tier. If the app hasn't been visited in a while, **the first load may take up to 30–60 seconds** as the server wakes up from sleep. This is completely normal — once it's awake, everything runs fast. Subsequent visits within the same active window are instant.

---

## 🚀 Try it now

**[→ Open Clerca](https://clerca.vercel.app)**

No installation. No setup. Just open and go.

---

<p align="center">Developed with ♥ by <strong>Tharun Kumar</strong></p>
