// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy.
// 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp
//    do widelców. Wyniki przedstaw na wykresach.

var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function(delay, cb) {
    // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
    //    i ponawia próbę, itd.
    var self = this;
    // console.log(self.state, this.state);
    setTimeout(function() {
      if (self.state == 0) {
        self.state = 1;
        // console.log(delay, 'acquired');
      } else {
        // console.log('beniz');
        self.acquire(delay * 2);
      }
      if (cb) cb();
    }, delay);
}

Fork.prototype.release = function() {
    this.state = 0;
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    this.label_count = -1;
    return this;
}

Philosopher.prototype.nextLabel = function () {
  this.label_count++;
  return 'aquire ' + this.id + ' ' + this.label_count;
}

function measure(label, forks, f1, f2) {
  setTimeout( function() {
    console.time(label);
    setTimeout(function() {
      forks[f1].acquire(1, function() {
        forks[f1].acquire(1, function () {
          forks[f1].release();
          forks[f2].release();
          console.timeEnd(label);
        });
      })
    }, 0);
  }, 0);
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    var self = this;
    for (var i = 0; i < count; i++) {
      measure('aquire' + id + i, forks, f1, f2);
    }

    // zaimplementuj rozwiązanie naiwne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
}

Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;
    if (id % 2 == 0) {
      var tmp = f1;
      f1 = f2;
      f2 = tmp;
    }
    for (var i = 0; i < count; i++) {
      measure('aquire' + id + i, forks, f1, f2);
    }
    // zaimplementuj rozwiązanie asymetryczne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
}

Philosopher.prototype.startConductor = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;
    for (var i = 0; i < count; i++) {
      measure2('aquire' + id + i, forks, f1, f2);
    }
    // zaimplementuj rozwiązanie z kelnerem
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
}

function measure2(label, forks, f1, f2) {
  setTimeout( function() {
    console.time(label);
    setTimeout(function() {
        acquireTwo(forks[f1], forks[f2], 1, function () {
          forks[f1].release();
          forks[f2].release();
          console.timeEnd(label);
        });
    }, 0);
  }, 0);
}

function acquireTwo(f1, f2, delay, cb) {
  setTimeout(function() {
    if (f1.state == 0 && f2.state == 0) {
      f1.state = 1;
      f2.state = 1;
      // console.log(delay, 'acquired');
    } else {
      // console.log('beniz');
      acquireTwo(f1, f2, delay * 2);
    }
    if (cb) cb();
  }, delay);
}

Philosopher.prototype.startHunger = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;
    for (var i = 0; i < count; i++) {
      measure2('aquire' + id + i, forks, f1, f2);
    }
    // zaimplementuj rozwiązanie z kelnerem
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
}

// TODO: wersja z jednoczesnym podnoszeniem widelców
// Algorytm BEB powinien obejmować podnoszenie obu widelców,
// a nie każdego z osobna


var N = 5;
var forks = [];
var philosophers = []
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

for (var i = 0; i < N; i++) {
    philosophers[i].startNaive(1000);
}

console.log('done');
