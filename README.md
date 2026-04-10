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
<img width="1920" height="1020" alt="bai5-firebase - Database - Data - Firebase console - Google Chrome 4_10_2026 5_59_20 PM" src="https://github.com/user-attachments/assets/d1d62f69-c75f-45be-b4c2-bcdb2d264dcd" />

### Dang nhap / Dang ky
<img width="1080" height="2400" alt="Screenshot_20260410_175032" src="https://github.com/user-attachments/assets/7726cb3b-47d3-40f2-b02e-bbcbfd4f4a6b" />
<img width="1080" height="2400" alt="Screenshot_20260410_175450" src="https://github.com/user-attachments/assets/e7e2322e-54fc-468a-bb36-27da988fb87e" />


### Trang chu phim + Loc danh muc
<img width="1080" height="2400" alt="Screenshot_20260410_175524" src="https://github.com/user-attachments/assets/36c9818d-709a-4768-b6e3-cdbf281bf317" />
<img width="1080" height="2400" alt="Screenshot_20260410_175543" src="https://github.com/user-attachments/assets/9ffa5d14-1f36-4a30-a507-81c2202a511a" />

### Chi tiet phim
<img width="1080" height="2400" alt="Screenshot_20260410_175617" src="https://github.com/user-attachments/assets/1dd6edb1-0b51-4d3d-998c-cc17239deb21" />

### Chon rap / Gio chieu
<img width="1080" height="2400" alt="Screenshot_20260410_175640" src="https://github.com/user-attachments/assets/3a71e208-bd5c-4edb-aff9-16a2b661f8d4" />
<img width="1080" height="2400" alt="Screenshot_20260410_175655" src="https://github.com/user-attachments/assets/7d90ac74-b4e1-46fd-b45d-72bd74418c06" />

### Chon ghe va tong tien
<img width="1080" height="2400" alt="Screenshot_20260410_175715" src="https://github.com/user-attachments/assets/4ca78a20-beff-4d32-ae9d-8871fd1e4b08" />

### Ve cua toi
<img width="1080" height="2400" alt="Screenshot_20260410_175736" src="https://github.com/user-attachments/assets/5f5e6168-90e0-4e21-939c-7b1493c10e78" />

## 6) Push len GitHub
```bash
git init
git add .
git commit -m "Build Movie Ticket App with Firebase auth, movie detail, seat booking, notifications"
git branch -M main
git remote add origin <your-repo-url>
git push -u origin main
```
