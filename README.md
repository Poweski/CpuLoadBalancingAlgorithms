# CpuLoadBalancingAlgorithms
Fifth project for operating systems.

Symulacja rozproszonego algorytmu równoważącego obciążenie procesorów. W systemie pracuje N identycznych
procesorów. Na każdym z nich pojawiają się nowe zadania (procesy), z RÓŻNĄ częstotliwością i RÓŻNYMI
wymaganiami (każdy proces wymaga określonego, różnego, udziału w mocy obliczeniowej procesora, np ~3%).
Zasymulować poniższe strategie przydziału:  

Na procesorze x pojawia się zadanie. Następnie:  

Strategia 1.:   
x pyta losowo wybrany procesor y o aktualne obciążenie. Jeśli jest mniejsze od progu p, proces jest tam
wysyłany. Jeśli nie, losujemy i pytamy następny, próbując co najwyżej z razy. Jeśli wszystkie wylosowane są
obciążone powyżej progu p, proces wykonuje się na x.  

Strategia 2.:   
Jeśli obciążenie x przekracza wartość progową p, proces zostaje wysłany na losowo wybrany procesor y
o obciążeniu mniejszym od p (jeśli wylosowany y ma obciążenie>p, losowanie powtarza się do skutku). Jeśli nie
przekracza - proces wykonuje się na x.  

Strategia 3.:   
Jak wyżej, z tym że procesory o obciążeniu mniejszym od minimalnego progu r pytają losowo wybrane
procesory i jeśli obciążenie zapytanego jest większe od p, pytający przejmuje część jego zadań (założyć jaką).

Wskazówki:  
Przeprowadzić symulację strategii 1,2,3 dla N=ok.50-100 i długiej serii zadań do wykonania (parametry dobrać
samodzielnie, tak by całość zadziałała:). W każdym przypadku podać jako wynik:  
1. Średnie obciążenie procesorów (zdecydować, rozsądnie, jak będzie obliczane).  
2. Średnie odchylenie od wartości z pkt. 1 wyżej.  
3. Ilość zapytań o obciążenie oraz migracji (przemieszczeń) procesów.  
4. Użytkownik powinien mieć możliwość podania (zmiany) wartości p,r,z,N. 
