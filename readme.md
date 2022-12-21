# Program implementujący rozwiązanie problemu pisarzy i czytelników
# Autor: Piotr Kubala

## Krótki opis problemu
n pisarzy i k czytelników próbuje otrzymać dostęp do wspólnego zasobu - biblioteki.
W bibliotece może znajdować się maksymalnie m czytelników jednocześnie.
Każdy pisarz potrzebuje dostępu do biblioteki na wyłączność, czyli nikt inny nie może przebywać razem z nim w bibliotece.
Problem polega na takim przydzielaniu dostępu poszczególnym osobom, aby każda zainteresowana mogła po pewnym czasie otrzymać dostęp do biblioteki zgodnie z powyższymi zasadami.

## Sposób uruchamiania programu
```bash
java -jar nazwa_pliku.jar <ilość pisarzy> <ilość czytelników>
```

## Rozwiązanie
Program implementuje rozwiązanie problemu przy pomocy semaforów i kolejki kolejnych osób.

Każda osoba może być albo czytelnikiem, albo pisarzem.
W zależności od jej roli może zażądać wyłącznego dostępu do biblioteki (jeżeli jest pisarzem) lub czytać razem z innymi (jeżeli jest czytelnikiem).

Gdy osoba zażąda dostępu do biblioteki, to dodaje się ją do kolejki (wspólnej dla pisarzy i czytelników).
Wówczas sprawdza się, czy osoba może wejść do biblioteki.
W momencie, gdy kolejna osoba z kolejki może wejść do biblioteki, to jest ona usuwana z kolejki i dostaje dostęp do biblioteki.

Po pewnym czasie pisarz lub czytelnik może zakończyć pracę w bibliotece, wtedy ponownie sprawdzane jest, czy kolejna osoba z kolejki może wejść do biblioteki.

Użyto semaforów:
1. po jednym na każdego użytkownika biblioteki
2. jeden do blokowania bibliotekarza
3. jeden do blokowania dostępu do biblioteki

## Inne informacje
Program przechwytuje sygnały SIGINT i SIGTERM, dzięki czemu można go bezpiecznie zatrzymać w dowolnym momencie.