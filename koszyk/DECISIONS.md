*** Wybór wzorca projektowego -> Strategy czy Command? ***

### Strategia (Strategy Pattern) - każda promocja jako osobna strategia obliczania ceny. 


*** Uzasadnienie wyboru: ***

### Dynamiczna zmiana promocji:
    Promocje w sklepie często się zmieniają, a wzorzec Strategia pozwala łatwo podpinać i odpinać różne rabaty w locie.

### Łatwo można dopisać nową promocję -> Zasada Open/Closed (Otwarte na rozbudowę, zamknięte na modyfikacje):
    Po prostu dopisuję kolejną klasę implementującą interfejs. Nie muszę grzebać w starym, działającym kodzie koszyka, więc niczego przypadkiem nie popsuję.

### Optymalizator porównuje promocję i wybiera najtańszą cenę: 
    Ponieważ każda promocja ma tę samą metodę apply(), algorytm może łatwo sprawdzić je wszystkie po kolei w pętli. Wykonuje je na kopiach koszyka, porównuje końcowe sumy i wybiera tę najtańszą dla klienta.


*** Dlaczego nie Command? ***
Wzorzec Command ma sens głównie wtedy, gdy potrzebujemy funkcji typu „Cofnij / Ponów” (Undo/Redo) albo chcemy kolejkować zadania do wykonania na później. Do zwykłego przeliczania cen w koszyku byłoby to niepotrzebne komplikowanie kodu (overengineering).