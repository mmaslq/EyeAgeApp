EyeAgeApp – Aplikacja do pomiaru wieku wzroku

Opis Systemu
EyeAgeApp to aplikacja mobilna na Androida umożliwiająca mierzenia wieku oczu użytkownika. 
Aplikacja została wykonana w architekturze Modern Android Architecture (MVVM/MVI) z Jetpack Compose.

Uruchomienie Programu 
./gradlew installDebug

Funkcjonalności

1. Kalibracja użytkownika
- Wyświetlana jest informacja dla użytkownika o przebiegu kalibracji
- Użytkownik wykonuje zdjęcie swoich tęczówek 3 razy
- Wyliczane na tej podstawie jest odległość pomiędzy środkami źrenic użytkownika
- Użytkownik ma możliwość odrzucenia pomiaru, końcowego wyniku oraz ponownego wyświetlenia informacji o przebiegu kalibracji
2. Wykonanie testu
- Wyświetlana jest informacja dla użytkownika o przebiegu testu
- Użytkownik ustala minimalną widoczną czcionkę trzymając stałą odległość od głowy od telefonu
- wykonuje zdjęcie twarzy 3 razy
- Wyliczane na tej podstawie jest wiek użytkownika
- Użytkownik ma możliwość odrzucenia pomiaru, końcowego wyniku oraz ponownego wyświetlenia informacji o przebiegu testu
4. Przeglądanie danych
- Użytkownik może przeglądać dane kalibracji oraz wybrać konkretną kalibracje do testu
- Użytkownik może przeglądać dane testów, wybierając odpowiedni wynik kalibracji

Działanie Systemu

Połączenie i Logika
- System działa jako aplikacja mobilna.
- Wszystkie zmiany są widoczne w przeglądaniu danych.
- W przypadku błędów system informuje użytkownika.

Zachowanie Aplikacji
- Po kalibracji system automatycznie zapamiętuje id kalibracji.
- Wykonane testy oraz kalibracje pozostają w pamięci urządzenia.
- Użytkownik może w każdej chwili opuścić aplikację.

Metoda liczenia wieku wzroku:
- Podczas Kalibracji wyliczane jest na podstawie średniej odległości tęczówki, odległość pomiędzy środkami źrenic
- Następnie program wylicza odległość użytkownika od ekranu korzystając z odległości pomiędzy źrenicami oraz zdjęcia twarzy
- Posiadając pomiary odległości oraz wielkość znaków, system z użyciem cyfrowych optotypów w standardzie zbliżonym do tablic LEA NUMBERS wylicza przedział wiekowy wzroku.

Wymagania
- Dostęp do kamery użytkownika
- Wymaga Android 7.0 (API 24) lub nowszego (zapewnia obsługę ponad 96% aktywnych urządzeń z systemem Android na całym świecie.)
  
Podsumowanie
EyeAgeApp to kompletny system do pomiaru wieku wzroku.
