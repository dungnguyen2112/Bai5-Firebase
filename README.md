# Movie Ticket App (Firebase - Android Java)

## 1) Tinh nang da lam
- Dang nhap / Dang ky bang Firebase Authentication (Email/Password)
- Danh sach phim tu Firestore + hien anh phim online (`imageUrl`)
- Xem chi tiet phim va dat ve tu man hinh chi tiet
- Luong dat ve: Phim -> Rap -> Gio chieu -> Chon ghe -> Xac nhan
- Chon ghe tu do (so luong tu dong theo so ghe da chon, toi da 10)
- Luu ve len Firestore (kem thong tin ghe da chon)
- Lich su ve da dat (`MyTicketsActivity`)
- Bottom navigation tren cac man hinh chinh: Trang chu / Ve cua toi / Tai khoan
- Loc phim theo danh muc (genre) + tu khoa ten phim
- Notification:
  - Thong bao dat ve thanh cong ngay sau khi dat
  - Nhac gio chieu local truoc 30 phut (`ReminderReceiver`)
  - Da co `FirebaseMessagingService` de nhan FCM

## 2) Firebase collections
- `users`
- `movies`
- `theaters`
- `showtimes`
- `tickets`
- `reminders`

## 3) Huong dan chay
1. Tao project Firebase va bat:
   - Authentication (Email/Password)
   - Cloud Firestore
   - Cloud Messaging
2. Tai `google-services.json` va dat vao:
   - `app/google-services.json`
3. Sync Gradle, sau do chay app tren Android Studio.

## 4) Cach test notification
- Test nhanh: nhan giu tieu de `Movie Ticket App` o man Trang chu, app se gui notification sau ~5 giay.
- Test dat ve: dat ve thanh cong se co thong bao "Dat ve thanh cong".
- Test nhac gio chieu: dat ve mot suat chieu sap toi, app se nhac truoc 30 phut.

## 5) Anh chup man hinh (ban bo sung)
Dat anh vao thu muc `screenshots/`, sau do chen vao README theo mau:

### Dang nhap / Dang ky
![Login](screenshots/login.png)
![Register](screenshots/register.png)

### Trang chu phim + Loc danh muc
![Movies](screenshots/movies.png)
![Filter](screenshots/filter.png)

### Chi tiet phim
![Movie Detail](screenshots/movie_detail.png)

### Chon rap / Gio chieu
![Theaters](screenshots/theaters.png)
![Showtimes](screenshots/showtimes.png)

### Chon ghe va tong tien
![Seat Selection](screenshots/seats.png)

### Ve cua toi
![My Tickets](screenshots/tickets.png)

### Tai khoan
![Profile](screenshots/profile.png)

## 6) Push len GitHub
```bash
git init
git add .
git commit -m "Build Movie Ticket App with Firebase auth, movie detail, seat booking, notifications"
git branch -M main
git remote add origin <your-repo-url>
git push -u origin main
```
