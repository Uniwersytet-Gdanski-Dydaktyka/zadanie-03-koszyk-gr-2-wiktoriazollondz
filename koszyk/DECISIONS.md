Decyzje Projektowe - System Koszyka JavaMarkt
Wybór wzorca projektowego: Strategia (Strategy)
Do implementacji systemu promocji wybrano wzorzec Strategia z następujących powodów:

Dynamiczna zmiana zachowania: Zadanie wymaga, aby promocje mogły się zmieniać w trakcie działania programu. Strategia pozwala na wstrzykiwanie różnych algorytmów obliczania zniżek do klasy Cart bez modyfikowania jej kodu źródłowego.

Zasada Open/Closed (SOLID): System jest otwarty na rozszerzenia (możemy dodać klasę BlackFridayDiscount), ale zamknięty na modyfikacje (nie musimy edytować klasy Cart, aby dodać nową logikę).

Złożoność obliczeń: Niektóre promocje (jak 2+1) wymagają operacji na całej kolekcji produktów, a inne (jak procent od sumy) tylko na wartościach. Strategia pozwala odizolować tę logikę w osobnych klasach, co ułatwia testowanie jednostkowe każdej promocji z osobna.

Dlaczego nie Command?
Wzorzec Command byłby lepszy, gdybyśmy potrzebowali funkcji "Cofnij" (Undo) dla każdej dodanej promocji lub gdybyśmy chcieli kolejkować operacje do wykonania w przyszłości. W obecnej specyfikacji głównym wyzwaniem jest logika naliczania rabatu, a nie zarządzanie historią operacji.
