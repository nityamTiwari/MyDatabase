# 📸 MySession (Kotlin, Android)

A simple yet powerful **session-based camera app** built with **Kotlin + CameraX + Room**.  
The app is designed for health or case tracking use-cases where you need to:

- Create **sessions** with basic metadata (Name, Age).
- Capture and store **photos per session**.
- Save everything in a local **SQLite database (Room)**.
- Retrieve sessions later with **search support**.

---

## ✨ Features

- 📷 **CameraX integration** — capture high-quality images using the device camera.
- 🗂 **Session management** — each session has its own subfolder & metadata.
- 💾 **Local storage** — all images are saved inside the app's private storage.
- 🔍 **Search sessions** by name or age.
- 📑 **View details** — see session info + all captured photos.
- 🚫 **No storage permissions needed** — only **Camera permission** is required.

---

## 🏗️ Tech Stack

- **Kotlin** (100%)
- **CameraX** — camera implementation
- **Room** — database (Session & Photo entities)
- **Coroutines + Flow** — async data handling
- **ViewModel** — state management
- **RecyclerView + Adapters** — lists & photo grids
- **Coil** — lightweight image loader

---

## 📂 Project Structure

