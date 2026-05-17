## Wybór Wzorca Projektowego: Strategia (Strategy Pattern)

W celu implementacji systemu promocji zdecydowano się na zastosowanie wzorca projektowego **Strategia (Strategy)**. 

### Uzasadnienie wyboru:
* **Dynamiczna wymiana logiki:** Specyfikacja zadania zakłada, że promocje mogą się dynamicznie zmieniać w trakcie działania programu (pojawiać się nowe, znikać stare). Wzorzec Strategia pozwala na wstrzykiwanie algorytmów obliczania zniżek przez interfejs `DiscountStrategy`, co uniezależnia klasę `Cart` od konkretnych implementacji promocji.

* **Zgodność z Open/Closed Principle (OCP):** Dodanie nowej promocji (np. kuponu rabatowego 30%) nie wymaga żadnych modyfikacji w istniejącym kodzie klasy `Cart` ani w innych strategiach. Wystarczy stworzyć nową klasę implementującą interfejs `DiscountStrategy`.

* **Zadanie dodatkowe (Optymalizator):** Zastosowanie wzorca Strategia umożliwiło łatwe zaimplementowanie klasy `DiscountOptimizer`. Dzięki jednolitemu interfejsowi, optymalizator może w pętli przeliterować po liście dostępnych strategii, wywołać metodę `apply()` na kopii koszyka i bezinwazyjnie porównać, która opcja jest najkorzystniejsza dla klienta.

### Dlaczego nie wzorzec Dowództwo (Command)?
Wzorzec *Command* doskonale sprawdza się w systemach wymagających operacji wycofywania zmian (Undo/Redo) lub kolejkowania zadań (np. "kliknięto przycisk, dodaj do kolejki zamówień"). W realiach prostego naliczania rabatów w koszyku, gdzie kluczowa jest elastyczność kalkulacji ceny końcowej na żądanie, narzut klas tworzonych przez wzorzec Command byłby zbędną komplikacją systemu (*Overengineering*).