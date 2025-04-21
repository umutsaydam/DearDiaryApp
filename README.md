<div align="center">
  <span><a href="#uygulama-icerik">TR</a></span> • 
  <span><a href="#app-content">EN</a></span>
</div>

---

# 📋 İçindekiler

- [Özellikler](#özellikler)
- [Kullanılan Teknolojiler](#kullanilan-teknolojiler)
- [Kurulum](#kurulum)
- [Arayüz](#arayuz)
- [Katkıda Bulunma](#katkida-bulunma)
- [Lisans](#lisans)

---

## <p id="uygulama-icerik">📔 DearDiary – Duygularınızı Kaydedin, Kendinizi Keşfedin</p>

**DearDiary**, günlüklerinizi yazabileceğiniz, ruh halinizi takip edebileceğiniz ve zaman içinde kendinizi daha yakından tanımanıza yardımcı olacak modern bir mobil günlük uygulamasıdır.

## <p id="özellikler">🚀 Özellikler</p>

- ✍️ **Günlük Yazma**  
  Günlüklerinizi kolayca yazabilir, geçmişe dönüp anılarınızı gözden geçirebilirsiniz.

- 😊 **Duygu Takibi**  
  - Emojiler ile manuel ruh hali seçimi  
  - 🧠 Makine öğrenmesi destekli *otomatik duygu analizi* (metinden ruh hali tespiti)

- 📊 **İstatistikler**  
  Günlük ruh hali değişimlerinizi grafiklerle takip edin.

- 🔐 **Gizlilik**  
  PIN ve uygulama kilidi ile verilerinizi koruyun.

### 🌐 Backend

Uygulamanın backend servisine aşağıdaki bağlantıdan ulaşabilirsiniz:  
👉 [Backend Repository](https://github.com/umutsaydam/DearDiaryAPI)

### 🛡️ Güvenlik

- PIN koruması  
- Parmak izi kilidi

---

## <p id="kullanilan-teknolojiler">🛠 Kullanılan Teknolojiler</p>

- **Kotlin**: Uygulama programlama dili.
- **Jetpack Compose**: Kullanıcı arayüzü oluşturmak için modern Android araç takımı.
- **Navigation Component**: Sayfa geçişlerinin yönetimi.
- **Room Database**: Lokal veritabanı işlemlerin gerçekleştirilmesi.
- **Data Store**: Anahtar/değer çiftlerini veya türlenmiş nesneleri depolanmasını sağlayan bir veri depolama çözümüdür.
- **Dagger Hilt Dependency Injection (Bağımlılık Enjeksiyonu)**: Hilt, bağımlılıkları merkezi bir yerde tanımlayıp yöneterek kod tekrarını azaltır ve kodun okunabilirliğini artırır. Aynı zamanda test edilebilirliği kolaylaştırır ve uygulamanın daha modüler bir yapıya kavuşmasını sağlar.
- **Coroutines**: işlemleri ana iş parcacığından (main thread) ayırarak arka planda çalışmasını sağlar. Bu sayede uzun süren işlemler (ağ istekleri, veritabanı işlemleri vs.) sırasında uygulamanın kullanıcı arayüzünün donmasını engeller.
- **Splash Screen**: Uygulamanın başlatılma sürecinde kullanıcıya görsel bir geçiş sağlar ve uygulamanın daha hızlı açılabilmesi için arka planda işlemler yapılırken kullanıcıyı bilgilendirir.
- **Notification**: Kullanıcılara uygulama hakkında önemli bilgiler, güncellemeler veya hatırlatıcılar göndermeyi sağlayan bir mekanizmadır.
- **Retrofit2**: Ağ istekleri gerçekleştirme sürecini basitleştiren, güvenli bir HTTP istemci kütüphanesidir.
- **Lottie Files**: Lottie, vektör grafik animasyonu için bir dosya formatıdır.
- **Biometric**: Uygulamanızın desteklediği kimlik doğrulama türlerini (parmak izi, yüz tanıma vb.) tanımlamak için kullanılır.
- **Serilizatiom**: Sınıf adını ve nesneleri koruyarak karşılık gelen değerleri oluşturur.
- **WorkManager**: Esnek planlama aralıkları kullanarak çalışmaları tek seferlik veya tekrar tekrar çalıştıracak şekilde planlamanıza olanak tanır. (Remind bildirimi için kullanıldı.)
- **MPAndroidChar**: Güçlü bir veri grafik kütüphanesidir.

---

## <p id="kurulum">📦 Kurulum</p>

1. Bu projeyi klonlayın:
    ```bash
    git clone https://github.com/umutsaydam/DearDiaryApp.git
    ```
2. Android Studio'da açın.  
3. `Gradle Sync` işlemini gerçekleştirin.  
4. Uygulamayı çalıştırın.

---

## <p id="arayuz">🧑‍💻 Arayüz</p>

- ![Image](https://github.com/user-attachments/assets/1f5d6117-da65-4025-bb7c-b711e7aa3da1)

---

## <p id="katkida-bulunma">🤝 Katkıda Bulunma</p>

Katkıda bulunmak istiyorsanız, bir `pull request` gönderin veya bir `issue` açın. Katkılarınız memnuniyetle karşılanacaktır!

---

## <p id="lisans">📜 Lisans</p>

Bilgi için `LICENSE` dosyasına göz atabilirsiniz.

---

---

# 📋 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [UI](#ui)
- [Contribution](#contribution)
- [License](#license)

---

## <p id="app-content">📔 DearDiary – Write Your Feelings, Discover Yourself</p>

**DearDiary** is a modern mobile diary app that lets you write your daily thoughts, track your mood, and reflect on your emotions over time.

## <p id="features">🚀 Features

- ✍️ **Daily Journaling**  
  Easily write your entries and revisit your memories.

- 😊 **Mood Tracking**  
  - Manual mood selection with emojis  
  - 🧠 AI-powered *automatic mood detection* from your diary text

- 📊 **Statistics**  
  Visualize your emotional trends over time.

- 🔐 **Privacy**  
  PIN and biometric lock to protect your data.

### 🌐 Backend

You can access the backend service from the link below:  
👉 [Backend Repository](https://github.com/umutsaydam/DearDiaryAPI)

### 🛡️ Security

- PIN protection  
- Biometric authentication

---

## <p id="technologies-used">🛠 Used Technologies</p>
- **Kotlin**: The programming language used for the application.
- **Jetpack Compose**: A modern Android toolkit for building user interfaces.
- **Navigation Component**: Manages navigation and screen transitions.
- **Room Database**: Handles local database operations.
- **Data Store**: A data storage solution for storing key-value pairs or typed objects.
- **Dagger Hilt Dependency Injection**: Hilt helps define and manage dependencies in a centralized way, reducing code duplication and increasing readability. It also enhances testability and supports a more modular architecture.
- **Coroutines**: Allows operations to run in the background by separating them from the main thread. This prevents the UI from freezing during long-running tasks like network or database operations.
- **Splash Screen**: Provides a visual transition while the app is launching, informing the user while background operations are performed to speed up app startup.
- **Notification**: A mechanism to send users important updates, reminders, or alerts about the application.
- **Retrofit2**: A secure HTTP client library that simplifies network request handling.
- **Lottie Files**: A file format for rendering vector graphic animations.
- **Biometric**: Used to define the authentication types supported by the app (e.g., fingerprint, facial recognition).
- **Serialization**: Preserves class names and objects to generate corresponding values.
- **WorkManager**: Allows you to schedule work to run once or periodically using flexible timing options. (Used for reminder notifications.)
- **MPAndroidChart**: A powerful data charting library.
  
---

## <p id="installation">📦 Setup</p>

1. Clone the project:
    ```bash
    git clone https://github.com/umutsaydam/DearDiaryApp.git
    ```
2. Open with Android Studio  
3. Perform `Gradle Sync`  
4. Run the app

---

## <p id="ui">🧑‍💻 UI</p>

- ![Image](https://github.com/user-attachments/assets/1f5d6117-da65-4025-bb7c-b711e7aa3da1)

---

## <p id="contribution">🤝 Contribution</p>

Feel free to open an `issue` or submit a `pull request`. Contributions are always welcome!

---

## <p id="license">📜 License</p>

See `LICENSE` file for details.
